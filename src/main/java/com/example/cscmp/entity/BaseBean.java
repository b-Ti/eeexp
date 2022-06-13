package com.example.cscmp.entity;

import lombok.Data;




@Data
public class BaseBean {

    private Integer queryId;

    private Integer queryK;

    private String queryMPath;

    private String sc;

    public Integer getQueryId() {
        return queryId;
    }

    public void setQueryId(Integer queryId) {
        this.queryId = queryId;
    }

    public Integer getQueryK() {
        return queryK;
    }

    public void setQueryK(Integer queryK) {
        this.queryK = queryK;
    }

    public String getQueryMPath() {
        return queryMPath;
    }

    public void setQueryMPath(String queryMPath) {
        this.queryMPath = queryMPath;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }
}

