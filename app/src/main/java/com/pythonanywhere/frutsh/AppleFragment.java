package com.pythonanywhere.frutsh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AppleFragment extends Fragment {

    TextView fruitName, condition, time;
    ImageView imageView;

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

        if (HomeActivity.hashMap != null) {
            fruitName.setText(String.format("Fruit : %s", HomeActivity.hashMap.get("a_name")));
            condition.setText(String.format("Condition : %s", HomeActivity.hashMap.get("a_condition")));
            time.setText(String.format("Last Uploaded : %s", HomeActivity.hashMap.get("a_time")));
            imageView.setImageBitmap(HomeActivity.bitmaps[0]);
        }

    }

}
