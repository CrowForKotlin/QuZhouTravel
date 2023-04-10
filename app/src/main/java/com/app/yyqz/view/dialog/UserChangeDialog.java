package com.app.yyqz.view.dialog;

import static com.app.yyqz.utils.TipsUtil.showToast;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.app.yyqz.databinding.UserChangeBinding;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.utils.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserChangeDialog {

    public enum UserChangeType {
        NAME, PWD
    }

    @FunctionalInterface
    public interface IUserChangeUsrCallback {
        void onSuccess(String text);
    }

    public Activity mActivity;
    public Context mContext;
    public UserChangeType mType;
    public IUserChangeUsrCallback mIUserChangeUsrCallback;

    public void show() {
        UserChangeBinding binding = UserChangeBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog;
        if (mType == UserChangeType.NAME) {
            binding.userTf.setHint("名称");
            alertDialog = new MaterialAlertDialogBuilder(mContext)
                    .setTitle("更改用户名")
                    .setView(binding.getRoot())
                    .create();
        } else {
            binding.userTf.setHint("修改后的密码");
            alertDialog = new MaterialAlertDialogBuilder(mContext)
                    .setTitle("更改密码")
                    .setView(binding.getRoot())
                    .create();
        }
        alertDialog.show();
        ViewUtils.clickGap(binding.userSubmit, ViewUtils.GAP_TIME, view -> {
            String text = Objects.requireNonNull(binding.userEditText.getText()).toString();
            if (text.isEmpty()) {
                showToast(mContext, "文本框不能为空");
                return;
            }
            doChangeUsrName(text);
            alertDialog.dismiss();
        });
    }

    private void doChangeUsrName(String text) {
        NetWorkFactory.ChangeUserName(mActivity, User.info.getUsername(), text, response -> {
            int code = Integer.parseInt(response);
            if (code == -1) {
                showToast(mContext, "修改失败");
                return;
            }
            showToast(mContext, "修改成功！");
            mIUserChangeUsrCallback.onSuccess(text);
        });
    }

    private void doChangeUsrPwd(String text) {
        NetWorkFactory.ChangeUserPwd(mActivity, User.info.getUsername(), text, response -> {
            int code = Integer.parseInt(response);
            if (code == -1) {
                showToast(mContext, "修改失败");
                return;
            }
            showToast(mContext, "修改成功！");
        });
    }
}
