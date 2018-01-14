package com.ninestars;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ninestars.android.R;

import org.greenrobot.eventbus.EventBus;

/**
 * 提示用户登录，或者是购买
 */
public class DialogActivity extends AppCompatActivity {
    private Button mOkButton;
    private Button mCancleButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setEnterTransition(new Slide().setDuration(500));
        getWindow().setExitTransition(new Slide().setDuration(500));
        setContentView(R.layout.dialog);
        setTitle("提示");
        mOkButton = (Button) findViewById(R.id.ok);
        mCancleButton = (Button) findViewById(R.id.cancel);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LoginEvent());
            }
        });
        mCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();

            }
        });
    }
}
