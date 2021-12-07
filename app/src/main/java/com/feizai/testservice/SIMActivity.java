package com.feizai.testservice;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Map;


public class SIMActivity extends AppCompatActivity {

    private final static String TAG = "SIMActivity";
    private TextView result;
    private TextView status;
    private TelephonyManager telephonyManager;
    private ActivityResultLauncher<String> mRequestPermissionsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView title = findViewById(R.id.title);
        title.setText("4G Status");
        result = findViewById(R.id.result);
        status = findViewById(R.id.status);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                mRequestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
                    if (result) {
                        get4G();
                    } else {
                        finish();
                    }
                });
                mRequestPermissionsLauncher.launch(Manifest.permission.READ_PHONE_STATE);
            } else {
                get4G();
            }
        } else {
            get4G();
        }
    }

    private void get4G() {
        String deviceId = telephonyManager.getDeviceId();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String line1Number = telephonyManager.getLine1Number();
        String operator = telephonyManager.getSimOperatorName();
        String networkType = getNetworkType(telephonyManager.getNetworkType());
        String location = telephonyManager.getCellLocation() != null ? telephonyManager.getCellLocation().toString() : "UNKNOWN LOCATION";
        String gsm = SystemProperties.get("gsm.sim.operator.numeric");
        String ip = SystemProperties.get("net.usb0.local-ip");
        String dns = SystemProperties.get("net.usb0.dns1");
        String imei = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imei = telephonyManager.getImei();
        }
        String simState = "";
        switch (telephonyManager.getSimState()) {
            case TelephonyManager.SIM_STATE_READY:
                simState = "良好";
                break;
            case TelephonyManager.SIM_STATE_ABSENT:
                simState = "无SIM卡";
                break;
            default:
                simState = "SIM卡被锁定或未知状态";
                break;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("运营商==>");
        builder.append(operator);
        builder.append("\n");
        builder.append("网络状态==>");
        builder.append(networkType);
        builder.append("\n");
        builder.append("Sim卡状态==>");
        builder.append(simState);
        builder.append("\n");
        builder.append("GSM==>");
        builder.append("gsm");
        builder.append("\n");
        builder.append("IMEI==>");
        builder.append(imei);
        builder.append("\n");
        builder.append("电话号码==>");
        builder.append(line1Number);
        builder.append("\n");
        builder.append("当前位置==>");
        builder.append(location);
        builder.append("\n");

        result.setText(builder.toString());
    }

    private String getNetworkType(int networkType) {
        // TODO Auto-generated method stub
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "UNKNOWN";
            default:
                return "UNKNOWN";
        }
    }
}