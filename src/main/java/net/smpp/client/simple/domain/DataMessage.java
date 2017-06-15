package net.smpp.client.simple.domain;

public class DataMessage {
    private String alphaName;
    private String phone;
    private String text;
    private UdhType udhType;

    private ServiceType serviceType;
    private Integer validityPeriod;
    private Byte sourceAddrTon;
    private Byte sourceAddrNpi;
    private Byte destAddrTon;
    private Byte destAddrNpi;

    public DataMessage(String alphaName, String phone, String text, UdhType udhType, ServiceType serviceType, Integer validityPeriod, Byte sourceAddrTon, Byte sourceAddrNpi, Byte destAddrTon, Byte destAddrNpi) {
        this.alphaName = alphaName;
        this.phone = phone;
        this.text = text;
        this.udhType = udhType;
        this.serviceType = serviceType;
        this.validityPeriod = validityPeriod;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.destAddrTon = destAddrTon;
        this.destAddrNpi = destAddrNpi;
    }

    public String getAlphaName() {
        return alphaName;
    }

    public String getPhone() {
        return phone;
    }

    public String getText() {
        return text;
    }

    public UdhType getUdhType() {
        return udhType;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    public Byte getSourceAddrTon() {
        return sourceAddrTon;
    }

    public Byte getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    public Byte getDestAddrTon() {
        return destAddrTon;
    }

    public Byte getDestAddrNpi() {
        return destAddrNpi;
    }
}
