package com.example.beata.testapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beata.testapp.R;

/**
 * Created by huangbiyun on 16-11-28.
 */
public class ScreenSlidePageFragment extends Fragment {

    private TextView textView;
    private int number;

    private int[] colorArray = {
        R.color.c1,R.color.c2,R.color.c3
    };

    public static ScreenSlidePageFragment newInstance(int number){
        final ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        final Bundle args = new Bundle();
        args.putInt("Number", number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        number = getArguments() != null ? getArguments().getInt("Number") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page
                , container, false);

        textView = (TextView) rootView.findViewById(R.id.title);
        textView.setText("page "+number);
        rootView.setBackgroundColor(getResources().getColor(colorArray[number]));

        return rootView;
    }
}
