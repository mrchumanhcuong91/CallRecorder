package com.project.android.callrecorder.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.DialogFragment;
import android.util.Log;
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

    public TextView nameFile,len_file,pos_now;
    public Button btnPlay;
    public ProgressBar playProgress;
    public MediaPlayer mPlayer;
    public Handler mHandler = new Handler();
    public int lastProgress = 0;
    public String pathFile;
    boolean isPlaying = false;
    private int msec_now;
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
        len_file = view.findViewById(R.id.len_file);
        pos_now = view.findViewById(R.id.position_now);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying && mPlayer!= null) {
                    isPlaying = false;
                    msec_now = mPlayer.getCurrentPosition();
                    mPlayer.pause();
                }else {
                    isPlaying = true;
                    mPlayer.seekTo(msec_now);
                    mPlayer.start();
                }
            }
        });
        //nameFile
        String[] temp = pathFile.split("_");
        nameFile.setText(temp[2]);
        playAudioFile(pathFile);
        return view;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer = null;
        }
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
        isPlaying = true;
        playProgress.setProgress(0);
        int file_len = mPlayer.getDuration();
        String len = String.format("%02d:%02d", file_len / 60000, (file_len % 60000)/1000);
        len_file.setText(len);
        playProgress.setMax(file_len);
        upDateMedia();
        Log.e("PlayDialog","len file " + mPlayer.getDuration());
        //mPlayer.seekTo(lastProgress);
        //on media run complete
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                mHandler.
                mp.stop();
                mPlayer = null;
            }
        });

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
            pos_now.setText(String.format("%02d:%02d", currentPossion / 60000, (currentPossion % 60000)/1000));
            lastProgress = currentPossion;
            mHandler.postDelayed(runnable, 100);
        }
    }

}
