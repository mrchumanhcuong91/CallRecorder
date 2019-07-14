package com.project.android.callrecorder.Model;

public class Audio_model {
    public String fileName;
    public String longTime;

    public Audio_model(String _fileName){
        fileName = _fileName;
    }

    public void setFileName(String newName){
        fileName = newName;
    }
    public void setLongTime(String time){
        longTime = time;
    }
    public String getFileName(){
        return fileName;
    }
    public String getTime(){
        return longTime;
    }
}
