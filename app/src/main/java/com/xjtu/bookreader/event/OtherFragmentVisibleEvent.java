package com.xjtu.bookreader.event;

/**
 * Created by LeonTao on 2018/1/17.
 */

public class OtherFragmentVisibleEvent {

    private boolean isVisible;

    public OtherFragmentVisibleEvent(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}

