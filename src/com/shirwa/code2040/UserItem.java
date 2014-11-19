package com.shirwa.code2040;

import com.google.gson.JsonObject;

/**
 * Created by shirwamohamed on 11/18/14.
 */
public class UserItem {
    private String email;
    private String github;
    private String token;
    private String stringtoReverse;

    public UserItem(String email, String github) {
        this.email = email;
        this.github = github;
    }

    public String getEmail() {
        return email;
    }

    public String getGithub() {
        return github;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStringtoReverse(String stringtoReverse) {
        this.stringtoReverse = stringtoReverse;
    }

    public String getStringtoReverse() {
        return stringtoReverse;
    }

    @Override
    public String toString() {
        return "Email: " + email + " Github: " + github + " Token: " + token;
    }

    public void getStatus(AsyncClient client) {
        JsonObject object = new JsonObject();
        object.addProperty("token", "DMZvjgq0T3");
        System.out.println(object.toString());
        client.post("http://challenge.code2040.org/api/status", object.toString(), new AsyncClient.Callback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }


}
