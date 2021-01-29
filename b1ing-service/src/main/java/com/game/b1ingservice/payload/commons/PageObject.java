package com.game.b1ingservice.payload.commons;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageObject<T> implements Serializable {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PageObject() {

    }

    public PageObject(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {

//        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }


}