package com.zero.ci.network.zrequest.conver;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FromJsonImpl implements IFromJson {
    @Override
    public <T> T onFromJson(String json, final Type type) {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(List.class, new JsonDeserializer<List<?>>() {
            @Override
            public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json.isJsonArray()) {
                    JsonArray array = json.getAsJsonArray();
                    Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                    List list = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        JsonElement element = array.get(i);
                        Object item = context.deserialize(element, itemType);
                        list.add(item);
                    }
                 //   Logger.d(" FromJsonImpl : === >>> " + list.toString());
                    return list;
                } else {
                    //和接口类型不符，返回空List
                    return Collections.EMPTY_LIST;
                }
            }
        }).create();


        return gson.fromJson(json, type);
    }

    @Override
    public String onToJson(Object object) {
        return new Gson().toJson(object);
    }
}
