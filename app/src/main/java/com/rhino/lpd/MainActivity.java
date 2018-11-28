package com.rhino.lpd;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rhino.permission.PermissionUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionUtils.PermissionCallback {

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermission(this, this, PERMISSIONS);
    }

    @Override
    public void onPermissionGranted(List<String> permissions) {
        Toast.makeText(this, "获取得所有权限！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied(List<String> permissions) {
        PermissionUtils.showSettingForPermissionDialog(this, this, permissions);
    }
}
