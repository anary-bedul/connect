package com.fxratecentral.connect.okx;

import java.util.ArrayList;
import java.util.List;

public class OkxResponseClass {
    private String code;
    private String msg;
    private List<Object[]> data = new ArrayList<Object[]>();

    // Getter Methods

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<Object[]> getData() {
        return data;
    }

    // Setter Methods

    public void setCode(final String code) {
        this.code = code;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public void setData(final List<Object[]> data) {
        this.data = data;
    }
}
