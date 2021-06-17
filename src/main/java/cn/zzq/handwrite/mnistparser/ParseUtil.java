package cn.zzq.handwrite.mnistparser;

public class ParseUtil {
    /**
     * 大端字节序解析32位无符号整数
     * @param bs 字节序列
     * @return 32位无符号整数
     */
    public static long parseU32bit(byte... bs){
        long ret = 0;
        for (byte b : bs) {
            ret = ret << 8 | Byte.toUnsignedInt(b);
        }
        return ret;
    }
}
