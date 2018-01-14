package com.xjtu.bookreader.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xjtu.bookreader.R;
import com.xjtu.bookreader.base.baseadapter.BaseRecyclerViewAdapter;
import com.xjtu.bookreader.base.baseadapter.BaseRecyclerViewHolder;
import com.xjtu.bookreader.bean.MallRecommendItemBean;
import com.xjtu.bookreader.databinding.ItemCategoryRankingBinding;
import com.xjtu.bookreader.ui.BookDetailActivity;
import com.xjtu.bookreader.util.PerfectClickListener;

/**
 * Created by jingbin on 2016/12/10.
 */

public class CategoryRankingAdapter extends BaseRecyclerViewAdapter<MallRecommendItemBean> {


    private Activity activity;

    public CategoryRankingAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_category_ranking);
    }

    class ViewHolder extends BaseRecyclerViewHolder<MallRecommendItemBean, ItemCategoryRankingBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final MallRecommendItemBean bean, final int position) {
            binding.setBean(bean);
            /**
             * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
             */
            binding.executePendingBindings();
            binding.llItemTop.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    BookDetailActivity.start(activity, bean.getId(), binding.ivTopPhoto);
                }
            });
        }
    }
}
