package com.xjtu.bookreader.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xjtu.bookreader.R;
import com.xjtu.bookreader.databinding.FragmentUserCenterBinding;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.util.CommonUtils;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.Logger;
import com.xjtu.bookreader.util.PerfectClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserCenterFragment extends Fragment {

    private MainActivity activity;

    private FragmentUserCenterBinding userCenterBinding;

    // 内容布局
    protected LinearLayout mContainer;

    public UserCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtil.debug("UserCenterFragment ---------> onPause");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        userCenterBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_user_center, container, false);
        View view = userCenterBinding.getRoot();
        //here data must be an instance of the class MarsDataProvider
//        binding.setMarsdata(data);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DebugUtil.debug("UserCenterFragment ---------> onStart");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        DebugUtil.debug("UserCenterFragment ---------> setUserVisibleHint : " + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        try{
            if(getUserVisibleHint()) {//界面可见时
                hideActionBar();
            } else {
                showActionBar();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        DebugUtil.debug("UserCenterFragment ---------> onResume");
        super.onResume();
//        hideActionBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        DebugUtil.debug("UserCenterFragment ---------> onPause");
//        showActionBar();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void hideActionBar() {
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        // 取消hide, show 动画
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
            actionBar.hide();
        }

//        Window window = activity.getWindow();
//        View decorView = activity.getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

    }


    @SuppressLint("RestrictedApi")
    private void showActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
            actionBar.show();
        }
//        activity.getSupportActionBar().show();
//        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
//        decorView.setSystemUiVisibility(uiOptions);
    }

    // 如果需要加载数据
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 设置点击事件
        initClick();
    }

    private void initClick() {

        // 点击用户头像，进入用户资料编辑页面
        userCenterBinding.civAvatarImg.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });

        // 点击登录，登录成功或点击进入用户资料编辑页面
        userCenterBinding.btnLogin.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });

        // 点击我的账号，查看余额、充值等
        userCenterBinding.rlMyAccountContainer.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });


        // 点击我的购买历史
        userCenterBinding.rlMyPurchaseContainer.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });


        // 点击设置中心
        userCenterBinding.rlSettingContainer.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });

        // 点击帮助中心
        userCenterBinding.rlHelpContainer.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop() {
        super.onStop();
        DebugUtil.debug("UserCenterFragment ---------> onStop");
    }
}
