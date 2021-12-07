package com.feizai.testservice;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class LocationActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> mRequestPermissionLauncher;
    private LocationManager mLocationManager;
    private TextView result;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView title = findViewById(R.id.title);
        title.setText("GPS Status");
        result = findViewById(R.id.result);
        status = findViewById(R.id.status);

        result.setText("正在定位");
        status.setText("正在定位");
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                mRequestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
                    if (result) {
                        requestLocation();
                    } else {
                        finish();
                    }
                });
                mRequestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                requestLocation();
            }
        } else {
            requestLocation();
        }


    }

    private void showToast(CharSequence content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location == null) {
                    result.setText("正在定位");
                    return;
                }
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                float speed = location.getSpeed();
                long time = location.getTime();
                Date date = new Date(time);
                StringBuilder builder = new StringBuilder();
                builder.append("维度==>");
                builder.append(latitude);
                builder.append("\n");
                builder.append("经度==>");
                builder.append(longitude);
                builder.append("\n");
                builder.append("速度==>");
                builder.append(speed);
                builder.append("\n");
                builder.append("时间==>");
                builder.append(date.toString());
                builder.append("\n");
                result.setText(builder.toString());
            }

            //当GPS状态发生改变的时候调用
            @Override
            public void onStatusChanged(String provider, int s, Bundle extras) {
                switch (s) {
                    case LocationProvider.AVAILABLE:
                        status.setText("当前GPS为可用状态!");
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        status.setText("当前GPS不在服务内!");
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        status.setText("当前GPS为暂停服务状态!");
                        break;
                }
            }

            //GPS开启的时候调用
            @Override
            public void onProviderEnabled(String provider) {
                showToast("GPS开启了");
            }

            //GPS关闭的时候调用
            @Override
            public void onProviderDisabled(String provider) {
                showToast("GPS关闭了");
            }
        });
    }

}