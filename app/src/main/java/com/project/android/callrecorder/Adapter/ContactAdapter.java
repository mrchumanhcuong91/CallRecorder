package com.project.android.callrecorder.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.project.android.callrecorder.Model.ChooseList;
import com.project.android.callrecorder.Model.Contact_Data;
import com.project.android.callrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public interface AdaptertoFragment{
        void transferToFragment(String phoneNum);
        void transferToFragmentRemove(String phoneNum);
    }
    public AdaptertoFragment listener;
    private List<Contact_Data> values;
    public ArrayList<Integer> chooseOut = new ArrayList<Integer>();
    public List<String> chooseList;
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
//                    if(btnSwitch.isChecked())
//                        return;
                    int pos = getPosition();
                    Contact_Data contact_data = values.get(pos);
                    Log.e("CUONGCM",contact_data.getName() +" is set");
                    if(isChecked){
//                        chooseOut.add(pos);
                        listener.transferToFragment(contact_data.getPhone());
//                        notifyDataSetChanged();
                    }else {
//                        chooseOut.remove(pos);
                        Log.e("CuongCM","transfer remove ");

                        listener.transferToFragmentRemove(contact_data.getPhone());
                    }

                }
            });
        }
    }
    public ContactAdapter(AdaptertoFragment _listen, List<Contact_Data> _value, List<String> recordList){
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
        if(contact_data.getChoose()) {
            holder.btnSwitch.setChecked(true);
        }else {
            holder.btnSwitch.setChecked(false);
        }

    }
    @Override
    public int getItemCount(){
        return values.size();
    }
}
