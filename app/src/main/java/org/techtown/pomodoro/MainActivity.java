package org.techtown.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import lib.kingja.switchbutton.SwitchMultiButton;

public class MainActivity extends AppCompatActivity {

    private TextView realtime;
    private Timer realtimeTimer;

    SwitchMultiButton switchButton;

    public int total_time = 0;

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

        // Topic 3 : 툴바(상단 메뉴바)에 아이콘 추가 및 화면 이동 ####################################
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ActionBar actionbar = getSupportActionBar();
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

    // Topic 3 : 툴바(상단 메뉴바)에 아이콘 추가 및 화면 이동 #########################################
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_weekly:
                Intent weeklyIntent = new Intent(this, Weekly.class);
                startActivity(weeklyIntent);
                break;

            case R.id.menu_settings:
                Intent settingIntent = new Intent(this, Settings.class);
                startActivity(settingIntent);
                break;
        }

        return true;
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
        realtimeTimer = new Timer();  // 타이머를 화면 이동시 새로 생성해주지 않으면 오류
        realtimeTimer.schedule(timerTask, 500, 3000);
        super.onResume();
    }
}