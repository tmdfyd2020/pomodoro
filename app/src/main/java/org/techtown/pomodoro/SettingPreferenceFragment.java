package org.techtown.pomodoro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.core.content.SharedPreferencesCompat;

/* 환경 설정 관련 class */

public class SettingPreferenceFragment extends PreferenceFragment {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    PreferenceScreen soundScreen;
    ListPreference soundList;
    ListPreference timerList;

    boolean sound_activate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        soundScreen = (PreferenceScreen) findPreference("sound_screen");  // 소리
        soundList = (ListPreference) findPreference("sound_list");  // 백색 소음 선택
        timerList = (ListPreference) findPreference("setting_timer");  // 타이머 시간 설정

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();

        if(prefs.getBoolean("sound_screen", false)) {
            soundScreen.setSummary("비활성화");
        }
        else {
            soundScreen.setSummary("활성화");
        }

        if(!prefs.getString("sound_list", "").equals("")) {
            soundList.setSummary(prefs.getString("sound_list", "sound1"));  //
        }

        if(!prefs.getString("setting_timer", "").equals("")) {
            timerList.setSummary(prefs.getString("setting_timer", "30"));
        }

        prefs.registerOnSharedPreferenceChangeListener(prefListener);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("sound_activate")) {
                sound_activate = prefs.getBoolean("sound_activate", false);
                editor.putBoolean("sound_activate", sound_activate);
                if(sound_activate) {
                    soundScreen.setSummary("활성화");
                }
                else {
                    soundScreen.setSummary("비활성화");
                }

                ((BaseAdapter) getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            }

            if(key.equals("sound_list")) {
                String sound = prefs.getString("sound_list", "sound1");
                editor.putString("sound_list", sound);
                soundList.setSummary(sound);  // 요약 부분을 선택한 것에 따라서 바꿔줌
                // Toast.makeText(getActivity(), "소리 : " + sound, Toast.LENGTH_SHORT).show();
                // 여기서 선택된 것을 메인 액티비티로 보내야됨
            }

            if(key.equals("setting_timer")) {
                String timer_time = prefs.getString("setting_timer", "30");
                timerList.setSummary(timer_time);
                // int timer_time_int = Integer.parseInt(timer_time);
                editor.putString("setting_timer", timer_time);

            }
        }
    };
}
