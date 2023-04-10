package com.app.yyqz.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.app.yyqz.R;
import com.app.yyqz.databinding.TourCancelBookBinding;
import com.app.yyqz.utils.TipsUtil;
import com.app.yyqz.utils.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
public class CancelBookingDialog {

    public interface ICancelTypeCallback {
        void doOnCancelAll();

        void doOnCancelCount(int value);
    }

    private final Activity mActivity;
    private final Context mContext;
    private final ICancelTypeCallback mICancelTypeCallback;

    public void show() {

        // 获取Binding
        TourCancelBookBinding binding = TourCancelBookBinding.inflate(mActivity.getLayoutInflater());

        // 创建AlertDialog
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(mContext)
                .setIcon(R.drawable.ic_edit_24dp)
                .setTitle("筛选")
                .setView(binding.getRoot())
                .setCancelable(true)
                .create();

        // 显示
        alertDialog.show();

        // 设置 当前门票全部取消 的点击事件
        binding.tourCancelAll.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // 未选中
            if (!isChecked) {
                // 显示
                binding.tourSliderx.setVisibility(View.VISIBLE);
            } else {
                // 消失
                binding.tourSliderx.setVisibility(View.GONE);
            }
        });

        // 设置点击事件
        ViewUtils.clickGap(binding.foodEditSubmit, ViewUtils.GAP_TIME, view1 -> {

            // 取消预约全部
            if (binding.tourCancelAll.isChecked()) {
                mICancelTypeCallback.doOnCancelAll();
                alertDialog.dismiss();
                return;
            }

            // 取消预约指定数量
            if (binding.tourCancelCount.isClickable()) {
                mICancelTypeCallback.doOnCancelCount((int)binding.tourSliderx.getValue());
                alertDialog.dismiss();
            }
        });
    }

}
