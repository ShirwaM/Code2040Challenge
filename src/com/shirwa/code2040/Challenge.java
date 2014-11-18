package com.shirwa.code2040;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by shirwamohamed on 11/18/14.
 */
public class Challenge {
    private static AsyncClient httpclient;

    public static void main(String[] args) {
        httpclient = new AsyncClient();
        getToken("shirwa99@gmail.com",);
    }

    public static void getToken(String email, String github) {
        final String token = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("github", github);
        httpclient.post("", params, new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage() + "");
            }
        });
    }

    /*
        This method reverses a String using String Builders.
     */
    public static String reverseString(String origin) {
        if (origin == null) {
            return null;
        }
        return new StringBuilder(origin).reverse().toString();
    }
}
