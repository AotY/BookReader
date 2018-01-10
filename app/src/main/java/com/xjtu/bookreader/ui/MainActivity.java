package com.xjtu.bookreader.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xjtu.bookreader.R;
import com.xjtu.bookreader.adapter.HomeViewPagerAdapter;
import com.xjtu.bookreader.databinding.ActivityMainBinding;
import com.xjtu.bookreader.ui.fragment.MallFragment;
import com.xjtu.bookreader.ui.fragment.ShelfFragment;
import com.xjtu.bookreader.ui.fragment.UserCenterFragment;
import com.xjtu.bookreader.util.Logger;
import com.xjtu.bookreader.view.CustomViewPager;

/**
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    //viewPager
    private CustomViewPager viewPager;

    //Fragments
    private ShelfFragment shelfFragment;
    private MallFragment mallFragment;
    private UserCenterFragment userCenterFragment;

    private MenuItem prevMenuItem;

    private ActivityMainBinding mBinding; // ActivityTransitionBinding

    /**
     * 这里切换Fragment
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_bookshelf:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_book_mall:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_user_center:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Initializing viewPager
        viewPager = mBinding.vpContent;
        bottomNavigationView = mBinding.navigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Logger.d("page onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        shelfFragment = new ShelfFragment(); // 书架
        mallFragment = new MallFragment();  // 书城
        userCenterFragment = new UserCenterFragment();  // 个人中心

        adapter.addFragment(shelfFragment);
        adapter.addFragment(mallFragment);
        adapter.addFragment(userCenterFragment);
        viewPager.setAdapter(adapter);
    }


}
