package net.smpp.client.simple.domain;

public enum ServiceType {

    default_type(""),
    cellular_messaging_CMT("CMT"),
    cellular_paging_CPT("CPT"),
    voice_mail_notification_VMN("VMN"),
    voice_mail_alerting_VMA("VMA"),
    wireless_application_protocol_WAP("WAP"),
    unstructured_supplementary_services_data_USSD("USSD");

    private String status;

    ServiceType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
