package com.project.android.callrecorder.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.project.android.callrecorder.Model.Audio_model;
import com.project.android.callrecorder.Other.ConstantRC;
import com.project.android.callrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView header;
        public HeaderViewHolder(View view){
            super(view);
            header = view.findViewById(R.id.headerText);
        }
    }
    public FileAdapter(List<Audio_model> list, final OnItemClickListener _listener){
        listData = list;
        listener = _listener;
    }

    //https://www.simplifiedcoding.net/audio-recording-android-example/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type){
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder vh = null;
        Log.e("FileAdapter","tyoe of data " +type);
        if(type == ConstantRC.HEADER){
            view = inflater.inflate(R.layout.header, parent,false);
            HeaderViewHolder hv = new HeaderViewHolder(view);
            return hv;
        }else if(type == ConstantRC.DATA){
            view = inflater.inflate(R.layout.recent_model, parent,false);
            vh = new ViewHolder(view);
            return vh;
        }
        return  vh;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position){
        Audio_model model = listData.get(position);
        if(viewHolder instanceof  ViewHolder){
            ViewHolder vh =(ViewHolder)viewHolder;
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
        }else {
            HeaderViewHolder header =(HeaderViewHolder)viewHolder;
            String name_file = model.getFileName();
            header.header.setText(name_file);
        }


    }
    @Override
    public int getItemCount(){
        return listData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getViewType();
    }

}
