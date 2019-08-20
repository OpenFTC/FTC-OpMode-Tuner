package net.frogbots.ftcopmodetuner.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.Map;
import java.util.Set;

public class GlobalPrefs
{
    private static GlobalPrefs singletonInstance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Resources res;

    private GlobalPrefs(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public static void initInstance(Context context)
    {
        if(singletonInstance == null)
        {
            singletonInstance = new GlobalPrefs(context);
            singletonInstance.res = context.getResources();
        }
        else
        {
            throw new RuntimeException();
        }
    }

    public static GlobalPrefs getInstance()
    {
        if(singletonInstance == null)
        {
            throw new RuntimeException();
        }

        return singletonInstance;
    }

    private String key(int key)
    {
        return res.getString(key);
    }

    public Map<String, ?> getAll()
    {
        return preferences.getAll();
    }

    @Nullable
    public String getString(@StringRes int key_id, @Nullable String defValue)
    {
        return preferences.getString(key(key_id), defValue);
    }

    @Nullable
    public Set<String> getStringSet(@StringRes int key_id, @Nullable Set<String> defValues)
    {
        return preferences.getStringSet(key(key_id), defValues);
    }

    public int getInt(@StringRes int key_id, int defValue)
    {
        return preferences.getInt(key(key_id), defValue);
    }

    public long getLong(@StringRes int key_id, long defValue)
    {
        return preferences.getLong(key(key_id), defValue);
    }

    public float getFloat(@StringRes int key_id, float defValue)
    {
        return preferences.getFloat(key(key_id), defValue);
    }

    public boolean getBoolean(@StringRes int key_id, boolean defValue)
    {
        return preferences.getBoolean(key(key_id), defValue);
    }

    public boolean contains(@StringRes int key_id)
    {
        return preferences.contains(key(key_id));
    }

    public GlobalPrefs edit()
    {
        preferences.edit();
        return this;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    //-----------------------------------------------------
    // EDITOR
    //------------------------------------------------------
    
    public GlobalPrefs putString(@StringRes int key_id, @Nullable String value)
    {
        editor.putString(key(key_id), value);
        return this;
    }
    
    public GlobalPrefs putStringSet(@StringRes int key_id, @Nullable Set<String> values)
    {
        editor.putStringSet(key(key_id), values);
        return this;
    }
    
    public GlobalPrefs putInt(@StringRes int key_id, int value)
    {
        editor.putInt(key(key_id), value);
        return this;
    }
    
    public GlobalPrefs putLong(@StringRes int key_id, long value)
    {
        editor.putLong(key(key_id), value);
        return this;
    }
    
    public GlobalPrefs putFloat(@StringRes int key_id, float value)
    {
        editor.putFloat(key(key_id), value);
        return this;
    }
    
    public GlobalPrefs putBoolean(@StringRes int key_id, boolean value)
    {
        editor.putBoolean(key(key_id), value);
        return this;
    }
    
    public void remove(@StringRes int key_id)
    {
        editor.remove(key(key_id));
    }
    
    public void clear()
    {
        editor.clear();
    }

    public boolean commit()
    {
        return editor.commit();
    }


    public void apply()
    {
        editor.apply();
    }
}
