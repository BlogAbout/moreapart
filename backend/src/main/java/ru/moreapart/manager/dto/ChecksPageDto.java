package ru.moreapart.manager.dto;

import ru.moreapart.manager.entity.Checks;

import java.util.List;

public class ChecksPageDto {
    private List<Checks> checks;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public ChecksPageDto(List<Checks> checks, int currentPage, int totalPages, long totalItems) {
        this.checks = checks;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<Checks> getChecks() {
        return checks;
    }

    public void setChecks(List<Checks> checks) {
        this.checks = checks;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}