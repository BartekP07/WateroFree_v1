package app.watero.waterofree.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.watero.waterofree.database.DBContract.*;
import app.watero.waterofree.helpers.Methods;


public class MyDBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Watero.db";
    private static final int DATABASE_VERSION = 3;

    private Methods methods = new Methods();
    private String today = methods.toDayDate();


    // Table Create Statements
    // Todo table create statement
    private static final String informationTab = "CREATE TABLE " +
            DBEntry.TABLE_INFORMATION + "(" +
            DBEntry.INFORMATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBEntry.COLUMN_IS_ACTIVE_DAY + " INTEGER, " +
            DBEntry.COLUMN_ACTIVE_DAY_DATE + " TEXT); ";

    // Table Create Statements
    // Todo table create statement
    private static final String drinksTab = "CREATE TABLE " +
            DBEntry.TABLE_DRINKS + "(" +
            DBEntry.DRINKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBEntry.COLUMN_DRINK_TYPE + " INTEGER, " +
            DBEntry.COLUMN_TIME + " TEXT, " +
            DBEntry.COLUMN_QUANTITY_DRINKED + " INTEGER, " +
            DBEntry.COLUMN_DRINKHYDRATION + " INTEGER," +
//          DBEntry.COLUMN_DRINKED_DAY + " INTEGER, " +
//          DBEntry.COLUMN_TARGET_DAY + " INTEGER, " +
            DBEntry.COLUMN_DATE + " TEXT, " +
            DBEntry.COLUMN_FINALY_DRINKED_ML + " INTEGER); ";

    // Table Create Statements
    // Todo table create statement
    private static final String settingsTab = "CREATE TABLE " +
            DBEntry.TABLE_SETTINGS + "(" +
            DBEntry.SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBEntry.SEX_COLUMN + " INTEGER, " +
            DBEntry.WEIGHT_COLUMN + " INTEGER, " +
            DBEntry.DAY_TARGET_COLUMN + " INTEGER, " +
            DBEntry.ACTIVE_DAY_TARGET_COLUMN + " INTEGER, " +
            DBEntry.NOTIFICATION_COLUMN + " INTEGER, " +
            DBEntry.WAKEUP_COLUMN + " TEXT, " +
            DBEntry.GOTOSLEEP_COLUMN + " TEXT, " +
            DBEntry.FREQUENCY_COLUMN + " INTEGER, " +
            DBEntry.ONBOARDING_COLUMN + " INTEGER, " +
            DBEntry.QUESTIONNAIRE_COLUMN + " INTEGER); ";
    // LANGUAGE_COLUMN + " TEXT); ";

    //TABLE HISTORY DAY SETTINGS
    private static final String historyDayTab = "CREATE TABLE " +
            DBEntry.TABLE_HISTORY_DAY_INFO + "(" +
            DBEntry.HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBEntry.HISTORY_COLUMN_DAY_NUMBER + " INTEGER, " +
            DBEntry.HISTORY_DATE + " INTEGER, " +
            DBEntry.HISTORY_QUANTITY + " INTEGER, " +
            DBEntry.HISTORY_PRECENTAGES + " INTEGER, " +
            DBEntry.HISTORY_ISACTIVE_DAY + " INTEGER, " +
            DBEntry.HISTORY_DAY_TARGET + " INTEGER); ";


    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(drinksTab);
        db.execSQL(informationTab);
        db.execSQL(settingsTab);
        db.execSQL(historyDayTab);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.TABLE_DRINKS);
        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.TABLE_INFORMATION);
        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.TABLE_HISTORY_DAY_INFO);

        // create new tables
        onCreate(db);
    }

    public Cursor readAllHistoryData() {
        String query = "SELECT " + "  " + DBEntry.HISTORY_ID + " , " + DBEntry.HISTORY_COLUMN_DAY_NUMBER + " , " + DBEntry.HISTORY_DATE + " , "
                + DBEntry.HISTORY_QUANTITY + " , " + DBEntry.HISTORY_PRECENTAGES + " , " + DBEntry.HISTORY_ISACTIVE_DAY + " FROM "
                + DBEntry.TABLE_HISTORY_DAY_INFO + " ORDER BY " + DBEntry.HISTORY_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean checkHistoryData() {

        // "'" + today + "'"
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + "*" + " FROM " + DBEntry.TABLE_HISTORY_DAY_INFO + " WHERE " + DBEntry.HISTORY_DATE + " = " + today;
        Cursor cursor = database.rawQuery(selectQuery, null);
       if(cursor.getCount() <= 0){
           cursor.close();
           return false;
       }
       cursor.close();
       return true;
    }

    public void updateHistoryData(int quantity, int precentages, int activeDay, int dayTarget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.HISTORY_QUANTITY, quantity);
        cv.put(DBEntry.HISTORY_PRECENTAGES, precentages);
        cv.put(DBEntry.HISTORY_ISACTIVE_DAY, activeDay);
        cv.put(DBEntry.HISTORY_DAY_TARGET, dayTarget);
        db.update(DBEntry.TABLE_HISTORY_DAY_INFO, cv, DBEntry.HISTORY_DATE + " = ?", new String[]{today});
    }

    public void addHistoryData(int dayNumber, String data, int quantity, int precentages, int activeDay, int dayTarget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.HISTORY_COLUMN_DAY_NUMBER, dayNumber);
        cv.put(DBEntry.HISTORY_DATE, data);
        cv.put(DBEntry.HISTORY_QUANTITY, quantity);
        cv.put(DBEntry.HISTORY_PRECENTAGES, precentages);
        cv.put(DBEntry.HISTORY_ISACTIVE_DAY, activeDay);
        cv.put(DBEntry.HISTORY_DAY_TARGET, dayTarget);
        db.insert(DBEntry.TABLE_HISTORY_DAY_INFO, null, cv);
    }

    //INSERT DATA
    public void updateSettings(String row_id, int sex, int weight, int dayTarget, int dayActive, int notification, String wakeup, String sleep, int frequency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBEntry.SEX_COLUMN, sex);
        cv.put(DBEntry.WEIGHT_COLUMN, weight);
        cv.put(DBEntry.DAY_TARGET_COLUMN, dayTarget);
        cv.put(DBEntry.ACTIVE_DAY_TARGET_COLUMN, dayActive);
        cv.put(DBEntry.NOTIFICATION_COLUMN, notification);
        cv.put(DBEntry.WAKEUP_COLUMN, wakeup);
        cv.put(DBEntry.GOTOSLEEP_COLUMN, sleep);
        cv.put(DBEntry.FREQUENCY_COLUMN, frequency);
        db.update(DBEntry.TABLE_SETTINGS, cv, DBEntry.SETTINGS_ID + " = ?", new String[]{row_id});
    }

    public void updateQuestionarySettings(String row_id, int sex, int weight, int dayTarget, int activeDayTarget, int questionaryActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.SEX_COLUMN, sex);
        cv.put(DBEntry.WEIGHT_COLUMN, weight);
        cv.put(DBEntry.DAY_TARGET_COLUMN, dayTarget);
        cv.put(DBEntry.ACTIVE_DAY_TARGET_COLUMN, activeDayTarget);
        cv.put(DBEntry.QUESTIONNAIRE_COLUMN, questionaryActive);
        db.update(DBEntry.TABLE_SETTINGS, cv, DBEntry.SETTINGS_ID + " = ?", new String[]{row_id});
    }

    public void addDrink(int drinkName, String time, int ml, int hydration, String date, int drinked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.COLUMN_DRINK_TYPE, drinkName);
        cv.put(DBEntry.COLUMN_TIME, time);
        cv.put(DBEntry.COLUMN_QUANTITY_DRINKED, ml);
        cv.put(DBEntry.COLUMN_DRINKHYDRATION, hydration);
        cv.put(DBEntry.COLUMN_DATE, date);
        cv.put(DBEntry.COLUMN_FINALY_DRINKED_ML, drinked);

        db.insert(DBEntry.TABLE_DRINKS, null, cv);
    }

    public void updateVersion(String version) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.COLUMN_VERSION, version);
        db.insert(DBEntry.TABLE_INFORMATION, null, cv);
    }

    public void updateInforamtion(int activeDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.COLUMN_IS_ACTIVE_DAY, activeDay);

        db.update(DBEntry.TABLE_INFORMATION, cv, DBEntry.COLUMN_ACTIVE_DAY_DATE + " =? ", new String[]{today});
    }

    public void addActiveInformation(int activeDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.COLUMN_IS_ACTIVE_DAY, activeDay);
        cv.put(DBEntry.COLUMN_ACTIVE_DAY_DATE, today);

        db.insert(DBEntry.TABLE_INFORMATION, null, cv);
    }

    public void deleteOneRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBEntry.TABLE_DRINKS, DBEntry.DRINKS_ID + "=" + id, null);

    }

    public int isActiveDay() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + " (" + DBEntry.COLUMN_IS_ACTIVE_DAY + ") " + " AS " + "active_day " + "FROM " + DBEntry.TABLE_INFORMATION +
                " WHERE " + DBEntry.COLUMN_ACTIVE_DAY_DATE + " = " + "'" + today + "'";
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public void offOnBoarding(int onboarding) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.ONBOARDING_COLUMN, onboarding);
        // update
        db.insert(DBEntry.TABLE_SETTINGS, null, cv);
        // where settings_id = 1
    }

    public void isQuestionaireActive(int active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.QUESTIONNAIRE_COLUMN, active);
        // update
        db.insert(DBEntry.TABLE_SETTINGS, null, cv);
        // where settings_id = 1
    }

    public void updateLanguage(String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.LANGUAGE_COLUMN, language);
        db.update(DBEntry.TABLE_SETTINGS, cv, DBEntry.LANGUAGE_COLUMN + " =? ", new String[]{"1"});
    }

    public String getLanguage() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.LANGUAGE_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS + " WHERE settings_id = 1";
        String result = null;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return result;
    }


    public int getOnBoarding() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.ONBOARDING_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS + " WHERE settings_id = 1";
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public Cursor redAllDataHistory() {
        String query = "SELECT * FROM " + DBEntry.TABLE_DRINKS + " ORDER BY " + DBEntry.DRINKS_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public int getSex() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.SEX_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public int getQuestionary() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.QUESTIONNAIRE_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public int getWeight() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.WEIGHT_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public int getDayTarget() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.DAY_TARGET_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public int getAtiveDay() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.ACTIVE_DAY_TARGET_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public int getNotification() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.NOTIFICATION_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public String getWakeUp() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.WAKEUP_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        String result = null;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public String getVersion() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.COLUMN_VERSION + " FROM " + DBEntry.TABLE_INFORMATION;
        String result = null;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public String getSleepTime() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.GOTOSLEEP_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        String result = null;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public int getFrequency() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT " + DBEntry.FREQUENCY_COLUMN + " FROM " + DBEntry.TABLE_SETTINGS;
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public int getToDayDrinked_ml() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT SUM " + " (" + DBEntry.COLUMN_FINALY_DRINKED_ML + ") " + " AS " + "ml_sum " + "FROM " + DBEntry.TABLE_DRINKS + " WHERE " +
                DBEntry.COLUMN_DATE + " = " + "'" + today + "'";
        int result = 0;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return result;
    }

}

