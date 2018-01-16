package com.xjtu.bookreader.event;

/**
 * Created by LeonTao on 2018/1/16.
 */

public class SwitchFragmentEvent {

    public static final int SHELF = 0;
    public static final int BOOK_MALL = 1;
    public static final int USER_CENTER = 2;


    private int switchIndex;

    public SwitchFragmentEvent(int switchIndex) {
        this.switchIndex = switchIndex;
    }

    public int getSwitchIndex() {
        return switchIndex;
    }

    public void setSwitchIndex(int switchIndex) {
        this.switchIndex = switchIndex;
    }
}
