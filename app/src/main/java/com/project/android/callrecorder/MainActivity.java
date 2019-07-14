package com.project.android.callrecorder;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.android.callrecorder.Fragments.Contact_Fragment;
import com.project.android.callrecorder.Fragments.recent_fragment;
import com.project.android.callrecorder.Model.Contact_Data;
import com.project.android.callrecorder.Other.DeviceAdminDemo;
import com.project.android.callrecorder.Service.TService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    private String READ_CONTACT = "android.permission.READ_CONTACTS";
    private String CALL_PHONE = "android.permission.CALL_PHONE";
    private String[] req_permission = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO" ,
            "android.permission.PROCESS_OUTGOING_CALLS",
            "android.permission.READ_PHONE_STATE",
            "android.permission.STORAGE",
            "android.permission.READ_CONTACTS",
            "android.permission.CALL_PHONE"
    };
    private static final int  PERMISSION_REQUEST_CODE = 200;
    private static final List<Contact_Data> list = new ArrayList<Contact_Data>();
    public recent_fragment recentFragment = new recent_fragment();
    public Contact_Fragment contactFragment = new Contact_Fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkPermission());
            requestPermission();
        // if savedInstanceState is null we do some cleanup
        if (savedInstanceState != null) {
            // cleanup any existing fragments in case we are in detailed mode
            getFragmentManager().executePendingTransactions();
            Fragment fragmentById = getFragmentManager().
                    findFragmentById(R.id.fragment_container);
            if (fragmentById!=null) {
                getFragmentManager().beginTransaction()
                        .remove(fragmentById).commit();
            }
        }
        recent_fragment recentFragment = new recent_fragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recentFragment).commit();
        //start service recorder
        try {
            // Initiate DevicePolicyManager.
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, DeviceAdminDemo.class);

            if (!mDPM.isAdminActive(mAdminName)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                // mDPM.lockNow();
                // Intent intent = new Intent(MainActivity.this,
                // TrackDeviceService.class);
                // startService(intent);
            }
//            Intent intent = new Intent(MainActivity.this, TService.class);
//            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE == requestCode) {
            android.content.Intent intent = new Intent(MainActivity.this, TService.class);
            startService(intent);
//            sendBroadcast(intent);
        }
    }
    public void clickRecent(View view){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, recentFragment,recent_fragment.TAG);
        transaction.addToBackStack(recent_fragment.TAG);

        transaction.commit();

    }
    public void clickContact(View view){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, contactFragment,Contact_Fragment.TAG);
        transaction.addToBackStack(Contact_Fragment.TAG);

        transaction.commit();
    }
    private boolean checkPermission() {
        ///int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACT);
        //int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);

        //return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        boolean is_Ok = false;
        for (int i =0; i < req_permission.length ;i++) {
            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), req_permission[i]);
            if(result1 == PackageManager.PERMISSION_GRANTED){
                is_Ok = true;
            }else {

                return false;
            }

        }
        return is_Ok;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,req_permission , PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data and READ_CONTACT.", Toast.LENGTH_LONG).show();
                    else {

                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data and READ_CONTACT.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CALL_PHONE, READ_CONTACT},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}
