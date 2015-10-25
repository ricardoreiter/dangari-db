package database.storage;

import java.io.UnsupportedEncodingException;

public class DataUtils {
	
	private static final int STR_BYTES = 3;

	public static String readString(byte[] buffer, int offset, int length) {
		int strSize = length * 3;

		byte[] strArray = new byte[strSize];

		for (int i = offset, index = 0; index < strSize; i++, index++) {
			strArray[index] = buffer[i];
		}

		String str = null;
		try {
			str = new String(strArray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return str;
	}

	public static int readInt(byte[] buffer, int offset) {
		return buffer[3 + offset] & 0xFF | 
			   (buffer[2 + offset] & 0xFF) << 8 | 
			   (buffer[1 + offset] & 0xFF) << 16 | 
			   (buffer[0 + offset] & 0xFF) << 24;
	}

	public static void writeInt(int value, byte[] buffer, int offset) {
		buffer[offset] = (byte) (value >> 24);
		buffer[offset + 1] = (byte) (value >> 16);
		buffer[offset + 2] = (byte) (value >> 8);
		buffer[offset + 3] = (byte) value;
	}
	
	public static void writeString(String value, int length, byte[] buffer, int offset) {
		int sizeInBytes = length * STR_BYTES;

		byte[] strBuffer = null;
		try {
			strBuffer = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		for (int index = offset, i = 0; i < sizeInBytes; index++, i++) {
			buffer[index] = i < strBuffer.length ? strBuffer[i] : 0x0; 
		}
	}
	
	public static byte[] toByteArray(int value) {
		return new byte[] { (byte) (value >> 24), 
							(byte) (value >> 16), 
							(byte) (value >> 8), 
							(byte) value };
	}
	
	public static byte[] toByteArray(String value) {
		int bufferSize = value.length() * STR_BYTES;
		byte[] buffer = new byte[bufferSize];

		byte[] strBuffer = null;
		try {
			strBuffer = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

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
