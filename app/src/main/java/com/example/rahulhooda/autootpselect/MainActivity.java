package com.example.rahulhooda.autootpselect;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextOtpNumber;
    private BroadcastReceiver mBroadcastReceiver;
    private String regexDetectOtpSms = "pin|otp|password|netsecure|dynamic access code|OTAC";
    private String regexFindOtp = "((^|[^0-9])[0-9]{6,8}([^0-9]|$))";
    private final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextOtpNumber = (EditText) findViewById(R.id.edittext_otp_number);

        // Here, this is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

            // MY_PERMISSIONS_REQUEST_RECEIVE_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            prepareSmsListener(); //initialize a receiver for sms

        }
    }

    /**
     * This method is called when Otp sms is received by receiver
     */
    public void fillOTP(String message) {
        try {
            mEditTextOtpNumber.setText(message);
        } catch (Exception e) {
        }
    }

    /**
     * Preparing sms listener.
     * Reads the sms and checks whether its a otp message.
     */

    void prepareSmsListener() {
        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {

                        Bundle myBundle = intent.getExtras();
                        Bundle extras = intent.getExtras();
                        if (extras != null) {

                            // Get received SMS array
                            SmsMessage[] msgs;
                            String msgBody = null;
                            String msgAddress = null;
                            Object[] pdus = (Object[]) extras.get("pdus");
                            if (pdus != null) {
                                msgs = new SmsMessage[pdus.length];
                                for (int i = 0; i < msgs.length; i++) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        String format = myBundle.getString("format");
                                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                                    } else {
                                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                    }
                                    msgBody += msgs[i].getMessageBody();
                                    msgAddress = msgs[i].getDisplayOriginatingAddress();
                                }
                            }

                            // We have the message!
                            //filter sms
                            String mPassword = filterSMS(msgBody);

                            // fill otp
                            if (mPassword != null) {
                                fillOTP(mPassword);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Intent intent = new Intent("android.provider.Telephony.SMS_RECEIVED");
            List<ResolveInfo> infos = getPackageManager().queryBroadcastReceivers(intent, 0);
            for (ResolveInfo info : infos) {
                System.out.println("Receiver name:" + info.activityInfo.name + ";     priority=" + info.priority);
            }

            // Register the broadcast receiver
            IntentFilter filter = new IntentFilter();
            filter.setPriority(99999999);
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerBroadcast(mBroadcastReceiver, filter);
        }
    }

    /**
     * Filter the OTP from sms
     *
     * @param msgBody message received
     * @return filter OTP
     */
    public String filterSMS(String msgBody) {
        String mPassword = null;
        try {
            Matcher match;
            if (msgBody != null) {
                match = Pattern.compile(regexDetectOtpSms, Pattern.CASE_INSENSITIVE).matcher(msgBody);
                if (match.find()) {
                    // we have otp sms
                    match = Pattern.compile(regexFindOtp, Pattern.CASE_INSENSITIVE).matcher(msgBody);
                    if (match.find()) {
                        mPassword = match.group(1).replaceAll("[^0-9]", "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mPassword;
    }

    /**
     * Should be implemented by Calling activity
     *
     * @param broadcastReceiver broadcast receiver to set up.
     * @param filter            - filter for receiving broadcast
     */

    public void registerBroadcast(BroadcastReceiver broadcastReceiver, IntentFilter filter) {
        mBroadcastReceiver = broadcastReceiver;
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * Unregister the broad cast receiver.
     *
     * @param broadcastReceiver reference of broadcast receiver
     */
    public void unregisterBroadcast(BroadcastReceiver broadcastReceiver) {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // sms-related task you need to do.

                    prepareSmsListener(); //initialize a receiver for sms

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregister the broadcast receiver.
        if (mBroadcastReceiver != null) {
            unregisterBroadcast(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }
}
