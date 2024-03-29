package com.project.android.callrecorder.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.project.android.callrecorder.Database.ContactDataSource;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Handler;

public class TService extends Service {
    MediaRecorder recorder;
    File audiofile;
    private ContactDataSource dataSource;
    String audio_format;
    public String Audio_Type;
    int audioSource;
    Context context;
    private Handler handler;
    Timer timer;
    Boolean offHook = false, ringing = false;
    Toast toast;
    Boolean isOffHook = false;
    private boolean recordstarted = false;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private CallBr br_call;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        dataSource.close();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final IntentFilter filter = new IntentFilter();
        dataSource = new ContactDataSource(getApplicationContext());

        filter.addAction(ACTION_OUT);
        filter.addAction(ACTION_IN);
        if(this.br_call == null)
            this.br_call = new CallBr();
        this.registerReceiver(this.br_call, filter);

        return START_STICKY;
    }

    public class CallBr extends BroadcastReceiver {
        Bundle bundle;
        String state;
        String inCall, outCall;
        String contacName;
        public boolean wasRinging = false;

        private void startRecord(String seed) {
            String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
            File sampleDir = new File(Environment.getExternalStorageDirectory(), "/TestRecordingDasa1");
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
            String file_name = seed + "_" + out;
            //parse string seed

            try {
                audiofile = File.createTempFile(file_name, ".amr", sampleDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder.start();
            recordstarted = true;
            Log.d("CuongCM","file name : "+audiofile.getAbsolutePath());
        }


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_IN)) {
                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        if(dataSource != null ){
                            dataSource.openRead();
                            if(dataSource.Exists(inCall)) {
                                dataSource.close();
                                return;
                            }
                            dataSource.close();
                        }
                        contacName = getContactName(inCall, context);
                        wasRinging = true;
                        Toast.makeText(context, "IN : " + inCall, Toast.LENGTH_LONG).show();
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        if (wasRinging == true) {

                            Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show();

                            startRecord("incoming_"+contacName);
                        }
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
                        Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                        if (recordstarted) {
                            recorder.stop();
                            recordstarted = false;
                        }
                    }
                }
            } else if (intent.getAction().equals(ACTION_OUT)) {
                if ((bundle = intent.getExtras()) != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    if(dataSource != null ){
                        dataSource.openRead();
                        if(dataSource.Exists(outCall)) {
                            dataSource.close();
                            return;
                        }
                        dataSource.close();
                    }
                    Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
                    contacName = getContactName(outCall, context);
                    startRecord("outgoing_"+contacName);
                    if ((bundle = intent.getExtras()) != null) {
                        state = bundle.getString(TelephonyManager.EXTRA_STATE);
                        if (state != null) {
                            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                                wasRinging = false;
                                Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                                if (recordstarted) {
                                    recorder.stop();
                                    recordstarted = false;
                                }
                            }
                        }


                    }
                }
            }
        }
        private String getContactName(String number, Context context) {
            String contactName = "";

            // // define the columns I want the query to return
            String[] projection = new String[] {
                    ContactsContract.PhoneLookup.DISPLAY_NAME,
                    ContactsContract.PhoneLookup.NUMBER,
                    ContactsContract.PhoneLookup.HAS_PHONE_NUMBER };

            // encode the phone number and build the filter URI
            Uri contactUri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(number));

            // query time
            Cursor cursor = context.getContentResolver().query(contactUri,
                    projection, null, null, null);
            // querying all contacts = Cursor cursor =
            // context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
            // projection, null, null, null);

            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }
            cursor.close();
            return contactName.equals("") ? number : contactName;

        }




}




}
