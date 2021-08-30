package ru.moreapart.manager.dto;

import ru.moreapart.manager.entity.ChecksAnswers;

import java.util.List;

public class ChecksAnswersPageDto {
    private List<ChecksAnswers> checksAnswers;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public ChecksAnswersPageDto(List<ChecksAnswers> checksAnswers, int currentPage, int totalPages, long totalItems) {
        this.checksAnswers = checksAnswers;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<ChecksAnswers> getChecksAnswers() {
        return checksAnswers;
    }

    public void setChecksAnswers(List<ChecksAnswers> checksAnswers) {
        this.checksAnswers = checksAnswers;
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