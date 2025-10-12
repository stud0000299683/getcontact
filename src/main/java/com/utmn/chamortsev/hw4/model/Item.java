package com.utmn.chamortsev.hw4.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private String key;
    private String data;
    private long timestamp;

    public Item(String key, String data) {
        this.key = key;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Item{key='" + key + "', data='" + data + "'}";
    }
}
