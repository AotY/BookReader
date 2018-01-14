package com.xjtu.bookreader.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.bumptech.glide.Glide;
import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.xjtu.bookreader.R;
import com.xjtu.bookreader.adapter.ShelfAdapter;
import com.xjtu.bookreader.base.BaseFragment;
import com.xjtu.bookreader.bean.ShelfBookBean;
import com.xjtu.bookreader.bean.ShelfBookItemBean;
import com.xjtu.bookreader.databinding.FragmentShelfBinding;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.util.CommonUtils;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.Logger;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * 书架Fragment
 */
public class ShelfFragment extends BaseFragment<FragmentShelfBinding> {

    private boolean mIsPrepared;

    private boolean mIsFirst = true;

    // 开始请求的角标
    private int mStart = 0;
    // 一次请求的数量

    private int mCount = 18;

    private MainActivity activity;

    private ShelfAdapter mShelfAdapter;

    private GridLayoutManager mLayoutManager;

    private final BookCollectionShadow myCollection = new BookCollectionShadow();

    public ShelfFragment() {
        // Required empty public constructor
    }

    @Override
    public int setContent() {
        return R.layout.fragment_shelf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtil.debug("ShelfFragment ---------> onCreate");
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DebugUtil.debug("ShelfFragment ---------> onActivityCreated");
        showContentView();

        bindingView.srlShelf.setColorSchemeColors(CommonUtils.getColor(R.color.colorPrimary));

        bindingView.srlShelf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DebugUtil.error("-----onRefresh");
                bindingView.srlShelf.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStart = 0;
                        loadCustomData();
                    }
                }, 1000);

            }
        });


        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        bindingView.xrvShelf.setLayoutManager(mLayoutManager);

        scrollRecycleView();

        // 准备就绪
        mIsPrepared = true;

        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();

    }

    @Override
    public void onStart() {
        super.onStart();
        DebugUtil.debug("ShelfFragment ---------> onStart");
//        showActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugUtil.debug("ShelfFragment ---------> onResume");

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

    @Override
    protected void loadData() {
        DebugUtil.error("-----loadData");
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }

        bindingView.srlShelf.setRefreshing(true);
        bindingView.srlShelf.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCustomData();
            }
        }, 500);
        DebugUtil.error("-----setRefreshing");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        activity.getMenuInflater().inflate(R.menu.shelf, menu);
        inflater.inflate(R.menu.shelf, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
//                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                Toasty.normal(activity, "Search").show();
                return true;

            case R.id.action_edit:
                Toasty.normal(activity, "edit").show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 加载我的书架
     */
    private void loadCustomData() {
        ShelfBookBean shelfBookBean = new ShelfBookBean();
        shelfBookBean.setError(false);
        List<ShelfBookItemBean> shelfBookItemBeanList = new ArrayList<>();

        shelfBookItemBeanList.add(new ShelfBookItemBean("1", "芳华", "https://img3.doubanio.com/lpic/s29418322.jpg"));
        shelfBookItemBeanList.add(new ShelfBookItemBean("2", "步履不停", "http://mebook.cc/wp-content/uploads/2017/06/blob-39.png"));
        shelfBookItemBeanList.add(new ShelfBookItemBean("3", "艺术的故事", "https://img3.doubanio.com/lpic/s3219163.jpg"));
        shelfBookItemBeanList.add(new ShelfBookItemBean("4", "我的前半生", "https://img1.doubanio.com/lpic/s2720819.jpg"));
        shelfBookItemBeanList.add(new ShelfBookItemBean("5", "百年孤独", "https://img3.doubanio.com/lpic/s6384944.jpg"));
        shelfBookItemBeanList.add(new ShelfBookItemBean("6", "活着", "https://img3.doubanio.com/lpic/s27279654.jpg"));
        shelfBookItemBeanList.add(new ShelfBookItemBean("7", "人间失格", "https://img3.doubanio.com/lpic/s6100756.jpg"));
        shelfBookItemBeanList.add(new ShelfBookItemBean("8", "月亮与六便士", "https://img1.doubanio.com/lpic/s2659208.jpg"));
        shelfBookBean.setResults(shelfBookItemBeanList);

        if (mShelfAdapter == null) {
            mShelfAdapter = new ShelfAdapter(activity, myCollection);
        }
        mShelfAdapter.setList(shelfBookBean.getResults());
        mShelfAdapter.notifyDataSetChanged();
        bindingView.xrvShelf.setAdapter(mShelfAdapter);

        showContentView();
//        if (bindingView.srlShelf.isRefreshing()) {
//            bindingView.srlShelf.setRefreshing(false);
//        }
        bindingView.srlShelf.setRefreshing(false);

//        // 加载我的书架数据
//        Subscription get = HttpClient.Builder.getShelfService().getShelf()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ShelfBookBean>() {
//                    @Override
//                    public void onCompleted() {
//                        showContentView();
//                        if (bindingView.srlShelf.isRefreshing()) {
//                            bindingView.srlShelf.setRefreshing(false);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        showContentView();
//                        if (bindingView.srlShelf.isRefreshing()) {
//                            bindingView.srlShelf.setRefreshing(false);
//                        }
//                        if (mStart == 0) {
//                            showError();
//                        }
//                    }
//
//                    @Override
//                    public void onNext(ShelfBookBean shelfBookBean) {
//                        if (mStart == 0) {
//                            if (shelfBookBean != null && shelfBookBean.getResults() != null && shelfBookBean.getResults().size() > 0) {
//
//                                if (mShelfAdapter == null) {
//                                    mShelfAdapter = new ShelfAdapter(activity);
//                                }
//                                mShelfAdapter.setList(shelfBookBean.getResults());
//                                mShelfAdapter.notifyDataSetChanged();
//                                bindingView.xrvShelf.setAdapter(mShelfAdapter);
//                            }
//                            mIsFirst = false;
//                        } else {
//                            mShelfAdapter.addAll(shelfBookBean.getResults());
//                            mShelfAdapter.notifyDataSetChanged();
//                        }
//                        if (mShelfAdapter != null) {
//                            mShelfAdapter.updateLoadStatus(ShelfAdapter.LOAD_PULL_TO);
//                        }
//                    }
//                });
//        addSubscription(get);
    }


    /**
     * 滚动页面
     */
    public void scrollRecycleView() {
        bindingView.xrvShelf.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                    /**StaggeredGridLayoutManager*/

                    if (mShelfAdapter == null) {
                        return;
                    }
                    if (mLayoutManager.getItemCount() == 0) {
                        mShelfAdapter.updateLoadStatus(ShelfAdapter.LOAD_NONE);
                        return;

                    }
                    if (lastVisibleItem + 1 == mLayoutManager.getItemCount()
                            && mShelfAdapter.getLoadStatus() != ShelfAdapter.LOAD_MORE) {
                        mShelfAdapter.updateLoadStatus(ShelfAdapter.LOAD_MORE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mStart += mCount;
                                loadCustomData();
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                /**StaggeredGridLayoutManager*/
            }
        });
    }

    @Override
    protected void onRefresh() {
        bindingView.srlShelf.setRefreshing(true);
        loadCustomData();
    }

    @Override
    public void onPause() {
        super.onPause();
        DebugUtil.debug("ShelfFragment ---------> onPause");
    }


    @Override
    public void onStop() {
        super.onStop();
        DebugUtil.debug("ShelfFragment ---------> onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myCollection.unbind();
        DebugUtil.debug("ShelfFragment ----onDestroy");
    }

}
