package com.koolearn.android.kooreader.animation;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;

public class FlipInTopXAnimator extends BaseItemAnimator {

    public FlipInTopXAnimator() {
    }

    public FlipInTopXAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .rotationX(90)
                .setDuration(getRemoveDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultRemoveVpaListener(holder))
                .setStartDelay(getRemoveDelay(holder))
                .start();
    }

    @Override protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        ViewCompat.setRotationX(holder.itemView, 90);
    }

    @Override protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .rotationX(0)
                .setDuration(getAddDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultAddVpaListener(holder))
                .setStartDelay(getAddDelay(holder))
                .start();
    }
}
