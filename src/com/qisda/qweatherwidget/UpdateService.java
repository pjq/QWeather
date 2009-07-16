
package com.qisda.qweatherwidget;

import com.qisda.qweather.QWeather;
import com.qisda.qweather.R;
import com.qisda.qweather.data.WeatherData;
import com.qisda.qweather.handle.HandleParseXML;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.R.integer;
import android.app.ProgressDialog;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * UpdateService is used to update the QWeatherWidget,You should define the
 * frequency in xml/qweatherwidgetprovider .
 * 
 * @author Percy
 */
public class UpdateService extends Service implements Runnable {
    /**
     * The log tag:UpdateService
     */
    private final String LOGTAG = "UpdateService";

    /**
     * Store the appWidgetIds in a Queue,then update them one by one,Remember
     * first in first out.
     */
    private static Queue<Integer> sAppWidgetIds = new LinkedList<Integer>();

    /**
     * The AppWidgetManager.
     */
    private AppWidgetManager mAppWidgetManager;

    /**
     * To indicate whether the thread is running or not
     */
    private static boolean sThreadRunning;

    /**
     * The HandleParseXML handle,used to parse the XML file got from the Google
     * website. And also use it to get the WeatherData.
     */
    public static HandleParseXML handle = null;

    /**
     * The XMLReader
     */
    public static XMLReader xr;

    /**
     * The WeatherData is used to store the parsed the weather infomation.Got
     * from the HandleParseXML.
     */
    private WeatherData weatherData;

    /**
     * Create a lock used by the synchonized(object).
     */
    public static Object object = new Object();

    /**
     *Just a indicator:Whether the Programming is working at office or not
     */
    public static final boolean bAtOffice = QWeather.bAtOffice;

    /**
     * Some constants used to decide which function I need call.
     */

    /**
     * When the refresh is starting,it should display "Refreshing" in the
     * AppWidget.
     */
    public static final int sRefreshStart = 0;

    /**
     * When the refresh is OK(exactly it is to getting the weather data
     * correctly),then it need to refresh the AppWidget with the data just got.
     */
    public static final int sRefreshOK = 1;

    /**
     * Refresh error,it should display "Refresh Error!!" in the AppWidget.
     */
    public static final int sRefreshError = 2;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // When first start this service,it will update the QWeatherWidget
        // automatically.
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName provider = new ComponentName(this, QWeatherWidget.class);
        int[] appWidgetIds = mAppWidgetManager.getAppWidgetIds(provider);

