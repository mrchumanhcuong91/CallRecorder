package com.project.android.callrecorder.Model;

public class Contact_Data {
    private String name;
    private long id;
    private boolean is_choose;
    private String phone_number;
    public void setId(long _id){
        this.id = _id;
    }
    public long getId(){return id;}
    public void setName(String _name){
        this.name = _name;
    }
    public void setPhone(String _phone){
        this.phone_number = _phone;
    }
    public void setChoose(boolean _choose){
        is_choose = _choose;
    }
    public boolean getChoose(){
        return is_choose;
    }
    public String getName(){
        return this.name;
    }
    public String getPhone(){
        return this.phone_number;
    }
}
