package com.xjtu.bookreader.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xjtu.bookreader.R;

public class PurchaseHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
    }

    //
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PurchaseHistoryActivity.class);
        activity.startActivity(intent);
    }
}
