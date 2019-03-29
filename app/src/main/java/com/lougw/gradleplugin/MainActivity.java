package com.lougw.gradleplugin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lougw.aop.AOPUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AOPUtil.getInstance().init(this);
        setContentView(R.layout.activity_main);
    }
}
