package com.project.android.callrecorder;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class recent_fragment extends Fragment {
    public static final String TAG = "recent_fragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recent_fragment,
                container, false);
        return view;
    }

}
