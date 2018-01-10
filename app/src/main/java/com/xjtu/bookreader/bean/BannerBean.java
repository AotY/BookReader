package com.xjtu.bookreader.bean;

import android.databinding.Bindable;

import com.xjtu.bookreader.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingbin on 2016/11/29.
 * 轮播图bean
 */

public class BannerBean implements Serializable {

    @ParamNames("error")
    private boolean error;

    @ParamNames("results")
    private List<BannerItemBean> results;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<BannerItemBean> getResults() {
        return results;
    }

    public void setResults(List<BannerItemBean> results) {
        this.results = results;
    }
}
