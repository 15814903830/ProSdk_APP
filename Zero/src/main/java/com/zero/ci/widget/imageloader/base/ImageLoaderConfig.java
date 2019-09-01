package com.zero.ci.widget.imageloader.base;

import java.util.HashMap;


public class ImageLoaderConfig {
    private HashMap<LoaderEnum, IImageLoaderStrategy> imageloaderMap;
    private long maxMemory = 0;

    private ImageLoaderConfig(Builder builder) {
        imageloaderMap = builder.imageloaderMap;
        maxMemory = builder.maxMemory;
    }

    public long getMaxMemory() {
        return maxMemory <= 0 ? 40 * 1024 * 1024 : maxMemory;
    }

    public HashMap<LoaderEnum, IImageLoaderStrategy> getImageloaderMap() {
        return imageloaderMap;
    }

    public static class Builder {
        private HashMap<LoaderEnum, IImageLoaderStrategy> imageloaderMap = new HashMap<>();
        private long maxMemory = 40 * 1024 * 1024;

        public Builder(LoaderEnum emun, IImageLoaderStrategy loaderstrategy) {
            imageloaderMap.put(emun, loaderstrategy);
        }

        public Builder addImageLodaer(LoaderEnum emun, IImageLoaderStrategy loaderstrategy) {
            imageloaderMap.put(emun, loaderstrategy);
            return this;
        }

        /**
         * @param maxMemory 单位为 Byte
         * @return
         */
        public Builder maxMemory(Long maxMemory) {
            this.maxMemory = maxMemory;
            return this;
        }

        public ImageLoaderConfig build() {
            return new ImageLoaderConfig(this);
        }
    }
}
