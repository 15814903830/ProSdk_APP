package com.zero.ci.widget.rxbus;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description : 标签信息
 * -------------------------------
 * 1. 用于区别不同的标签信息
 */

public class TagMessage {
    Object mEvent;
    String mTag;

    public TagMessage(Object event, String tag) {
        mEvent = event;
        mTag = tag;
    }

    @Override
    public boolean equals(Object event) {
        if (event != null && event instanceof TagMessage) {
            TagMessage tagMessage = (TagMessage) event;
            return RxUtil.equals(tagMessage.mEvent.getClass(), mEvent.getClass()) && RxUtil.equals(tagMessage.mTag, mTag);
        }
        return false;
    }

    boolean isSameType(final Class eventType, final String tag) {
        return RxUtil.equals(mEvent.getClass(), eventType)
                && RxUtil.equals(this.mTag, tag);
    }

    @Override
    public String toString() {
        return "TagMessage{" +
                "mEvent=" + mEvent +
                ", mTag='" + mTag + '\'' +
                '}';
    }
}
