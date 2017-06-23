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

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                String hexMessageId = delReceipt.getId();
                String source = deliverSm.getSourceAddr();

                OptionalParameter[] optionalParameters = deliverSm.getOptionalParameters();
                String parametersCollect = "";

                parametersCollect = Arrays.stream(optionalParameters)
                        .map(v -> {
                            try {
                                return "[hex tag=" + Integer.toHexString(v.tag) + "] [value=" + new String(v.serialize(), "UTF-8").trim() + "]" + System.lineSeparator();
                            } catch (UnsupportedEncodingException e) {
                                logger.error(e.getMessage(), e);
                                return "";
                            }
                        })
                        .collect(Collectors.joining());

                logger.info("delivery receipt: messageId=" + hexMessageId + System.lineSeparator() +
                        "source: " + source + System.lineSeparator() +
                        "text=" + delReceipt + System.lineSeparator() +
                        parametersCollect + System.lineSeparator());
            } catch (InvalidDeliveryReceiptException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            // regular short message
            logger.info("receiving message: " + new String(deliverSm.getShortMessage()));
        }
    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {
        logger.info("Received AlertNotification");
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
        logger.info("Received DataSm");
        return null;
    }
}
