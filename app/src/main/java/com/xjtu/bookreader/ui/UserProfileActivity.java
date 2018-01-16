package com.xjtu.bookreader.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xjtu.bookreader.R;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    //
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, UserProfileActivity.class);
        activity.startActivity(intent);
    }
}
