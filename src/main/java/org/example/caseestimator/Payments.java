package org.example.caseestimator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Payments {


    private SimpleDoubleProperty total_paid;
    private SimpleObjectProperty<Client> client; // Use SimpleObjectProperty for the Client
    private SimpleDoubleProperty total_owned; // Use SimpleDoubleProperty for totalOwned
    private double amount;
    private String cardNumber;
    private String cvv;
    private String expiryDate;


    public Payments(Client client, double total_owned, double total_paid) {
        this.client = new SimpleObjectProperty<>(client);
        this.total_owned = new SimpleDoubleProperty(total_owned);
        this.total_paid = new SimpleDoubleProperty(total_paid);
    }

    public Payments(double amount, String cardNumber, String cvv, String expiryDate) {
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setClient(Client client) {
        this.client.set(client);
    }

    public double getTotal_owned() {
        return total_owned.get();
    }

    public SimpleDoubleProperty total_ownedProperty() {
        return total_owned;
    }

    public void setTotal_owned(double total_owned) {
        this.total_owned.set(total_owned);
    }

    public double getTotal_paid() {
        return total_paid.get();
    }

    public SimpleDoubleProperty total_paidProperty() {
        return total_paid;
    }

    public void setTotal_paid(double total_paid) {
        this.total_paid.set(total_paid);
    }

    public Client getClient() {
        return client.get(); // Get the Client object
    }

    public double getTotalOwned() {
        return total_owned.get(); // Get the total owned value
    }

    public SimpleObjectProperty<Client> clientProperty() {
        return client; // Allows for property binding
    }

    public SimpleDoubleProperty totalOwnedProperty() {
        return total_owned; // Allows for property binding
    }
}