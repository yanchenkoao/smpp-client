package net.smpp.client.simple.service;

import org.apache.commons.lang.exception.ExceptionUtils;
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

    private Logger logger = Logger.getLogger(MessageReceiver.class);

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                String hexMessageId = delReceipt.getId();
                long longMessageId = Long.parseLong(hexMessageId, 16);

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
                } catch (NullPointerException e) {
                    logger.info("no Optional parameters in hex tags");
                }

                logger.info("Delivery Receipt: hex messageId=" + hexMessageId + " long messageId=" + longMessageId + System.lineSeparator() +
                        "text=" + delReceipt + System.lineSeparator() +
                        parametersCollect + System.lineSeparator());
            } catch (InvalidDeliveryReceiptException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        } else {
            // regular short message
            logger.info("Receiving message : " + new String(deliverSm.getShortMessage()));
        }
    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {
        logger.info("Received AlertNotification (onAcceptAlertNotification method)");
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
        logger.info("Received DataSm (onAcceptDataSm method)");
        return null;
    }
}
