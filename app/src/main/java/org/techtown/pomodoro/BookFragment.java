package org.techtown.pomodoro;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;


public class BookFragment extends Fragment {

    public static int total_time;
    static ImageView imageView;
    static ArrayList<Drawable> drawableList = new ArrayList<Drawable>();
    static Handler handler = new Handler();
    static boolean on = false;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_book, container, false);

        Resources res = getResources();

        drawableList.add(res.getDrawable(R.drawable.day1));
        drawableList.add(res.getDrawable(R.drawable.day2));
        drawableList.add(res.getDrawable(R.drawable.day3));
        drawableList.add(res.getDrawable(R.drawable.day4));
        drawableList.add(res.getDrawable(R.drawable.day5));
        drawableList.add(res.getDrawable(R.drawable.day6));
        drawableList.add(res.getDrawable(R.drawable.day7));
        drawableList.add(res.getDrawable(R.drawable.day8));
        drawableList.add(res.getDrawable(R.drawable.day9));
        drawableList.add(res.getDrawable(R.drawable.day10));
        drawableList.add(res.getDrawable(R.drawable.day11));
        drawableList.add(res.getDrawable(R.drawable.night1));
        drawableList.add(res.getDrawable(R.drawable.night2));
        drawableList.add(res.getDrawable(R.drawable.night3));
        drawableList.add(res.getDrawable(R.drawable.night4));
        drawableList.add(res.getDrawable(R.drawable.night5));
        drawableList.add(res.getDrawable(R.drawable.night6));
        drawableList.add(res.getDrawable(R.drawable.night7));
        drawableList.add(res.getDrawable(R.drawable.night8));
        drawableList.add(res.getDrawable(R.drawable.night9));
        drawableList.add(res.getDrawable(R.drawable.night10));
        drawableList.add(res.getDrawable(R.drawable.night11));


        imageView = (ImageView)rootView.findViewById(R.id.imageView1);

            if(on==false){
                on=true;
                org.techtown.pomodoro.BookFragment.BookThread thread = new org.techtown.pomodoro.BookFragment.BookThread();
                thread.start();
            }
            return rootView;
        }

        static class BookThread extends Thread {
            public void run() {
                int index = 0;
                while(true) {
                    total_time = MainActivity.total_time;
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh");
                    String getTime = dateFormat.format(date);
                    int time = Integer.parseInt(getTime);
                    String t = String.valueOf(time);
                    //index = 오늘 내가 공부한 시간에 따라 결정하는 구문
                    index = total_time / 5 + 2;
                    if(index >= 10){
                        index = 10;
                    }
                    if (time < 12) {

                        final Drawable drawable = drawableList.get(index);
                        handler.post(new Runnable() {
                            public void run() {
                                imageView.setImageDrawable(drawable);
                            }
                        });
                    } else if (time > 12) {
                        index += 11;
                        final Drawable drawable = drawableList.get(index);
                        handler.post(new Runnable() {
                            public void run() {
                                imageView.setImageDrawable(drawable);
                            }
                        });
                    }

                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
}
