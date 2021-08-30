package ru.moreapart.manager.dto;

import java.util.List;

public class PageDto {
    private List<?> list;
    private int page;
    private int pages;
    private long total;

    public PageDto(List<?> list, int page, int pages, long total) {
        this.list = list;
        this.page = page;
        this.pages = pages;
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}