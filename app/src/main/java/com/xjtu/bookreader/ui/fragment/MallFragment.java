package com.xjtu.bookreader.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xjtu.bookreader.R;
import com.xjtu.bookreader.adapter.EmptyAdapter;
import com.xjtu.bookreader.adapter.MallAdapter;
import com.xjtu.bookreader.app.Constants;
import com.xjtu.bookreader.base.BaseFragment;
import com.xjtu.bookreader.bean.BannerBean;
import com.xjtu.bookreader.bean.BannerItemBean;
import com.xjtu.bookreader.bean.MallRecommendItemBean;
import com.xjtu.bookreader.databinding.FragmentMallBinding;
import com.xjtu.bookreader.databinding.MallHeaderItemBinding;
import com.xjtu.bookreader.http.RequestImpl;
import com.xjtu.bookreader.http.cache.ACache;
import com.xjtu.bookreader.model.MallModel;
import com.xjtu.bookreader.ui.BookDetailActivity;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.util.CommonUtil;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.GlideImageLoader;
import com.xjtu.bookreader.util.PerfectClickListener;
import com.xjtu.bookreader.util.SharedPreferencesUtils;
import com.xjtu.bookreader.util.TimeUtil;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import rx.Subscription;

/**
 * 书城Fragment，逻辑
 * <p>
 * 更新逻辑：判断是否是第二天(相对于2016-11-26)
 * 是：判断是否是大于12：30
 * *****     |是：刷新当天数据
 * *****     |否：使用缓存：|无：请求前一天数据,直到请求到数据为止
 * **********             |有：使用缓存
 * 否：使用缓存 ： |无：请求今天数据
 * **********    |有：使用缓存
 */
public class MallFragment extends BaseFragment<FragmentMallBinding> {

    private static final String TAG = "MallFragment";

    private ACache maCache;

    private ArrayList<List<MallRecommendItemBean>> mLists;

    private ArrayList<String> mBannerImages;

    private MallModel mMallModel;

    private MallHeaderItemBinding mHeaderBinding;

    private View mHeaderView;

    private MallAdapter mMallAdapter;

    private boolean mIsPrepared = false;
    private boolean mIsFirst = true;

    private MainActivity activity;

    // 是否是上一天的请求
    private boolean isOldDayRequest;
    private RotateAnimation animation;

    // 记录请求的日期
    private String year = getTodayTime().get(0);
    private String month = getTodayTime().get(1);
    private String day = getTodayTime().get(2);


