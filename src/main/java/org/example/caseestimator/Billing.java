package org.example.caseestimator;

import java.time.LocalDate;
import java.util.Date;

public class Billing extends Client{
    private Integer rate;
    private String tasks;
    private Integer timeSpent;
    private String user;
    private Integer officeNo;
    private Date date;

    public Billing(Integer rate, String tasks, Integer timeSpent, String user, Integer officeNo, Date date) {
        this.rate = rate;
        this.tasks = tasks;
        this.timeSpent = timeSpent;
        this.user = user;
        this.officeNo = officeNo;
        this.date = date;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getOfficeNo() {
        return officeNo;
    }

    public void setOfficeNo(Integer officeNo) {
        this.officeNo = officeNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
