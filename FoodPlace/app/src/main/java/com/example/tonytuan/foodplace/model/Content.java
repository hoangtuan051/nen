package com.example.tonytuan.foodplace.model;

import java.io.Serializable;

/**
 * Created by Tony Tuan on 01/10/2018.
 */

public class Content implements Serializable {
    private String content;
    private Gallery gallery;

    public Content(String content, Gallery gallery) {
        this.content = content;
        this.gallery = gallery;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
