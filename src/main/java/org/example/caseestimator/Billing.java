package org.example.caseestimator;

import java.time.LocalDate;
import java.time.LocalTime;

public class Billing extends Client{
    private int rate;
    private String tasks;
    private LocalTime timeSpent;
    private String user;
    private String officeNo;
    private LocalDate date;
    private Double sum;
    private Billing_Table_Records billingTableRecords;

    public Billing(int rate, String tasks, LocalTime timeSpent, String user, String officeNo, LocalDate date, Double sum) {
        this.rate = rate;
        this.tasks = tasks;
        this.timeSpent = timeSpent;
        this.user = user;
        this.officeNo = String.valueOf(officeNo);
        this.date = date;
        this.sum = sum;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
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

    public LocalTime getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(LocalTime timeSpent) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}