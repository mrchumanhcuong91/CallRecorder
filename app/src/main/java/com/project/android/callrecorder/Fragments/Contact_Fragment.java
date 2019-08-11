package com.project.android.callrecorder.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.android.callrecorder.Adapter.ContactAdapter;
import com.project.android.callrecorder.Database.ContactDataSource;
import com.project.android.callrecorder.Other.DividerItemDecorator;
import com.project.android.callrecorder.Model.Contact_Data;
import com.project.android.callrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class Contact_Fragment extends Fragment {
    public static final String TAG = "Contact_Fragment";
    public RecyclerView recyclerView;
    private ContactDataSource database;
    List<Contact_Data> list = new ArrayList<Contact_Data>();
    List<String> chooseList = new ArrayList<String>();
    ContactAdapter.AdaptertoFragment transfer = new ContactAdapter.AdaptertoFragment() {
        @Override
        public void transferToFragment(String phoneNum) {
            if(!chooseList.contains(phoneNum)){
                Log.e("CuongCM","transferToFragment " +phoneNum);
                Contact_Data contact_data = searchContact(phoneNum);
                if(contact_data != null){
                    contact_data.setChoose(true);
                }
                chooseList.add(phoneNum);
            }
        }
        @Override
        public void transferToFragmentRemove(String phoneNum){
            chooseList.remove(phoneNum);
            Contact_Data contact_data = searchContact(phoneNum);
            if(contact_data != null){
                contact_data.setChoose(false);
            }
        }
    };


    @Override
    public void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.e("CuongCM","onCreate");
        if(list.size() ==0){
            list = getAllContact();
        }
        //read data from db first

        if(chooseList.size() == 0){
            if(database == null){
                database = new ContactDataSource(getContext());
                database.openRead();
            }
            chooseList = database.getSelectContact();
            database.close();
            for(int i =0;i< chooseList.size();i++){
                String temp = chooseList.get(i);
                Log.e("CuongCM","onCreate " +temp);

                Contact_Data contact_data = searchContact(temp);
                if(contact_data != null){
                    contact_data.setChoose(true);
                }
            }
        }

//        if(savedInstanceState != null)
//            chooseList = savedInstanceState.getIntegerArrayList(ConstantRC.CHOOSE_LIST);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment,
                container, false);
        Log.e("CuongCM","onCreateView "+ list.size());

        recyclerView = (RecyclerView) view.findViewById(R.id.recContact);
        RecyclerView.ItemDecoration itemDecorator = new DividerItemDecorator(ContextCompat.getDrawable(getContext(),R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);
        if (list.size() == 0)
            return view;
        ContactAdapter myAdapter = new ContactAdapter(transfer, list, chooseList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle onSaveInstanseState){
        super.onActivityCreated(onSaveInstanseState);
        Log.e("CuongCM","onActivityCreated");

    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.e("CuongCM","onSaveInstanceState");
//        if(chooseList.size()> 0){
//            chooseList = myAdapter.getBackUp();
//            if(chooseList != null){
//                outState.putIntegerArrayList(ConstantRC.CHOOSE_LIST, chooseList);
//            }
//        }


    }
    @Override
    public void onPause(){
        super.onPause();
        Log.e("CuongCM","onPause "+ chooseList.size());

        //merge list and chooseList
        for(int i =0;i< chooseList.size();i++){
            String temp = chooseList.get(i);
            Contact_Data contact_data = searchContact(temp);
            if(contact_data != null){
                contact_data.setChoose(true);
            }
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //save data contact to db
        if(database == null)
            database = new ContactDataSource(getContext());
        if(database != null){
            database.open();
            database.deleteAllContact();
            for(int i=0; i< chooseList.size();i++){
                String phoneTemp = chooseList.get(i);
                database.saveContactDb(phoneTemp);

            }
            database.close();
        }
    }
    public List<Contact_Data> getAllContact() {


        List<Contact_Data> contactModelArrayList = new ArrayList<>();
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Contact_Data contact_data = new Contact_Data();
            contact_data.setName(name);
            contact_data.setPhone(phoneNumber);
            contact_data.setChoose(false);
            contactModelArrayList.add(contact_data);
            Log.d("name>>", name + "  " + phoneNumber);
        }
        phones.close();
        return contactModelArrayList;
    }
    public Contact_Data searchContact(String phoneNum){
        Contact_Data contact_data ;
        for(int i =0;i< list.size(); i++){
            contact_data = list.get(i);
            String phoneTemp = contact_data.getPhone();
            if(phoneNum.compareTo(phoneTemp) ==0)
                return contact_data;
        }
        return null;
    }



}
