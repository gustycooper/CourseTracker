package zzwierko.coursetracker;

import java.util.Map;

class User {
    private String token;
    private String name;
    private String email;

    User(Map<String, String> user_data) {
        token = user_data.get("uid");
        if (token == null) {
            throw new RuntimeException("User token not found.");
        }

        name = user_data.get("name");
        email = user_data.get("email");
    }

    String getToken() {
        return token;
    }

    String getName() {
        return name;
    }

    String getEmail() {
        return email;
    }
}