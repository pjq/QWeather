
package com.qisda.qweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.KeyEvent.*;
import android.view.KeyEvent;

/*
 * This class is used to store the settings
 * Here is use EditTextPreference to edit and store the settings.
 * */
public class Settings extends PreferenceActivity {

    public final int bCITY1 = 1;

    public final int bCITY2 = 2;

    private EditTextPreference city1;

    private EditTextPreference city2;

    private PreferenceScreen root;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPreferenceScreen(createPreferenceScreen());

    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRestart() {
        super.onDestroy();

    }

    public PreferenceScreen createPreferenceScreen() {
        this.root = getPreferenceManager().createPreferenceScreen(this);

        this.root.setTitle(getString(R.string.setting));

        String city1 = getString(R.string.city1);
        String city2 = getString(R.string.city2);

        this.city1 = new EditTextPreference(this);
        this.city1.setKey("city1");
        this.city1.setTitle(city1);
        this.city1.setDialogTitle(city1);
        this.city1.getEditText().setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                updateDisplaySettings(bCITY1);
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        return true;
                    default:
                        break;

                }
                return false;
            }
        });

        this.city2 = new EditTextPreference(this);
        this.city2.setKey("city2");
        this.city2.setTitle(city2);
        this.city2.setDialogTitle(city2);
        this.city2.getEditText().setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                // updateDisplaySettings(bCITY2);
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        return true;
                    default:
                        break;

                }
                return false;
            }
        });

        this.city1.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                updateDisplaySettings(bCITY1);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

        });

        this.city2.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                updateDisplaySettings(bCITY2);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

        });

        this.root.addPreference(this.city1);
        this.root.addPreference(this.city2);

        this.city1.setSummary(getPreferences("city1"));
        this.city2.setSummary(getPreferences("city2"));

        return this.root;

    }

    public void updateDisplaySettings(int city) {
        switch (city) {
            case 1:
                String text1 = this.city1.getEditText().getText().toString();
                if (text1 == "") {
                    this.city1.setSummary(getString(R.string.notSet));
                } else {
                    this.city1.setSummary(text1);
                }

                break;
            case 2:
                String text2 = this.city2.getEditText().getText().toString();
                if (text2 == "") {
                    this.city2.setSummary(getString(R.string.notSet));
                } else {
                    this.city2.setSummary(text2);
                }
                break;
            default:
                break;
        }

    }

    public String getPreferences(String keyCode) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String preferences = settings.getString(keyCode, getString(R.string.notSet));
        if (preferences.equals("")) {
            return getString(R.string.notSet);
        } else {
            return preferences;
        }
    }

}
