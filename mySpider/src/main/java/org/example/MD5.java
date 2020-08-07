package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(byte[] source) {
        String s = null;
        //用来将字节转换成十六进制表示的字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            //MD5 的计算结果是一个 128 位的长整数，字节表示就是 16 个字节
            byte[] tmp = md.digest();
            //每个字节用 16 进制表示，需要两个字符
            char[] str = new char[16 * 2];      //???????????
            int k = 0;     //表示转换结果对应的字符位置
            for(int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                //取字节中高 4 位的数字转换.....>>>为逻辑右移，前面填充0
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                //取字节中低 4 位的数字转换
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }
}
