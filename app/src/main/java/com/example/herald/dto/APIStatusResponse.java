package com.example.herald.dto;

public class APIStatusResponse {
    private StatusPage page;
    private APIStatus status;

    public StatusPage getPage() {
        return page;
    }

    public void setPage(StatusPage page) {
        this.page = page;
    }

    public APIStatus getStatus() {
        return status;
    }

    public void setStatus(APIStatus status) {
        this.status = status;
    }
}
