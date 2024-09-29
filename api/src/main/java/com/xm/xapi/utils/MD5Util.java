package com.xm.xapi.utils;

import java.security.MessageDigest;

/**
 * md5 加密封装接口.
 */

public final class MD5Util {


    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    /**
     * 并  的数值.
     */
    private static final int SMALL = 0x000000FF;

    /**
     * 或  的数值.
     */
    private static final int BIG = 0xFFFFFF00;

    /**
     * 截取字符串的长度.
     */
    private static final int SUB_LENGTH = 6;


    private MD5Util() {
    }

    /**
     * md5 加密接口.
     *
     * @param password 待加密的数据
     * @return 加密后的数据.
     */
    public static String encrypt(final String password) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] s = m.digest(password.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : s) {
                result.append(Integer.toHexString(
                        (SMALL & b) | BIG).substring(SUB_LENGTH));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }


}
