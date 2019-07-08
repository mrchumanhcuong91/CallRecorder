package com.project.android.callrecorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public interface AdaptertoFragment{
        void transferToFragment(int position);
    }
    public AdaptertoFragment listener;
    private List<Contact_Data> values;
    public ArrayList<Integer> chooseOut = new ArrayList<Integer>();
    public ArrayList<Integer> chooseList;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView phoneNumber;
        public View layout;
        public Switch btnSwitch;
        public ViewHolder(final View v){
            super(v);
            layout = v;
            name = v.findViewById(R.id.name_contact);
            phoneNumber = v.findViewById(R.id.phone_number);
            btnSwitch = v.findViewById(R.id.chooseBtn);
            btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = getPosition();
                    if(isChecked){

                        Contact_Data contact_data = values.get(pos);
                        Log.e("CUONGCM",contact_data.getName() +" is set");
                        chooseOut.add(pos);
                        listener.transferToFragment(pos);
//                        notifyDataSetChanged();
                    }

                }
            });
        }
    }
    public ContactAdapter(AdaptertoFragment _listen, List<Contact_Data> _value, ArrayList<Integer> recordList){
        values = _value;
        chooseList = recordList;
        listener = _listen;


    }
    public void add(int position, Contact_Data _stn){
        values.add(position, _stn);
        notifyItemInserted(position);
    }
    public void remove(int position){
        values.remove(position);
        notifyItemRemoved(position);
    }
    public ArrayList<Integer> getBackUp(){
        Log.e("CuongCM","size of ChooseOUt adapter " + chooseOut.size());
        return chooseOut;
    }
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.contact_sub_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        Contact_Data contact_data = values.get(position);
        String _name = contact_data.getName();
        String _phone = contact_data.getPhone();
        holder.name.setText(_name);
        holder.phoneNumber.setText(_phone);
        for(int i =0;i <chooseList.size();i++){
            if(position != chooseList.get(i)) {
                holder.btnSwitch.setChecked(false);
                continue;
            }else {
                holder.btnSwitch.setChecked(true);
            }
        }
    }
    @Override
    public int getItemCount(){
        return values.size();
    }
}
