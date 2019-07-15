package com.project.android.callrecorder.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.android.callrecorder.Adapter.FileAdapter;
import com.project.android.callrecorder.Model.Audio_model;
import com.project.android.callrecorder.Other.ConstantRC;
import com.project.android.callrecorder.Other.DividerItemDecorator;
import com.project.android.callrecorder.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class recent_fragment extends Fragment {
    public static final String TAG = "recent_fragment";
    public RecyclerView recyclerView;
//    public interface onPlayAudioListener{
//        void playAudioFile(String file);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recent_fragment,
                container, false);
        recyclerView = view.findViewById(R.id.recentList);
        RecyclerView.ItemDecoration itemDecorator = new DividerItemDecorator(ContextCompat.getDrawable(getContext(),R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);
        List<Audio_model> data_tmp = getAllAudioFile();

        FileAdapter fileAdapter = new FileAdapter(data_tmp, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fileAdapter);


        return view;
    }
    public List<Audio_model> getAllAudioFile(){
        List<Audio_model> data = new ArrayList<>();
        File root = android.os.Environment.getExternalStorageDirectory();

        String path = root.getAbsolutePath()+"/TestRecordingDasa1";

        File directory = new File(path);
        File[] files = directory.listFiles();

        if(files != null){
            for (int i =0;i < files.length;i++){

                String name_file = files[i].getName();
//                String name_path_file = path +"/"+name_file;
//                Log.e("Audio_file",name_path_file);
                Audio_model model = new Audio_model(name_file);
                data.add(model);
            }
        }
        return data;

    }
    FileAdapter.OnItemClickListener listener = new FileAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Audio_model item) {
            //make new dialog listener
            Bundle bundle = new Bundle();
            bundle.putString(ConstantRC.NAME_FILE,item.getFileName());

            PlayDialog dialog = new PlayDialog();
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(),"play_fragment");
        }
    };

}
