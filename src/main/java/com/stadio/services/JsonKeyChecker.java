package com.stadio.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonKeyChecker {

    public boolean checkIfKeyExists(String response, String key) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        return jsonObject.has(key);
    }
}
