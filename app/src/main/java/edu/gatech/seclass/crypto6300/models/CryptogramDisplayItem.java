package edu.gatech.seclass.crypto6300.models;

public class CryptogramDisplayItem {
    public CryptogramDisplayItem(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String name;
    public String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
