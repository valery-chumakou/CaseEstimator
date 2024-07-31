package org.example.caseestimator;

import java.time.LocalDate;

public class Payments   {
    private String user;
    private String officeNo;
    private LocalDate date;
    private double total_owned;
    private double total_outstanding;
    private String first_name;
    private String last_name;
    private String business_name;

    public Payments(String user, String officeNo, LocalDate date, double total_owned, double total_outstanding, String first_name, String last_name, String business_name) {
        this.user = user;
        this.officeNo = officeNo;
        this.date = date;
        this.total_owned = total_owned;
        this.total_outstanding = total_outstanding;
        this.first_name = first_name;
        this.last_name = last_name;
        this.business_name = business_name;
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
        this.officeNo = officeNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotal_owned() {
        return total_owned;
    }

    public void setTotal_owned(double total_owned) {
        this.total_owned = total_owned;
    }

    public double getTotal_outstanding() {
        return total_outstanding;
    }

    public void setTotal_outstanding(double total_outstanding) {
        this.total_outstanding = total_outstanding;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }
}
