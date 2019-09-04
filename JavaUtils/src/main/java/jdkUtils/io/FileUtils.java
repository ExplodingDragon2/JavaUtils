package jdkUtils.io;

import jdkUtils.data.StringUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.pow;

public class FileUtils {
	final public static String UTF_8 = "UTF-8";
	final public static String GBK = "GBK";
	final public static String ISO_8859_1 = "ISO-8859-1";
	final public static String GB2312 = "GB2312";
	final public static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * <p>
	 * 读取文件中所有字符
	 * </p>
	 * 
	 * @param file 文件地址
	 * @return 返回文件所有UTF-8编码后的字符串，如果不可读或者其他异常返回 null
	 */
	public static String fileToString(File file)  {
		return fileToString(file, UTF_8);
	}

	/**
	 * <p>
	 * 以特定编码读取文件中所有字符
	 * </p>
	 * 
	 * @param file     文件地址
	 * @param encoding 编码
	 * @return 返回文件所有 <b style="color:red;">特定编码</b> 后的字符串，如果不可读或者其他异常返回 null
	 */
	public static String fileToString(File file, Charset encoding) {
		if (!file.isFile())
			return null;
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			return StringUtils.readInputStream(fileInputStream, encoding);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * <p>将数据写入到文件</p>
	 * @param file 输出的位置
	 * @param data 数据源
	 * @param coding 编码方式
	 * @return 是否成功
	 */
	public static boolean write(File file, String data, String coding) {
		if (file.isDirectory() || null == data)
			return false;
		boolean result;
		FileOutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
			outputStream = new FileOutputStream(file);
			outputStreamWriter = new OutputStreamWriter(outputStream, coding);
			outputStreamWriter.write(data);
			outputStreamWriter.flush();
			outputStream.flush();
			result = true;
		} catch (Exception e) {
			result = false;
		} finally {
			try {
				outputStreamWriter.close();
				outputStream.close();
			} catch (Exception e) {
			}
		}

		return result;
	}

	/**
	 <p>将数据写入到文件</p>
	 * @param file 输出的位置
	 * @param data 数据源
	 * @return 是否成功
	 */
	public static boolean write(File file, String data) {
		return write(file, data, UTF_8);
	}

	/**
	 * <p>得到未知字符的编码方式 </p>
	 * @param str 未知字符
	 * @return 编码方式
	 */
	@Deprecated
	public static String getEncoding(String str) {
		try {
			if (str.equals(new String(str.getBytes(GBK), GBK))) {
				return GBK;
			} else if (str.equals(new String(str.getBytes(GB2312), GB2312))) {
				return GB2312;
			}else if (str.equals(new String(str.getBytes(ISO_8859_1), ISO_8859_1))) {
				return ISO_8859_1;
			}else if(str.equals(new String(str.getBytes(UTF_8), UTF_8))) {
				return UTF_8;
			}
		} catch (Exception exception) {
		}

		return "other";
	}

	/**
	 * <p>得到文件 SHA-1 校验码</p>
	 * @param file 文件位置
	 * @return SHA-1 校验码 或者 null
	 */
	public static String getFileSha1(File file) {
		return fileCalculation(file, "SHA-1");
	}

