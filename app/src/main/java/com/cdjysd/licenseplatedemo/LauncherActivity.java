package com.cdjysd.licenseplatedemo;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cdjysd.licenseplatedemo.adapter.NumAdapter;
import com.cdjysd.licenseplatelib.utils.LPalte;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends Activity {
    private final String TAG = LauncherActivity.class.getSimpleName();
    private Context context;
    private LinearLayout nva1, nva2, nva3, nva4;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_launcher);
        nva1 = findViewById(R.id.launcher_nva_1);
        nva2 = findViewById(R.id.launcher_nva_2);
        nva3 = findViewById(R.id.launcher_nva_3);
        nva4 = findViewById(R.id.launcher_nva_4);
        nva1.setOnClickListener(nvaClick);
        nva2.setOnClickListener(nvaClick);
        nva3.setOnClickListener(nvaClick);
        nva4.setOnClickListener(nvaClick);
        title = findViewById(R.id.launcher_title);
    }

    View.OnClickListener nvaClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.launcher_nva_1:
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.launcher_nva_2:
                    Toast.makeText(context, "功能开发中。。。", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.launcher_nva_3:
                    Toast.makeText(context, "功能开发中。。。", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.launcher_nva_4:
                    Toast.makeText(context, "功能开发中。。。", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
