package org.techtown.pomodoro;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.appcompat.app.AppCompatActivity;

public class AniFragment extends Fragment {

    static ImageView imageView;
    static ArrayList<Drawable> drawableList = new ArrayList<Drawable>();
    static Handler handler = new Handler();
    static boolean on = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_ani, container, false);

        Resources res = getResources();

        drawableList.add(res.getDrawable(R.drawable.book1));
        drawableList.add(res.getDrawable(R.drawable.book2));
        drawableList.add(res.getDrawable(R.drawable.book3));
        drawableList.add(res.getDrawable(R.drawable.book4));

        imageView = (ImageView)rootView.findViewById(R.id.imageView);

        if(on==false){
        on=true;
            AnimThread thread = new AnimThread();
        thread.start();

        }
        return rootView;

    }

    static class AnimThread extends Thread {
        public void run() {
            int index = 0;
            while (true) {
                final Drawable drawable = drawableList.get(index);
                index += 1;
                if (index > 3) {
                    index = 0;
                }
                handler.post(new Runnable() {
                    public void run() {
                        imageView.setImageDrawable(drawable);
                    }
                });
                try {
                    Thread.sleep(600);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
