package com.xjtu.bookreader.bean;



import com.xjtu.bookreader.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingbin on 2016/11/24.
 */

public class ShelfBookBean implements Serializable {

    @ParamNames("error")
    private boolean error;

    @ParamNames("results")
    private List<ShelfBookItemBean> results;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ShelfBookItemBean> getResults() {
        return results;
    }

    public void setResults(List<ShelfBookItemBean> results) {
        this.results = results;
    }
}
