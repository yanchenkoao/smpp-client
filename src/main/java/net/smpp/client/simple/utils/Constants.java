package net.smpp.client.simple.utils;

public interface Constants {

	int ENQUIRE_LINK_TIMER = 40000; //40 seconds
	int MAX_SMS_MESSAGES_PARTS = 10; //max parts for message (for this smpp-client)
	short GSM_LENGTH = 160;
	short UCS_LENGTH = 70;
	short GSM_CONCAT_LENGTH = 153;
	short UCS_CONCAT_LENGTH = 67;

	int MAX_LOGIN_LENGTH = 15;
	int MAX_PASSWORD_LENGTH = 8;

	byte GSM_7_BIT = 0x00;
	byte LATIN_8859_1 = 0x03;
	byte ALPHA_UCS2 = 0x08;

	int MINUTES_IN_AN_HOUR = 60;
	int SECONDS_IN_A_MINUTE = 60;
	int HOURS_IN_A_DAY = 24;

	String UCS2_ENCODING = "UTF-16BE";

	int MAX_MESSAGES_PER_BATCH = 200;

}
