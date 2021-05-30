package org.techtown.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import lib.kingja.switchbutton.SwitchMultiButton;

public class MainActivity extends AppCompatActivity {

    private TextView realtime;
    private Timer realtimeTimer;

    SwitchMultiButton switchButton;

    StopWatchFragment stopWatchFragment;
    TimerFragment timerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Topic 1 : 현재 시각 실시간 출력 ##########################################################
        realtime = (TextView) findViewById(R.id.realtime);
        MainTimerTask timerTask = new MainTimerTask();
        realtimeTimer = new Timer();
        realtimeTimer.schedule(timerTask, 500, 1000);
        // #########################################################################################

        // Topic 2 : 스위치 버튼을 사용하여 프래그먼트 전환 ###########################################
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_change, new StopWatchFragment());
        fragmentTransaction.commit();
        switchButton = (SwitchMultiButton) findViewById(R.id.switchButton);
        switchButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Fragment fr;

                if (position == 0) {
                    fr = new StopWatchFragment();

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_change, fr);
                    fragmentTransaction.commit();

                    // Toast.makeText(getApplicationContext(), tabText, Toast.LENGTH_SHORT).show();

                } else if (position == 1) {
                    fr = new TimerFragment();

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_change, fr);
                    fragmentTransaction.commit();

                    // Toast.makeText(getApplicationContext(), tabText, Toast.LENGTH_SHORT).show();
                }

            }
        });
        // #########################################################################################

    }

    // Topic 1 : 현재 시각 실시간 출력 ##############################################################
    private Handler realtimeHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일   hh : mm : ss ");
            String dateString = formatter.format(rightNow);
            realtime.setText(dateString);

        }
    };
    class MainTimerTask extends TimerTask {
        public void run() {
            realtimeHandler.post(mUpdateTimeTask);
        }
    }
    // #############################################################################################


    @Override
    protected void onDestroy() {
        realtimeTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        realtimeTimer.cancel();
        super.onPause();
    }

    @Override
    protected void onResume() {
        MainTimerTask timerTask = new MainTimerTask();
        realtimeTimer.schedule(timerTask, 500, 3000);
        super.onResume();
    }
}