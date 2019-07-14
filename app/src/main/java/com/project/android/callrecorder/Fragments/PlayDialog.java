package com.project.android.callrecorder.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.android.callrecorder.Other.ConstantRC;
import com.project.android.callrecorder.R;

import java.io.File;
import java.io.IOException;

public class PlayDialog extends DialogFragment {

    public TextView nameFile;
    public Button btnPlay;
    public ProgressBar playProgress;
    public MediaPlayer mPlayer;
    public Handler mHandler = new Handler();
    public int lastProgress = 0;
    public String pathFile;
    public PlayDialog(){
    }
    @Override
    public void onCreate(Bundle saveInstanseState){
        super.onCreate(saveInstanseState);

        Bundle bundle = getArguments();
        pathFile = bundle.getString(ConstantRC.NAME_FILE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saveInstanseState){

        View view = inflater.inflate(R.layout.play_dialog, parent, false);
        nameFile = view.findViewById(R.id.nameRecord);
        btnPlay = view.findViewById(R.id.playButton);
        playProgress = view.findViewById(R.id.playProgress);
        playAudioFile(pathFile);
        return view;
    }

    public void playAudioFile(String fileName) {

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playProgress.setProgress(lastProgress);
        playProgress.setMax(mPlayer.getDuration());
        mPlayer.seekTo(lastProgress);
        upDateMedia();
        //on media run complete


    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            upDateMedia();
        }
    };

    public void upDateMedia(){
        if(mPlayer != null){
            int currentPossion = mPlayer.getCurrentPosition();
            playProgress.setProgress(currentPossion);
            lastProgress = currentPossion;
        }
        mHandler.postDelayed(runnable, 100);
    }

}
