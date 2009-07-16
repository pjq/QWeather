
package com.qisda.qweatherwidget;

import com.qisda.qweather.R;
import com.qisda.qweatherwidget.broadcastreceiver.QWeatherWidgetBroadcastReceiver;

import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * QWeatherWidget is used to update the QWeather info. displayed as a APP Widget
 * on the Screen.And it will be updated according to the time set in
 * xml/qweatherwidgetprovier.xml
 * 
 * @author Percy
 */
public class QWeatherWidget extends AppWidgetProvider {
    public static final String LOGTAG = "QWeatherWidget";

    /** Called when the activity is first created. */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Intent intent = new Intent();
        intent.setAction("com.qisda.qweatherwidget.UpdateService");
        context.startService(intent);
        UpdateService.requestUpdate(appWidgetIds);

        /*
         * for (int i = 0; i < appWidgetIds.length; i++) { int appWidgetId =
         * appWidgetIds[i]; Log.i(LOGTAG, "updateAppWidget:" + appWidgetId);
         * updateAppWidget(context, appWidgetManager, appWidgetId,""); }
         */
    }

    /**
     * This methord is used to update the appWidgets
     * 
     * @param context The context
     * @param appWidgetManager The app widget manager
     * @param appWidgetId The widget need to be update.
     */
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, String message) {
        Log.i(LOGTAG, "updateAppWidget():" + appWidgetId);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String citynameString = sharedPreferences.getString(Integer.toString(appWidgetId),
                "Not Set");
        Log.i(LOGTAG, "cityname:" + citynameString);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetmain);
        views.setTextViewText(R.id.cityTextView, citynameString);
        views.setTextViewText(R.id.conditionTextView, message);
        views.setImageViewResource(R.id.currentImageView, R.drawable.qweatherwidgeticon);
        views = setRemoteViewListener(context, views);
        updateAppWidgeWithViews(context, appWidgetManager, appWidgetId, views);

    }

    /**
     * Update the AppWidget with the centain RemoteVIews.
     * 
     * @param context The context
     * @param appWidgetManager The AppWidgetManager
     * @param appWidgetId The AppWidget id need to be updated.
     * @param views The RemoteViews used to be displayed after the
     *            AppWidgetManager updating..
     */
    public static void updateAppWidgeWithViews(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, RemoteViews views) {
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    /**
     * Set the RemoteViews listener here,If the AppWidget need to listener
     * key/mouse event,you should add the listener here.
     * 
     * @param context The context.
     * @param remoteViews The RemoteViews need to add listener. 
     * @return The RemoteViews has been added listeneres.
     */
    public static RemoteViews setRemoteViewListener(Context context, RemoteViews remoteViews) {
        ComponentName componentName = new ComponentName(context,
                QWeatherWidgetBroadcastReceiver.class);
        Intent intent = new Intent(QWeatherWidgetBroadcastReceiver.ACTION_UPDATE);
        intent.setComponent(componentName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.cityTextView, pendingIntent);
        /*
         * RemoteViews views = new RemoteViews(context.getPackageName(),
         * R.layout.main); Intent intent = new
         * Intent(context,QWeatherActivity.class); PendingIntent pendingIntent =
         * PendingIntent.getActivity(context, 0, intent,
         * PendingIntent.FLAG_CANCEL_CURRENT);
         * views.setOnClickPendingIntent(appWidgetId, pendingIntent);
         */

        return remoteViews;
    }

}
