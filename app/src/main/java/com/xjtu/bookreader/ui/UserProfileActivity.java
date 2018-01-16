package com.xjtu.bookreader.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.xjtu.bookreader.R;
import com.xjtu.bookreader.databinding.ActivityUserProfileBinding;
import com.xjtu.bookreader.util.UserUtil;

public class UserProfileActivity extends AppCompatActivity {


    private ActivityUserProfileBinding userProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
//        setContentView(R.layout.activity_user_profile);
        setTitle("个人资料");
        initView();

    }

    //
    private void initView() {
        if (UserUtil.isLogined()) {
            userProfileBinding.setUserId(UserUtil.getUserId() + "");
            userProfileBinding.setUserName(UserUtil.getUserName());
//            userProfileBinding.setUserAvatar(UserUtil.getUserAvatar());


            Glide.with(this)
                    .load(UserUtil.getUserAvatar())
                    .crossFade(500)
                    .placeholder(R.drawable.girl_avatar)
                    .error(R.drawable.girl_avatar)
                    .into(userProfileBinding.civUserImage);
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, UserProfileActivity.class);
        activity.startActivity(intent);
    }
}
