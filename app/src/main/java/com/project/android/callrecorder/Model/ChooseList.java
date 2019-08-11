package com.project.android.callrecorder.Model;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseList {
    private List<String> phoneContact;

    public ChooseList(){
        Log.e("CuongCM","create new ChooseList");
        phoneContact = new ArrayList<String>();
    }
    public void addPhone(String newPhone){
        if(Arrays.asList(phoneContact).contains(newPhone)){
            return;
        }else {
            phoneContact.add(newPhone);
        }
    }
    public void removePhone(String phoneNum){
        if (phoneContact != null)
            phoneContact.remove(phoneNum);
    }
    public List<String> getListPhone(){
        return phoneContact;
    }
    public int getSize(){
        return phoneContact.size();
    }
    public void setList(List<String> newList){
        phoneContact = newList;
    }
    public boolean checkContain(String _string){

        boolean exist =  Arrays.asList(phoneContact).contains(_string);

        Log.e("CuongCM","number income "+_string + "contain "+exist);



        return exist;
    }
}