    //
    public MallFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtil.debug("MallFragment ---------> onCreate");
        setHasOptionsMenu(true);
    }

    @Override
    public int setContent() {
        return R.layout.fragment_mall;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 设置加载完成的状态
        showContentView();
        // 初始化动画
        initAnimation();

        // 获取缓存
        maCache = ACache.get(getContext());

        // mallModal 设置数据等工作
        mMallModel = new MallModel();

        // banner图缓存
        mBannerImages = (ArrayList<String>) maCache.getAsObject(Constants.BANNER_PIC);

        // header item
        mHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.mall_header_item, null, false);

        // 设置每日日期，和设置点击事件等
        initLocalSetting();

        // init RecyclerView
        initRecyclerView();

        mIsPrepared = true;

        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();

        // 测试，清除缓存
        maCache.clear();
        maCache.remove(Constants.EVERYDAY_DATA);
        maCache.remove(Constants.BANNER_PIC);
    }


    @Override
    public void onStart() {
        super.onStart();
        DebugUtil.debug("MallFragment ---------> onStart");
//        showActionBar();
    }

    @Override
    public void onResume() {
        DebugUtil.debug("MallFragment ---------> onResume");
        super.onResume();
        // 失去焦点，否则RecyclerView第一个item会回到顶部
        bindingView.xrvRecommend.setFocusable(false);
        // 开始图片请求
        Glide.with(getActivity()).resumeRequests();


//        showActionBar();

    }

    private void showActionBar() {
        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        activity.getSupportActionBar().show();
    }

    /**
     *
     */
    private void initAnimation() {
        bindingView.llLoading.setVisibility(View.VISIBLE);
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(3000);//设置动画持续时间
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(10);
        bindingView.ivLoading.setAnimation(animation);
        animation.startNow();
    }

    /**
     * 获取当天日期
     */
    private ArrayList<String> getTodayTime() {
        String data = TimeUtil.getData();
        String[] split = data.split("-");
        String year = split[0];
        String month = split[1];
        String day = split[2];
        ArrayList<String> list = new ArrayList<>();
        list.add(year);
        list.add(month);
        list.add(day);
        return list;
    }

    @Override
    protected void loadData() {

        // 显示时轮播图滚动
        if (mHeaderBinding != null && mHeaderBinding.banner != null) {
            mHeaderBinding.banner.startAutoPlay();
            mHeaderBinding.banner.setDelayTime(4000);
        }

        if (!mIsVisible || !mIsPrepared) {
            return;
        }

        // 获取时间
        String oneData = SharedPreferencesUtils.getString(Constants.EVERYDAY_DATA, "2018-1-8");


//        isOldDayRequest = false;
//        mMallModel.setDate(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));
//
//        showRotaLoading(true);
//
//        // 加载banner图片
//        loadBannerPicture();
//
//        // 加载推荐内容
//        loadRecommendData();

        if (!oneData.equals(TimeUtil.getData())) {// 是第二天
            if (TimeUtil.isRightTime()) {//大于12：30,请求

                isOldDayRequest = false;
                mMallModel.setDate(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));

                showRotaLoading(true);

                // 加载banner图片
                loadBannerPicture();

                // 加载推荐内容
                loadRecommendData();


            } else {// 小于，取缓存没有请求前一天
                ArrayList<String> lastTime = TimeUtil.getLastTime(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));
                mMallModel.setDate(lastTime.get(0), lastTime.get(1), lastTime.get(2));

                year = lastTime.get(0);
                month = lastTime.get(1);
                day = lastTime.get(2);

                isOldDayRequest = true;// 是昨天
                getACacheData();
            }
        } else {// 当天，取缓存，没有请求当天
            isOldDayRequest = false;
            getACacheData();
        }
    }

    /**
     *
     */
    private void initLocalSetting() {
        mMallModel.setDate(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));


        // 显示日期,去掉第一位的"0" ，设置当天日期
        mHeaderBinding.includeEveryday.tvDailyText.setText(getTodayTime().get(2).indexOf("0") == 0 ?
                getTodayTime().get(2).replace("0", "") : getTodayTime().get(2));


        // 精选书籍
        mHeaderBinding.includeEveryday.ibSelected.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //  WebViewActivity.loadUrl(v.getContext(), "https://gank.io/xiandu", "加载中...");
            }
        });

        // 图书排名
        mHeaderBinding.includeEveryday.ibBookRanking.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                RxBus.getDefault().post(RxCodeConstants.JUMP_TYPE_TO_ONE, new RxBusBaseMessage());
            }
        });
    }

    /**
     * 取缓存
     */
    private void getACacheData() {
        if (!mIsFirst) {
            return;
        }

        if (mBannerImages != null && mBannerImages.size() > 0) {
            mHeaderBinding.banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
        } else {
            loadBannerPicture();
        }

        mLists = (ArrayList<List<MallRecommendItemBean>>) maCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            setAdapter(mLists);
        } else {
            showRotaLoading(true);
            loadRecommendData();
        }
    }


    /**
     * 加载正文内容 （推荐）
     */
    private void loadRecommendData() {

        mMallModel.showRecyclerViewData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                if (mLists != null) {
                    mLists.clear();
                }
                mLists = (ArrayList<List<MallRecommendItemBean>>) object;
                if (mLists.size() > 0 && mLists.get(0).size() > 0) {
                    setAdapter(mLists);
                } else {
                    requestBeforeData();
                }
            }

            @Override
            public void loadFailed() {
                if (mLists != null && mLists.size() > 0) {
                    return;
                }
                showError();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                MallFragment.this.addSubscription(subscription);
            }
        });
    }

    /**
     * 没请求到数据就取缓存，没缓存一直请求前一天数据
     */
    private void requestBeforeData() {
        mLists = (ArrayList<List<MallRecommendItemBean>>) maCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            setAdapter(mLists);
        } else {
            // 一直请求，知道请求到数据为止
            ArrayList<String> lastTime = TimeUtil.getLastTime(year, month, day);
            mMallModel.setDate(lastTime.get(0), lastTime.get(1), lastTime.get(2));
            year = lastTime.get(0);
            month = lastTime.get(1);
            day = lastTime.get(2);
            loadRecommendData();
        }
    }

    /**
     * 无数据返回时，暂时去掉
     */
    private void setEmptyAdapter() {
        showRotaLoading(false);

        EmptyAdapter emptyAdapter = new EmptyAdapter();
        ArrayList<String> list = new ArrayList<>();
        list.add(CommonUtil.getString(R.string.string_everyday_empty));
        emptyAdapter.addAll(list);
        bindingView.xrvRecommend.setAdapter(emptyAdapter);

        // 保存请求的日期
        SharedPreferencesUtils.putString("everyday_data", TimeUtil.getData());

        mIsFirst = false;
    }

    private void initRecyclerView() {
        bindingView.xrvRecommend.setPullRefreshEnabled(false);
        bindingView.xrvRecommend.setLoadingMoreEnabled(false);

        if (mHeaderView == null) {
            mHeaderView = mHeaderBinding.getRoot();
            bindingView.xrvRecommend.addHeaderView(mHeaderView);
        }


        bindingView.xrvRecommend.setLayoutManager(new LinearLayoutManager(getContext()));

        // 需加，不然滑动不流畅
        bindingView.xrvRecommend.setNestedScrollingEnabled(false);
        bindingView.xrvRecommend.setHasFixedSize(false);
        bindingView.xrvRecommend.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 设置adapter
     *
     * @param lists
     */
    private void setAdapter(ArrayList<List<MallRecommendItemBean>> lists) {
        showRotaLoading(false);

        if (mMallAdapter == null) {
            mMallAdapter = new MallAdapter(getActivity());
        } else {
            mMallAdapter.clear();
        }

        mMallAdapter.addAll(lists);

        maCache.remove(Constants.EVERYDAY_CONTENT);

        // 缓存三天，这样就可以取到缓存了！
        maCache.put(Constants.EVERYDAY_CONTENT, lists, 259200);

        if (isOldDayRequest) {
            ArrayList<String> lastTime = TimeUtil.getLastTime(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));
            SharedPreferencesUtils.putString(Constants.EVERYDAY_DATA, lastTime.get(0) + "-" + lastTime.get(1) + "-" + lastTime.get(2));
        } else {
            // 保存请求的日期
            SharedPreferencesUtils.putString(Constants.EVERYDAY_DATA, TimeUtil.getData());
        }

        mIsFirst = false;

        bindingView.xrvRecommend.setAdapter(mMallAdapter);
        mMallAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onInvisible() {
        // 不可见时轮播图停止滚动
        if (mHeaderBinding != null && mHeaderBinding.banner != null) {
            mHeaderBinding.banner.stopAutoPlay();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        DebugUtil.debug("MallFragment ---------> onPause");
        // 停止全部图片请求 跟随着Activity
        Glide.with(getActivity()).pauseRequests();

    }

    /**
     * 加载banner图
     */
    private void loadBannerPicture() {
        mMallModel.showBannerPage(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                if (mBannerImages == null) {
                    mBannerImages = new ArrayList<>();
                } else {
                    mBannerImages.clear();
                }
                BannerBean bean = (BannerBean) object;

                if (bean != null && bean.getResults() != null) {
                    final List<BannerItemBean> result = bean.getResults();
                    if (result != null && result.size() > 0) {

                        for (int i = 0; i < result.size(); i++) {
                            //获取所有图片
                            mBannerImages.add(result.get(i).getImage());
                        }

                        mHeaderBinding.banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
                        // 这个默认下标是从0开始的。
                        mHeaderBinding.banner.setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
//                                Toast.makeText(activity, "OnBannerClick", Toast.LENGTH_SHORT).show();
//                                position = position - 1;
                                // 链接没有做缓存，如果轮播图使用的缓存则点击图片无效
                                if (result.get(position) != null) {
                                    // 跳转到书籍详情页面
                                    // 跳转页面，传入ID就好
                                    Toast.makeText(activity, "id: " + result.get(position).getId(), Toast.LENGTH_SHORT).show();
                                    BookDetailActivity.start(activity, result.get(position).getId(), mHeaderBinding.banner.getImageView(position));

                                }
                            }
                        });

                        maCache.remove(Constants.BANNER_PIC);
                        maCache.put(Constants.BANNER_PIC, mBannerImages, 30000);
                    }
                }
            }

            @Override
            public void loadFailed() {

            }

            @Override
            public void addSubscription(Subscription subscription) {
                MallFragment.this.addSubscription(subscription);
            }
        });
    }

    /**
     * @param isLoading
     */
    private void showRotaLoading(boolean isLoading) {
        if (isLoading) {
            bindingView.llLoading.setVisibility(View.VISIBLE);
            bindingView.xrvRecommend.setVisibility(View.GONE);
            animation.startNow();
        } else {
            bindingView.llLoading.setVisibility(View.GONE);
            bindingView.xrvRecommend.setVisibility(View.VISIBLE);
            animation.cancel();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        activity.getMenuInflater().inflate(R.menu.mall, menu);

        inflater.inflate(R.menu.mall, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
//                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                Toasty.normal(activity, "Search").show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onRefresh() {
        showContentView();
        showRotaLoading(true);
        loadData();
    }


    @Override
    public void onStop() {
        super.onStop();
        DebugUtil.debug("MallFragment ---------> onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugUtil.debug("--MallFragment   ----onDestroy");
    }

}
