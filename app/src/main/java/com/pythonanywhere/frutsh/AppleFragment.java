package com.pythonanywhere.frutsh;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

public class AppleFragment extends Fragment {

    TextView fruitName,condition,time;
    ImageView imageView;
    int number = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fruitName = view.findViewById(R.id.nameTextView);
        condition = view.findViewById(R.id.conditionTextView);
        time = view.findViewById(R.id.timeTextView);

        imageView = view.findViewById(R.id.imageView);

        fruitName.setText(String.format("Fruit : %s", SplashActivity.fruitName.get(0)));
        condition.setText(String.format("Condition : %s", SplashActivity.condition.get(0)));
        time.setText(String.format("Last Uploaded : %s", SplashActivity.time.get(0)));

        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, "image" + number + ".jpg");
        if (file.exists()){
            imageView.setImageDrawable(Drawable.createFromPath(file.toString()));
        }


    }

}
