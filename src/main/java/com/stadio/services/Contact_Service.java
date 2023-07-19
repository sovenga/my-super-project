package com.stadio.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Contact_Service {
    public void processContactData(String payload) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject studRecord = (JSONObject) jsonParser.parse(payload);
        String ec_studentnumber = (String) studRecord.get("ec_studentnumber");

    }
}
