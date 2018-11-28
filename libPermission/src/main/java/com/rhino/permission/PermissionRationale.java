package com.rhino.permission;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

/**
 * @author rhino
 * @since Create on 2018/11/24.
 **/
public final class PermissionRationale implements Rationale<List<String>> {

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = String.format("请对 %1$s 权限进行授权,以继续使用应用", TextUtils.join(",", permissionNames));
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> executor.execute())
                .setNegativeButton("取消", (dialog, which) -> executor.cancel())
                .show();
    }

}