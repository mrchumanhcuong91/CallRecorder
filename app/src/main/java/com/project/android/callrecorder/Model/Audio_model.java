package com.project.android.callrecorder.Model;

public class Audio_model {
    public String fileName;
    public String longTime;
    private int viewType;

    public Audio_model(String _fileName){
        fileName = _fileName;
    }

    public void setFileName(String newName){
        fileName = newName;
    }
    public void setLongTime(String time){
        longTime = time;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getFileName(){
        return fileName;
    }
    public String getTime(){
        return longTime;
    }
}
