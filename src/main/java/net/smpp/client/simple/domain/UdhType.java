package net.smpp.client.simple.domain;

public enum UdhType {

    no_udh("no_udh"),
    udh_8bit("udh_8bit"),
    udh_16bit("udh_16bit"),
    tlv("tlv");

    private String status;

    UdhType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
