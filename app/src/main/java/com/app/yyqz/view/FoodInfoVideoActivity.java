package com.app.yyqz.view;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.yyqz.R;
import com.app.yyqz.databinding.ActivityFoodInfoVideoBinding;
import com.app.yyqz.network.NetworkPath;
import com.gyf.immersionbar.ImmersionBar;
import com.just.agentweb.AgentWeb;

public class FoodInfoVideoActivity extends AppCompatActivity {

    private ActivityFoodInfoVideoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFoodInfoVideoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView() {

        // 沉浸式效果
        ImmersionBar.with(this).statusBarDarkFont(true)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .navigationBarColor(R.color.white).init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        // 获取mAid AID 是 哔哩哔哩视频的一个值
        String mAid = getIntent().getStringExtra("data");

        // 初始化AgentWeb
        AgentWeb.with(this)
                .setAgentWebParent(mBinding.foodInfoVideoContainer, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(NetworkPath.BiliBili + mAid);
    }
}