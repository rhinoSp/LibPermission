package com.rhino.permission;

import android.app.Activity;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rhino
 * @since Create on 2018/11/24.
 **/
public final class PermissionUtils {

    public interface PermissionCallback {

        void onPermissionGranted(List<String> permissions);

        void onPermissionDenied(List<String> permissions);
    }

    /**
     * 请求权限
     *
     * @param activity    activity
     * @param callback    callback
     * @param permissions permissions
     */
    public static void requestPermission(Activity activity, PermissionCallback callback, String... permissions) {
        AndPermission.with(activity)
                .runtime()
                .permission(permissions)
                .rationale(new PermissionRationale())
                .onGranted(permissions12 -> callback.onPermissionGranted(permissions12))
                .onDenied(permissions1 -> {
                    if (AndPermission.hasAlwaysDeniedPermission(activity, permissions1)) {
                        showSettingForPermissionDialog(activity, callback, permissions1);
                    } else {
                        callback.onPermissionDenied(permissions1);
                    }
                })
                .start();
    }

    /**
     * 显示权限提示
     *
     * @param activity    activity
     * @param callback    callback
     * @param permissions permissions
     */
    public static void showSettingForPermissionDialog(Activity activity, PermissionCallback callback, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(activity, permissions);
        String message = "您未授权 " + TextUtils.join(",", permissionNames) + " 权限, 请在权限管理中开启此权限。";
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("设置", (dialog, which) -> startSettingForPermission(activity, permissions, callback))
                .setNegativeButton("取消", (dialog, which) -> callback.onPermissionDenied(permissions))
                .show();
    }

    /**
     * 跳转到权限设置界面进行权限设置
     *
     * @param activity    activity
     * @param permissions permissions
     * @param callback    callback
     */
    public static void startSettingForPermission(Activity activity, final List<String> permissions, PermissionCallback callback) {
        AndPermission.with(activity)
                .runtime()
                .setting()
                .onComeback(() -> {
                    if (AndPermission.hasPermissions(activity, permissions.toArray(new String[]{}))) {
                        callback.onPermissionGranted(permissions);
                    } else {
                        ArrayList<String> deniedPermissions = new ArrayList<>();
                        for (String permission : permissions) {
                            if (!AndPermission.hasPermissions(activity, permission)) {
                                deniedPermissions.add(permission);
                            }
                        }
                        callback.onPermissionDenied(deniedPermissions);
                    }
                })
                .start();
    }

    /**
     * 显示退出Dialog
     *
     * @param activity    activity
     * @param callback    callback
     * @param permissions permissions
     */
    public static void showExitDialog(Activity activity, PermissionCallback callback, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(activity, permissions);
        String message = "您未授权 " + TextUtils.join(",", permissionNames) + " 权限, 将无法使用系统。";
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("重新授权", (dialog, which) -> {
                    requestPermission(activity, callback, permissions.toArray(new String[]{}));
                    dialog.dismiss();
                })
                .setNegativeButton("退出", (dialog, which) -> {
                    System.exit(0);
                    Process.killProcess(Process.myPid());
                })
                .show();
    }
}