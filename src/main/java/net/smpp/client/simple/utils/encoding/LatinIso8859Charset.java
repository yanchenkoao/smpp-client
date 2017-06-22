
package net.smpp.client.simple.utils.encoding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * brief Provides services to translate UNICODE into ISO8859-1 character set and vice versa
 * http://www.unicode.org/Public/MAPPINGS/ISO8859/8859-1.TXT
 */
public final class LatinIso8859Charset implements CharsetEncoding {

	private static final Map<Byte, Character> BYTE_TO_UNICODE_MAP;

	private static final Map<Character, Byte> UNICODE_TO_BYTE_MAP = Collections.unmodifiableMap(new HashMap<Character, Byte>() {
		{
			put((char) 0x0000, (byte) 0x00); //	0	NULL
			put((char) 0x0001, (byte) 0x01); //	1	START OF HEADING
			put((char) 0x0002, (byte) 0x02); //	2	START OF TEXT
			put((char) 0x0003, (byte) 0x03); //	3	END OF TEXT
			put((char) 0x0004, (byte) 0x04); //	4	END OF TRANSMISSION
			put((char) 0x0005, (byte) 0x05); //	5	ENQUIRY
			put((char) 0x0006, (byte) 0x06); //	6	ACKNOWLEDGE
			put((char) 0x0007, (byte) 0x07); //	7	BELL
			put((char) 0x0008, (byte) 0x08); //	8	BACKSPACE
			put((char) 0x0009, (byte) 0x09); //	9	HORIZONTAL TABULATION
			put((char) 0x000A, (byte) 0x0A); //	10	\n	LINE FEED
			put((char) 0x000B, (byte) 0x0B); //	11	VERTICAL TABULATION
			put((char) 0x000C, (byte) 0x0C); //	12	FORM FEED
			put((char) 0x000D, (byte) 0x0D); //	13	\r	CARRIAGE RETURN
			put((char) 0x000E, (byte) 0x0E); //	14	SHIFT OUT
			put((char) 0x000F, (byte) 0x0F); //	15	SHIFT IN
			put((char) 0x0010, (byte) 0x10); //	16	DATA LINK ESCAPE
			put((char) 0x0011, (byte) 0x11); //	17	DEVICE CONTROL ONE
			put((char) 0x0012, (byte) 0x12); //	18	DEVICE CONTROL TWO
			put((char) 0x0013, (byte) 0x13); //	19	DEVICE CONTROL THREE
			put((char) 0x0014, (byte) 0x14); //	20	DEVICE CONTROL FOUR
			put((char) 0x0015, (byte) 0x15); //	21	NEGATIVE ACKNOWLEDGE
			put((char) 0x0016, (byte) 0x16); //	22	SYNCHRONOUS IDLE
			put((char) 0x0017, (byte) 0x17); //	23	END OF TRANSMISSION BLOCK
			put((char) 0x0018, (byte) 0x18); //	24	CANCEL
			put((char) 0x0019, (byte) 0x19); //	25	END OF MEDIUM
			put((char) 0x001A, (byte) 0x1A); //	26	SUBSTITUTE
			put((char) 0x001B, (byte) 0x1B); //	27	ESCAPE
			put((char) 0x001C, (byte) 0x1C); //	28	FILE SEPARATOR
			put((char) 0x001D, (byte) 0x1D); //	29	GROUP SEPARATOR
			put((char) 0x001E, (byte) 0x1E); //	30	RECORD SEPARATOR
			put((char) 0x001F, (byte) 0x1F); //	31	UNIT SEPARATOR
			put((char) 0x0020, (byte) 0x20); //	32	 	SPACE
			put((char) 0x0021, (byte) 0x21); //	33	!	EXCLAMATION MARK
			put((char) 0x0022, (byte) 0x22); //	34	"	QUOTATION MARK
			put((char) 0x0023, (byte) 0x23); //	35); // NUMBER SIGN
			put((char) 0x0024, (byte) 0x24); //	36	$	DOLLAR SIGN
			put((char) 0x0025, (byte) 0x25); //	37	%	PERCENT SIGN
			put((char) 0x0026, (byte) 0x26); //	38	&	AMPERSAND
			put((char) 0x0027, (byte) 0x27); //	39	'	APOSTROPHE
			put((char) 0x0028, (byte) 0x28); //	40	(	LEFT PARENTHESIS
			put((char) 0x0029, (byte) 0x29); //	41	)	RIGHT PARENTHESIS
			put((char) 0x002A, (byte) 0x2A); //	42	*	ASTERISK
			put((char) 0x002B, (byte) 0x2B); //	43	+	PLUS SIGN
			put((char) 0x002C, (byte) 0x2C); //	44	,	COMMA
			put((char) 0x002D, (byte) 0x2D); //	45	-	HYPHEN-MINUS
			put((char) 0x002E, (byte) 0x2E); //	46	.	FULL STOP
			put((char) 0x002F, (byte) 0x2F); //	47	/	SOLIDUS
			put((char) 0x0030, (byte) 0x30); //	48	0	DIGIT ZERO
			put((char) 0x0031, (byte) 0x31); //	49	1	DIGIT ONE
			put((char) 0x0032, (byte) 0x32); //	50	2	DIGIT TWO
			put((char) 0x0033, (byte) 0x33); //	51	3	DIGIT THREE
			put((char) 0x0034, (byte) 0x34); //	52	4	DIGIT FOUR
			put((char) 0x0035, (byte) 0x35); //	53	5	DIGIT FIVE
			put((char) 0x0036, (byte) 0x36); //	54	6	DIGIT SIX
			put((char) 0x0037, (byte) 0x37); //	55	7	DIGIT SEVEN
			put((char) 0x0038, (byte) 0x38); //	56	8	DIGIT EIGHT
			put((char) 0x0039, (byte) 0x39); //	57	9	DIGIT NINE
			put((char) 0x003A, (byte) 0x3A); //	58	:	COLON
			put((char) 0x003B, (byte) 0x3B); //	59	;	SEMICOLON
			put((char) 0x003C, (byte) 0x3C); //	60	<	LESS-THAN SIGN
			put((char) 0x003D, (byte) 0x3D); //	61	=	EQUALS SIGN
			put((char) 0x003E, (byte) 0x3E); //	62	>	GREATER-THAN SIGN
			put((char) 0x003F, (byte) 0x3F); //	63	?	QUESTION MARK
			put((char) 0x0040, (byte) 0x40); //	64		COMMERCIAL AT
			put((char) 0x0041, (byte) 0x41); //	65	A	LATIN CAPITAL LETTER A
			put((char) 0x0042, (byte) 0x42); //	66	B	LATIN CAPITAL LETTER B
			put((char) 0x0043, (byte) 0x43); //	67	C	LATIN CAPITAL LETTER C
			put((char) 0x0044, (byte) 0x44); //	68	D	LATIN CAPITAL LETTER D
			put((char) 0x0045, (byte) 0x45); //	69	E	LATIN CAPITAL LETTER E
			put((char) 0x0046, (byte) 0x46); //	70	F	LATIN CAPITAL LETTER F
			put((char) 0x0047, (byte) 0x47); //	71	G	LATIN CAPITAL LETTER G
			put((char) 0x0048, (byte) 0x48); //	72	H	LATIN CAPITAL LETTER H
			put((char) 0x0049, (byte) 0x49); //	73	I	LATIN CAPITAL LETTER I
			put((char) 0x004A, (byte) 0x4A); //	74	J	LATIN CAPITAL LETTER J
			put((char) 0x004B, (byte) 0x4B); //	75	K	LATIN CAPITAL LETTER K
			put((char) 0x004C, (byte) 0x4C); //	76	L	LATIN CAPITAL LETTER L
			put((char) 0x004D, (byte) 0x4D); //	77	M	LATIN CAPITAL LETTER M
			put((char) 0x004E, (byte) 0x4E); //	78	N	LATIN CAPITAL LETTER N
			put((char) 0x004F, (byte) 0x4F); //	79	O	LATIN CAPITAL LETTER O
			put((char) 0x0050, (byte) 0x50); //	80	P	LATIN CAPITAL LETTER P
			put((char) 0x0051, (byte) 0x51); //	81	Q	LATIN CAPITAL LETTER Q
			put((char) 0x0052, (byte) 0x52); //	82	R	LATIN CAPITAL LETTER R
			put((char) 0x0053, (byte) 0x53); //	83	S	LATIN CAPITAL LETTER S
			put((char) 0x0054, (byte) 0x54); //	84	T	LATIN CAPITAL LETTER T
			put((char) 0x0055, (byte) 0x55); //	85	U	LATIN CAPITAL LETTER U
			put((char) 0x0056, (byte) 0x56); //	86	V	LATIN CAPITAL LETTER V
			put((char) 0x0057, (byte) 0x57); //	87	W	LATIN CAPITAL LETTER W
			put((char) 0x0058, (byte) 0x58); //	88	X	LATIN CAPITAL LETTER X
			put((char) 0x0059, (byte) 0x59); //	89	Y	LATIN CAPITAL LETTER Y
			put((char) 0x005A, (byte) 0x5A); //	90	Z	LATIN CAPITAL LETTER Z
			put((char) 0x005B, (byte) 0x5B); //	91		LEFT SQUARE BRACKET
			put((char) 0x005C, (byte) 0x5C); //	92		REVERSE SOLIDUS
			put((char) 0x005D, (byte) 0x5D); //	93		RIGHT SQUARE BRACKET
			put((char) 0x005E, (byte) 0x5E); //	94		CIRCUMFLEX ACCENT
			put((char) 0x005F, (byte) 0x5F); //	95		LOW LINE
			put((char) 0x0060, (byte) 0x60); //	96		GRAVE ACCENT
			put((char) 0x0061, (byte) 0x61); //	97	a	LATIN SMALL LETTER A
			put((char) 0x0062, (byte) 0x62); //	98	b	LATIN SMALL LETTER B
			put((char) 0x0063, (byte) 0x63); //	99	c	LATIN SMALL LETTER C
			put((char) 0x0064, (byte) 0x64); //	100	d	LATIN SMALL LETTER D
			put((char) 0x0065, (byte) 0x65); //	101	e	LATIN SMALL LETTER E
			put((char) 0x0066, (byte) 0x66); //	102	f	LATIN SMALL LETTER F
			put((char) 0x0067, (byte) 0x67); //	103	g	LATIN SMALL LETTER G
			put((char) 0x0068, (byte) 0x68); //	104	h	LATIN SMALL LETTER H
			put((char) 0x0069, (byte) 0x69); //	105	i	LATIN SMALL LETTER I
			put((char) 0x006A, (byte) 0x6A); //	106	j	LATIN SMALL LETTER J
			put((char) 0x006B, (byte) 0x6B); //	107	k	LATIN SMALL LETTER K
			put((char) 0x006C, (byte) 0x6C); //	108	l	LATIN SMALL LETTER L
			put((char) 0x006D, (byte) 0x6D); //	109	m	LATIN SMALL LETTER M
			put((char) 0x006E, (byte) 0x6E); //	110	n	LATIN SMALL LETTER N
			put((char) 0x006F, (byte) 0x6F); //	111	o	LATIN SMALL LETTER O
			put((char) 0x0070, (byte) 0x70); //	112	p	LATIN SMALL LETTER P
			put((char) 0x0071, (byte) 0x71); //	113	q	LATIN SMALL LETTER Q
			put((char) 0x0072, (byte) 0x72); //	114	r	LATIN SMALL LETTER R
			put((char) 0x0073, (byte) 0x73); //	115	s	LATIN SMALL LETTER S
			put((char) 0x0074, (byte) 0x74); //	116	t	LATIN SMALL LETTER T
			put((char) 0x0075, (byte) 0x75); //	117	u	LATIN SMALL LETTER U
			put((char) 0x0076, (byte) 0x76); //	118	v	LATIN SMALL LETTER V
			put((char) 0x0077, (byte) 0x77); //	119	w	LATIN SMALL LETTER W
			put((char) 0x0078, (byte) 0x78); //	120	x	LATIN SMALL LETTER X
			put((char) 0x0079, (byte) 0x79); //	121	y	LATIN SMALL LETTER Y
			put((char) 0x007A, (byte) 0x7A); //	122	z	LATIN SMALL LETTER Z
			put((char) 0x007B, (byte) 0x7B); //	123		LEFT CURLY BRACKET
			put((char) 0x007C, (byte) 0x7C); //	124		VERTICAL LINE
			put((char) 0x007D, (byte) 0x7D); //	125		RIGHT CURLY BRACKET
			put((char) 0x007E, (byte) 0x7E); //	126		TILDE
			put((char) 0x007F, (byte) 0x7F); //	127		DELETE
			put((char) 0x0080, (byte) 0x80); // <control>
			put((char) 0x0081, (byte) 0x81); // <control>
			put((char) 0x0082, (byte) 0x82); // <control>
			put((char) 0x0083, (byte) 0x83); // <control>
			put((char) 0x0084, (byte) 0x84); // <control>
			put((char) 0x0085, (byte) 0x85); // <control>
			put((char) 0x0086, (byte) 0x86); // <control>
			put((char) 0x0087, (byte) 0x87); // <control>
			put((char) 0x0088, (byte) 0x88); // <control>
			put((char) 0x0089, (byte) 0x89); // <control>
			put((char) 0x008A, (byte) 0x8A); // <control>
			put((char) 0x008B, (byte) 0x8B); // <control>
			put((char) 0x008C, (byte) 0x8C); // <control>
			put((char) 0x008D, (byte) 0x8D); // <control>
			put((char) 0x008E, (byte) 0x8E); // <control>
			put((char) 0x008F, (byte) 0x8F); // <control>
			put((char) 0x0090, (byte) 0x90); // <control>
			put((char) 0x0091, (byte) 0x91); // <control>
			put((char) 0x0092, (byte) 0x92); // <control>
			put((char) 0x0093, (byte) 0x93); // <control>
			put((char) 0x0094, (byte) 0x94); // <control>
			put((char) 0x0095, (byte) 0x95); // <control>
			put((char) 0x0096, (byte) 0x96); // <control>
			put((char) 0x0097, (byte) 0x97); // <control>
			put((char) 0x0098, (byte) 0x98); // <control>
			put((char) 0x0099, (byte) 0x99); // <control>
			put((char) 0x009A, (byte) 0x9A); // <control>
			put((char) 0x009B, (byte) 0x9B); // <control>
			put((char) 0x009C, (byte) 0x9C); // <control>
			put((char) 0x009D, (byte) 0x9D); // <control>
			put((char) 0x009E, (byte) 0x9E); // <control>
			put((char) 0x009F, (byte) 0x9F); // <control>
			put((char) 0x00A0, (byte) 0xA0); // NO-BREAK SPACE
			put((char) 0x00A1, (byte) 0xA1); // INVERTED EXCLAMATION MARK
			put((char) 0x00A2, (byte) 0xA2); // CENT SIGN
			put((char) 0x00A3, (byte) 0xA3); // POUND SIGN
			put((char) 0x00A4, (byte) 0xA4); // CURRENCY SIGN
			put((char) 0x00A5, (byte) 0xA5); // YEN SIGN
			put((char) 0x00A6, (byte) 0xA6); // BROKEN BAR
			put((char) 0x00A7, (byte) 0xA7); // SECTION SIGN
			put((char) 0x00A8, (byte) 0xA8); // DIAERESIS
			put((char) 0x00A9, (byte) 0xA9); // COPYRIGHT SIGN
			put((char) 0x00AA, (byte) 0xAA); // FEMININE ORDINAL INDICATOR
			put((char) 0x00AB, (byte) 0xAB); // LEFT-POINTING DOUBLE ANGLE QUOTATION MARK
			put((char) 0x00AC, (byte) 0xAC); // NOT SIGN
			put((char) 0x00AD, (byte) 0xAD); // SOFT HYPHEN
			put((char) 0x00AE, (byte) 0xAE); // REGISTERED SIGN
			put((char) 0x00AF, (byte) 0xAF); // MACRON
			put((char) 0x00B0, (byte) 0xB0); // DEGREE SIGN
			put((char) 0x00B1, (byte) 0xB1); // PLUS-MINUS SIGN
			put((char) 0x00B2, (byte) 0xB2); // SUPERSCRIPT TWO
			put((char) 0x00B3, (byte) 0xB3); // SUPERSCRIPT THREE
			put((char) 0x00B4, (byte) 0xB4); // ACUTE ACCENT
			put((char) 0x00B5, (byte) 0xB5); // MICRO SIGN
			put((char) 0x00B6, (byte) 0xB6); // PILCROW SIGN
			put((char) 0x00B7, (byte) 0xB7); // MIDDLE DOT
			put((char) 0x00B8, (byte) 0xB8); // CEDILLA
			put((char) 0x00B9, (byte) 0xB9); // SUPERSCRIPT ONE
			put((char) 0x00BA, (byte) 0xBA); // MASCULINE ORDINAL INDICATOR
			put((char) 0x00BB, (byte) 0xBB); // RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK
			put((char) 0x00BC, (byte) 0xBC); // VULGAR FRACTION ONE QUARTER
			put((char) 0x00BD, (byte) 0xBD); // VULGAR FRACTION ONE HALF
			put((char) 0x00BE, (byte) 0xBE); // VULGAR FRACTION THREE QUARTERS
			put((char) 0x00BF, (byte) 0xBF); // INVERTED QUESTION MARK
			put((char) 0x00C0, (byte) 0xC0); // LATIN CAPITAL LETTER A WITH GRAVE
			put((char) 0x00C1, (byte) 0xC1); // LATIN CAPITAL LETTER A WITH ACUTE
			put((char) 0x00C2, (byte) 0xC2); // LATIN CAPITAL LETTER A WITH CIRCUMFLEX
			put((char) 0x00C3, (byte) 0xC3); // LATIN CAPITAL LETTER A WITH TILDE
			put((char) 0x00C4, (byte) 0xC4); // LATIN CAPITAL LETTER A WITH DIAERESIS
			put((char) 0x00C5, (byte) 0xC5); // LATIN CAPITAL LETTER A WITH RING ABOVE
			put((char) 0x00C6, (byte) 0xC6); // LATIN CAPITAL LETTER AE
			put((char) 0x00C7, (byte) 0xC7); // LATIN CAPITAL LETTER C WITH CEDILLA
			put((char) 0x00C8, (byte) 0xC8); // LATIN CAPITAL LETTER E WITH GRAVE
			put((char) 0x00C9, (byte) 0xC9); // LATIN CAPITAL LETTER E WITH ACUTE
			put((char) 0x00CA, (byte) 0xCA); // LATIN CAPITAL LETTER E WITH CIRCUMFLEX
			put((char) 0x00CB, (byte) 0xCB); // LATIN CAPITAL LETTER E WITH DIAERESIS
			put((char) 0x00CC, (byte) 0xCC); // LATIN CAPITAL LETTER I WITH GRAVE
			put((char) 0x00CD, (byte) 0xCD); // LATIN CAPITAL LETTER I WITH ACUTE
			put((char) 0x00CE, (byte) 0xCE); // LATIN CAPITAL LETTER I WITH CIRCUMFLEX
			put((char) 0x00CF, (byte) 0xCF); // LATIN CAPITAL LETTER I WITH DIAERESIS
			put((char) 0x00D0, (byte) 0xD0); // LATIN CAPITAL LETTER ETH (Icelandic)
			put((char) 0x00D1, (byte) 0xD1); // LATIN CAPITAL LETTER N WITH TILDE
			put((char) 0x00D2, (byte) 0xD2); // LATIN CAPITAL LETTER O WITH GRAVE
			put((char) 0x00D3, (byte) 0xD3); // LATIN CAPITAL LETTER O WITH ACUTE
			put((char) 0x00D4, (byte) 0xD4); // LATIN CAPITAL LETTER O WITH CIRCUMFLEX
			put((char) 0x00D5, (byte) 0xD5); // LATIN CAPITAL LETTER O WITH TILDE
			put((char) 0x00D6, (byte) 0xD6); // LATIN CAPITAL LETTER O WITH DIAERESIS
			put((char) 0x00D7, (byte) 0xD7); // MULTIPLICATION SIGN
			put((char) 0x00D8, (byte) 0xD8); // LATIN CAPITAL LETTER O WITH STROKE
			put((char) 0x00D9, (byte) 0xD9); // LATIN CAPITAL LETTER U WITH GRAVE
			put((char) 0x00DA, (byte) 0xDA); // LATIN CAPITAL LETTER U WITH ACUTE
			put((char) 0x00DB, (byte) 0xDB); // LATIN CAPITAL LETTER U WITH CIRCUMFLEX
			put((char) 0x00DC, (byte) 0xDC); // LATIN CAPITAL LETTER U WITH DIAERESIS
			put((char) 0x00DD, (byte) 0xDD); // LATIN CAPITAL LETTER Y WITH ACUTE
			put((char) 0x00DE, (byte) 0xDE); // LATIN CAPITAL LETTER THORN (Icelandic)
			put((char) 0x00DF, (byte) 0xDF); // LATIN SMALL LETTER SHARP S (German)
			put((char) 0x00E0, (byte) 0xE0); // LATIN SMALL LETTER A WITH GRAVE
			put((char) 0x00E1, (byte) 0xE1); // LATIN SMALL LETTER A WITH ACUTE
			put((char) 0x00E2, (byte) 0xE2); // LATIN SMALL LETTER A WITH CIRCUMFLEX
			put((char) 0x00E3, (byte) 0xE3); // LATIN SMALL LETTER A WITH TILDE
			put((char) 0x00E4, (byte) 0xE4); // LATIN SMALL LETTER A WITH DIAERESIS
			put((char) 0x00E5, (byte) 0xE5); // LATIN SMALL LETTER A WITH RING ABOVE
			put((char) 0x00E6, (byte) 0xE6); // LATIN SMALL LETTER AE
			put((char) 0x00E7, (byte) 0xE7); // LATIN SMALL LETTER C WITH CEDILLA
			put((char) 0x00E8, (byte) 0xE8); // LATIN SMALL LETTER E WITH GRAVE
			put((char) 0x00E9, (byte) 0xE9); // LATIN SMALL LETTER E WITH ACUTE
			put((char) 0x00EA, (byte) 0xEA); // LATIN SMALL LETTER E WITH CIRCUMFLEX
			put((char) 0x00EB, (byte) 0xEB); // LATIN SMALL LETTER E WITH DIAERESIS
			put((char) 0x00EC, (byte) 0xEC); // LATIN SMALL LETTER I WITH GRAVE
			put((char) 0x00ED, (byte) 0xED); // LATIN SMALL LETTER I WITH ACUTE
			put((char) 0x00EE, (byte) 0xEE); // LATIN SMALL LETTER I WITH CIRCUMFLEX
			put((char) 0x00EF, (byte) 0xEF); // LATIN SMALL LETTER I WITH DIAERESIS
			put((char) 0x00F0, (byte) 0xF0); // LATIN SMALL LETTER ETH (Icelandic)
			put((char) 0x00F1, (byte) 0xF1); // LATIN SMALL LETTER N WITH TILDE
			put((char) 0x00F2, (byte) 0xF2); // LATIN SMALL LETTER O WITH GRAVE
			put((char) 0x00F3, (byte) 0xF3); // LATIN SMALL LETTER O WITH ACUTE
			put((char) 0x00F4, (byte) 0xF4); // LATIN SMALL LETTER O WITH CIRCUMFLEX
			put((char) 0x00F5, (byte) 0xF5); // LATIN SMALL LETTER O WITH TILDE
			put((char) 0x00F6, (byte) 0xF6); // LATIN SMALL LETTER O WITH DIAERESIS
			put((char) 0x00F7, (byte) 0xF7); // DIVISION SIGN
			put((char) 0x00F8, (byte) 0xF8); // LATIN SMALL LETTER O WITH STROKE
			put((char) 0x00F9, (byte) 0xF9); // LATIN SMALL LETTER U WITH GRAVE
			put((char) 0x00FA, (byte) 0xFA); // LATIN SMALL LETTER U WITH ACUTE
			put((char) 0x00FB, (byte) 0xFB); // LATIN SMALL LETTER U WITH CIRCUMFLEX
			put((char) 0x00FC, (byte) 0xFC); // LATIN SMALL LETTER U WITH DIAERESIS
			put((char) 0x00FD, (byte) 0xFD); // LATIN SMALL LETTER Y WITH ACUTE
			put((char) 0x00FE, (byte) 0xFE); // LATIN SMALL LETTER THORN (Icelandic)
			put((char) 0x00FF, (byte) 0xFF); // LATIN SMALL LETTER Y WITH DIAERESIS
		}
	});

