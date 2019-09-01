package com.zero.ci.network.zrequest.conver;

import java.lang.reflect.Type;

/**
 * 数据解析
 */

public interface IFromJson {

    <T> T onFromJson(String json, Type type);

    String onToJson(Object object);
}
