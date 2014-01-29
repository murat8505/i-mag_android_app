package by.imag.app;


import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.io.File;

import by.imag.app.classes.Constants;

public class FragmentPrefs extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        String prefName = getActivity().getResources().getString(R.string.clear_cache_key);
        Preference preference = findPreference(prefName);
        assert preference != null;
        setSummaryCacheSize(preference);
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clear(getActivity().getBaseContext());
                setSummaryCacheSize(preference);
                return false;
            }
        });
    }

    private void setSummaryCacheSize(Preference preference) {
        long cacheSize = getCacheDirSize(getActivity().getBaseContext());
        preference.setSummary(cacheSize/1024/1024+" Mb");
    }

    private void clear(Context context) {
        File dir = context.getCacheDir();
        if (dir != null && dir.isDirectory()) {
            deleteDir(dir);
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] subDirs = dir.list();
            for (String s: subDirs) {
                boolean result = deleteDir(new File(dir, s));
                if (!result) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private long getCacheDirSize(Context context) {
        long size = 0;
        File dir = context.getCacheDir();
        if (dir != null && dir.isDirectory()) {
            size = getDirSize(dir);
        }
        return size;
    }

    private long getDirSize(File dir) {
        long length = 0;
        if (dir != null && dir.isDirectory()) {
            for (File file: dir.listFiles()) {
                if (file.isFile()) {
                    length += file.length();
                } else {
                    length += getDirSize(file);
                }
            }
        }
        return length;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }
}