	static {
		BYTE_TO_UNICODE_MAP = Collections.unmodifiableMap(
			UNICODE_TO_BYTE_MAP.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey))
		);
	}

	@Override
	public byte[] convertStringToBytes(String text) {
		if (text == null) {
			return new byte[0];
		} else {
			int len = text.length();
			byte[] byteArray = new byte[len];
			for (int i = 0; i < len; i++) {
				char symbol = text.charAt(i);
				Byte currentByte = UNICODE_TO_BYTE_MAP.get(symbol);
				if (currentByte == null) {
					byteArray[i] = (byte) 0x3F; //question mark
				} else {
					byteArray[i] = currentByte;
				}
			}
			return byteArray;
		}
	}

	@Override
	public String convertBytesToString(byte[] bytes) {
		if (bytes == null) {
			return "";
		} else {
			StringBuilder stringBuilder = new StringBuilder(bytes.length + 2);

			for (byte aByte : bytes) {
				Character currentChar = BYTE_TO_UNICODE_MAP.get(aByte);
				if (currentChar == null) {
					stringBuilder.append(BYTE_TO_UNICODE_MAP.get((byte) 0x3F)); //question mark
				} else {
					stringBuilder.append(currentChar);
				}
			}
			return stringBuilder.toString();
		}
	}

	@Override
	public boolean isLatinEncoding(String text) {
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (!UNICODE_TO_BYTE_MAP.containsKey(character)) {
				return false;
			}
		}
		return true;
	}
}
