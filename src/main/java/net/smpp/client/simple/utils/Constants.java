package net.smpp.client.simple.utils;

public interface Constants {

	String UCS2_ENCODING = "UTF-16BE";
	int ENQUIRE_LINK_TIMER = 40000; //40 seconds

	int MAX_SMS_MESSAGES_PARTS = 6; //max parts for message (for this smpp-client)

	short GSM_LENGTH = 160;
	short UCS_LENGTH = 70;
	short GSM_CONCAT_LENGTH = 153;
	short UCS_CONCAT_LENGTH = 67;

}
