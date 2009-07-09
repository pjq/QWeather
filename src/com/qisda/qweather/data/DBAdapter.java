
package com.qisda.qweather.data;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.StaticLayout;

public class DBAdapter {
    public final static String KEYID = "_id";

    public final static String TOTRANSLATETEXT = "ToTranslateText";

    public final static String TRANSLATEDTEXT = "TranslatedText";

    public final static String TABLENAME = "WeatherDataTable";

    public final static String DATABASENAME = "QWeatherDataBase";

    public final static int DATABASEVERSION = 1;

    // CurrentConditionsData;
    private String currentcondition;

    private String temp_f;

    private String temp_c;

    private String humidity;

    private String currenticon;

    private String wind_condition;

    // ForecastConditionsData
    private String day_of_week;

    private int lowTemp;

    private int highTemp;

    private String foreicon;

    private String forecondition;

    // Forecast Info
    public final static String CITY = "city";

    public final static String POSTALCODE = "postal_code";

    public final static String LATITUDE = "latitude_e6";

    public final static String LONGITUDE = "longitude_e6";

    public final static String FORECASTDATE = "forecast_date";

    public final static String CURRENTDATETIME = "current_date_time";

    public final static String UNITSYSTEM = "unit_system";

    // CurrentCOnditionsData
    public final static String CURRENTCONDITION = "currentcondition";

    public final static String TEMPF = "temp_f";

    public final static String TEMPC = "temp_c";

    public final static String HUMIDITY = "humidity";

    public final static String CURRENTICON = "currenticon";

    public final static String WINDCONDITION = "wind_condition";

    // ForecastConditionsData1
    public final static String DAYOFWEEK1 = "day_of_week1";

    public final static String LOWTEMP1 = "lowTemp1";

    public final static String HIGHTEMP1 = "highTemp1";

    public final static String FOREICON1 = "foreicon1";

    public final static String FORECONDITION1 = "forecondition1";

    // ForecastConditionsData2
    public final static String DAYOFWEEK2 = "day_of_week2";

    public final static String LOWTEMP2 = "lowTemp2";

    public final static String HIGHTEMP2 = "highTemp2";

    public final static String FOREICON2 = "foreicon2";

    public final static String FORECONDITION2 = "forecondition2";

    // ForecastConditionsData3
    public final static String DAYOFWEEK3 = "day_of_week3";

    public final static String LOWTEMP3 = "lowTemp3";

    public final static String HIGHTEMP3 = "highTemp3";

    public final static String FOREICON3 = "foreicon3";

    public final static String FORECONDITION3 = "forecondition3";

    // ForecastConditionsData4
    public final static String DAYOFWEEK4 = "day_of_week4";

    public final static String LOWTEMP4 = "lowTemp4";

    public final static String HIGHTEMP4 = "highTemp4";

    public final static String FOREICON4 = "foreicon4";

    public final static String FORECONDITION4 = "forecondition4";

    public final static String weatherString[] = new String[] {
            CITY, POSTALCODE, LATITUDE, LONGITUDE, FORECASTDATE, CURRENTDATETIME, UNITSYSTEM,
            CURRENTCONDITION, TEMPF, TEMPC, HUMIDITY, CURRENTICON, WINDCONDITION, DAYOFWEEK1,
            LOWTEMP1, HIGHTEMP1, FOREICON1, FORECONDITION1, DAYOFWEEK2, LOWTEMP2, HIGHTEMP2,
            FOREICON2, FORECONDITION2, DAYOFWEEK3, LOWTEMP3, HIGHTEMP3, FOREICON3, FORECONDITION1,
            DAYOFWEEK4, LOWTEMP4, HIGHTEMP4, FOREICON4, FORECONDITION4
    };

    private static final String CREATETABLE = "create table " + TABLENAME + " (" + KEYID
            + " integer primary key autoincrement,"

            + CITY + " text not null, " + POSTALCODE + " text not null, " + LATITUDE
            + " text not null, " + LONGITUDE + " text not null, " + FORECASTDATE
            + " text not null, " + CURRENTDATETIME + " text not null, " + UNITSYSTEM
            + " text not null, "

            + CURRENTCONDITION + " text not null, " + TEMPF + " text not null, " + TEMPC
            + " text not null, " + HUMIDITY + " text not null, " + CURRENTICON + " text not null, "
            + WINDCONDITION + " text not null, "

            + DAYOFWEEK1 + " text not null, " + LOWTEMP1 + " integer, " + HIGHTEMP1 + " integer, "
            + FOREICON1 + " text not null, " + FORECONDITION1 + " text not null, "

            + DAYOFWEEK2 + " text not null, " + LOWTEMP2 + " integer, " + HIGHTEMP2 + " integer, "
            + FOREICON2 + " text not null, " + FORECONDITION2 + " text not null, "

            + DAYOFWEEK3 + " text not null, " + LOWTEMP3 + " integer, " + HIGHTEMP3 + " integer, "
            + FOREICON3 + " text not null, " + FORECONDITION3 + " text not null, "

            + DAYOFWEEK4 + " text not null, " + LOWTEMP4 + " integer, " + HIGHTEMP4 + " integer, "
            + FOREICON4 + " text not null, " + FORECONDITION4 + " text not null);";

    private DatabaseHelper DBHelper;

    private SQLiteDatabase db;

    private Context context;

    public DBAdapter(Context context) {
        this.context = context;
        this.DBHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        // public DatabaseHelper(Context context, String name, CursorFactory
        // factory, int version)
        public DatabaseHelper(Context context) {
            super(context, DATABASENAME, null, DATABASEVERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATETABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

    public void open() throws SQLException {
        db = this.DBHelper.getWritableDatabase();
        // return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertItem(ContentValues contentValues) {
        // ContentValues contentValues = new ContentValues();
        // contentValues.put(TOTRANSLATETEXT, fromText);
        // contentValues.put(TRANSLATEDTEXT, toText);
        return db.insert(TABLENAME, null, contentValues);
    }

    public Cursor getItem(long id) {
        // db.query(TABLENAME, new String[]{}, selection, selectionArgs,
        // groupBy, having, orderBy);
        try {
            Cursor mCursor = db.query(TABLENAME, weatherString, KEYID + "=" + id, null, null, null,
                    null);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        } catch (SQLException e) {
            // TODO: handle exception
            return null;
        }
    }

    public Cursor getItem(String city) {
        // db.query(TABLENAME, new String[]{}, selection, selectionArgs,
        // groupBy, having, orderBy);
        try {

            Cursor mCursor = db.query(TABLENAME, new String[] {
                "city"
            }, CITY + "=" + "'" + city + "'", null, null, null, null);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public Cursor getAllItem() throws SQLException {
        Cursor mCursor = db.query(TABLENAME, weatherString, null, null, null, null, null);

        return mCursor;
    }

}
