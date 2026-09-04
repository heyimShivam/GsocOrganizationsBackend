package com.organization.gsoc.DTO;

public record GsocProjectFilterRequest(
        String title,
        Integer year
) {

    public GsocProjectFilterRequest {
        if (title == null) {
            title = "";
        }
    }

    public GsocProjectFilterRequest() {
        this("", null);
    }
}