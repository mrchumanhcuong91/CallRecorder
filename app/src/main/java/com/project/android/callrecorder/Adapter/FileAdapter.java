package com.project.android.callrecorder.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.project.android.callrecorder.Model.Audio_model;
import com.project.android.callrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    public interface OnItemClickListener{
        public void onItemClick(Audio_model item);
    }
    private final OnItemClickListener listener;
    List<Audio_model> listData = new ArrayList<>();
    public class ViewHolder extends RecyclerView.ViewHolder{


            public TextView nameFile;
            public TextView timeLong;
            public View view;
            public ViewHolder(final View v){
                super(v);
                view = v;
                nameFile = v.findViewById(R.id.name_file);
                timeLong = v.findViewById(R.id.time_long);

            }
            public void bind(final Audio_model item, final OnItemClickListener listener){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }

    }
    public FileAdapter(List<Audio_model> list, final OnItemClickListener _listener){
        listData = list;
        listener = _listener;
    }

    //https://www.simplifiedcoding.net/audio-recording-android-example/
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recent_model, parent,false);

        ViewHolder vh = new ViewHolder(view);
        return  vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder vh, final int position){
        Audio_model model = listData.get(position);
//        boolean incoming = false;
        String name_file = model.getFileName();
        if(!name_file.contains("incoming")){
//            incoming = true;
            //set incon incoming
        }
        String[] temp = name_file.split("_");

        vh.nameFile.setText(temp[1]);
        vh.timeLong.setText(temp[2]);
        vh.bind(model, listener);

    }
    @Override
    public int getItemCount(){
        return listData.size();
    }
}
