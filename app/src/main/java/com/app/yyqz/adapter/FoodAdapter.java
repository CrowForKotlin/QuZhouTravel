package com.app.yyqz.adapter;

import static com.app.yyqz.utils.ViewUtils.GAP_TIME;
import static com.app.yyqz.utils.ViewUtils.GAP_TIME_SHORT;
import static com.app.yyqz.utils.ViewUtils.clickGap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yyqz.App;
import com.app.yyqz.databinding.FoodRvBinding;
import com.app.yyqz.view.FoodInfoActivity;
import com.app.yyqz.R;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.NetworkPath;
import com.app.yyqz.network.entity.FoodEntity;
import com.app.yyqz.utils.TipsUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lombok.val;

// 主页轮播图是适配器
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.VH> {

    // VH rv持有者， 存放控件
    public static class VH extends RecyclerView.ViewHolder {
        TextView foodTitle;
        ImageView foodImage;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;

        public VH(@NonNull FoodRvBinding rvBinding) {
            super(rvBinding.getRoot());
            foodTitle = rvBinding.foodRvTitle;
            foodImage = rvBinding.foodRvImage;
            imageView1 = rvBinding.foodStartImage1;
            imageView2 = rvBinding.foodStartImage2;
            imageView3 = rvBinding.foodStartImage3;
            imageView4 = rvBinding.foodStartImage4;
            imageView5 = rvBinding.foodStartImage5;
        }
    }

    public FoodAdapter(Activity activity, ArrayList<FoodEntity> foods) {
        this.mFoods = foods;
        this.mActivity = activity;
    }

    // 图片数据列表
    public ArrayList<FoodEntity> mFoods;
    private final Activity mActivity;

    // 默认状态值 用户执行回滚操作，确保评分失败后上一次的评分会拿到并恢复到上一次的评分
    private int mState = 1;

    // 红色
    private final ColorStateList mRedColor = ContextCompat.getColorStateList(App.getContextObject(), R.color.red);

    // 白色
    private final ColorStateList mWhiteColor = ContextCompat.getColorStateList(App.getContextObject(), R.color.white_tran);

    // 获取数据大小
    @Override
    public int getItemCount() {
        return mFoods.size();
    }


    @NonNull
    @Override
    public FoodAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FoodAdapter.VH vh = new FoodAdapter.VH(FoodRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        // 点击图片 1 评分1分
        clickGap(vh.imageView1, GAP_TIME_SHORT, view -> {
            setImageViewTint(1, vh.imageView1, vh.imageView2, vh.imageView3, vh.imageView4, vh.imageView5);
            doOnChangeScore(vh, 1);
        });

        // 点击图片 2 评分2分
        clickGap(vh.imageView2, GAP_TIME_SHORT, view -> {
            setImageViewTint(2, vh.imageView1, vh.imageView2, vh.imageView3, vh.imageView4, vh.imageView5);
            doOnChangeScore(vh, 2);
        });

        // 点击图片 3 评分3分
        clickGap(vh.imageView3, GAP_TIME_SHORT, view -> {
            setImageViewTint(3, vh.imageView1, vh.imageView2, vh.imageView3, vh.imageView4, vh.imageView5);
            doOnChangeScore(vh, 3);
        });

        // 点击图片 4 评分4分
        clickGap(vh.imageView4, GAP_TIME_SHORT, view -> {
            setImageViewTint(4, vh.imageView1, vh.imageView2, vh.imageView3, vh.imageView4, vh.imageView5);
            doOnChangeScore(vh, 4);
        });

        // 点击图片 5 评分5分
        clickGap(vh.imageView5, GAP_TIME_SHORT, view -> {
            setImageViewTint(5, vh.imageView1, vh.imageView2, vh.imageView3, vh.imageView4, vh.imageView5);
            doOnChangeScore(vh, 5);
        });

        // 点击卡片 跳转美食信息
        clickGap(vh.itemView, GAP_TIME, view -> {
            val intent = new Intent(parent.getContext(), FoodInfoActivity.class);
            val bundle = new Bundle();

            // 获取数据
            FoodEntity foodEntity = mFoods.get(vh.getAdapterPosition());

            // 根据美食评分的色调得到score
            int score = 0;
            if (vh.imageView1.getBackgroundTintList() == mRedColor) score += 1;
            if (vh.imageView2.getBackgroundTintList() == mRedColor) score += 1;
            if (vh.imageView3.getBackgroundTintList() == mRedColor) score += 1;
            if (vh.imageView4.getBackgroundTintList() == mRedColor) score += 1;
            if (vh.imageView5.getBackgroundTintList() == mRedColor) score += 1;

            // 设置评分
            foodEntity.setScore(score);
            bundle.putSerializable("data", foodEntity);
            intent.putExtras(bundle);

            // 跳转
            parent.getContext().startActivity(intent);
        });
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        // 给每个ItemView指定不同的类型，这样在RecyclerView看来，这些ItemView全是不同的，不能复用
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.VH holder, int position) {

        // 获取数据
        FoodEntity food = mFoods.get(position);

        // Glide 加载图片 （图片路径 + 图片名）
        Glide.with(holder.itemView)
                .load(NetworkPath.FoodImageContainer + food.getImageName())
                .into(holder.foodImage);

        // 设置美食标题
        holder.foodTitle.setText(food.getTitle());

        // 评分不为0则设置评分色调
        int score = food.getScore();
        if (score != 0) {
            setImageViewTint(score, holder.imageView1, holder.imageView2, holder.imageView3, holder.imageView4, holder.imageView5);
        }
    }

    // 设置图片色调 index 代表从左往右每个图片的位置， 比如 第一个图片 1星 、 第二哥图片 2星 ... ImageView 为控件
    private void setImageViewTint(int index, ImageView... imageView) {

        // ImageView的长度不为 5 则退出方法
        if (imageView.length != 5) return;

        // 判断mState的数值是否在 1..5 不在则退出方法
        if (mState < 1 || mState > 5) return;

        // 因为集合元素都是从0开始，所以index - 1
        int pos = index - 1;

        // 循环 5次
        for (int i = 0; i < 5; i++) {

            // 顺序是 评分一到五从左往右
            // 如果 当前点击的位置 小于 i 代表需要将 右边的评分的颜色初始值
            if (pos < i) {
                if (imageView[i].getBackgroundTintList() == mRedColor) {
                    imageView[i].setBackgroundTintList(mWhiteColor);
                }
            }
            // 否则 左边的评分颜色 设置红色突出评分
            else {
                if (imageView[i].getBackgroundTintList() == mWhiteColor) {
                    imageView[i].setBackgroundTintList(mRedColor);
                }
            }
        }
    }

    // 修改评分
    private void doOnChangeScore(FoodAdapter.VH vh, int score) {

        // 网络请求 修改评分 需要传入用户名和美食名称
        NetWorkFactory.ChangeUserScore(mActivity, User.info.getUsername(), vh.foodTitle.getText().toString(), score, response -> {

            // 解析响应的结果 -1 代表失败 执行回滚重新操作
            if (Integer.parseInt(response) == -1) {
                TipsUtil.showToast(mActivity.getApplicationContext(), "评分失败，已回滚！");
                setImageViewTint(mState, vh.imageView1, vh.imageView2, vh.imageView3, vh.imageView4, vh.imageView5);
            } else {

                // 否则设置评分 给mState 代表请求响应200成功
                mState = score;
            }
        });
    }
}
