package org.example.caseestimator;

import java.time.LocalDate;

public class Payments extends Client {



    private double total_owned;
    private double total_outstanding;

    private Client client;

    public Payments( double total_owned, double total_outstanding, Client client) {
         this.total_owned = total_owned;
        this.total_outstanding = total_outstanding;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}