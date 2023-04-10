package com.app.yyqz.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yyqz.App;
import com.app.yyqz.R;
import com.app.yyqz.databinding.TourRvTrafficBinding;

import java.util.ArrayList;

public class TourTrafficRvAdapter extends RecyclerView.Adapter<TourTrafficRvAdapter.VH> {

    // 存储控件
    static class VH extends RecyclerView.ViewHolder {
        public TextView vhRoadName;
        public TextView vhRoadStatus;
        public VH(@NonNull TourRvTrafficBinding rvBinding) {
            super(rvBinding.getRoot());
            vhRoadName = rvBinding.trafficRvRoadName;
            vhRoadStatus = rvBinding.trafficRvDesc;
        }

    }

    // 道路名称列表
    public ArrayList<String> mRoadNames;
    public String mCompleteDesc;
    public String mStatusDesc;
    private ColorStateList mGreen = null;

    public TourTrafficRvAdapter(ArrayList<String> mRoadNames, String mCompleteDesc, String mStatusDesc) {
        this.mRoadNames = mRoadNames;
        this.mCompleteDesc = mCompleteDesc;
        this.mStatusDesc = mStatusDesc;
    }

    @Override
    public int getItemCount() {
        return mRoadNames.size();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(TourRvTrafficBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {
        String roadName = mRoadNames.get(position);

        // 设置道路名称
        vh.vhRoadName.setText(roadName);

        // 设置道路状态
        vh.vhRoadStatus.setText(mStatusDesc);
        if (mStatusDesc.equals("畅通")) {
            if (mGreen == null) mGreen = ContextCompat.getColorStateList(App.getContextObject(), R.color.green);
            vh.vhRoadStatus.setTextColor(mGreen );
        }
    }
}
