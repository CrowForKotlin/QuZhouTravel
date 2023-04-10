package com.app.yyqz.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yyqz.App;
import com.app.yyqz.R;
import com.app.yyqz.databinding.UserRvBinding;
import com.app.yyqz.network.NetworkPath;
import com.app.yyqz.network.response.UserTicketsDatas;
import com.app.yyqz.utils.ViewUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

// 用户RecyclerView适配器
public class UserRvAdapter extends RecyclerView.Adapter<UserRvAdapter.VH> {

    // 定义回调接口
    public interface IUserRvCancel {
        void callabck(UserTicketsDatas datas);
    }

    // Rv持有者，存放控件
    static class VH extends RecyclerView.ViewHolder {

        ImageView vImage;
        TextView vTickets;
        TextView vName;
        TextView vDate;


        public VH(@NonNull UserRvBinding rvBinding) {
            super(rvBinding.getRoot());
            this.vTickets = rvBinding.userRvTickets;
            this.vName = rvBinding.userRvTourName;
            this.vDate = rvBinding.userRvDate;
            this.vImage = rvBinding.userRvImage;
        }

    }

    // 门票数据列表
    public ArrayList<UserTicketsDatas> mUserTicketsData;

    // 回调接口， 点击 ”取消预订“ 按钮时触发
    public IUserRvCancel mIUserRvCancel;

    public UserRvAdapter(ArrayList<UserTicketsDatas> userTicketsDatas, IUserRvCancel iUserRvCancel) {
        this.mUserTicketsData = userTicketsDatas;
        this.mIUserRvCancel = iUserRvCancel;
    }

    @Override
    public int getItemCount() {
        return mUserTicketsData.size();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 获取rvBinding
        UserRvBinding rvBinding = UserRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        // 获取VH
        VH vh = new VH(rvBinding);

        // 设置 ”取消预定“ 按钮的点击事件 --> 执行回调
        ViewUtils.clickGap(rvBinding.userRvCancel, ViewUtils.GAP_TIME, view -> mIUserRvCancel.callabck(mUserTicketsData.get(vh.getAdapterPosition())));
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {

        // 门票数据
        UserTicketsDatas datas = mUserTicketsData.get(position);

        // 设置控件信息 通过门票数据获取
        vh.vName.setText(datas.getTourName());
        vh.vTickets.setText(App.getContextObject().getString(R.string.AlreadyTickets, datas.getBookingTicketsCount()));
        vh.vDate.setText(datas.getDate());

        // Glide 加载网络图片 （图片仓库路径 + 图片名称）
        Glide.with(vh.itemView)
                .load(NetworkPath.ImageContainer + datas.getTourImageName())
                .into(vh.vImage);

    }
}
