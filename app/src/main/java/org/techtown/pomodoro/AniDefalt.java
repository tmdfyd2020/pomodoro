package org.techtown.pomodoro;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.annotation.IncompleteAnnotationException;

public class AniDefalt extends Fragment {

    static ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.defalt_ani, container, false);

        Resources res = getResources();

        imageView = (ImageView)rootView.findViewById(R.id.imageView);

        return rootView;

    }
}
