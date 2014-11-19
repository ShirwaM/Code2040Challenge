package com.shirwa.code2040;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Created by shirwamohamed on 11/18/14.
 */
public class Challenge {
    private static AsyncClient httpclient;
    private static UserItem userItem;

    public static void main(String[] args) {
        httpclient = new AsyncClient();
        userItem = new UserItem("shirwa99@gmail.com", "https://www.github.com/ShirwaM/Code2040Challenge");
        getToken();
//        userItem.getStatus(httpclient);
    }

    public static void getToken() {
        JsonObject object = new JsonObject();
        object.addProperty("email", userItem.getEmail());
        object.addProperty("github", userItem.getGithub());
        System.out.println(object.toString());
        httpclient.post("http://challenge.code2040.org/api/register", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                userItem.setToken(o.get("result").getAsString());
                //getString(); stage 1
                getNeedleInHayStack();
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
        httpclient.post("http://challenge.code2040.org/api/getstring", object.toString(), new AsyncClient.Callback() {
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
        httpclient.post("http://challenge.code2040.org/api/validatestring", object.toString(), new AsyncClient.Callback() {
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
        httpclient.post("http://challenge.code2040.org/api/haystack", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                JsonObject object = o.getAsJsonObject("result");
                JsonArray array = o.getAsJsonArray("haystack");
                String needle = object.get("needle").getAsString();
                sendNeedleIndex(getNeedleInHayStackHelper(needle, array));
            }

            @Override
            public void onError(Exception e) {
                System.out.println("getNeedleInHayStack " + e.toString());
            }
        });
    }

    public static int getNeedleInHayStackHelper(String needle, JsonArray haystack) {
        for (int i = 0; i < haystack.size(); i++) {
            if (haystack.get(i).toString().equals(needle))
                return i;
        }
        return -1;//Needle was not found in the Haystack.
    }

    public static void sendNeedleIndex(int index) {
        JsonObject object = new JsonObject();
        object.addProperty(userItem.getToken(), index);
        object.addProperty("needle", index);
        httpclient.post("http://challenge.code2040.org/api/validateneedle", object.toString(), new AsyncClient.Callback() {
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


}
