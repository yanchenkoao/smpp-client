package net.smpp.client.simple.utils;

import net.smpp.client.simple.domain.UdhType;
import org.jsmpp.SMPPConstant;
import org.jsmpp.util.OctetUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public final class TextUtils {

    public static final long serialVersionUID = 1_000_031L;

    private static final int MINUTES_IN_AN_HOUR = 60;
    private static final int SECONDS_IN_A_MINUTE = 60;
    private static final int HOURS_IN_A_DAY = 24;

    private static final String UCS2_ENCODING = "UTF-16BE";

    private TextUtils() {
    }

    public static byte determineEncodingStatus(String text) {
        if (Gsm0338Charset.isGsmChars(text)) {
            return SMPPConstant.ALPHA_DEFAULT; //0x00
        } else {
            return SMPPConstant.ALPHA_UCS2; //0x08
        }
    }

    public static byte[] convertStringToByte(String text, byte dataCoding) throws UnsupportedEncodingException {
        if (dataCoding == SMPPConstant.ALPHA_UCS2) { //Cyrillic
            return text.getBytes(UCS2_ENCODING);
        } else {
            return Gsm0338Charset.toGsm(text);
        }
    }

    public static String convertByteToString(byte[] array, byte dataCoding) throws UnsupportedEncodingException {
        //0-latin, 8-cyrillic
        if (dataCoding == SMPPConstant.ALPHA_UCS2) { //Cyrillic
            return new String(array, UCS2_ENCODING);
        } else {
            return Gsm0338Charset.toUnicode(array);
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

    public static String getSmsValidityPeriod(int ttl) {
        //        000000000500000R  Relative validity format "YYMMDDhhmmss000R", 5 minutes
        //        YYMMDDHHMMSS000R
        int seconds = ttl % SECONDS_IN_A_MINUTE;
        int totalMinutes = ttl / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hoursTotal = totalMinutes / MINUTES_IN_AN_HOUR;
        int hours = hoursTotal % HOURS_IN_A_DAY;
        int daysTotal = hoursTotal / HOURS_IN_A_DAY;
        int days = daysTotal % HOURS_IN_A_DAY;

        //begin build string
        String result = "0000";

        if (days > 0) {
            if (days < 10) {
                result = result + "0" + days;
            } else {
                result = result + days;
            }
        } else {
            result = result + "00";
        }
        if (hours > 0) {
            if (hours < 10) {
                result = result + "0" + hours;
            } else {
                result = result + hours;
            }
        } else {
            result = result + "00";
        }
        if (minutes > 0) {
            if (minutes < 10) {
                result = result + "0" + minutes;
            } else {
                result = result + minutes;
            }
        } else {
            result = result + "00";
        }
        if (seconds > 0) {
            if (seconds < 10) {
                result = result + "0" + seconds;
            } else {
                result = result + seconds;
            }
        } else {
            result = result + "00";
        }

        return result + "000R";
    }

    public static byte[] addUdh(String text, byte part, byte parts, int ref, byte encoding, UdhType udhType) throws UnsupportedEncodingException {

        byte[] aMessage;

        if (encoding == SMPPConstant.ALPHA_UCS2) { //Cyrilic
            aMessage = text.getBytes(UCS2_ENCODING);
        } else {
            aMessage = Gsm0338Charset.toGsm(text);
        }

        if (parts == 1) {
            return aMessage;
        }

        byte UDHIE_HEADER_LENGTH;
        byte UDHIE_IDENTIFIER_SAR;
        byte UDHIE_SAR_LENGTH;

        if (udhType == UdhType.udh_16bit) {
            UDHIE_HEADER_LENGTH = 0x06;
            UDHIE_IDENTIFIER_SAR = 0x08;
            UDHIE_SAR_LENGTH = 0x04;

            int lengthOfData = aMessage.length;

            byte[] segments = new byte[7 + lengthOfData];
            segments[0] = UDHIE_HEADER_LENGTH;
            segments[1] = UDHIE_IDENTIFIER_SAR;
            segments[2] = UDHIE_SAR_LENGTH;
            segments[3] = (byte) ((ref & 0xFF00) >> 8);
            segments[4] = (byte) (ref & 0xFF);
            segments[5] = parts;
            segments[6] = part;
            System.arraycopy(aMessage, 0, segments, 7, lengthOfData);
            return segments;

        } else {
            //8bit
            UDHIE_HEADER_LENGTH = 0x05;
            UDHIE_IDENTIFIER_SAR = 0x00;
            UDHIE_SAR_LENGTH = 0x03;

            int lengthOfData = aMessage.length;

            byte[] segments = new byte[6 + lengthOfData];
            segments[0] = UDHIE_HEADER_LENGTH;
            segments[1] = UDHIE_IDENTIFIER_SAR;
            segments[2] = UDHIE_SAR_LENGTH;
            segments[3] = (byte) ref;
            segments[4] = parts;
            segments[5] = part;
            System.arraycopy(aMessage, 0, segments, 6, lengthOfData);
            return segments;

        }
    }
}
