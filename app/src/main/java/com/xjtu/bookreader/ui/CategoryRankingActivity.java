package com.xjtu.bookreader.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.xjtu.bookreader.R;
import com.xjtu.bookreader.adapter.CategoryRankingAdapter;
import com.xjtu.bookreader.base.BaseActivity;
import com.xjtu.bookreader.bean.CategoryRankingBean;
import com.xjtu.bookreader.databinding.ActivityCategoryRankingBinding;
import com.xjtu.bookreader.http.HttpClient;
import com.xjtu.bookreader.view.xrecyclerview.XRecyclerView;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 展示各种类别的排名榜书籍
 */
public class CategoryRankingActivity extends BaseActivity<ActivityCategoryRankingBinding> {


    public static final String TITLE = "title";
    public static final String TYPE = "type";

    private CategoryRankingAdapter categoryRankingAdapter;


    private int mStart = 0;
    private int mCount = 21;

    // 类型
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_ranking);

        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        // 类型，是获取类别书籍数据，还是排行榜数据
        type = intent.getStringExtra(TYPE);

        // 设置类型或者是排行榜
        setTitle(title);
        categoryRankingAdapter = new CategoryRankingAdapter(CategoryRankingActivity.this);
        loadData();

        bindingView.xrvTop.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // 可以不做操作
            }

            @Override
            public void onLoadMore() {
                mStart += mCount;
                loadData();
            }
        });
    }


    /**
     * 加载数据
     */
    private void loadData() {
        Subscription get = HttpClient.Builder.getBookListService().getBookList(type, mStart, mCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryRankingBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        bindingView.xrvTop.refreshComplete();
                        if (categoryRankingAdapter.getItemCount() == 0) {
                            showError();
                        }
                    }

                    @Override
                    public void onNext(CategoryRankingBean categoryRankingBean) {
                        if (mStart == 0) {
                            if (categoryRankingBean != null && categoryRankingBean.getResults() != null && categoryRankingBean.getResults().size() > 0) {

                                categoryRankingAdapter.clear();
                                categoryRankingAdapter.addAll(categoryRankingBean.getResults());
                                //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
                                bindingView.xrvTop.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                                bindingView.xrvTop.setAdapter(categoryRankingAdapter);
                                bindingView.xrvTop.setPullRefreshEnabled(false);
                                bindingView.xrvTop.clearHeader();
                                bindingView.xrvTop.setLoadingMoreEnabled(true);
                                categoryRankingAdapter.notifyDataSetChanged();
                            } else {
                                bindingView.xrvTop.setVisibility(View.GONE);
                            }
                        } else {
                            if (categoryRankingBean != null && categoryRankingBean.getResults() != null && categoryRankingBean.getResults().size() > 0) {
                                bindingView.xrvTop.refreshComplete();
                                categoryRankingAdapter.addAll(categoryRankingBean.getResults());
                                categoryRankingAdapter.notifyDataSetChanged();
                            } else {
                                bindingView.xrvTop.noMoreLoading();
                            }
                        }

                    }
                });
        addSubscription(get);
    }

    @Override
    protected void onRefresh() {
        loadData();
    }

    public static void start(Context mContext, String title, String type) {
        Intent intent = new Intent(mContext, CategoryRankingActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(TYPE, type);
        mContext.startActivity(intent);
    }

}
