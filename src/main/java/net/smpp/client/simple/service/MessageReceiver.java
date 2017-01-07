package net.smpp.client.simple.service;

import org.apache.log4j.Logger;
import org.jsmpp.bean.*;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
class MessageReceiver implements MessageReceiverListener {

//    private Logger logger = Logger.getLogger(MessageReceiver.class);

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                long id = Long.parseLong(delReceipt.getId());
                String messageId = Long.toString(id, 16).toUpperCase();

                OptionalParameter[] optionalParameters = deliverSm.getOptionalParameters();

                String parametersCollect = "";

                try {
                    parametersCollect = Arrays.stream(optionalParameters)
                            .map(v -> {
                                try {
                                    return "[hex tag=" + Integer.toHexString(v.tag) + "] [value=" + new String(v.serialize(), "UTF-8").trim() + "]" + System.lineSeparator();
                                } catch (UnsupportedEncodingException e) {
                                    System.out.println(e);
                                    return "";
                                }
                            })
                            .collect(Collectors.joining());
                } catch (NullPointerException e){
                    System.out.println("no special hex tags");
                }

                System.out.println("Delivery Receipt: messageId=" + messageId + "  text=" + delReceipt + System.lineSeparator() + parametersCollect);
            } catch (InvalidDeliveryReceiptException e) {
                System.out.println(e);
            }
        } else {
            // regular short message
            System.out.println("Receiving message : " + new String(deliverSm.getShortMessage()));
        }
    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {
        System.out.println("onAcceptAlertNotification");
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
        System.out.println("onAcceptDataSm");
        return null;
    }
}
