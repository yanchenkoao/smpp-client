package net.smpp.client.simple.utils;

import org.jsmpp.SMPPConstant;

import java.util.ArrayList;
import java.util.List;

public final class TextUtils {

    public static final long serialVersionUID = 1_000_025L;

    private TextUtils() {
    }

    public static byte determineEncodingStatus(String text) {
        if (Gsm0338Charset.isGsmChars(text)) {
            return SMPPConstant.ALPHA_DEFAULT; //0x00
        } else {
            return SMPPConstant.ALPHA_UCS2; //0x08
        }

    }

    public static String[] getPartsOfMessage(String value) {

        int partsLength;
        int textLength;
        List<String> list = new ArrayList<>();

        if (Gsm0338Charset.isGsmChars(value)) {
            byte[] byteMessage = Gsm0338Charset.toGsm(value);
            textLength = byteMessage.length;
            if (textLength > Constants.GSM_LENGTH) {
                partsLength = Constants.GSM_CONCAT_LENGTH;
            } else {
                partsLength = Constants.GSM_LENGTH;
            }

            int currentPosition = 0;
            for (int i = 0; i < Constants.MAX_SMS_MESSAGES_PARTS + 1 && currentPosition < textLength; i++) {
                int currentMax;
                int plannedMax = currentPosition + partsLength;
                if (plannedMax >= textLength) {
                    currentMax = textLength;
                } else {
                    if (byteMessage[plannedMax - 1] == 0x1B) {
                        currentMax = plannedMax - 1;
                    } else {
                        currentMax = plannedMax;
                    }
                }
                int currentLength = currentMax - currentPosition;
                byte[] currentPart = new byte[currentLength];
                System.arraycopy(byteMessage, currentPosition, currentPart, 0, currentLength);
                list.add(Gsm0338Charset.toUnicode(currentPart));
                currentPosition += currentLength;
            }
        } else {
            textLength = value.length();
            if (textLength > Constants.UCS_LENGTH) {
                partsLength = Constants.UCS_CONCAT_LENGTH;
            } else {
                partsLength = Constants.UCS_LENGTH;
            }
            int currentPosition = 0;
            for (int i = 0; i < Constants.MAX_SMS_MESSAGES_PARTS + 1 && currentPosition < textLength; i++) {
                int currentMax;
                int plannedMax = currentPosition + partsLength;
                if (plannedMax > textLength) {
                    currentMax = textLength;
                } else {
                    currentMax = plannedMax;
                }
                int currentLength = currentMax - currentPosition;
                String currentPart = value.substring(currentPosition, currentMax);
                list.add(currentPart);
                currentPosition += currentLength;
            }
        }
        return list.toArray(new String[list.size()]);
    }
}
