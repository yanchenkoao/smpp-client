package net.smpp.client.simple.service;

import net.smpp.client.simple.domain.ServiceType;
import net.smpp.client.simple.domain.UdhType;
import net.smpp.client.simple.utils.TextUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;
import org.springframework.stereotype.Component;

import java.util.Random;

import static net.smpp.client.simple.domain.UdhType.*;
import static org.jsmpp.SMPPConstant.ESMCLS_DEFAULT_MODE;
import static org.jsmpp.SMPPConstant.ESMCLS_UDHI_INDICATOR_SET;

@Component
public class MessageSender {

    private Logger logger = Logger.getLogger(MessageSender.class);

    public void sendMessage(UdhType udhType,
                            String text,
                            String alphaName,
                            String phone,
                            SMPPSession session,
                            ServiceType serviceType,
                            Integer validityPeriod,
                            Byte sourceAddrTon,
                            Byte sourceAddrNpi,
                            Byte destAddrTon,
                            Byte destAddrNpi) {
        try {
            byte encoding = TextUtils.determineEncodingStatus(text);
            String[] partsMessage = TextUtils.getPartsOfMessage(text);

            byte refNum = (byte) new Random().nextInt();

            byte totalSegments = (byte) partsMessage.length;
            OptionalParameter sarMsgRefNum = OptionalParameters.newSarMsgRefNum(refNum);
            OptionalParameter sarTotalSegments = OptionalParameters.newSarTotalSegments(totalSegments);


            for (int i = 0; i < totalSegments; i++) {
                byte[] message;

                if (udhType == udh_8bit || udhType == udh_16bit) {
                    message = TextUtils.addUdh(partsMessage[i], (byte) (i + 1), totalSegments, refNum, encoding, udhType);
                } else {
                    message = TextUtils.convertStringToByte(partsMessage[i], encoding);
                }

                OptionalParameter sarSegmentSeqnum = OptionalParameters.newSarSegmentSeqnum(i + 1);

                try {
                    String messageId;
                    if (udhType == udh_8bit || udhType == udh_16bit) {
                        messageId = session.submitShortMessage(serviceType.getStatus(),
                                TypeOfNumber.valueOf(sourceAddrTon),
                                NumberingPlanIndicator.valueOf(sourceAddrNpi),
                                alphaName,
                                TypeOfNumber.valueOf(destAddrTon),
                                NumberingPlanIndicator.valueOf(destAddrNpi),
                                phone,
                                getEsmClass(udhType),
                                (byte) 0,
                                (byte) 1,
                                "",
                                TextUtils.getSmsValidityPeriod(validityPeriod),
                                new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
                                (byte) 0,
                                DataCodings.newInstance(encoding),
                                (byte) 0,
                                message);
                    } else if (udhType == tlv){
                        messageId = session.submitShortMessage(serviceType.getStatus(),
                                TypeOfNumber.valueOf(sourceAddrTon),
                                NumberingPlanIndicator.valueOf(sourceAddrNpi),
                                alphaName,
                                TypeOfNumber.valueOf(destAddrTon),
                                NumberingPlanIndicator.valueOf(destAddrNpi),
                                phone,
                                getEsmClass(udhType),
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
                    } else {
                        messageId = session.submitShortMessage(serviceType.getStatus(),
                                TypeOfNumber.valueOf(sourceAddrTon),
                                NumberingPlanIndicator.valueOf(sourceAddrNpi),
                                alphaName,
                                TypeOfNumber.valueOf(destAddrTon),
                                NumberingPlanIndicator.valueOf(destAddrNpi),
                                phone,
                                getEsmClass(udhType),
                                (byte) 0,
                                (byte) 1,
                                "",
                                TextUtils.getSmsValidityPeriod(validityPeriod),
                                new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
                                (byte) 0,
                                DataCodings.newInstance(encoding),
                                (byte) 0,
                                message);
                    }


                    logger.info("Message sent, message_id is " + Long.valueOf(messageId, 16));
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private ESMClass getEsmClass(UdhType udhType) {
        if (udhType == no_udh || udhType == tlv) {
            return new ESMClass(ESMCLS_DEFAULT_MODE);
        } else if (udhType == udh_8bit || udhType == udh_16bit) {
            return new ESMClass(ESMCLS_UDHI_INDICATOR_SET);
        }
        return new ESMClass(ESMCLS_DEFAULT_MODE);
    }
}
