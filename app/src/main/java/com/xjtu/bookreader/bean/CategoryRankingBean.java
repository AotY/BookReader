package com.xjtu.bookreader.bean;


import com.xjtu.bookreader.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingbin on 2016/11/24.
 */

public class CategoryRankingBean implements Serializable {

    @ParamNames("error")
    private boolean error;

    @ParamNames("results")
    private List<MallRecommendItemBean> results;

    public CategoryRankingBean(boolean error, List<MallRecommendItemBean> results) {
        this.error = error;
        this.results = results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<MallRecommendItemBean> getResults() {
        return results;
    }

    public void setResults(List<MallRecommendItemBean> results) {
        this.results = results;
    }
}

