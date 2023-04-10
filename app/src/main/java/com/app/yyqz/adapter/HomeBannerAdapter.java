package com.app.yyqz.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yyqz.databinding.HomeBannerBinding;
import com.app.yyqz.network.NetworkPath;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

// 主页轮播图是适配器
public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.VH> {

    public static class VH extends RecyclerView.ViewHolder {
        HomeBannerBinding rvBinding;

        public VH(@NonNull HomeBannerBinding rvBinding) {
            super(rvBinding.getRoot());
            this.rvBinding = rvBinding;
        }
    }

    public HomeBannerAdapter(ArrayList<String> imageUrls) {
        this.mImageUrls = imageUrls;
    }

    // 图片数据列表
    public ArrayList<String> mImageUrls;

    // 获取数据大小
    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }


    @NonNull
    @Override
    public HomeBannerAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeBannerAdapter.VH(HomeBannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBannerAdapter.VH holder, int position) {
        // Glide 加载图片 （图片路径 + 图片名）
        Glide.with(holder.rvBinding.getRoot())
                .load(NetworkPath.ImageContainer + mImageUrls.get(position))
                .into(holder.rvBinding.homeBannerImage);
    }

}
