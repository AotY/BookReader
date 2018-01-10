package com.xjtu.bookreader.bean;


import android.databinding.Bindable;

import com.xjtu.bookreader.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingbin on 2016/11/24.
 */

public class MallRecommendBean implements Serializable {

    @ParamNames("error")
    private boolean error;

    @ParamNames("results")
    private ResultsBean results;

    @ParamNames("category")
    private List<String> category;

    public static class ResultsBean {

        @ParamNames("文化")
        private List<MallRecommendItemBean> cultureBooks;

        @ParamNames("政治")
        private List<MallRecommendItemBean> politicsBooks;

        @ParamNames("历史")
        private List<MallRecommendItemBean> historyBooks;

        @ParamNames("经济")
        private List<MallRecommendItemBean> economicsBooks;


//        @Bindable
        public List<MallRecommendItemBean> getCultureBooks() {
            return cultureBooks;
        }

//        @Bindable
        public List<MallRecommendItemBean> getPoliticsBooks() {
            return politicsBooks;
        }

//        @Bindable
        public List<MallRecommendItemBean> getHistoryBooks() {
            return historyBooks;
        }

//        @Bindable
        public List<MallRecommendItemBean> getEconomicsBooks() {
            return economicsBooks;
        }


    }

    public boolean isError() {
        return error;
    }

    public ResultsBean getResults() {
        return results;
    }

    public List<String> getCategory() {
        return category;
    }
}

