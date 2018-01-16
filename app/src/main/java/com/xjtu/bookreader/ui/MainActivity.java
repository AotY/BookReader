package com.xjtu.bookreader.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.xjtu.bookreader.R;
import com.xjtu.bookreader.adapter.HomeViewPagerAdapter;
import com.xjtu.bookreader.bean.model.BookOfShelf;
import com.xjtu.bookreader.databinding.ActivityMainBinding;
import com.xjtu.bookreader.event.BackFromKooReaderEvent;
import com.xjtu.bookreader.event.SwitchFragmentEvent;
import com.xjtu.bookreader.ui.fragment.MallFragment;
import com.xjtu.bookreader.ui.fragment.ShelfFragment;
import com.xjtu.bookreader.ui.fragment.UserCenterFragment;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.Logger;
import com.xjtu.bookreader.util.StringResourceUtil;
import com.xjtu.bookreader.view.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.Date;

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

    public static final int REQUEST_FROM_SHELF_ADAPTER = 1;


    private static final String BOOK_ID = "bookId";
    private static final String LAST_TIME = "lastTime";
    private static final String PROGRESS = "progress";


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
                    // Change the title associated with this activity.
                    setTitle(StringResourceUtil.getStringById(R.string.bookshelf));
                    return true;
                case R.id.navigation_book_mall:
                    viewPager.setCurrentItem(1);
                    setTitle(StringResourceUtil.getStringById(R.string.book_mall));
                    return true;
                case R.id.navigation_user_center:
                    viewPager.setCurrentItem(2);
                    setTitle("");
                    return true;
            }
            return false;
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(StringResourceUtil.getStringById(R.string.bookshelf));
        // 取消hide, show 动画
        getSupportActionBar().setShowHideAnimationEnabled(false);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        EventBus.getDefault().register(this);

        initView();


    }

    private void initView() {
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

    // 设置ViewPager， 书架、书城和个人中心
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

    // 切换到商城Fragment
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchFragment(SwitchFragmentEvent event) {
        if (event.getSwitchIndex() == SwitchFragmentEvent.BOOK_MALL) {
            viewPager.setCurrentItem(1);
            setTitle(StringResourceUtil.getStringById(R.string.book_mall));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    /**
     * 调用Activity 后返回数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DebugUtil.debug("MainActivity ---------------> onActivityResult");

        if (requestCode == REQUEST_FROM_SHELF_ADAPTER) {
            if (resultCode == RESULT_OK) {

                final long bookId = data.getLongExtra(BOOK_ID, 0);
                DebugUtil.debug("MainActivity  bookId ---------------> " + bookId);

                if (bookId != 0) {
                    float progress = data.getFloatExtra(PROGRESS, 0);
                    Date date = (Date) data.getSerializableExtra(LAST_TIME);

                    DebugUtil.debug("MainActivity  progress ---------------> " + progress);
                    DebugUtil.debug("MainActivity  date ---------------> " + date.toString());

                    // update
                    BookOfShelf bookOfShelf = DataSupport.find(BookOfShelf.class, bookId);
                    bookOfShelf.setLastTime(date);
                    bookOfShelf.setProgress(progress);
                    bookOfShelf.save();

                    // ShelfFragment 重新加载数据
                    EventBus.getDefault().post(new BackFromKooReaderEvent());
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }
}
