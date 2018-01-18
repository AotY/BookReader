package com.xjtu.bookreader.view.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xjtu.bookreader.util.DebugUtil;

/**
 * Created by LeonTao on 2018/1/18.
 */

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    private int spanCount; // 一行显示item个数

    public SimpleDividerItemDecoration(Context context, int drawableId, int spanCount) {
        this.mDivider = context.getResources().getDrawable(drawableId);
        DebugUtil.debug("mDivider ---------------------> IntrinsicHeight:  " + mDivider.getIntrinsicHeight());
        this.spanCount = spanCount;
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.save();
        //左右
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();

        final int left = 0;
        final int right = parent.getWidth();

        DebugUtil.debug("onDrawOver left, right ---------------- : " + left + ", " + right);
        int childCount = parent.getChildCount();


        /**
         * 因为ShelfFragment中的recycleview有一个header，所以都绘制啦，需要自己判断
         */
        if (childCount == 0)
            return;

        childCount = childCount - 1; // headerview不绘制
        DebugUtil.debug("onDrawOver childCount ----------------> " + childCount);

        int lineNum = childCount / spanCount + (childCount % spanCount >= 1 ? 1 : 0);
        DebugUtil.debug("onDrawOver lineNum ----------------> " + lineNum);

        for (int i = 0; i < lineNum; i++) {
            // 绘制每一行
            final int curIndex = spanCount * i + 1;

            // 这里一行只需要绘制一次
            final View child = parent.getChildAt(curIndex);

            drawDivider(left, right, child, c);
        }
//        // 强制绘制最后一行 ？
//        final View child = parent.getChildAt(parent.getChildCount() - 1);
//        drawDivider(left, right, child, c);
        c.restore();
    }


    private void drawDivider(int left, int right, View child, Canvas c) {

        if (child == null)
            return;

        // 强制绘制最后一行 ？
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int startMargin = params.getMarginStart();
        int endMargin = params.getMarginEnd();
        int topMargin = params.topMargin;
        int bottomMargin = params.bottomMargin;

        left = left - startMargin;
        right = right + endMargin;
        // 上下, 不需要绘制矩形
//        int top = child.getBottom() + params.bottomMargin;
//        int bottom = top; // + mDivider.getIntrinsicHeight(); //intrinsic: 固有的

//        final int bottom = child.getBottom() + child.getPaddingBottom() + 6;
        final int bottom = child.getBottom() + child.getPaddingBottom() - 7;
        final int top = bottom - 5;

        DebugUtil.debug("drawDivider top, bottom ---------------- : " + top + ", " + bottom);

        // 设置上下左右返回吗？
        mDivider.setBounds(new Rect(left, top, right, bottom));
//        mDivider.setBounds(left, top, right, bottom);
        mDivider.setAlpha(200);
        mDivider.draw(c);
        // 不行可以绘制两次的
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        DebugUtil.debug("getItemOffsets -----------> mDivider.getIntrinsicHeight(): " + mDivider.getIntrinsicHeight());

        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }

}
