package com.project.android.callrecorder;

public class Contact_Data {
    private String name;
    private String phone_number;

    public void setName(String _name){
        this.name = _name;
    }
    public void setPhone(String _phone){
        this.phone_number = _phone;
    }
    public String getName(){
        return this.name;
    }
    public String getPhone(){
        return this.phone_number;
    }
}
