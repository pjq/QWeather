
package com.qisda.qweather.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.qisda.qweather.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SingleWeatherInfoView extends LinearLayout {

    private ImageView imageView = null;

    private TextView tempText = null;

    private TextView dayOfWeek = null;

    private final int WIDTH = 90;

    public SingleWeatherInfoView(Context context) {
        super(context);
        this.imageView = new ImageView(context);

        /*
         * Setup the textView that will show the temperature.
         */
        this.tempText = new TextView(context);
        this.tempText.setText("Temp:H   L   ");
        this.tempText.setTextSize(16);
        this.tempText.setTypeface(Typeface.create("Tahoma", Typeface.BOLD));

        this.dayOfWeek = new TextView(context);
        this.dayOfWeek.setText("Day of Week");

        /*
         * setOrientation
         */
        this.setOrientation(LinearLayout.VERTICAL);

        this.addView(this.dayOfWeek, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 20));
        /*
         * Set the text to display in center:0x11.
         */
        this.dayOfWeek.setGravity(0x11);

        /*
         * Add the item to the this layout.
         */
        this.addView(this.imageView, new LinearLayout.LayoutParams(WIDTH, 100));
        this.addView(this.tempText, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 23));
        this.tempText.setGravity(0x11);
        this.setPadding(15, 0, 15, 0);
    }

    /*
     * Setter and Getter
     */

    public void setTempCelcius(int aTemp) {
        this.tempText.setText("" + aTemp + " �C");
    }

    public void setTempFahrenheit(int aTemp) {
        this.tempText.setText("" + aTemp + " �F");
    }

    public void setTempFahrenheitMinMax(int aMinTemp, int aMaxTemp) {
        this.tempText.setText("" + aMinTemp + "/" + aMaxTemp + " �F");
    }

    public void setTempCelciusMinMax(int aMinTemp, int aMaxTemp) {
        this.tempText.setText("" + aMinTemp + "/" + aMaxTemp + " �C");
    }

    public void setTempString(String aTempString) {
        this.tempText.setText(aTempString);
    }

    public void setImageResId(int resid) {
        this.imageView.setBackgroundResource(resid);

    }

    public void setBitmap(Bitmap bm) {
        this.imageView.setDrawingCacheEnabled(true);
        this.imageView.setImageBitmap(bm);
    }

    public Bitmap getBitmap() {
        return this.imageView.getDrawingCache();
    }

    public void setDayOfWeek(String day) {
        this.dayOfWeek.setText(day);
    }

    /*
     * I want to change the color but it seems no effect,Anybody has any idea
     * about this?
     */
    public void setTextColor(int color) {
        this.tempText.setTextColor(color);
        this.dayOfWeek.setTextColor(color);
    }
}
