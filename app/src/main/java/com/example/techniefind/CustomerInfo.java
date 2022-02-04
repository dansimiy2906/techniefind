package com.example.techniefind;

public class CustomerInfo {
    String Image,CustomerName,CustomerIDNumber,CustomerLocation,CustomerContact,CustomerPk,role;

    public CustomerInfo() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerIDNumber() {
        return CustomerIDNumber;
    }

    public void setCustomerIDNumber(String customerIDNumber) {
        CustomerIDNumber = customerIDNumber;
    }

    public String getCustomerLocation() {
        return CustomerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        CustomerLocation = customerLocation;
    }

    public String getCustomerContact() {
        return CustomerContact;
    }

    public void setCustomerContact(String customerContact) {
        CustomerContact = customerContact;
    }

    public String getCustomerPk() {
        return CustomerPk;
    }

    public void setCustomerPk(String customerPk) {
        CustomerPk = customerPk;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
