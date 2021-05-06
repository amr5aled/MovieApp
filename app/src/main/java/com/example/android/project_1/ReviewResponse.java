package com.example.android.project_1;

import java.util.List;

/**
 * Created by amr5aled on 23/03/18.
 */

public class ReviewResponse {

    private int id,page,total_pages,total_results;
    private List<Review> results;

    public ReviewResponse(int id, int page, int total_pages, int total_results, List<Review> results) {
        this.id = id;
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
