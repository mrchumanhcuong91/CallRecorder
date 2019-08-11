package com.project.android.callrecorder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class recent_fragment extends Fragment {
    public static final String TAG = "recent_fragment";
//    public static final long one_day =
    public RecyclerView recyclerView;
    String path ;
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

        path = root.getAbsolutePath()+"/TestRecordingDasa1";

        File directory = new File(path);
        boolean hasToday = false;
        boolean hasYestoday = false;
        boolean hasLast = false;

        File[] files = directory.listFiles();
        if(files != null){
            for (int i =files.length-1;i > 0;i--){

                String name_file = files[i].getName();
                Audio_model model = new Audio_model(name_file);
                int code = getFormattedDate(getContext(),files[i].lastModified());
                if(code == ConstantRC.TODAY && !hasToday){
                    Audio_model today = new Audio_model("Today");
                    today.setViewType(ConstantRC.HEADER);
                    hasToday = true;
                    data.add(today);
                }else if(code == ConstantRC.YESTERDAY && !hasYestoday){
                    Audio_model yesterday = new Audio_model("Yesterday");
                    yesterday.setViewType(ConstantRC.HEADER);
                    hasYestoday = true;
                    data.add(yesterday);
                }else if(code == ConstantRC.LAST_7_DAY && !hasLast) {
                    Audio_model Last7day = new Audio_model("Last 7 days");
                    Last7day.setViewType(ConstantRC.HEADER);
                    hasLast = true;
                    data.add(Last7day);
                }
                model.setViewType(ConstantRC.DATA);
                data.add(model);
            }
        }
        return data;

    }
    public int getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
//            return "Today " + DateFormat.format(timeFormatString, smsTime);
            return ConstantRC.TODAY;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
//            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
            return ConstantRC.YESTERDAY;
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
//            return "Last 7 Days";//DateFormat.format(dateTimeFormatString, smsTime).toString();
            return ConstantRC.LAST_7_DAY;
        } else {
            //return "Last 7 Days";//DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
            return ConstantRC.LAST_7_DAY;
        }
    }
    FileAdapter.OnItemClickListener listener = new FileAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Audio_model item) {
            //make new dialog listener
            Bundle bundle = new Bundle();
            bundle.putString(ConstantRC.NAME_FILE,path + "/" + item.getFileName());

            PlayDialog dialog = new PlayDialog();
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(),"play_fragment");
        }
    };

}
