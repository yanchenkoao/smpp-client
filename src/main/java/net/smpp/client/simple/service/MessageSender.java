package net.smpp.client.simple.service;

import net.smpp.client.simple.utils.Constants;
import net.smpp.client.simple.utils.TextUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class MessageSender {

    private Logger logger = Logger.getLogger(MessageSender.class);

    public void sendMessage(String text, String alphaName, String phone, SMPPSession session) {
        try {
            byte encoding = TextUtils.determineEncodingStatus(text);
            String[] partsMessage = TextUtils.getPartsOfMessage(text);

            Random random = new Random();

            byte totalSegments = (byte) partsMessage.length;
            OptionalParameter sarMsgRefNum = OptionalParameters.newSarMsgRefNum((byte) random.nextInt());
            OptionalParameter sarTotalSegments = OptionalParameters.newSarTotalSegments(totalSegments);

            for (int i = 0; i < totalSegments; i++) {
                byte[] message = TextUtils.convertStringToByte(partsMessage[i], encoding);

                OptionalParameter sarSegmentSeqnum = OptionalParameters.newSarSegmentSeqnum(i + 1);
                String messageId = submitMessage(
                        session,
                        message,
                        sarMsgRefNum,
                        sarSegmentSeqnum,
                        sarTotalSegments,
                        alphaName,
                        phone,
                        encoding);
                logger.info("Message sent, message_id is " + Long.valueOf(messageId, 16));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private String submitMessage(SMPPSession session,
                                 byte[] message,
                                 OptionalParameter sarMsgRefNum,
                                 OptionalParameter sarSegmentSeqnum,
                                 OptionalParameter sarTotalSegments,
                                 String alphaName,
                                 String phone,
                                 byte encoding) {
        String messageId = null;
        try {
            messageId = session.submitShortMessage("CMT",
                    TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.UNKNOWN,
                    alphaName,
                    TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.UNKNOWN,
                    phone,
                    new ESMClass(),
                    (byte) 0,
                    (byte) 1,
                    "",
                    null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
                    (byte) 0,
                    DataCodings.newInstance(encoding),
                    (byte) 0,
                    message,
                    sarMsgRefNum,
                    sarSegmentSeqnum,
                    sarTotalSegments);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return messageId;
    }
}
