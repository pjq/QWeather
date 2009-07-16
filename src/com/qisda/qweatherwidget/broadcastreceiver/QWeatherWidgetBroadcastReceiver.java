
package com.qisda.qweatherwidget.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class QWeatherWidgetBroadcastReceiver extends BroadcastReceiver {
    public static final String LOGTAG = "QWeatherWidgetBroadcastReceiver";

    public static final String ACTION_UPDATE = "update";

 
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.i(LOGTAG,"Intent="+intent);

        if (null != intent) {
            Bundle extras = intent.getExtras();
            String actionString = intent.getAction();
            if (actionString.equals(ACTION_UPDATE)) {

            }
        }
        return;

    }

}
