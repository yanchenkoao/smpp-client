package net.smpp.client.simple.utils;

import net.smpp.client.simple.domain.UdhType;
import net.smpp.client.simple.enums.LatinEncodingType;
import net.smpp.client.simple.utils.encoding.CharsetEncoding;
import net.smpp.client.simple.utils.encoding.Gsm0338Charset;
import net.smpp.client.simple.utils.encoding.LatinIso8859Charset;
import org.jsmpp.SMPPConstant;
import org.jsmpp.util.RelativeTimeFormatter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static net.smpp.client.simple.enums.LatinEncodingType.GSM_0338;
import static net.smpp.client.simple.utils.Constants.*;

public final class TextUtils {

    public static final long serialVersionUID = 1_000_032L;

    private TextUtils() {
    }

    public static byte determineEncodingStatus(String text, LatinEncodingType latinLatinEncodingType) {
        CharsetEncoding charsetEncoding;

        if (latinLatinEncodingType == GSM_0338) {
            charsetEncoding = new Gsm0338Charset();
        } else {
            charsetEncoding = new LatinIso8859Charset();
        }

        if (charsetEncoding.isLatinEncoding(text)) {
            return GSM_7_BIT;
        } else {
            return ALPHA_UCS2;
        }
    }

    public static byte[] convertStringToByte(String text, byte dataCoding, LatinEncodingType latinEncodingTypeType) throws UnsupportedEncodingException {
        if (dataCoding == GSM_7_BIT || dataCoding == LATIN_8859_1) {
            CharsetEncoding charsetEncoding;
            if (latinEncodingTypeType == GSM_0338) {
                charsetEncoding = new Gsm0338Charset();
            } else {
                charsetEncoding = new LatinIso8859Charset();
            }
            return charsetEncoding.convertStringToBytes(text);
        } else {
            //cyrillic ALPHA_UCS2
            return text.getBytes(UCS2_ENCODING);
        }
    }

    public static String convertByteToString(byte[] array, byte dataCoding, LatinEncodingType latinEncodingTypeType) throws UnsupportedEncodingException {
        if (dataCoding == ALPHA_UCS2) {
            return new String(array, UCS2_ENCODING);
        } else {
            CharsetEncoding charsetEncoding;
            if (latinEncodingTypeType == GSM_0338) {
                charsetEncoding = new Gsm0338Charset();
            } else {
                charsetEncoding = new LatinIso8859Charset();
            }

            return charsetEncoding.convertBytesToString(array);
        }
    }

    public static String[] getPartsOfMessage(String value, LatinEncodingType latinEncodingType) {

        int partsLength;
        int textLength;
        List<String> list = new ArrayList<>();

        CharsetEncoding charsetEncoding;
        if (latinEncodingType == LatinEncodingType.GSM_0338) {
            charsetEncoding = new Gsm0338Charset();
        } else {
            charsetEncoding = new LatinIso8859Charset();
        }

        if (charsetEncoding.isLatinEncoding(value)) {
            byte[] byteMessage = charsetEncoding.convertStringToBytes(value);
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
                list.add(charsetEncoding.convertBytesToString(currentPart));
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

    public static String generateSmsValidityPeriod(int ttlSeconds) {
        int seconds = ttlSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = ttlSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hoursTotal = totalMinutes / MINUTES_IN_AN_HOUR;
        int hours = hoursTotal % HOURS_IN_A_DAY;
        int daysTotal = hoursTotal / HOURS_IN_A_DAY;
        int days = daysTotal % HOURS_IN_A_DAY;

        return RelativeTimeFormatter.format(0, 0, days, hours, minutes, seconds);
    }

    public static byte[] addUdh(String text,
                                byte part,
                                byte parts,
                                int ref,
                                byte encoding,
                                UdhType udhType,
                                LatinEncodingType latinEncodingType) throws UnsupportedEncodingException {

        byte[] aMessage;

        if (encoding == SMPPConstant.ALPHA_UCS2) { //Cyrilic
            aMessage = text.getBytes(UCS2_ENCODING);
        } else {
            CharsetEncoding charsetEncoding;
            if (latinEncodingType == LatinEncodingType.GSM_0338) {
                charsetEncoding = new Gsm0338Charset();
            } else {
                charsetEncoding = new LatinIso8859Charset();
            }
            aMessage = charsetEncoding.convertStringToBytes(text);
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
