package net.smpp.client.simple.enums;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Getter
public enum LatinEncodingType {
	GSM_0338((byte) 0),
	LATIN_ISO8859_1((byte) 1);

	private static final Map<Byte, LatinEncodingType> TYPES = Stream.of(values()).collect(toMap(LatinEncodingType::getCode, Function.identity()));

	private byte code;

	public static LatinEncodingType getTypeByCode(Byte code) {
		return TYPES.getOrDefault(code, GSM_0338);
	}

	LatinEncodingType(byte code) {
		this.code = code;
	}
}
