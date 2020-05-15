package com.dh.testproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dh.testproject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main6Activity extends AppCompatActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String[] NORMAL_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CAMERA
    };
    private String deniedPermission;
    private static final int REQ_PERMISSION = 100;
    private static final String TAG = "Main6Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        init();
    }

    private void init() {
        findViewById(R.id.btNormalPermission).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNormalPermission:
                requestNormalPermission(NORMAL_PERMISSIONS);
                break;
        }
    }

    private void requestNormalPermission(String[] permissions) {
        /*if (!checkPermission(permissions)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, deniedPermission)) {
                // 用户拒绝，并且用户勾选了不在询问的按钮，需要给用户解释
                // 跳转到权限设置页面
                Toast.makeText(this, "这是必要的权限，您您打开应用设置界面，开启权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{deniedPermission}, REQ_PERMISSION);
            } else {
                // 用户拒绝，不需要解释，直接继续申请权限
                ActivityCompat.requestPermissions(this, new String[]{deniedPermission}, REQ_PERMISSION);
            }
        } else {
            Toast.makeText(this, "已经授予所有权限", Toast.LENGTH_SHORT).show();
        }*/

        String[] deniedArr = checkPermissions(permissions);
        if (deniedArr.length > 0) {
            for (String s : deniedArr) {
                Log.e(TAG, "拒绝的权限: " + s);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, deniedArr[0])) {
                // 用户拒绝，并且用户勾选了不在询问的按钮，需要给用户解释
                // 跳转到权限设置页面
                Toast.makeText(this, "这是必要的权限，您您打开应用设置界面，开启权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, deniedArr, REQ_PERMISSION);
                Log.e(TAG, "需要解释，然后申请权限");
            } else {
                // 用户拒绝，不需要解释，直接继续申请权限
                ActivityCompat.requestPermissions(this, deniedArr, REQ_PERMISSION);
                Log.e(TAG, "需要申请权限");
            }
        } else {
            Log.e(TAG, "权限全部同意");
            Toast.makeText(this, "已经授予所有权限", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String[] permissions) {
        boolean isGranted = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                deniedPermission = permission;
                break;
            } else {
                isGranted = true;
            }
        }
        return isGranted;
    }

    private String[] checkPermissions(String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        String[] deniedArr = new String[list.size()];
        return list.toArray(deniedArr);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                Toast.makeText(this, "已经同意了一项权限", Toast.LENGTH_SHORT).show();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                // 判断是否用户已经拒绝，并且勾选了不在请求
                boolean permissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (permissionRationale) {
                    requestNormalPermission(NORMAL_PERMISSIONS);
                    Log.e(TAG, "需要再次请求权限");
                } else {
                    // 用户拒绝了会返回false，当用户勾选了不在询问就会一致返回false，请求权限的弹窗就再也不能弹出来
                    Log.e(TAG, "请到设置中打开权限");
                }
            }
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
