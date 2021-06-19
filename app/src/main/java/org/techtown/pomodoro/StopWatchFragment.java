package org.techtown.pomodoro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StopWatchFragment extends Fragment {

    private TextView total_time, stopwatch;
    private Button bt_stopwatch_start, bt_stopwatch_save;

    // 상태를 표시하는 '상수' 지정 - 각각의 숫자는 독립적인 개별 '상태' 의미
    private static final int INIT = 0;   // 처음 상태
    private static final int RUN = 1;    // 실행 중인 상태
    private static final int PAUSE = 2;  // 중지 상태
    private static int status = INIT;    // 상태를 처음 상태로 초기화

    // 스톱워치 시간 값을 저장할 변수
    private long baseTime, pauseTime;

    // 스톱워치의 총 시간을 저장할 변수
    private long storeTime = 0;

    MediaPlayer mediaPlayer;
    int pausePosition;
    String sound;
    boolean sound_pass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stopwatch, container, false);

        MainActivity activity = (MainActivity) getActivity();
        total_time = (TextView) activity.findViewById(R.id.total);  // 액티비티 누적 시간

        stopwatch = (TextView) rootView.findViewById(R.id.stopwatch);  // 실시간 스톱워치 표시
        bt_stopwatch_start = (Button) rootView.findViewById(R.id.bt_stopwatch_start);  // [ 시작 / 중지 ] 버튼
        bt_stopwatch_save = (Button) rootView.findViewById(R.id.bt_stopwatch_save);  // [ 저장 ] 버튼
        bt_stopwatch_save.setEnabled(false);  // 비활성화

        bt_stopwatch_start.setOnClickListener(onClickListener);
        bt_stopwatch_save.setOnClickListener(onClickListener);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sound = preferences.getString("sound_list", "sound1");
        sound_pass = preferences.getBoolean("sound_activate", false);
        mediaPlayer = null;

        return rootView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_stopwatch_start:
                    startButton();
                    break;

                case R.id.bt_stopwatch_save:
                    saveButton();
                    break;
            }
        }
    };

    private void startButton() {
        switch (status) {
            case INIT:  // "00 : 00 : 00인 상태"일 때 [ 시작 ] 버튼을 누르면,
                baseTime = SystemClock.elapsedRealtime();  // 부팅 이후의 시간을 리턴, sleep으로 소모된 시간이 포함된다.

                handler.sendEmptyMessage(0); /*
                  ○ sendEmptyMessage : Message 클래스 변수 중 what 멤버만 채워진 Message 객체 전달
                  ○ int what : 메시지 종류 식별을 위한 사용자 정의 메시지 코드 */
                bt_stopwatch_start.setText("중지");  // [ 시작 ] 버튼이 [ 중지 ] 버튼으로 바뀜

                status = RUN;  // 스톱워치가 "실행 중인 상태"로 전환

                if(sound_pass == true) {
                    playMusic();
                }
                break;

            case RUN:  // 스톱 워치가 "실행되고 있는 상태"일 때 [ 중지 ] 버튼을 누르면,
                handler.removeMessages(0);  // 핸들러 정지

                pauseTime = SystemClock.elapsedRealtime();  // 정지 시간 체크

                bt_stopwatch_save.setEnabled(true);
                bt_stopwatch_start.setText("계속");

                status = PAUSE;  // "중지 상태"로 전환

                if(sound_pass == true) {
                    pauseMusic();
                }

                break;

            case PAUSE:  // "스톱워치가 중지 된 상태"일 때 [ 계속 ] 버튼을 누르면,
                long reStart = SystemClock.elapsedRealtime();
                baseTime += (reStart - pauseTime);  // [ 중지 ] 버튼을 누른 후 [ 계속 ] 버튼을 누르기까지 쉬는 시간을 제외시킨다.

                handler.sendEmptyMessage(0);

                bt_stopwatch_start.setText("중지");
                bt_stopwatch_save.setEnabled(false);

                status = RUN;  // "진행 중인 상태"로 전환

                if(sound_pass == true) {
                    playMusic();
                }

                break;
        }
    }

    private void saveButton() {
        if(status == PAUSE) {  // "스톱워치가 중지 된 상태"일 때 [ 저장 ] 버튼을 누르면,
            storeTime = pauseTime - baseTime;  /*
              ○ < 최종 pauseTime >에서 < 쉬는 시간을 추가한 baseTime >을 뺀 < 순수 학습 시간 >
              ○ 밀리 초 단위  */
            int convert_second = (int) storeTime / 1000;  // 밀리 초 단위를 초 단위로 변환

            MainActivity activity = (MainActivity) getActivity();
            activity.total_time += convert_second;  // 액티비티에 누적 시간 저장
            int time = activity.total_time;

            int hour = time / 3600;
            int minute = (time / 60) % 60;
            int second = time % 60;

            if(hour != 0) {
                total_time.setText(String.format("%d시간 %d분 %d초", hour, minute, second));
            }
            else if((hour == 0) && (minute != 0)) {
                total_time.setText(String.format("%d분 %d초", minute, second));
            }
            else {
                total_time.setText(String.format("%d초", second));
            }

            bt_stopwatch_start.setText("시작");
            bt_stopwatch_save.setEnabled(false);
            stopwatch.setText("00 : 00 : 00");

            baseTime = 0;
            pauseTime = 0;
            status = INIT;

            if(sound_pass == true) {
                stopMusic();
            }
        }

    }

    public String getTime() {  // 스톱워치 실시간 시간
        long nowTime = SystemClock.elapsedRealtime();
        long overTime = nowTime - baseTime;
        int convert_second = (int) overTime / 1000;

        /* < 밀리초 분, 초, 밀리초 처리 기능 >
        long m = overTime / 1000 / 60;
        long s = (overTime / 1000) % 60;
        long ms = overTime % 1000;
        */

        int hour = convert_second / 3600;
        int minute = (convert_second / 60) % 60;
        int second = convert_second % 60;

        String recTime = String.format("%02d : %02d : %02d", hour, minute, second);

        return recTime;
    }

    public void playMusic() {
        if(mediaPlayer == null) {
            if(sound.equals("sound1")) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound1);
                mediaPlayer.start();
            }
            else if(sound.equals("sound2")) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound2);
                mediaPlayer.start();
            }
            else if(sound.equals("sound3")) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound3);
                mediaPlayer.start();
            }
        }
        else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(pausePosition);
            mediaPlayer.start();
        }
    }

    public void pauseMusic() {
        Log.d("test", "pauseMusic() 호출됨");
        if(mediaPlayer != null) {
            mediaPlayer.pause();
            pausePosition = mediaPlayer.getCurrentPosition();
            Log.d("pause check", ":" + pausePosition);
        }
    }

    public void stopMusic() {
        Log.d("test", "stopMusic() 호출됨");
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sound = preferences.getString("sound_list", "sound1");
        sound_pass = preferences.getBoolean("sound_activate", false);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            stopwatch.setText(getTime());

            handler.sendEmptyMessage(0);
        }
    };
}