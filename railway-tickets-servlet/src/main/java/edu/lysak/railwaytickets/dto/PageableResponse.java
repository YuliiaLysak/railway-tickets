package edu.lysak.railwaytickets.dto;

import java.util.List;

public class PageableResponse<T> {
    private int currentPage;
    private int totalPages;
    private List<T> content;

    public PageableResponse() {
    }

    public PageableResponse(int currentPage, int totalPages, List<T> content) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.content = content;
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

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
