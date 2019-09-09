package jdkUtils.data;

/**
 * 字节转换工具类
 *
 * @author dragon
 */
public class AtomicUtils {
    /**
     * int 型转 byte 数组
     *
     * @param value 被转换 int
     * @return 4bytes
     * @throws ArrayIndexOutOfBoundsException 如果下标越界
     */
    public static byte[] intToBytes(int value) throws ArrayIndexOutOfBoundsException {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * int 型转 byte 逆向数组
     *
     * @param value 被转换 int
     * @return 4 bytes
     * @throws ArrayIndexOutOfBoundsException 如果下标越界
     */
    public static byte[] intToBytes2(int value) throws ArrayIndexOutOfBoundsException {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * bytes 转 int 型
     *
     * @param src    源数组
     * @param offset 偏移量
     * @return int 型
     * @throws ArrayIndexOutOfBoundsException 数组异常
     */
    public static int bytesToInt(byte[] src, int offset) throws ArrayIndexOutOfBoundsException {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * 逆向bytes 转 int 型
     *
     * @param src    源数组
     * @param offset 偏移量
     * @return int 型
     * @throws ArrayIndexOutOfBoundsException 数组异常
     */
    public static int bytesToInt2(byte[] src, int offset) throws ArrayIndexOutOfBoundsException {
        int value;
        value = ((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF);
        return value;
    }

    /**
     * <p>long 型 转 byte 数组</p>
     *
     * @param value 数据
     * @return byte array
     * @throws ArrayIndexOutOfBoundsException 数组异常
     */
    public static byte[] longToBytes(long value) throws ArrayIndexOutOfBoundsException {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((value >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long bytesToLong(byte[] src, int offset) throws ArrayIndexOutOfBoundsException {
        long num = 0;
        for (int ix = offset; ix < offset + 8; ++ix) {
            num <<= 8;
            num |= (src[ix] & 0xff);
        }
        return num;
    }

    /**
     * <p> 将16位的short转换成byte数组</p>
     *
     * @param s 原始数据
     * @return 转换后的数组
     */
    public static byte[] shortToBytes(short s) {
        int temp = s;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }

    /**
     * <p> 将byte数组1转换成6位的short</p>
     *
     * @param b      原始数组
     * @param offset 偏移量
     * @return 转换后的数据
     */
    public static short byteToShort(byte[] b, int offset) {
        short s;
        short s0 = (short) (b[offset] & 0xff);// 最低位
        short s1 = (short) (b[offset + 1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }


}