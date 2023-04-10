package com.app.yyqz.adapter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yyqz.App;
import com.app.yyqz.R;
import com.app.yyqz.databinding.HomeRvBinding;
import com.app.yyqz.view.TourActivity;
import com.app.yyqz.network.NetworkPath;
import com.app.yyqz.network.response.RespTourEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


// 主页列表适配器
public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.VH> {

    // 适配器持有者 ViewHolder(VH)
    public static class VH extends RecyclerView.ViewHolder {

        // HomeRvViewBinding， 轻量级组件，代替不断findViewById
        public HomeRvBinding rvBinding;

        // 有参构造VH
        public VH(@NonNull HomeRvBinding rvBinding) {
            super(rvBinding.getRoot());
            this.rvBinding = rvBinding;
        }
    }

    // 有参构造
    public HomeRvAdapter(ArrayList<RespTourEntity> mRespTourEntities) {
        this.mRespTourEntities = mRespTourEntities;
    }

    // 要显示在页面上的集合数据
    public ArrayList<RespTourEntity> mRespTourEntities;

    // 获取图片高度 = 屏幕像素高度 / 3
    private final int mMeasureHeight = App.getContextObject().getResources().getDisplayMetrics().widthPixels / 3;

    // 获取数据大小
    @Override
    public int getItemCount() {
        return mRespTourEntities.size();
    }

    // 创建ViewHolder
    @NonNull
    @Override
    public HomeRvAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 从parent(父布局)去加载home_rv这个布局文件
        HomeRvBinding rvBinding = HomeRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        VH vh = new VH(rvBinding);

        // 给rv的项目视图设置监听事件
        vh.itemView.setOnClickListener(v -> {

            // 设置要前往的Activity
            Intent intent = new Intent(parent.getContext(), TourActivity.class);
            Bundle bundle = new Bundle();

            // 键值对的方式存入数据
            bundle.putSerializable("data", mRespTourEntities.get(vh.getAdapterPosition()));
            intent.putExtras(bundle);

            // 跳转
            parent.getContext().startActivity(intent);
        });
        return vh;
    }

    // 绑定ViewHolder
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        // 获取单个数据
        RespTourEntity tourEntity = mRespTourEntities.get(position);

        // 大于Android 8.0设置 小工具提示（长按会有提示）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.rvBinding.homeRvTitle.setTooltipText(tourEntity.getName());
        }

        // 设置显示的内容
        holder.rvBinding.homeRvTitle.setText(tourEntity.getName());
        holder.rvBinding.homeRvTickets.setText(App.getContextObject().getString(R.string.Tickets, tourEntity.getTickets()));
        holder.rvBinding.homeRvLocation.setText(tourEntity.getLocate());
        holder.rvBinding.homeRvOpenTime.setText(tourEntity.getOpenTime());
        holder.rvBinding.homeRvCardFrame.getLayoutParams().height = mMeasureHeight;

        // Glide加载网络图片
        Glide.with(holder.rvBinding.getRoot())
                .load(NetworkPath.ImageContainer + tourEntity.getImageName())
                .into(holder.rvBinding.homeRvImage);
    }

}
