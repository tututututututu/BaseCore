package com.hzecool.core.activity.imagepager;

/**
 * Created by tutu on 16/5/24.
 */
public class ImageSize {
    private static final int TO_STRING_MAX_LENGHT = 9;
    private static final String SEPARATOR = "x";
    private final int width;
    private final int height;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ImageSize(int width, int height, int rotation) {
        if (rotation % 180 == 0) {
            this.width = width;
            this.height = height;
        } else {
            this.width = height;
            this.height = width;
        }

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ImageSize scaleDown(int sampleSize) {
        return new ImageSize(this.width / sampleSize, this.height / sampleSize);
    }

    public ImageSize scale(float scale) {
        return new ImageSize((int) ((float) this.width * scale), (int) ((float) this.height * scale));
    }

    public String toString() {
        return (new StringBuilder(9)).append(this.width).append("x").append(this.height).toString();
    }
}

