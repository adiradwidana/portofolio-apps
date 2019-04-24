package com.base.app.domain.ui.slideshow;

public class SlideItem {
    private String urlImage;
    private String urlContent;

    public String getUrlImage() {
        return urlImage;
    }

    public SlideItem setUrlImage(String urlImage) {
        this.urlImage = urlImage;
        return this;
    }

    public String getUrlContent() {
        return urlContent;
    }

    public SlideItem setUrlContent(String urlContent) {
        this.urlContent = urlContent;
        return this;
    }
}
