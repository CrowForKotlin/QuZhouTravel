package com.app.yyqz.view;

import static com.app.yyqz.utils.GsonUtil.gson;
import static com.app.yyqz.utils.TipsUtil.showToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.yyqz.databinding.ActivityMainBinding;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.response.RespLoginEntity;
import com.app.yyqz.utils.TipsUtil;
import com.app.yyqz.utils.ViewUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.Type;

import lombok.val;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding; // 声明 Binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater()); // 初始化mBinding
        setContentView(mBinding.getRoot());                          // 获取Binding的布局并重置
        initView();                                                  // 初始化视图
    }

    // 初始化视图
    @SuppressLint("SetTextI18n")
    private void initView() {

        // 沉浸式效果
        ImmersionBar.with(this)
                .statusBarDarkFont(false)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        // 登录
        ViewUtils.clickGap(mBinding.login, ViewUtils.GAP_TIME, view -> doLogin());

        // 前往注册
        ViewUtils.clickGap(mBinding.toReg, ViewUtils.GAP_TIME, view -> startActivity(new Intent(this, RegActivity.class)));

        val sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (sp.getBoolean("auto", false) && !sp.getBoolean("exit_by_home", false) && !sp.getBoolean("exit_by_user", false)) {
            mBinding.loginUsr.setText(sp.getString("usr", ""));
            mBinding.loginPwd.setText(sp.getString("pwd", ""));
            mBinding.check.setChecked(true);
            doLogin();
        } else {
            mBinding.loginUsr.setText(sp.getString("usr", null));
            mBinding.loginPwd.setText(sp.getString("pwd", null));
            mBinding.check.setChecked(sp.getBoolean("auto", false));
        }
        sp.edit().putBoolean("exit_by_home", false).apply();
        sp.edit().putBoolean("exit_by_user", false).apply();
    }

    // 执行登录逻辑
    private void doLogin() {
        String usr = String.valueOf(mBinding.loginUsr.getText());
        String pwd = String.valueOf(mBinding.loginPwd.getText());

        // 如果用户名 和 密码不为空 代表可以登录
        if (!usr.isEmpty() && !pwd.isEmpty()) {
            NetWorkFactory.Login(this, usr, pwd, response -> {

                // 获取服务端发过来的 数据，这里对接后得到的数据一个实体，利用Gson反序列化Json得到一个实体类
                RespLoginEntity loginEntity = gson.fromJson(response, (Type) RespLoginEntity.class);

                // 如果数据是 -1 代表 账号密码不匹配
                if (loginEntity.getCode() == -1)
                    showToast(LoginActivity.this, "账号密码有误，请重新输入！");
                else {
                    User.info = loginEntity;
                    User.IsGuest = false;
                    val edit = getSharedPreferences("login", Context.MODE_PRIVATE).edit();
                    edit.putString("usr", loginEntity.getUsername());
                    edit.putString("pwd", loginEntity.getPassword());
                    edit.putBoolean("auto", mBinding.check.isChecked());
                    edit.apply();
                    showToast(LoginActivity.this, "登录成功");
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            });
            return; // 退出当前函数逻辑 避免往下执行
        }

        // 否则就提示
        showToast(LoginActivity.this, "账号密码不能为空！");
    }
}