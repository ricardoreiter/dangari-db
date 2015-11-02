package database.storage;

import java.io.UnsupportedEncodingException;

public class DataUtils {

	public static final int STR_BYTES = 3;

	public static String readString(byte[] buffer, int offset, int length) {
		byte[] strBuffer = new byte[length];

		for (int i = offset, index = 0; index < length; i++, index++) {
			strBuffer[index] = buffer[i];
		}

		String str = null;
		try {
			str = new String(strBuffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return str.trim();
	}

	public static int readInt(byte[] buffer, int offset) {
		return buffer[3 + offset] & 0xFF | (buffer[2 + offset] & 0xFF) << 8 | (buffer[1 + offset] & 0xFF) << 16
				| (buffer[0 + offset] & 0xFF) << 24;
	}

	public static void writeInt(int value, byte[] buffer, int offset) {
		buffer[offset] = (byte) (value >> 24);
		buffer[offset + 1] = (byte) (value >> 16);
		buffer[offset + 2] = (byte) (value >> 8);
		buffer[offset + 3] = (byte) value;
	}

	public static void writeString(String value, int size, byte[] buffer, int offset) {
		byte[] strBuffer = getUTFBuffer(value);

		for (int index = offset, i = 0; i < size; index++, i++) {
			buffer[index] = i < strBuffer.length ? strBuffer[i] : 0x0;
		}
	}

	private static byte[] getUTFBuffer(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] toByteArray(int value) {
		return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}

	public static byte[] toByteArray(String value) {
		int bufferSize = value.length() * STR_BYTES;
		byte[] buffer = new byte[bufferSize];

		byte[] strBuffer = getUTFBuffer(value);

		for (int i = 0; i < strBuffer.length; i++) {
			buffer[i] = strBuffer[i];
		}
		return buffer;
	}

	public static byte[] join(byte[]... arrays) {
		int size = 0;

		for (byte[] array : arrays) {
			size += array.length;
		}

		byte[] buffer = new byte[size];

		int index = 0;
		for (byte[] a : arrays) {
			for (int i = 0; i < a.length; i++) {
				buffer[index] = a[i];
				++index;
			}
		}

		return buffer;
	}

}
