package com.feizai.testservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: feizai
 * Date: 2021/12/7-0007 上午 11:23:46
 * Describe:
 */
public class PhoneStatusActivity extends AppCompatActivity {

    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonestatus);
        Intent intent = new Intent();
        mContext = this;
        findViewById(R.id.gps).setOnClickListener(view->{
            intent.setClass(mContext, LocationActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.four_g).setOnClickListener(view->{
            intent.setClass(mContext, SIMActivity.class);
            startActivity(intent);
        });
    }
}
