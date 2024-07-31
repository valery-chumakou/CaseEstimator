package org.example.caseestimator;

public class UserStore {
    static String loggedInUser;
    static int loggedInRate;

    public static int getLoggedInRate() {
        return loggedInRate;
    }

    public static void setLoggedInRate(int rate) {
        UserStore.loggedInRate = rate;
    }

    public static void setLoggedInUser(String user) {
        loggedInUser = user;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }
}
