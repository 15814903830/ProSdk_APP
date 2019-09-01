package com.zero.ci.widget.rxbus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 缓存事件
 * -------------------------------
 */

public class CacheUtil {
    private static class CacheHolder {
        private static final CacheUtil CACHE_UTIL = new CacheUtil();
    }

    private final Map<Class, List<TagMessage>> stickEventMaps = new ConcurrentHashMap<>();
    private final Map<Object, List<Disposable>> disposableMaps = new ConcurrentHashMap<>();

    public CacheUtil() {
    }

    public static CacheUtil getInstance() {
        return CacheHolder.CACHE_UTIL;
    }

    /**
     * 添加事件
     *
     * @param stickyEvent 事件
     */
    void addStickyEvent(final TagMessage stickyEvent) {
        Class<?> eventType = stickyEvent.mEvent.getClass();
        synchronized (stickEventMaps) {
            List<TagMessage> stickyEvents = stickEventMaps.get(eventType);
            if (stickyEvents == null) {
                stickyEvents = new ArrayList<>();
                stickyEvents.add(stickyEvent);
                stickEventMaps.put(eventType, stickyEvents);
            } else {
                int indexOf = stickyEvents.indexOf(stickyEvent);
                if (indexOf == -1) {
                    //事件不存在则插入
                    stickyEvents.add(stickyEvent);
                } else {
                    //事件若存在则覆盖
                    stickyEvents.set(indexOf, stickyEvent);
                }
            }
        }

    }

    /**
     * 查找事件
     *
     * @param eventType 事件类型
     * @param tag       事件标签
     * @return
     */
    TagMessage findStickyEvent(final Class eventType, final String tag) {
        synchronized (stickEventMaps) {
            List<TagMessage> stickyEvents = stickEventMaps.get(eventType);
            if (stickyEvents == null) return null;
            int size = stickyEvents.size();
            TagMessage res = null;
            for (int i = size - 1; i >= 0; i--) {
                TagMessage stickyEvent = stickyEvents.get(i);
                if (stickyEvent.isSameType(eventType, tag)) {
                    res = stickyEvents.get(i);
                    break;
                }
            }
            return res;
        }
    }

    void addDisposable(Object subscriber, Disposable disposable) {
        synchronized (disposableMaps) {
            List<Disposable> disposables = disposableMaps.get(subscriber);
            if (disposables == null) {
                disposables = new ArrayList<>();
                disposables.add(disposable);
                disposableMaps.put(subscriber, disposables);
            } else {
                disposables.add(disposable);
            }
        }
    }

    void removeDisposables(final Object subscriber) {
        synchronized(disposableMaps) {
            List<Disposable> disposables = disposableMaps.get(subscriber);
            if (disposables == null) return;
            for (Disposable disposable : disposables) {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
            disposables.clear();
            disposableMaps.remove(subscriber);
        }
    }
}
