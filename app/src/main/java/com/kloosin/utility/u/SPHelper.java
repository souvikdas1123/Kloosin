package com.kloosin.utility.u;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SPHelper {
    private final String LOG_TAG = SPHelper.class.getSimpleName();
    private static SPHelper mInstance;
    private static Context _context;

    public static SPHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = newInstance(context);
        }
        return mInstance;
    }

    private static SPHelper newInstance(Context context) {
        _context = context;
        return new SPHelper();
    }

    public void write(String key, String value) {
        Log.i(LOG_TAG, key + " >==>> " + value);
        SharedPreferences sharedPreferences = ((Activity) _context).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String read(String key) {
        SharedPreferences sharedPreferences = ((Activity) _context).getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public boolean readBoolean(String key) {
        try {
            SharedPreferences sharedPreferences = ((Activity) _context).getPreferences(Context.MODE_PRIVATE);
            return sharedPreferences.getBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void writeBoolean(String key, boolean value) {
        Log.i(LOG_TAG, key + " >==>> " + value);
        SharedPreferences sharedPreferences = ((Activity) _context).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public void clear(String key) {
        write(key, "");
    }
}
