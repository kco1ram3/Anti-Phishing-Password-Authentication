/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javaxt.utils.string;

/**
 *
 * @author A
 */
public class HmacUtils {

    public static String hmacSha(String crypto, String key, String value) {
        try {
            byte[] keyBytes = null;
            try {
                keyBytes = key.getBytes("UTF-8"); //hexStr2Bytes(key);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(HmacUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "HmacSHA1"); //"RAW"
            hmac.init(macKey);
            return bytesToHex(hmac.doFinal(value.getBytes()));
        } catch (GeneralSecurityException gse) {
            throw new RuntimeException(gse);
        }
    }

    public static String hmacSha1(String key, String value) {
        return hmacSha("HmacSHA1", key, value);
    }

    private static byte[] hexStr2Bytes(String hex) {
		// Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = bArray[i + 1];
        }
        return ret;
    }

    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    static String hmacSha1(string key, string value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
