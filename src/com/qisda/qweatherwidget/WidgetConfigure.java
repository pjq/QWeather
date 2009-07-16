
package com.qisda.qweatherwidget;

import com.qisda.qweather.R;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter.ViewBinder;

/**
 * The QWeatherWidget configure Activity.You should enter a city name here then
 * press OK.After that,you have added a QWeatherWidget to the Desktop.
 * 
 * @author Percy
 */
public class WidgetConfigure extends Activity implements OnClickListener, OnKeyListener {
    /**
     * The WidgetConfigure log tag.
     */
    private static final String LOGTAG = "WidgetConfigure";

    /**
     * The OK Button,After you enter a city,then press this Button.
     */
    private Button okButton;

    /**
     * The EditText for your entering the city.
     */
    private EditText editText;

    /**
     * This AppWidgeId,Because the UpdateService.requestUpdate() param need a
     * Int Array,so use "int[]" here
     */
    private int[] mAppWidgetId = new int[1];

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.widgetconfigure);
        okButton = (Button)findViewById(R.id.okButton);
        editText = (EditText)findViewById(R.id.editText);
        editText.setOnKeyListener(this);
        okButton.setOnClickListener(this);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle extras = intent.getExtras();
            mAppWidgetId[0] = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();

        switch (id) {
            case R.id.okButton:
                Log.i(LOGTAG, "Press OK Button");
                addAppWidget();

                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                addAppWidget();

                return true;

            default:
                break;
        }

        return false;
    }

    public void addAppWidget() {
        String citynameString = editText.getText().toString();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putString(Integer.toString(mAppWidgetId[0]), citynameString);

        editor.commit();

        final Context context = WidgetConfigure.this;
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId[0]);
        setResult(RESULT_OK, resultValue);
        finish();

        // Start the UpdataService with a Intent and add this
        // appWidgetId to the update Queue.
        Intent intent = new Intent();
        intent.setAction("com.qisda.qweatherwidget.UpdateService");
        UpdateService.requestUpdate(mAppWidgetId);
        context.startService(intent);
    }

}
