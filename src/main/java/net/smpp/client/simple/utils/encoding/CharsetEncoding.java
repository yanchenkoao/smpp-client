package net.smpp.client.simple.utils.encoding;

public interface CharsetEncoding {

	byte[] convertStringToBytes(String text);

	String convertBytesToString(byte[] bytes);

	boolean isLatinEncoding(String text);
}
