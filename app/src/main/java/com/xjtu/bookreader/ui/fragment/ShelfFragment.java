package com.xjtu.bookreader.ui.fragment;

import android.content.Context;
import android.content.Intent;
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


import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.xjtu.bookreader.R;
import com.xjtu.bookreader.adapter.ShelfAdapter;
import com.xjtu.bookreader.base.BaseFragment;
import com.xjtu.bookreader.bean.model.BookOfShelf;
import com.xjtu.bookreader.databinding.FragmentShelfBinding;
import com.xjtu.bookreader.event.BackFromKooReaderEvent;
import com.xjtu.bookreader.event.SwitchFragmentEvent;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.util.CommonUtil;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.PerfectClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * 书架Fragment
 */
public class ShelfFragment extends BaseFragment<FragmentShelfBinding> {

    private boolean mIsPrepared;

    private boolean mIsFirst = true;

    private MainActivity activity;

    private ShelfAdapter mShelfAdapter;

    private GridLayoutManager mLayoutManager;

    private List<BookOfShelf> bookOfShelfList;

    private final BookCollectionShadow myCollection = new BookCollectionShadow();

    private Menu mMenu ;
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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DebugUtil.debug("ShelfFragment ---------> onActivityCreated");

        showContentView();

        initView();

        // 准备就绪
        mIsPrepared = true;
        /**
         * 因为启动时先走loadData() 再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();

    }

    //
    private void initView() {
        bindingView.srlShelf.setColorSchemeColors(CommonUtil.getColor(R.color.colorPrimary));

        // 刷新页面
        bindingView.srlShelf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DebugUtil.error("-----onRefresh");
                bindingView.srlShelf.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCustomData();
                    }
                }, 1000);

            }
        });

        // 设置LayoutManger
        mLayoutManager = new GridLayoutManager(activity, 3);
        bindingView.xrvShelf.setLayoutManager(mLayoutManager);
        scrollRecycleView();


        // 切换到MallFragment
        bindingView.btnShelfEmpty.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DebugUtil.debug("rlShelfEmptyContainer ----------------> OnClick()");
                // 通知MainActivity切换到MallFragment
                EventBus.getDefault().post(new SwitchFragmentEvent(SwitchFragmentEvent.BOOK_MALL));
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        DebugUtil.debug("ShelfFragment ---------> onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugUtil.debug("ShelfFragment ---------> onResume");

    }

    // 当用户从阅读页返回时，重新加载数据（重新排序排序）
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackFromKooReaderEvent(BackFromKooReaderEvent event) {
        loadData();
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
        mMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Toasty.normal(activity, "edit").show();
                mMenu.getItem(0).setVisible(false);
                mMenu.getItem(1).setVisible(true);
                return true;

            case R.id.action_delete:
                Toasty.normal(activity, "delete").show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 加载我的书架
     */
    private void loadCustomData() {
//        ShelfBookBean shelfBookBean = new ShelfBookBean();
//        shelfBookBean.setError(false);
//        List<BookOfShelf> bookOfShelfList = new ArrayList<>();

//        bookOfShelfList.add(new BookOfShelf("1", "芳华", "https://img3.doubanio.com/lpic/s29418322.jpg"));
//        bookOfShelfList.add(new BookOfShelf("2", "步履不停", "http://mebook.cc/wp-content/uploads/2017/06/blob-39.png"));
//        bookOfShelfList.add(new BookOfShelf("3", "艺术的故事", "https://img3.doubanio.com/lpic/s3219163.jpg"));
//        bookOfShelfList.add(new BookOfShelf("4", "我的前半生", "https://img1.doubanio.com/lpic/s2720819.jpg"));
//        bookOfShelfList.add(new BookOfShelf("5", "百年孤独", "https://img3.doubanio.com/lpic/s6384944.jpg"));
//        bookOfShelfList.add(new BookOfShelf("6", "活着", "https://img3.doubanio.com/lpic/s27279654.jpg"));
//        bookOfShelfList.add(new BookOfShelf("7", "人间失格", "https://img3.doubanio.com/lpic/s6100756.jpg"));
//        bookOfShelfList.add(new BookOfShelf("8", "月亮与六便士", "https://img1.doubanio.com/lpic/s2659208.jpg"));
//        shelfBookBean.setResults(bookOfShelfList);

        // 这里按照上次阅读时间排序(降序）
//        bookOfShelfList = DataSupport.findAll(BookOfShelf.class);
        bookOfShelfList = DataSupport.where().order("lastTime desc").find(BookOfShelf.class);

        if (mShelfAdapter == null) {
            mShelfAdapter = new ShelfAdapter(activity, myCollection);
        }

        mShelfAdapter.setList(bookOfShelfList);
        mShelfAdapter.notifyDataSetChanged();
        bindingView.xrvShelf.setAdapter(mShelfAdapter);

        showContentView();
//        if (bindingView.srlShelf.isRefreshing()) {
//            bindingView.srlShelf.setRefreshing(false);
//        }
        bindingView.srlShelf.setRefreshing(false);

        // 如果没有数据，则设置空白页面
       if (bookOfShelfList.size() == 0) {
            bindingView.rlShelfEmptyContainer.setVisibility(View.VISIBLE);
        } else {
            bindingView.rlShelfEmptyContainer.setVisibility(View.GONE);
        }

//        mIsFirst = false;

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
        DebugUtil.debug("ShelfFragment ---------> onPause");
        super.onPause();
    }


    @Override
    public void onStop() {
        DebugUtil.debug("ShelfFragment ---------> onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        myCollection.unbind();
        EventBus.getDefault().unregister(this);
        DebugUtil.debug("ShelfFragment ----onDestroy");

        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DebugUtil.debug("ShelfFragment ---------------> onActivityResult");
    }
}
