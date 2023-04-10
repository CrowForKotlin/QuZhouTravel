package com.app.yyqz.view.dialog;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.app.yyqz.R;
import com.app.yyqz.databinding.FoodEditTypeBinding;
import com.app.yyqz.utils.TipsUtil;
import com.app.yyqz.utils.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EditTypeDialog {

    public interface IEditTypeCallback {
        void doOnLikeType();

        void doOnRecommend();
    }

    private final Activity mActivity;
    private final Context mContext;
    private final Boolean mIsLikeType;
    private final IEditTypeCallback iEditTypeCallback;


    public void show() {
        FoodEditTypeBinding binding = FoodEditTypeBinding.inflate(mActivity.getLayoutInflater());
        binding.foodEditLikeType.setChecked(mIsLikeType);
        binding.foodEditRecommend.setChecked(!mIsLikeType);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(mContext)
                .setIcon(R.drawable.ic_edit_24dp)
                .setTitle("筛选")
                .setView(binding.getRoot())
                .setCancelable(true)
                .create();
        alertDialog.show();
        ViewUtils.clickGap(binding.foodEditSubmit, ViewUtils.GAP_TIME, view1 -> {
            if (binding.foodEditLikeType.isChecked()) {
                iEditTypeCallback.doOnLikeType();
                alertDialog.dismiss();
            } else if (binding.foodEditRecommend.isChecked()) {
                iEditTypeCallback.doOnRecommend();
                alertDialog.dismiss();
            } else {
                TipsUtil.showToast(mContext, "请至少选择一项提交");
            }
        });
    }

}
