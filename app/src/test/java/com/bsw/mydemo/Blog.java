package com.bsw.mydemo;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class Blog {
    @Test
    public void jsonTest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("aBoolean", true);
            jsonObject.put("string", "hasController");
            jsonObject.put("param", new Param());

            MyChange change = new Gson().fromJson(jsonObject.toString(), MyChange.class);
            System.out.println("转换成功   \n****************************\naBoolean = " + change.aBoolean
                    + " \nstring = " + change.string
                    + " \nparam = " + change.param
                    + "\n****************************");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MyChange {
        boolean aBoolean;
        boolean string = true;
        boolean param = true;
    }

    private class Param {

    }
}