package com.qisda.qweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.*;
import android.view.*;
import android.view.KeyEvent.*;
import android.view.KeyEvent;

public class SearchDialog extends Activity implements OnClickListener,
		OnKeyListener
{
	private AutoCompleteTextView	inputCity;
	private ImageButton				searchButton;
	public static final String		WEATHERDATA	= "WeatherData";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.searchdialog);
		searchButton = (ImageButton) findViewById(R.id.searchDialogButton);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, CITYNAMES);
		inputCity = (AutoCompleteTextView) findViewById(R.id.searchDialogInput);
		inputCity.setAdapter(adapter);
		inputCity.setOnKeyListener(this);
		// inputCity.setFocusable(true);
		searchButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.searchDialogButton:
			String cityName = inputCity.getText().toString();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("city", cityName);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			SearchDialog.this.finish();
			break;

		case R.id.searchDialogInput:
			inputCity.setText("");
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		/*
		 * If you press enter.
		 */
		case KeyEvent.KEYCODE_ENTER:
			String cityName = inputCity.getText().toString();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("city", cityName);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);

			SearchDialog.this.finish();
			return true;
			// break;

		default:
			break;

		}
		return false;
	}

	/*
	 * You can add more cities here.
	 */
	static final String[]	CITYNAMES	= new String[]
										{ "suzhou", "shanghai", "wuxi",
			"nanjing", "wuchang", "beijing", "nanchang", "wuhang", "changzhou",
			"xuzhou", "kunshan", "hangzhou" };
}
