package net.smpp.client.simple.service;

import net.smpp.client.simple.domain.ServiceType;
import net.smpp.client.simple.utils.TextUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MessageSender {

    private Logger logger = Logger.getLogger(MessageSender.class);

    public void sendMessage(String text,
                            String alphaName,
                            String phone,
                            SMPPSession session,
                            ServiceType serviceType,
                            Integer validityPeriod,
                            Integer esmClass,
                            Byte sourceAddrTon,
                            Byte sourceAddrNpi,
                            Byte destAddrTon,
                            Byte destAddrNpi) {
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

                String messageId = null;
                try {
                    messageId = session.submitShortMessage(serviceType.getStatus(),
                            TypeOfNumber.valueOf(sourceAddrTon),
                            NumberingPlanIndicator.valueOf(sourceAddrNpi),
                            alphaName,
                            TypeOfNumber.valueOf(destAddrTon),
                            NumberingPlanIndicator.valueOf(destAddrNpi),
                            phone,
                            new ESMClass(esmClass),
                            (byte) 0,
                            (byte) 1,
                            "",
                            TextUtils.getSmsValidityPeriod(validityPeriod),
                            new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
                            (byte) 0,
                            DataCodings.newInstance(encoding),
                            (byte) 0,
                            message,
                            sarMsgRefNum,
                            sarSegmentSeqnum,
                            sarTotalSegments);

                    logger.info("Message sent, message_id is " + Long.valueOf(messageId, 16));

                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
