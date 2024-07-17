package org.example.caseestimator;

public class UserStore {
    static String loggedInUser;
    public static void setLoggedInUser(String user) {
        loggedInUser = user;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }
}
