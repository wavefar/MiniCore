package org.wavefar.lib.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * 数据加密解密工具
 * @author summer
 */
public class EncryptUtils {

	private static final char[] LEGAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	
	/**
	 * 用MD5算法进行加密
	 * 
	 * @param str
	 *            需要加密的字符串
	 * @return MD5加密后的结果
	 */
	public static String encodeMD5(String str) {
		return encode(str, "MD5");
	}

	/**
	 * 用SHA算法进行加密
	 * 
	 * @param str
	 *            需要加密的字符串
	 * @return SHA加密后的结果
	 */
	public static String encodeSHA(String str) {
		return encode(str, "SHA");
	}

	/**
	 * 用base64算法进行加密
	 * @param str 需要加密的字符串
	 * @return base64加密后的结果
	 */
	public static String encodeBase64(String str) {
		return encodeBase64(str.getBytes());
	}


    /**
     * 用base64算法进行加密
     * @param data 字节数组
     * @return
     */
    public static String encodeBase64(byte[] data) {  
        int start = 0;  
        int len = data.length;  
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);  

        int end = len - 3;  
        int i = start;  
        int n = 0;  

        while (i <= end) {  
            int d = ((((int) data[i]) & 0x0ff) << 16)  
                    | ((((int) data[i + 1]) & 0x0ff) << 8)  
                    | (((int) data[i + 2]) & 0x0ff);  

            buf.append(LEGAL_CHARS[(d >> 18) & 63]);
            buf.append(LEGAL_CHARS[(d >> 12) & 63]);
            buf.append(LEGAL_CHARS[(d >> 6) & 63]);
            buf.append(LEGAL_CHARS[d & 63]);

            i += 3;  

            if (n++ >= 14) {  
                n = 0;  
                buf.append(" ");  
            }  
        }  

        if (i == start + len - 2) {  
            int d = ((((int) data[i]) & 0x0ff) << 16)  
                    | ((((int) data[i + 1]) & 255) << 8);  

            buf.append(LEGAL_CHARS[(d >> 18) & 63]);
            buf.append(LEGAL_CHARS[(d >> 12) & 63]);
            buf.append(LEGAL_CHARS[(d >> 6) & 63]);
            buf.append("=");  
        } else if (i == start + len - 1) {  
            int d = (((int) data[i]) & 0x0ff) << 16;  

            buf.append(LEGAL_CHARS[(d >> 18) & 63]);
            buf.append(LEGAL_CHARS[(d >> 12) & 63]);
            buf.append("==");  
        }  

        return buf.toString();  
    }  
    
    /**
     * 用base64算法进行解密
     * @param s 待解码字符串
     * @return 返回字符串
     */
    public static String decodeBase64(String s) {  

        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        try {  
            decode(s, bos);  
        } catch (IOException e) {  
            throw new RuntimeException();  
        }  
        byte[] decodedBytes = bos.toByteArray();  
        try {  
            bos.close();  
            bos = null;  
        } catch (IOException ex) {  
            ex.printStackTrace();
        }  
        return new String(decodedBytes);  
    }  

    private static int decode(char c) {  
        if (c >= 'A' && c <= 'Z') {
            return ((int) c) - 65;
        } else if (c >= 'a' && c <= 'z') {
            return ((int) c) - 97 + 26;
        } else if (c >= '0' && c <= '9') {
            return ((int) c) - 48 + 26 + 26;
        } else {
            switch (c) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
            default:
                throw new RuntimeException("unexpected code: " + c);
            }
        }
    }  


    private static void decode(String s, OutputStream os) throws IOException {  
        int i = 0;  

        int len = s.length();  

        while (true) {  
            while (i < len && s.charAt(i) <= ' ') {
                i++;
            }

            if (i == len) {
                break;
            }

            int tri = (decode(s.charAt(i)) << 18)  
                    + (decode(s.charAt(i + 1)) << 12)  
                    + (decode(s.charAt(i + 2)) << 6)  
                    + (decode(s.charAt(i + 3)));  

            os.write((tri >> 16) & 255);  
            if (s.charAt(i + 2) == '=') {
                break;
            }
            os.write((tri >> 8) & 255);  
            if (s.charAt(i + 3) == '=') {
                break;
            }
            os.write(tri & 255);  

            i += 4;  
        }  
    } 
	
	
	/**
	 * 加密方式选择
	 * @param str 待加密字符串
	 * @param method MD5\SHA
	 * @return
	 */
	private static String encode(String str, String method) {
		MessageDigest mdInst = null;
		// 把密文转换成十六进制的字符串形式
		StringBuffer sb = new StringBuffer();
		try {
			// 获得MD5\SHA摘要算法的 MessageDigest对象
			mdInst = MessageDigest.getInstance(method);
			// 使用指定的字节更新摘要
			mdInst.update(str.getBytes());
			// 获得密文
			byte[] md = mdInst.digest();
			for (int i = 0; i < md.length; i++) {
				int tmp = md[i];
				if (tmp < 0) {
					tmp += 256;
				}
				if (tmp < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(tmp));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	
	

	/**
	 * DES算法，加密
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @param data
	 *            待加密字符串
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws InvalidAlgorithmParameterException
	 * @throws Exception
	 */
	public static String encodeDES(String key, String data) {
		if (data == null) {
            return null;
        }
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data.getBytes());
			return byte2hex(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * DES算法，解密
	 * 
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @param data
	 *            待解密字符串
	 * @return 解密后的字节数组
	 * @throws Exception
	 *             异常
	 */
	public static String decodeDES(String key, String data) {
		if (data == null) {
            return null;
        }
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return new String(cipher.doFinal(hex2byte(data.getBytes())));
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * 二行制转十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		if(b==null) {
            return "";
        }
		int length = b.length;
		for (int n = 0; b != null && n < length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
                hs.append('0');
            }
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

	/**
	 * 十六进制转二进制
	 * @param b
	 * @return
	 */
	private static byte[] hex2byte(byte[] b) {
		int length = b.length;
		if ((length % 2) != 0) {
            throw new IllegalArgumentException();
        }
		byte[] b2 = new byte[length / 2];
		for (int n = 0; n < length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
}
