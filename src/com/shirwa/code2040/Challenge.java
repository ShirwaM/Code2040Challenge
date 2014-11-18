package com.shirwa.code2040;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Created by shirwamohamed on 11/18/14.
 */
public class Challenge {
    private static AsyncClient httpclient;
    private static String token;
    private static String toReverse;

    public static void main(String[] args) {
        httpclient = new AsyncClient();
        getToken("shirwa99@gmail.com", "https://www.github.com/ShirwaM/Code2040Challenge");
        getString();
    }

    private static void setToken(String data) {
        token = data;
    }

    public static void getToken(String email, String github) {
        JsonObject object = new JsonObject();
        object.addProperty("email", email);
        object.addProperty("github", github);
        System.out.println(object.toString());
        httpclient.post("http://challenge.code2040.org/api/register", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                setToken(o.get("result").toString());
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Get Token" + e.getMessage());
            }
        });
    }
    public static void getString() {
        JsonObject object = new JsonObject();
        object.addProperty("token", token);
        httpclient.post("http://challenge.code2040.org/api/getstring", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                JsonObject o = new JsonParser().parse(response).getAsJsonObject();
                String input = o.get("result").toString().replace("\"", "");
                System.out.println(input);
                reverseString(input,token.replace("\"",""));
            }

            @Override
            public void onError(Exception e) {
                System.out.println("getString " + e.getMessage());
            }
        });
    }

    public static void reverseString(String origin,String token) {
        String reversed = reverseStringHelper(origin);
        JsonObject object = new JsonObject();
        object.addProperty("token", token);
        object.addProperty("string", reversed);
        System.out.println(object.toString());
        httpclient.post("http://challenge.code2040.org/api/validatestring", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("reverseString " + e.getMessage());
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
}
