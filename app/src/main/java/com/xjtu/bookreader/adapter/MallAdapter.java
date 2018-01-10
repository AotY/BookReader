package com.xjtu.bookreader.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xjtu.bookreader.R;
import com.xjtu.bookreader.base.baseadapter.BaseRecyclerViewAdapter;
import com.xjtu.bookreader.base.baseadapter.BaseRecyclerViewHolder;
import com.xjtu.bookreader.bean.MallRecommendItemBean;
import com.xjtu.bookreader.databinding.MallItemThreeBinding;
import com.xjtu.bookreader.databinding.MallItemTitleBinding;
import com.xjtu.bookreader.http.rx.RxBus;
import com.xjtu.bookreader.http.rx.RxCodeConstants;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.ui.BookDetailActivity;
import com.xjtu.bookreader.util.CommonUtils;
import com.xjtu.bookreader.util.ImgLoadUtil;
import com.xjtu.bookreader.util.PerfectClickListener;

import java.util.List;

import static com.xjtu.bookreader.util.StringResourceUtil.getStringById;

/**
 * Created by jingbin on 2016/12/27.
 */

public class MallAdapter extends BaseRecyclerViewAdapter<List<MallRecommendItemBean>> {

    private static final int TYPE_TITLE = 0; // title
    private static final int TYPE_THREE = 3;// 三张图

    private MainActivity context;


    public MallAdapter(Context context) {
        this.context = (MainActivity) context;
    }


    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(getData().get(position).get(0).getType_title())) {
            return TYPE_TITLE;
        } else if (getData().get(position).size() == 3) {
            return TYPE_THREE;
        }
        return super.getItemViewType(position);
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return new TitleHolder(parent, R.layout.mall_item_title);
            default:
                return new ThreeHolder(parent, R.layout.mall_item_three);
        }
    }


    /**
     * 显示标题，比如文化、历史、政治等
     */
    private class TitleHolder extends BaseRecyclerViewHolder<List<MallRecommendItemBean>, MallItemTitleBinding> {

        TitleHolder(ViewGroup parent, int title) {
            super(parent, title);
        }

        @Override
        public void onBindViewHolder(List<MallRecommendItemBean> object, final int position) {
            int index = 0;
            String title = object.get(0).getType_title();
            binding.tvTitleType.setText(title);
            // 图标
            if (getStringById(R.string.culture_book).equals(title)) {  // 文化
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_android));
                index = 0;
            } else if (getStringById(R.string.politics_book).equals(title)) { // 政治
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_meizi));
                index = 1;
            } else if (getStringById(R.string.history_book).equals(title)) { // 历史
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_ios));
                index = 2;
            } else if (getStringById(R.string.economics_book).equals(title)) { // 经济
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_android));
                index = 2;
            }

            if (position != 0) {
                binding.viewLine.setVisibility(View.VISIBLE);
            } else {
                binding.viewLine.setVisibility(View.GONE);
            }

            final int finalIndex = index;
            binding.llTitleMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.getDefault().post(RxCodeConstants.JUMP_TYPE, finalIndex);
                }
            });
        }
    }

    /**
     * 显示内容，一行显示三张图片，
     * 后期添加可以左滑显示更多功能
     */
    private class ThreeHolder extends BaseRecyclerViewHolder<List<MallRecommendItemBean>, MallItemThreeBinding> {

        ThreeHolder(ViewGroup parent, int title) {
            super(parent, title);
        }

        @Override
        public void onBindViewHolder(List<MallRecommendItemBean> object, int position) {
            // 图片
            displayRandomImg(3, 0, binding.ivThreeOneOne, object);
            displayRandomImg(3, 1, binding.ivThreeOneTwo, object);
            displayRandomImg(3, 2, binding.ivThreeOneThree, object);

            // 点击事件
            setOnClick(binding.llThreeOneOne, binding.ivThreeOneOne, object.get(0));
            setOnClick(binding.llThreeOneTwo, binding.ivThreeOneTwo, object.get(1));
            setOnClick(binding.llThreeOneThree, binding.ivThreeOneThree, object.get(2));

            // 描述
            setDes(object, 0, binding.tvThreeOneOneTitle);
            setDes(object, 1, binding.tvThreeOneTwoTitle);
            setDes(object, 2, binding.tvThreeOneThreeTitle);
        }
    }

    private void setDes(List<MallRecommendItemBean> object, int position, TextView textView) {
        textView.setText(object.get(position).getTitle());
    }

    private void displayRandomImg(int imgNumber, int position, ImageView imageView, List<MallRecommendItemBean> object) {
//        DebugUtil.error("-----Image_url: "+object.get(position).getImage_url());
        ImgLoadUtil.displayRandom(imgNumber, object.get(position).getImage(), imageView);
    }




    /**
     * 这里要跳转到BookDetailActivity页面
     * 这里不知道有没有那个问题，因为你要实现图片的移动，需要传入一个imageView
     *
     * @param linearLayout
     * @param bean
     */
    private void setOnClick(final LinearLayout linearLayout, final ImageView imageView, final MallRecommendItemBean bean) {
        linearLayout.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                // 跳转页面，传入ID就好
                BookDetailActivity.start(context, bean.getId(), imageView);
            }
        });

        // 后续可以类似实现3D touch功能
    }
}

