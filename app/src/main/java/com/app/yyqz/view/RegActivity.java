package com.app.yyqz.view;

import static com.app.yyqz.utils.TipsUtil.showToast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.yyqz.databinding.ActivityRegBinding;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.utils.ViewUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.Objects;

public class RegActivity extends AppCompatActivity {

    private ActivityRegBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRegBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView() {

        // 沉浸式效果
        ImmersionBar.with(this)
                .statusBarDarkFont(false)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        // 点击注册
        ViewUtils.clickGap(mBinding.reg, ViewUtils.GAP_TIME, (v -> {

            // 获取四个参数 分别是注册的信息
            String name = Objects.requireNonNull(mBinding.regName.getText()).toString();
            String usr = Objects.requireNonNull(mBinding.regUsr.getText()).toString();
            String pwd = Objects.requireNonNull(mBinding.regPwd.getText()).toString();
            String verifypwd = Objects.requireNonNull(mBinding.regPwdVerify.getText()).toString();

            // 判断信息是否为空 ，是的话提示并退出回调避免继续执行
            if (name.isEmpty() || usr.isEmpty() || pwd.isEmpty() || verifypwd.isEmpty()) {
                showToast(this, "请检查填写的信息是否有误，不能为空！");
                return;
            }

            // 判断密码是否输入一致
            if (pwd.equals(verifypwd)) {
                doOnReg(name, usr, pwd);
            }
        }));
    }

    // 执行注册
    private void doOnReg(String name, String usr, String pwd) {
        NetWorkFactory.Reg(this, name, usr, pwd, response -> {

            // 数据为 -1 代表注册失败
            if (Integer.parseInt(response) == -1) {
                showToast(this, "注册失败，请换一个用户名试试！");
            } else {
                showToast(this, "注册成功！");
                finish();
            }
        });
    }
}