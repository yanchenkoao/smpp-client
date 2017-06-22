package net.smpp.client.simple.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.smpp.client.simple.enums.LatinEncodingType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    private LatinEncodingType latinEncodingType;
    private int registeredDelivery;
}
