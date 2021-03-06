package com.shirwa.code2040;


import com.google.gson.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by shirwamohamed on 11/18/14.
 */

public class Challenge {
    private static AsyncClient httpclient;
    private static UserItem userItem;
    private static String baseURL = "http://challenge.code2040.org/api/";
    private static String email = "shirwa99@gmail.com";
    private static String github = "https://www.github.com/ShirwaM/Code2040Challenge";

    public static void main(String[] args) {
        httpclient = new AsyncClient();
        userItem = new UserItem(email, github);
        getToken(); //This method initiates the overall process and does what is required for each stage.
    }

    public static void getToken() {
        JsonObject object = new JsonObject();
        object.addProperty("email", userItem.getEmail());
        object.addProperty("github", userItem.getGithub());
        System.out.println(object.toString());
        httpclient.post(baseURL + "register", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                userItem.setToken(o.get("result").getAsString());
                //getString(); stage 1
                //getNeedleInHayStack(); stage 2
                //getPrefix(); stage 3
                getTime();//stage 4
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Get Token" + e.getMessage());
            }
        });
    }

    public static void getString() {
        JsonObject object = new JsonObject();
        object.addProperty("token", userItem.getToken());
        System.out.println(object.toString());
        httpclient.post(baseURL + "getstring", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                userItem.setStringtoReverse(o.get("result").getAsString());
                reverseString();
            }

            @Override
            public void onError(Exception e) {
                System.out.println("getString " + e.getMessage());
            }
        });
    }

    public static void reverseString() {
        String reversed = reverseStringHelper(userItem.getStringtoReverse());
        JsonObject object = new JsonObject();
        object.addProperty("token", userItem.getToken());
        object.addProperty("string", reversed);
        System.out.println(object.toString());
        httpclient.post(baseURL + "validatestring", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("reverseString " + e.toString());
            }
        });
    }

    /*
        This method reverses a String using String Builders.
     */
    public static String reverseStringHelper(String origin) {
        if (origin == null) {
            return null;
        }
        return new StringBuilder(origin).reverse().toString();
    }

    public static void getNeedleInHayStack() {
        JsonObject object = new JsonObject();
        object.addProperty("token", userItem.getToken());
        System.out.println(object.toString());
        httpclient.post(baseURL + "haystack", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                JsonObject object = o.getAsJsonObject("result");
                JsonArray array = object.get("haystack").getAsJsonArray();
                sendNeedleIndex(getNeedleInHayStackHelper(object.get("needle").getAsString(), array));
            }

            @Override
            public void onError(Exception e) {
                System.out.println("getNeedleInHayStack " + e.toString());
            }
        });
    }

    public static int getNeedleInHayStackHelper(String needle, JsonArray haystack) {
        String[] haystackString = getStringArray(haystack);
        for (int i = 0; i < haystack.size(); i++) {
            if (haystackString[i].equals(needle))
                return i;
        }
        return -1;//Needle was not found in the Haystack.
    }

    public static void sendNeedleIndex(int index) {
        JsonObject object = new JsonObject();
        object.addProperty("token", userItem.getToken());
        object.addProperty("needle", index);
        System.out.print(object.toString());
        httpclient.post(baseURL + "validateneedle", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.print("sendNeedleIndex " + e.getMessage());
            }
        });
    }

    public static String[] getStringArray(JsonArray jsonArray) {
        String[] stringArray;
        int length = jsonArray.size();
        stringArray = new String[length];
        for (int i = 0; i < length; i++) {
            stringArray[i] = jsonArray.get(i).getAsString();
        }
        return stringArray;
    }

    public static void getPrefix() {
        JsonObject object = new JsonObject();
        object.addProperty("token", userItem.getToken());
        httpclient.post(baseURL + "prefix", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                JsonObject object = o.get("result").getAsJsonObject();
                JsonArray array = object.get("array").getAsJsonArray();
                String prefix = object.get("prefix").getAsString();
                sendPrefix(getPrefix(array, prefix));
            }

            @Override
            public void onError(Exception e) {
                System.out.print("Get Prefix" + e.toString());
            }
        });
    }

    public static List<String> getPrefix(JsonArray jsonArray, String prefix) {
        String array[] = getStringArray(jsonArray);
        List<String> containPrefix = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            if (!array[i].startsWith(prefix))
                containPrefix.add(array[i]);
        }
        return containPrefix;
    }

    public static void sendPrefix(List<String> array) {
        JsonObject object = new JsonObject();
        Gson gson = new Gson();
        String jsonString = gson.toJson(array);
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        object.addProperty("token", userItem.getToken());
        object.add("array", jsonArray);
        System.out.println(object.toString());
        httpclient.post(baseURL + "validateprefix", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                System.out.print(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("SendPrefix " + e.getMessage());
            }
        });
    }

    public static void getTime() {
        JsonObject object = new JsonObject();
        object.addProperty("token", userItem.getToken());
        httpclient.post(baseURL + "time", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                JsonObject object = o.get("result").getAsJsonObject();
                String recievedDate = object.get("datestamp").getAsString();
                String interval = object.get("interval").getAsString();
                sendNewDate(getTimeInterval(recievedDate, interval));
            }

            @Override
            public void onError(Exception e) {
                System.out.print("Get Time " + e.getMessage());
            }
        });
    }

    public static String getTimeInterval(String date, String i) {
        int interval = Integer.parseInt(i);
        Date mainDate = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println("Given : " + date + " With Interal : " + i);
        try {
            mainDate = df.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mainDate);
        calendar.add(Calendar.SECOND, interval);
        return df.format(calendar.getTime());
    }

    public static void sendNewDate(String date) {
        JsonObject object = new JsonObject();
        object.addProperty("token", userItem.getToken());
        object.addProperty("datestamp", date);
        System.out.println(object.toString());
        httpclient.post(baseURL + "validatetime", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                System.out.print(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.print("Send Date " + e.getMessage());
            }
        });
    }
}
