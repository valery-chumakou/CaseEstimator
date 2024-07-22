package org.example.caseestimator;

import java.time.LocalDate;

public class Billing extends Client{
    private int rate;
    private String tasks;
    private String timeSpent;
    private String user;
    private String officeNo;
    private LocalDate date;

    public Billing(int rate, String tasks, String timeSpent, String user, String officeNo, LocalDate date) {
        this.rate = rate;
        this.tasks = tasks;
        this.timeSpent = timeSpent;
        this.user = user;
        this.officeNo = String.valueOf(officeNo);
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOfficeNo() {
        return officeNo;
    }

    public void setOfficeNo(String officeNo) {
        this.officeNo = String.valueOf(officeNo);
    }

    public String getDate() {
        return LocalDate.now().toString();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}