        // Append the update widget id to the list:sAppWidgetIds,just
        // remember:First in,first out.
        requestUpdate(appWidgetIds);
        Log.i(LOGTAG, "onCreate()");

    }

    /**
     * Every time when you start the service with the Intent,it will run here
     * but will not run onCreate();If you use "stopSelf();"to stop the
     * service,then it will also run onCreate()first.
     */

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(LOGTAG, "onStart()");

        if (true != sThreadRunning) {
            sThreadRunning = true;
            Log.i(LOGTAG, "new Thread(this).start()");
            // Start the update process in the thread.
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Log.i(LOGTAG, "run()");
        String message = Long.toString(System.currentTimeMillis());

        while (hasMoreUpdates()) {
            int appWidgetId = getNextUpdate();
            synchronized (object) {
                handle = null;
                buildAndUpdateAppWidget(appWidgetId, sRefreshStart);
                search(getCityNameFromAppWidgetId(appWidgetId));
                if (null == handle || true != handle.bParseOK()) {
                    buildAndUpdateAppWidget(appWidgetId, sRefreshError);
                    Log.i(LOGTAG, "Failed to get data");
                } else if (true == handle.bParseOK()) {
                    Log.i(LOGTAG, "Get data successfully");
                    buildAndUpdateAppWidget(appWidgetId, sRefreshOK);
                }

            }

        }

        // If you need stop this service,add "stopSelf();"here.
        // stopSelf();
    }

    /**
     * It is used to add the appWidgetIds that need to be update to the Queue
     * 
     * @param appWidgetIds the appWidgetIds which are all need to be update.
     */
    public static void requestUpdate(int[] appWidgetIds) {
        synchronized (object) {
            for (int appWidgetId : appWidgetIds) {
                sAppWidgetIds.add(appWidgetId);
            }
        }

    }

    /**
     * The main function used to update the AppWidget. You can add some other
     * status here.
     * 
     * @param appWidgetId the AppWidget identical Id.
     * @param refreshStatus the Status decide which operation you should do.
     */
    public void buildAndUpdateAppWidget(int appWidgetId, int refreshStatus) {

        switch (refreshStatus) {
            case sRefreshStart:
                Log.i(LOGTAG, "Refresh Start!!");
                RemoteViews views1 = new RemoteViews(this.getPackageName(), R.layout.widgetmain);
                views1.setTextViewText(R.id.cityTextView, getCityNameFromAppWidgetId(appWidgetId));
                views1.setTextViewText(R.id.conditionTextView, getResources().getString(
                        R.string.refresh));

                views1 = QWeatherWidget.setRemoteViewListener(this, views1);
                QWeatherWidget
                        .updateAppWidgeWithViews(this, mAppWidgetManager, appWidgetId, views1);
                break;

            case sRefreshOK:
                Log.i(LOGTAG, "Refresh OK!!");
                WeatherData weatherData = handle.getWeatherData();
                RemoteViews views2 = new RemoteViews(this.getPackageName(), R.layout.widgetmain);
                views2.setTextViewText(R.id.cityTextView, getCityNameFromAppWidgetId(appWidgetId));
                String temp = weatherData.getForecastConditionsData().get(0).getLowTemp() + "~"
                        + weatherData.getForecastConditionsData().get(0).getHighTemp();
                String condition = weatherData.getCurrentConditionsData().getCondition();
                views2.setTextViewText(R.id.conditionTextView, condition + "/" + temp);
                
                String iconUri = weatherData.getCurrentConditionsData().getIcon();

                if (iconUri.equals("")) {
                    iconUri = weatherData.getForecastConditionsData().get(0).getIcon();
                }
                Bitmap bitmap = getLocalImage(iconUri);
                if (null == bitmap) {
                    bitmap = getRemoteImage(iconUri);
                }

                // Get bitmap failed,you must add some default drawable here.
                if (null == bitmap) {
                    views2.setImageViewResource(R.id.currentImageView, R.drawable.qweatherwidgeticon);
                } else {
                    views2.setImageViewBitmap(R.id.currentImageView, bitmap);
                }

                views2 = QWeatherWidget.setRemoteViewListener(this, views2);
                QWeatherWidget
                        .updateAppWidgeWithViews(this, mAppWidgetManager, appWidgetId, views2);
                break;

            case sRefreshError:
                Log.i(LOGTAG, "Refresh Error!!");
                RemoteViews views3 = new RemoteViews(this.getPackageName(), R.layout.widgetmain);
                views3.setTextViewText(R.id.cityTextView, getCityNameFromAppWidgetId(appWidgetId));
                views3.setTextViewText(R.id.conditionTextView, getResources().getString(
                        R.string.refreshError));
                views3.setImageViewResource(R.id.currentImageView, R.drawable.qweatherwidgeticon);
                views3 = QWeatherWidget.setRemoteViewListener(this, views3);
                QWeatherWidget
                        .updateAppWidgeWithViews(this, mAppWidgetManager, appWidgetId, views3);
                break;

            default:
                break;
        }

    }

    /**
     * It is used to get the city name according to the appWidgetId. When you
     * add a widget to the Desktop,it will ask you to enter a city name.Then the
     * appWidgetId will store as key of the city in the SharedPreferences.
     * 
     * @param appWidgetId Get the city according to the appWidgetId from the
     *            SharedPreferences.
     * @return The city you ask for.
     */
    public String getCityNameFromAppWidgetId(int appWidgetId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(Integer.toString(appWidgetId), getResources().getString(
                R.string.notset));
    }

    /**
     * To detect whether there has more AppWidget need to be update in the
     * sAppWidgetIds.
     * 
     * @return A boolean type.if true,it has more.
     */
    private static boolean hasMoreUpdates() {
        synchronized (object) {
            boolean hasMore = !sAppWidgetIds.isEmpty();
            if (!hasMore) {
                sThreadRunning = false;
            }
            return hasMore;
        }
    }

    /**
     * Get the next appWidgetId from the sAppWidgetIds and remove it from the
     * Queue
     * 
     * @return A appWidgetId need to be update next.
     */
    private static int getNextUpdate() {
        if (sAppWidgetIds.peek() == null) {
            return AppWidgetManager.INVALID_APPWIDGET_ID;
        } else {
            return sAppWidgetIds.poll();
        }
    }

    /**
     * The main function used to get and parse the weather data from the Google
     * website. And store the data in weatherData.
     * 
     * @param city The city need to get the weather data.
     */
    public void search(String city) {

        Log.i(LOGTAG, "search(" + city + ")");
        String queryString = null;
        if (city.equals(getResources().getString(R.string.notset))) {
            return;
        }
        handle = new HandleParseXML();

        if (bAtOffice) {
            queryString = "http://10.85.40.153/" + city + ".xml";
        } else {
            queryString = "http://www.google.com/ig/api?weather=" + city;
        }
        URL url = null;
        try {
            url = new URL(queryString.replace(" ", "%20"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
        try {
            sp = spf.newSAXParser();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            xr = sp.getXMLReader();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        xr.setContentHandler(handle);
        try {
            Log.i("SearchThread", "xr.parse(new InputSource(url.openStream()))");
            xr.parse(new InputSource(url.openStream()));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (null == handle) {
            Log.i(LOGTAG, "handle==null");
            return;
        }
        if (handle.bParseOK()) {
            Log.i("QWeather", "handle.bParseOK()==true");
            weatherData = handle.getWeatherData();

        } else {
            Log.i(LOGTAG, "handle.bParseOK()==false");
            String string = getString(R.string.searchException);
            return;

        }
    }

    /**
     * Get the image from local file according to the imageURI
     * 
     * @param imageURI the file need to get from the local file.
     * @return The bitmap file decoded from the local file.If can't get the file
     *         return null.
     */
    public Bitmap getLocalImage(String imageURI) {
        Log.i(LOGTAG, "Get local image:" + imageURI);
        Bitmap bitmap = null;

        try {
            InputStream is = this.openFileInput(getImageName(imageURI));
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);

            bis.close();
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e(LOGTAG, "Failed to get Local image:" + imageURI);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }

    /**
     * Get the file name from the imageURI
     * @param imageURI Contain a file name.
     * @return The file name of the imageURI.
     */
    public String getImageName(String imageURI) {
        String imageName = null;
        int index = imageURI.lastIndexOf("/");
        imageName = imageURI.substring(index + 1);

        return imageName;
    }

    public Bitmap getRemoteImage(String imageURI) {
        try {
            URL url;
            Log.i(LOGTAG, "Get remote image:" + imageURI);
            
            if (this.bAtOffice) {
                url = new URL("http://10.85.40.153" + imageURI);
            } else {
                url = new URL("http://www.google.com" + imageURI);
            }

            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Bitmap bm = BitmapFactory.decodeStream(bis);

            //Save this image to localfile.
            saveBitmap(bm, imageURI);

            bis.close();
            is.close();
            return bm;
        } catch (IOException e) {
            Log.i(LOGTAG, "Failed to get Remote image" + imageURI);
        }
        
        return null;
    }

    /**
     * Save the bitmap to a local file according the imageURI
     * @param bitmap The bitmap need to be stored to local file.
     * @param imageURI Contain the file name need to store as.
     */
    public void saveBitmap(Bitmap bitmap, String imageURI) {
        Log.i(LOGTAG, "save image:" + imageURI);
        
        try {
            OutputStream oStream = openFileOutput(getImageName(imageURI), this.MODE_WORLD_READABLE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);

            oStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
