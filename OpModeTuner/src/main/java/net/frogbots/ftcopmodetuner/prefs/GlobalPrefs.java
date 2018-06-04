package net.frogbots.ftcopmodetuner.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class GlobalPrefs implements SharedPreferences, SharedPreferences.Editor
{
    private static GlobalPrefs singletonInstance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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

    @Override
    public Map<String, ?> getAll()
    {
        return preferences.getAll();
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue)
    {
        return preferences.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues)
    {
        return preferences.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue)
    {
        return preferences.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue)
    {
        return preferences.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue)
    {
        return preferences.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue)
    {
        return preferences.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key)
    {
        return preferences.contains(key);
    }

    @Override
    public Editor edit()
    {
        return preferences.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
    {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
    {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    //-----------------------------------------------------
    // EDITOR
    //------------------------------------------------------

    @Override
    public Editor putString(String key, @Nullable String value)
    {
        return editor.putString(key, value);
    }

    @Override
    public Editor putStringSet(String key, @Nullable Set<String> values)
    {
        return editor.putStringSet(key, values);
    }

    @Override
    public Editor putInt(String key, int value)
    {
        return editor.putInt(key, value);
    }

    @Override
    public Editor putLong(String key, long value)
    {
        return editor.putLong(key, value);
    }

    @Override
    public Editor putFloat(String key, float value)
    {
        return editor.putFloat(key, value);
    }

    @Override
    public Editor putBoolean(String key, boolean value)
    {
        return editor.putBoolean(key, value);
    }

    @Override
    public Editor remove(String key)
    {
        return editor.remove(key);
    }

    @Override
    public Editor clear()
    {
        return editor.clear();
    }

    @Override
    public boolean commit()
    {
        return editor.commit();
    }

    @Override
    public void apply()
    {
        editor.apply();
    }
}
