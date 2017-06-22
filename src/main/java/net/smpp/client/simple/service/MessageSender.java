package net.smpp.client.simple.service;

import net.smpp.client.simple.domain.DataMessage;
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
import static org.jsmpp.bean.SMSCDeliveryReceipt.*;

@Component
public class MessageSender {

    private Logger logger = Logger.getLogger(MessageSender.class);

    public void sendMessage(SMPPSession session,
                            DataMessage dataMessage,
                            boolean isBatch) {
        try {
            byte encoding = TextUtils.determineEncodingStatus(dataMessage.getText(), dataMessage.getLatinEncodingType());
            String[] partsMessage = TextUtils.getPartsOfMessage(dataMessage.getText(), dataMessage.getLatinEncodingType());

            byte refNum = (byte) new Random().nextInt();

            byte totalSegments = (byte) partsMessage.length;
            OptionalParameter sarMsgRefNum = OptionalParameters.newSarMsgRefNum(refNum);
            OptionalParameter sarTotalSegments = OptionalParameters.newSarTotalSegments(totalSegments);


            for (int i = 0; i < totalSegments; i++) {
                byte[] message;
                UdhType udhType = dataMessage.getUdhType();

                if (udhType == udh_8bit || udhType == udh_16bit) {
                    message = TextUtils.addUdh(partsMessage[i], (byte) (i + 1), totalSegments, refNum, encoding, udhType, dataMessage.getLatinEncodingType());
                } else {
                    message = TextUtils.convertStringToByte(partsMessage[i], encoding, dataMessage.getLatinEncodingType());
                }

                OptionalParameter sarSegmentSeqnum = OptionalParameters.newSarSegmentSeqnum(i + 1);

                ServiceType serviceType = dataMessage.getServiceType();
                Byte sourceAddrTon = dataMessage.getSourceAddrTon();
                Byte sourceAddrNpi = dataMessage.getSourceAddrNpi();
                String alphaName = dataMessage.getAlphaName();
                Byte destAddrTon = dataMessage.getDestAddrTon();
                Byte destAddrNpi = dataMessage.getDestAddrNpi();
                String phone = dataMessage.getPhone();
                Integer validityPeriod = dataMessage.getValidityPeriod();
                SMSCDeliveryReceipt smscDeliveryReceipt = dataMessage.getRegisteredDelivery() == 1 ? SUCCESS_FAILURE : DEFAULT;

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
                                TextUtils.generateSmsValidityPeriod(validityPeriod),
                                new RegisteredDelivery(smscDeliveryReceipt),
                                (byte) 0,
                                DataCodings.newInstance(encoding),
                                (byte) 0,
                                message);
                    } else if (udhType == tlv) {
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
                                TextUtils.generateSmsValidityPeriod(validityPeriod),
                                new RegisteredDelivery(smscDeliveryReceipt),
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
                                TextUtils.generateSmsValidityPeriod(validityPeriod),
                                new RegisteredDelivery(smscDeliveryReceipt),
                                (byte) 0,
                                DataCodings.newInstance(encoding),
                                (byte) 0,
                                message);
                    }
                    if (!isBatch) {
                        logger.info(String.format("Message sent, message_id hex=%s, long=%s", Long.valueOf(messageId, 16), messageId));
                    }
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
