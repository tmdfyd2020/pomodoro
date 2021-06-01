package org.techtown.pomodoro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimerFragment extends Fragment {

    public static final int fixed_time = 5;  // 타이머 설정 시간
    int timerTime = fixed_time;

    private TextView total_time, timer;
    private Button bt_timer_start, bt_timer_save;

    private TimerHandler timerHandler;

    boolean isRunning = true;
    public static final int INIT = 0;
    public static final int RUN = 1;
    public static final int PAUSE = 2;
    int status = INIT;

    boolean timer_out = false;  // 타이머가 완전히 끝났음을 알리는 장치

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_timer, container, false);

        MainActivity activity = (MainActivity) getActivity();
        total_time = (TextView) activity.findViewById(R.id.total);  // 액티비티 누적 시간

        timer = (TextView)  rootView.findViewById(R.id.timer);
        bt_timer_start = (Button) rootView.findViewById(R.id.bt_timer_start);
        bt_timer_save = (Button)  rootView.findViewById(R.id.bt_timer_save);
        bt_timer_save.setEnabled(false);  // 비활성화

        timerHandler = new TimerHandler();

        bt_timer_start.setOnClickListener(onClickListener);
        bt_timer_save.setOnClickListener(onClickListener);

        show_time(timerTime);

        return rootView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.bt_timer_start:  // [ 시작 / 중지 ] 버튼을 누를 때,
                    if(status == INIT || status == PAUSE) {  // 타이머가 돌아가고 있지 않은 상태라면("초기 상태", "중지 상태"), 타이머를 "실행"시키기
                        timerHandler.sendEmptyMessage(0);
                    }
                    else if(status == RUN) {  // 타이머가 "실행" 중이라면, 타이머를 중지 시키기
                        timerHandler.sendEmptyMessage(1);
                    }

                    break;

                case R.id.bt_timer_save:  // [ 저장 / 초기화 ] 버튼을 누를 때,
                    timerHandler.sendEmptyMessage(2);

                    break;
            }
        }
    };

    class TimerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case 0:  // 타이머가 돌아가고 있지 않은 상태("초기 상태", "중지 상태")에서 타이머를 "실행"시켰을 때,
                    if(timerTime == 0) {  // 타이머에 맞춰 놓은 시간이 끝나면
                        timer_out = true;

                        show_time(timerTime);  // 지정했던 타이머 시각으로 다시 맞춰주기
                        save(fixed_time);  // 공부한 시간을 총 시간에 저장

                        bt_timer_start.setEnabled(false);
                        bt_timer_save.setEnabled(true);
                        bt_timer_save.setText("초기화");

                        removeMessages(0);

                        break;
                    }
                    status = RUN;

                    bt_timer_start.setText("중지");
                    bt_timer_save.setEnabled(false);

                    sendEmptyMessageDelayed(0, 1000);
                    show_time(timerTime--);

                    break;

                case 1:  // 타이머가 "실행" 상태에서 타이머를 중지 시켰을 때,
                    status = PAUSE;

                    removeMessages(0);  // 타이머 메시지 삭제

                    show_time(timerTime + 1);

                    bt_timer_start.setText("시작");
                    bt_timer_save.setEnabled(true);

                    break;

                case 2:  // [ 저장 / 초기화 ] 버튼을 누를 때,
                    if(timer_out == true) {  // 지정한 타이머가 완전히 끝나서 [ 초기화 ] 버튼을 누르면,
                        bt_timer_start.setEnabled(true);
                        bt_timer_save.setText("저장");

                        timer_out = false;
                    }

                    else if(timer_out == false) {  // 실행 중인 타이머를 중지 시킨 후, [ 저장 ] 버튼을 누르면,
                        save(fixed_time - timerTime - 1);
                    }

                    removeMessages(0);  // 타이머 메시지 삭제

                    timerTime = fixed_time;
                    show_time(timerTime);

                    bt_timer_start.setText("시작");
                    bt_timer_save.setEnabled(false);

                    status = INIT;

                    break;
            }
        }
    }

    public void show_time(int timerTime) {
        int hour, minute, second;

        hour = timerTime / 3600;
        minute = (timerTime / 60) % 60;
        second = timerTime % 60;

        timer.setText(String.format("%02d : %02d : %02d", hour, minute, second));
    }

    public void save(int timer_time) {
        int hour, minute, second;

        MainActivity activity = (MainActivity) getActivity();
        activity.total_time += timer_time;
        int time = activity.total_time;

        hour = time / 3600;
        minute = (time / 60) % 60;
        second = time % 60;

        if(hour != 0) {
            total_time.setText("총 공부 시간  " + String.format("%d시간 %d분 %d초", hour, minute, second));
        }
        else if((hour == 0) && (minute != 0)) {
            total_time.setText("총 공부 시간  " + String.format("%d분 %d초", minute, second));
        }
        else {
            total_time.setText("총 공부 시간  " + String.format("%d초", second));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(timerHandler != null) {
            timerHandler.removeMessages(0);
        }
        isRunning = false;
    }
}