	/**
	 * <p>得到文件 MD5 校验码</p>
	 * @param file 文件位置
	 * @return MD5 校验码 或者 null
	 */
	public static String getFileMd5(File file) {
		return fileCalculation(file, "MD5");
	}
	/**
	 * <p>递归删除文件(夹)</p>
	 * @param file 指定的文件夹 或者 文件
	 * @return 是否删除成功
	 */
	public static boolean delete(File file) {
		if (file == null)
			return false;
		if (file.isFile()) {
			return file.delete();
		} else {
			try {
				if (!file.delete()) {
					for (File childrenFile : file.listFiles()) {
						if (!delete(childrenFile)) {
							return false;
						}
					}
					return delete(file);
				} else {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * <p>复制文件</p>
	 * @param getFile 原始文件
	 * @param putFile 目标文件
	 * @return 是否成功
	 */
	public static boolean copyFile(File getFile, File putFile) {
		if (getFile.isDirectory() || putFile.isDirectory() || !getFile.isFile())
			return false;
		boolean result = false;
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;
		try {
			fileIn = new FileInputStream(getFile);
			fileOut = new FileOutputStream(putFile);
			byte data[] = new byte[4096];
			int dataLen = 0;
			while (-1 != (dataLen = fileIn.read(data, 0, data.length))) {
				fileOut.write(data, 0, dataLen);
				fileOut.flush();
			}
			result = true;

		} catch (Exception e) {
		} finally {
			try {
				fileIn.close();
				fileOut.close();
			} catch (Exception e) {
			}
		}
		return result;

	}

	/**
	 * <p>将文件剪切到指定位置[ 复制 + 删除 ] </p>
	 * @param getFile 原始文件
	 * @param putFile 目标文件
	 * @return 是否成功
	 */
	public static boolean moveFile(File getFile, File putFile) {
		if (getFile.isDirectory() || putFile.isDirectory() || !getFile.isFile())
			return false;
		if (copyFile(getFile, putFile))
			if (getFile.delete()) {
				return true;
			}
		putFile.delete();
		return false;

	}
	/**
	 * <p>将文件大小转换为可视化文本（</p>
	 * @param updateSize 文件大小（单位：B）
	 * @param i 小数点位数
	 * @return 格式化的字符串
	 */
	public static String bytesToString(double updateSize, int i) {
		final double [] maxByte = {1,pow(2,10),pow(2,20),pow(2,30),pow(2,40),pow(2,50),pow(2,60),pow(2,70),pow(2,80),pow(2,90),pow(2,100),pow(2,110),pow(2,120)};
		final String [] byteUnits = {"B","KB","MB","GB","TB","PB","EB","ZB","YB","BB","NB","DB","CB"};
		int unitPosition = 0;
		for (int position = 0; position < maxByte.length; position++) {
			if(updateSize < maxByte[position]){
				break;
			}
			unitPosition = position;
		}
		double decimal = pow(10, i);
		long postConversionData = (long) ((updateSize / maxByte[unitPosition]) * decimal);
		double endSize = postConversionData/decimal;
		return endSize + byteUnits[unitPosition];
	}

    /**
     * <p>对File 的 List进行排序</p>
     * @param fileList 原始数据
     */
    public static void sort(List<File> fileList){
        Collections.sort(fileList, (o1, o2) -> {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            } else if (o1.isFile() && o2.isDirectory()) {
                return 1;
            } else {
                String name1 = o1.getName();
                String name2 = o2.getName();
                if (name1.equals(name2))
                    return 0;
                char[] char1 = name1.toCharArray();
                char[] char2 = name2.toCharArray();
                int len = char1.length > char2.length ? char2.length : char1.length;
                if (Character.isUpperCase(char1[0]) && !Character.isUpperCase(char2[0])) {
                    return -1;
                } else if (!Character.isUpperCase(char1[0]) && Character.isUpperCase(char2[0])) {
                    return 1;
                }
                for (int i = 0; i < len; i++) {
                    int c1 = getGBCode(char1[i]);
                    int c2 = getGBCode(char2[i]);
                    return c1 - c2;
                }
                return char1.length - char2.length;
            }
        });
    }

    /**
     * <p>计算单一char 字符的GBK位置</p>
     * @param c 字符
     * @return 顺序
     */
    public static int getGBCode(char c) {
        byte[] bytes = null;
        try {
            bytes = new StringBuffer().append(c).toString().getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
        }
        if (bytes.length == 1) {
            return bytes[0];
        }
        int a = bytes[0] - 0xA0 + 256;
        int b = bytes[1] - 0xA0 + 256;

        return a * 100 + b;
    }


	private static String fileCalculation(File file, String s) {
		String result = null;
		if (!file.isFile())
			return null;
		FileInputStream inputStream = null;
		FileChannel channel = null;
		MappedByteBuffer byteBuffer = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(s);
			inputStream = new FileInputStream(file);
			channel = inputStream.getChannel();
			byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			messageDigest.update(byteBuffer);
			result = bufferToHex(messageDigest.digest());

		} catch (Exception e) {
			result = null;
		} finally {
			try {
				byteBuffer.clear();
				channel.close();
				inputStream.close();
			} catch (Exception e) {
			}
		}

		return result;
	}
	private static String bufferToHex(byte[] bytes) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte[] bytes, int m, int length) {
		StringBuffer buffer = new StringBuffer(2 * length);
		int k = m + length;
		for (int i = m; i < k; i++) {
			appendHexPair(bytes[i], buffer);
		}
		return buffer.toString();
	}

	private static void appendHexPair(byte b, StringBuffer stringBuffer) {
		char c0 = hexDigits[(b & 0xf0) >> 4], c1 = hexDigits[b & 0xf];
		stringBuffer.append(c0);
		stringBuffer.append(c1);

	}

}