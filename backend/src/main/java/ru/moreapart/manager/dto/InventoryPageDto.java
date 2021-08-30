package ru.moreapart.manager.dto;

import ru.moreapart.manager.entity.Inventory;

import java.util.List;

public class InventoryPageDto {
    private List<Inventory> inventories;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public InventoryPageDto(List<Inventory> inventories, int currentPage, int totalPages, long totalItems) {
        this.inventories = inventories;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
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