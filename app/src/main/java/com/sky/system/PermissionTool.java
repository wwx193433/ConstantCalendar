package com.sky.system;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 17-8-9.
 */
public class PermissionTool {
    private Activity activity;

    public PermissionTool(Activity activity) {
        this.activity = activity;
    }


    /**
     * 读取外部存储卡权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void applyStoragePermission() {
        if (Integer.parseInt(Build.VERSION.SDK) >= 23) {
            int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("系统需要获取手机铃声，请允许",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestStoragePermission();//确定后申请权限。
                                }
                            });
                    return;
                }
                requestStoragePermission();//没有权限的话，申请。
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请 WRITE_CONTACTS 权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void applyLocatePermission(){
        if (Integer.parseInt(Build.VERSION.SDK) >= 23) {
            int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOKCancel("系统需要GPS和网络权限，请允许",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestLocatePermission();//确定后申请权限。
                                }
                            });
                    return;
                }
                requestStoragePermission();//没有权限的话，申请。
            }
        }
    }

    private void requestLocatePermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请 WRITE_CONTACTS 权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
}
