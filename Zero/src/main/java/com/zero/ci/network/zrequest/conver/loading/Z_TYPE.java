package com.zero.ci.network.zrequest.conver.loading;


import com.zero.ci.network.zrequest.conver.loading.star.LeafBuilder;
import com.zero.ci.network.zrequest.conver.loading.star.StarBuilder;
import com.zero.ci.network.zrequest.conver.loading.text.TextBuilder;


public enum Z_TYPE {
    TEXT(TextBuilder.class),
    STAR_LOADING(StarBuilder.class),
    LEAF_ROTATE(LeafBuilder.class);

    private final Class<?> mBuilderClass;

    Z_TYPE(Class<?> builderClass) {
        this.mBuilderClass = builderClass;
    }

    <T extends ZLoadingBuilder> T newInstance() {
        try {
            return (T) mBuilderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
