package its.madruga.warevamp.ui.fragments.core;

import static android.content.Context.MODE_WORLD_READABLE;

import static its.madruga.warevamp.core.Receivers.sendNeedRebootWpp;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import its.madruga.warevamp.BuildConfig;

public class BasePreferenceActivity extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressLint("WorldReadableFiles")
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
        getPreferenceManager().setSharedPreferencesName(BuildConfig.APPLICATION_ID + "_preferences");
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String s) {
        sendNeedRebootWpp();
    }

    public ActionBar getSupportActionBar() {
        if (getActivity() == null) return null;
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}
