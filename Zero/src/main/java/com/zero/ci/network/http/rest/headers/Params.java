package com.zero.ci.network.http.rest.headers;

import android.text.TextUtils;

import com.zero.ci.network.http.BasicMultiValueMap;

import java.util.LinkedHashMap;
import java.util.List;

public class Params extends BasicMultiValueMap<String, Object> {

    public Params() {
        super(new LinkedHashMap<String, List<Object>>() {
            @Override
            public List<Object> put(String key, List<Object> value) {
                return super.put(formatKey(key), value);
            }

            @Override
            public List<Object> get(Object key) {
                if (key != null) {
                    key = formatKey(key.toString());
                }
                return super.get(key);
            }

            @Override
            public List<Object> remove(Object key) {
                if (key != null) {
                    key = formatKey(key.toString());
                }
                return super.remove(key);
            }

            @Override
            public boolean containsKey(Object key) {
                if (key != null) {
                    key = formatKey(key.toString());
                }
                return super.containsKey(key);
            }
        });
    }

    /**
     * Format to Hump-shaped words.
     */
    public static String formatKey(String key) {
        return TextUtils.isEmpty(key) ? "" : key;
    }
}