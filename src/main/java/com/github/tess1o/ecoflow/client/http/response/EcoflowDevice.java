package com.github.tess1o.ecoflow.client.http.response;

public class EcoflowDevice {

    private String sn;
    private int online;
    private String productName;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    @Override
    public String toString() {
        return "EcoflowDevice{" +
                "sn='" + sn + '\'' +
                ", online=" + online +
                ", productName='" + productName + '\'' +
                '}';
    }
}
