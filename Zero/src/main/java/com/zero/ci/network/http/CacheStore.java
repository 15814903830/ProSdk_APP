package com.zero.ci.network.http;

/**
 * CacheStore interface.
 */
public interface CacheStore<T> {

    /**
     * According to the key to getList the cache data.
     *
     * @param key unique key.
     * @return cache data.
     */
    T get(String key);

    /**
     * According to the key to replace or save the data.
     *
     * @param key  unique key.
     * @param data cache data.
     * @return cache data.
     */
    T replace(String key, T data);

    /**
     * According to the key to remove the data.
     *
     * @param key unique.
     * @return cache data.
     */
    boolean remove(String key);

    /**
     * Clear all data.
     *
     * @return myreturn to true to clear the failure when the false is cleared.
     */
    boolean clear();
}
