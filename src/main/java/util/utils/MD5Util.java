package util.utils;


import java.security.MessageDigest;


/**
 *      用于实现cookie自动登录
 */

public class MD5Util {
    public static String encode(String s, String algorithm){

        byte[] unencodedPassword = s.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            return s;
        }
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (byte b : encodedPassword) {
            if ((b & 0xFF) < 16) {
                buf.append("0");
            }
            buf.append(Long.toString(b & 0xFF, 16));
        }
        return buf.toString();
    }
}

