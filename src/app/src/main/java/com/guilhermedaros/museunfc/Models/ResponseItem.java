package com.guilhermedaros.museunfc.Models;

import com.guilhermedaros.museunfc.Entities.ItemMuseu;

import java.util.List;

public class ResponseItem extends Response {

    private List<ItemMuseu> data = null;

    public List<ItemMuseu> getData() {
        return data;
    }

    public void setData(List<ItemMuseu> data) {
        this.data = data;
    }
}
