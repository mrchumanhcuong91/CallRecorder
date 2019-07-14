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
import com.project.android.callrecorder.Other.DividerItemDecorator;
import com.project.android.callrecorder.Model.Contact_Data;
import com.project.android.callrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class Contact_Fragment extends Fragment {
    public static final String TAG = "Contact_Fragment";
    public RecyclerView recyclerView;
    public ContactAdapter myAdapter ;
    ArrayList<Integer> chooseList = new ArrayList<Integer>();
    ContactAdapter.AdaptertoFragment transfer = new ContactAdapter.AdaptertoFragment() {
        @Override
        public void transferToFragment(int position) {
            Log.e("CuongCM","transferToFragment " +position);
            chooseList.add(position);
        }
    };


    @Override
    public void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.e("CuongCM","onCreate");

//        if(savedInstanceState != null)
//            chooseList = savedInstanceState.getIntegerArrayList(ConstantRC.CHOOSE_LIST);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment,
                container, false);
        Log.e("CuongCM","onCreateView");

        recyclerView = (RecyclerView) view.findViewById(R.id.recContact);
        RecyclerView.ItemDecoration itemDecorator = new DividerItemDecorator(ContextCompat.getDrawable(getContext(),R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);
        List<Contact_Data> list = getAllContact();
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
    public List<Contact_Data> getAllContact() {


        List<Contact_Data> contactModelArrayList = new ArrayList<>();
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Contact_Data Contact_Data = new Contact_Data();
            Contact_Data.setName(name);
            Contact_Data.setPhone(phoneNumber);
            contactModelArrayList.add(Contact_Data);
            Log.d("name>>", name + "  " + phoneNumber);
        }
        phones.close();
        return contactModelArrayList;
    }



}
