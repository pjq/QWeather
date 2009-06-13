package com.qisda.qweather;

import com.qisda.qweather.data.WeatherData;
import com.qisda.qweather.handle.*;
import com.qisda.qweather.QWeather.*;
import com.qisda.qweather.view.SingleWeatherInfoView;

import android.view.*;
import android.view.KeyEvent.*;
import android.view.View.OnKeyListener;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.TextView.OnEditorActionListener;
import org.xml.sax.*;
import android.content.Intent;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import android.view.animation.Animation;

public class QWeather extends Activity implements OnItemClickListener,
		View.OnClickListener
{
	private Button				nextButton;
	private TextView			textViewCityName;
	private ProgressBar			progressBar;
	private ImageView			imageViewWeatherIcon;
	private TextView			textViewDate;
	private TextView			textViewCondition;
	private TextView			textViewTemp;
	private TextView			textViewWind;
	private TextView			textViewHumidity;
	private TextView			textViewCurrent;

	private Gallery				g;

	private ViewFlipper			mFlipper;
	private ViewFlipper			cityNameflipper;

	private LinearLayout		weatherDetailLayout;
	private LinearLayout		weatherInfo;
	private LinearLayout		cityNameLayout;
	private LinearLayout		textViewCurrentLayout;
	private LinearLayout		mainInfoLayout;
	private LinearLayout		galleryLayout;
	private LinearLayout		weatherIconLayout;
	private LinearLayout		mainLayout;

	private WeatherInfoAdapter	adapter;
	private WeatherData			weatherData;
	HandleParseXML				handle;

	private Animation			animUp;

	public static boolean		bAtOffice	= true;

	private ProgressDialog		progressDialog;

	public static final int		sleepTime	= 1500;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		/*
		 * When the activity is created,the application will run here first. I
		 * do almost all the simple initiation here
		 */

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Get widget and layout
		nextButton = (Button) findViewById(R.id.button);
		textViewCityName = (TextView) findViewById(R.id.textViewCityName);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		imageViewWeatherIcon = (ImageView) findViewById(R.id.imageViewWeatherIcon);
		textViewDate = (TextView) findViewById(R.id.textViewDate);
		textViewCondition = (TextView) findViewById(R.id.textViewCondition);
		textViewTemp = (TextView) findViewById(R.id.textViewTemp);
		textViewWind = (TextView) findViewById(R.id.textViewWind);
		textViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
		textViewCurrent = (TextView) findViewById(R.id.textViewCurrent);

		g = (Gallery) findViewById(R.id.gallery);

		cityNameLayout = (LinearLayout) findViewById(R.id.cityNameLayout);
		mainInfoLayout = (LinearLayout) findViewById(R.id.mainInfoLayout);
		weatherIconLayout = (LinearLayout) findViewById(R.id.weatherIconLayout);
		weatherDetailLayout = (LinearLayout) findViewById(R.id.weatherDetailLayout);
		weatherInfo = (LinearLayout) findViewById(R.id.weatherInfo);
		textViewCurrentLayout = (LinearLayout) findViewById(R.id.textViewCurrentLayout);
		galleryLayout = (LinearLayout) findViewById(R.id.galleryLayout);
		mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

		// Init the widget and layout
		nextButton.setOnClickListener(this);

		// imageViewWeatherIcon.setDrawingCacheEnabled(true);
		imageViewWeatherIcon.setAdjustViewBounds(true);
		imageViewWeatherIcon.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.wave_scale));
		imageViewWeatherIcon.setFadingEdgeLength(20);
		imageViewWeatherIcon.setScaleType(ScaleType.CENTER_CROP);
		imageViewWeatherIcon.setAlpha(200);

		// imageViewWeatherIcon.setPadding(10, 10, 10, 10);

		// g.setBackgroundResource(R.drawable.green);
		g.setFadingEdgeLength(40);
		g.setHorizontalFadingEdgeEnabled(true);
		g.setVerticalFadingEdgeEnabled(true);
		g.setOnItemClickListener(this);

		cityNameLayout.setBackgroundResource(R.drawable.shape_5);
		mainInfoLayout.setBackgroundResource(R.drawable.shape_5);
		weatherIconLayout.setBackgroundResource(R.drawable.shape_5);
		// weatherDetailLayout.setBackgroundResource(R.drawable.shape_5);
		textViewCurrentLayout.setBackgroundResource(R.drawable.shape_4);
		galleryLayout.setBackgroundResource(R.drawable.shape_4);

		// Init animation:animUp
		animUp = new TranslateAnimation(0, 0, 250, -140);
		animUp.setDuration(6000);
		animUp.setRepeatMode(1);
		animUp.setRepeatCount(1000);
		// anim.setInterpolator(new AccelerateDecelerateInterpolator());
		animUp.setInterpolator(new LinearInterpolator());
		// layout44.setAnimation(anim);

		weatherInfo.setAnimation(animUp);

		Animation textViewCurrentAnim = new TranslateAnimation(-480, 350, 0, 0);
		textViewCurrentAnim.setDuration(15000);
		textViewCurrentAnim.setRepeatMode(2);
		textViewCurrentAnim.setRepeatCount(100000);
		// anim.setInterpolator(new AccelerateDecelerateInterpolator());
		textViewCurrentAnim.setInterpolator(new LinearInterpolator());
		textViewCurrent.setLines(1);
		textViewCurrent.setHorizontallyScrolling(true);
		textViewCurrent.setAnimation(textViewCurrentAnim);

		/*
		 * Though I set a flipper here,but I disable it here:
		 * mFlipper.startFlipping(); Because I set a animation for the text to
		 * replace the flipper animation. See above:textViewCurrentAnim
		 */
		mFlipper = (ViewFlipper) this.findViewById(R.id.flipper);
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		// mFlipper.startFlipping();

		cityNameflipper = (ViewFlipper) this.findViewById(R.id.cityNameflipper);
		cityNameflipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		cityNameflipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		cityNameflipper.startFlipping();

	}

	@Override
	public void onStart()
	{

		super.onStart();

		/*
		 * Here we use the default SharedPreferences to store the data,But we
		 * can also use a SharedPreferences we defined. Like: SharedPreferences
		 * settings = getSharedPreferences("WeDefinedPreferences", 0); The
		 * default Preference file is here:
		 * /data/data/com.qisda.qweather/shared_prefs
		 * /com.qisda.qweather_preferences.xml You can open it to see the
		 * content.
		 */

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);

		String city1 = settings.getString("city1", "");
		String city2 = settings.getString("city2", "");

		nextButton.setText(city2);

		if (city1.equals(""))
		{
			Toast.makeText(this, getString(R.string.noSettingsPrompt), 4000)
					.show();
			nextButton.setVisibility(4);
		}

		if (city2.equals("") || city1.equals(""))
		{
			nextButton.setVisibility(4);

		} else
		{
			nextButton.setVisibility(0);
		}

		search(city1);
		adapter = new WeatherInfoAdapter(this);
		g.setAdapter(adapter);

	}

	/*
	 * We can set breakpoints in the following override functions to see the
	 * life cycle. BE REMERBER:In these override functions,you must first call
	 * the parent function,like this: super.onRestart();
	 */

	@Override
	public void onRestart()
	{
		super.onRestart();
	}

	@Override
	public void onResume()
	{
		super.onResume();

	}

	@Override
	public void onPause()
	{
		super.onPause();

	}

	@Override
	public void onStop()
	{
		super.onStop();

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

	}

	/*
	 * This abstract function is inherited from OnClickListener You must
	 * override the function if you implements OnClickListener in this class.
	 */

	@Override
	public void onClick(View v)
	{

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);

		String city1 = settings.getString("city1", "");
		String city2 = settings.getString("city2", "");

		String current = nextButton.getText().toString();

		if (current.equals(city1))
		{
			nextButton.setText(city2);

		} else if (current.equals(city2))
		{
			nextButton.setText(city1);
		}
		search(current);
		adapter.notifyDataSetChanged();

		applyRotation(0, 360);

	}

	/*
	 * This abstract function is inherited from OnItemClickListener You must
	 * override the function if you implements OnItemClickListener in this
	 * class.
	 */
	@Override
	public void onItemClick(AdapterView parent, View v, int position, long id)
	{
		SingleWeatherInfoView weather = (SingleWeatherInfoView) v;
		imageViewWeatherIcon.setImageBitmap(weather.getBitmap());

		/*
		 * I want to change the text color on the Item I just clicked, But it
		 * seems no effect.
		 */
		// weather.setTextColor(0xea4012);
		textViewDate.setText(weatherData.getForecastConditionsData().get(
				position).getDayOfWeek());
		textViewCondition.setText(weatherData.getForecastConditionsData().get(
				position).getCondition());

		textViewTemp.setText(weatherData.getForecastConditionsData().get(
				position).getLowTemp()
				+ "~"
				+ weatherData.getForecastConditionsData().get(position)
						.getHighTemp());
		textViewWind.setText("");
		textViewHumidity.setText("");

		switch (position)
		{
		case 0:
			imageViewWeatherIcon.setAnimation(AnimationUtils.loadAnimation(
					this, R.anim.wave_scale));
			break;
		case 1:
			imageViewWeatherIcon.setAnimation(AnimationUtils.loadAnimation(
					this, R.anim.hyperspace_in));
			break;
		case 2:
			imageViewWeatherIcon.setAnimation(AnimationUtils.loadAnimation(
					this, R.anim.push_left_in));
			break;
		case 3:
			imageViewWeatherIcon.setAnimation(AnimationUtils.loadAnimation(
					this, R.anim.push_up_in));
			break;

		}

		// weather.setAnimation(anim);
		// weather.setAnimation(AnimationUtils.loadAnimation(this,
		// R.anim.wave_scale));

		weatherInfo.startAnimation(animUp);

	}

	/*
	 * Create the menu here:Setting,Search,About
	 */
	private static final int	SETTING_ID	= Menu.FIRST;
	private static final int	SEARCH_ID	= Menu.FIRST + 1;
	private static final int	ABOUT_ID	= Menu.FIRST + 2;
	private static final int	RQ_RESULT	= 0;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		menu.add(0, SETTING_ID, 0, getString(R.string.setting)).setIcon(
				R.drawable.settings);
		menu.add(0, SEARCH_ID, 0, getString(R.string.search)).setIcon(
				R.drawable.find);
		menu.add(0, ABOUT_ID, 0, getString(R.string.about)).setIcon(
				R.drawable.about);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case SETTING_ID:
			Intent intentSettings = new Intent();
			intentSettings.setClass(QWeather.this, Settings.class);
			startActivity(intentSettings);
			return true;

		case SEARCH_ID:
			Intent intent = new Intent();
			intent.setClass(QWeather.this, SearchDialog.class);
			startActivityForResult(intent, RQ_RESULT);
			return true;

		case ABOUT_ID:
			Toast.makeText(this, getString(R.string.aboutInfo), 4000).show();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * The search function.
	 * 
	 * @Params: city:[in]:the cityname you want to search.
	 */
	public void search(String city)
	{

		progressDialog = ProgressDialog.show(this,
				getString(R.string.waitText), getString(R.string.searchingText)
						+ city + "...", true);

		if (city == "" || city == null)
		{
			progressDialog.dismiss();
		}

		// progressBar.setVisibility(0);
		handle = new HandleParseXML();
		try
		{

			String cityName = city;
			if (cityName == "")
			{
				// cityName = "suzhou";
			}

			String queryString;
			if (this.bAtOffice)
			{
				queryString = "http://10.85.40.153:8000/" + cityName + ".xml";
			} else
			{
				queryString = "http://www.google.com/ig/api?weather="
						+ cityName;
			}
			URL url = new URL(queryString.replace(" ", "%20"));

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(handle);
			xr.parse(new InputSource(url.openStream()));

			if (handle.bParseOK())
			{
				weatherData = handle.getWeatherData();
				Bitmap bitmap = getLocalImage(weatherData
						.getCurrentConditionsData().getIcon());
				if (null == bitmap)
				{
					bitmap = getRemoteImage(weatherData
							.getCurrentConditionsData().getIcon());
				}
				imageViewWeatherIcon.setImageBitmap(bitmap);

			} else
			{
				String string = getString(R.string.searchException);
				Toast.makeText(this, string, 4000).show();

				new Thread()
				{
					public void run()
					{
						try
						{
							sleep(sleepTime);
						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						progressDialog.dismiss();
					}
				}.start();

				return;

			}

			textViewDate.setText(getString(R.string.current));
			textViewCondition.setText(weatherData.getCurrentConditionsData()
					.getCondition());
			textViewCityName.setText(weatherData.getForecastInformationData()
					.getCity());
			// textViewCityName.setBackgroundResource(R.drawable.shape_5);

			textViewTemp.setText(weatherData.getCurrentConditionsData()
					.getTempC()
					+ " C");
			textViewHumidity.setText(weatherData.getCurrentConditionsData()
					.getHumidity());

			String current = weatherData.getForecastInformationData()
					.getPostalCode()
					+ "  "
					+ weatherData.getForecastInformationData()
							.getForecastDate()
					+ "  "
					+ weatherData.getCurrentConditionsData().getCondition()
					+ "  "
					+ weatherData.getCurrentConditionsData().getTempC()
					+ "  "
					+ weatherData.getCurrentConditionsData().getHumidity()
					+ "  "
					+ weatherData.getCurrentConditionsData().getWindCondition();

			textViewCurrent.setText(current);

			textViewWind.setText(weatherData.getCurrentConditionsData()
					.getWindCondition());

			// imageViewWeatherIcon.setBackgroundResource(R.drawable.weather_few_clouds);

			/*
			 * VISIBLE:0 INVISIBLE:4 GONE:8
			 */
			// progressBar.setVisibility(4);
		} catch (Exception e)
		{
			new Thread()
			{
				public void run()
				{
					try
					{
						sleep(sleepTime);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					progressDialog.dismiss();
				}
			}.start();

			/*
			 * Here I just show a simple message.
			 */
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(this);

			String city1 = settings.getString("city1", "");
			if (city1 == "")
			{
				// do nothing
				if (city.equals(""))
				{
					Toast.makeText(this, getString(R.string.searchException),
							Toast.LENGTH_SHORT).show();

				} else
				{
					String string = city + ":"
							+ getString(R.string.searchException);
					Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
					// progressBar.setVisibility(4);
				}
			} else
			{
				String string = city + ":"
						+ getString(R.string.searchException);
				Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
				// progressBar.setVisibility(4);

			}
		}

	}

	/*
	 * This function is the callback of startActivityForResult. Use it to pass
	 * the city name You just input in the search dialog.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == RQ_RESULT)
		{
			if (resultCode == RESULT_OK)
			{
				String cityName = data.getExtras().getCharSequence("city")
						.toString();
				search(cityName);
				/*
				 * Notify the adapter to refresh the status.
				 */
				if (handle.bParseOK())
				{
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	/*
	 * The WeatherInfoAdapter.
	 */
	public class WeatherInfoAdapter extends BaseAdapter
	{
		private Context	mContext;
		int				count	= 0;

		public WeatherInfoAdapter(Context context)
		{
			mContext = context;
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			if (null == weatherData)
			{
				return 0;
			} else
			{
				int size = weatherData.getForecastConditionsData().size();
				count = size;
				// return weatherData.getForecastConditionsData().size();
				return size;
			}
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			SingleWeatherInfoView view = new SingleWeatherInfoView(mContext);

			String imageURI = weatherData.getForecastConditionsData().get(
					position).getIcon();

			Bitmap bitmap = getLocalImage(imageURI);
			if (null == bitmap)
			{
				bitmap = getRemoteImage(imageURI);
			}

			view.setBitmap(bitmap);
			String temp = weatherData.getForecastConditionsData().get(position)
					.getLowTemp()
					+ "~"
					+ weatherData.getForecastConditionsData().get(position)
							.getHighTemp();
			view.setTempString(temp);
			String day = weatherData.getForecastConditionsData().get(position)
					.getDayOfWeek();
			view.setDayOfWeek(day);

			if (position == weatherData.getForecastConditionsData().size() - 2)
			{
				new Thread()
				{
					public void run()
					{
						try
						{
							sleep(sleepTime);
						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						progressDialog.dismiss();
					}
				}.start();
			}

			return view;
		}
	}

	/*
	 * Get the weather icon from the site: http://www.google.com and return the
	 * Bitmap.
	 */

	public Bitmap getLocalImage(String imageURI)
	{
		Bitmap bitmap = null;

		try
		{
			InputStream is = this.openFileInput(getImageName(imageURI));
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);

			bis.close();
			is.close();

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}

	public Bitmap getRemoteImage(String imageURI)
	{
		try
		{
			URL url;
			if (this.bAtOffice)
			{
				url = new URL("http://10.85.40.153:8000" + imageURI);
			} else
			{
				url = new URL("http://www.google.com" + imageURI);
			}

			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap bm = BitmapFactory.decodeStream(bis);

			saveBitmap(bm, imageURI);

			bis.close();
			is.close();
			return bm;
		} catch (IOException e)
		{

			Toast.makeText(this,
					getString(R.string.faiedToGetImage) + imageURI, 4000)
					.show();

		}
		return null;
	}

	private void applyRotation(float start, float end)
	{

		final float centerX = 160;
		final float centerY = 240;

		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 10.0f, true);

		rotation.setDuration(2000);
		rotation.setInterpolator(new LinearInterpolator());
		mainLayout.startAnimation(rotation);
	}

	public void saveBitmap(Bitmap bitmap, String imageURI)
	{
		try
		{
			OutputStream oStream = openFileOutput(getImageName(imageURI),
					this.MODE_WORLD_READABLE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);

			oStream.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getImageName(String imageURI)
	{
		String imageName = null;
		int index = imageURI.lastIndexOf("/");
		imageName = imageURI.substring(index + 1);

		return imageName;
	}

}