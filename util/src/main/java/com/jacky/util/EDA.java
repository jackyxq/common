package com.jacky.util;

import android.text.TextUtils;

import com.jacky.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jacky on 2018/10/31.
 *
 * 加密解密算法集合
 */
public @interface EDA {

    class AES {
        private static final String CipherMode = "AES/CFB/NoPadding";//使用CFB加密，需要设置IV
        private static final String AES = "AES";

        public static String encrypt(String content, String password) {
            try {
                Cipher cipher = Cipher.getInstance(CipherMode);
                SecretKeySpec keyspec = getKey(password);
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
                byte[] encrypted = cipher.doFinal(content.getBytes());
//                return android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT);
                return Formatter.transferByte2String(encrypted);
            } catch (Exception e) {
                Logger.e(e);
                return null;
            }
        }

        public static String decrypt(String content, String password) {
            try {
//                byte[] encrypted1 = android.util.Base64.decode(content.getBytes(), android.util.Base64.DEFAULT);
                byte[] encrypted1 = Formatter.transferString2Byte(content);
                Cipher cipher = Cipher.getInstance(CipherMode);
                SecretKeySpec keyspec = getKey(password);
                cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, "UTF-8");
            } catch (Exception e) {
                Logger.e(e);
                return null;
            }
        }

        private static SecretKeySpec getKey(String password) {
            byte[] bs = password.getBytes();
            Logger.i("ori password's length:", bs.length);
            int count = 0;
            if(bs.length <= 16) count = 16 - bs.length;
            else if(bs.length <= 24) count = 24 - bs.length;
            else count = 32 - bs.length;
            if(count == 0) {
                return new SecretKeySpec(bs, AES);
            } else {
                byte[] bss = Arrays.copyOf(bs, count + bs.length);
                return new SecretKeySpec(bss, AES);
            }
        }
    }

    class MD5 {
        /**
         * 获取字符串信息摘要
         *
         * @param string
         * @return 返回经过MD5算法加密后的字符串。如果string为null或空，则返回空字符串
         */
        public static String digest(String string) {
            return TextUtils.isEmpty(string) ? "" : digest(string.getBytes());
        }

        /**
         * 获取字符串信息摘要
         *
         * @param bytes
         * @return 返回经过MD5算法加密后的字符串
         */
        public static String digest(byte[] bytes) {
            if (bytes == null || bytes.length == 0) return "";

            byte[] b = digest_md5(bytes);
            return Formatter.transferByte2String(b);
        }

        /**
         *  获取文件的信息摘要
         * @param file
         * @return
         */
        public static String digest(File file) {
            if(file == null || !file.exists()) return "";

            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
                MessageDigest mdTemp = MessageDigest.getInstance("MD5");
                int i;
                byte[] bytes = new byte[1024];
                while (true) {
                    i = stream.read(bytes);
                    if(i == -1) break;
                    mdTemp.update(bytes, 0, i);
                }
                byte[] b = mdTemp.digest();
                return Formatter.transferByte2String(b);
            } catch (Exception e) {
                Logger.e(e);
                return "";
            } finally {
                if(stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        /**
         * 获取字符串信息摘要
         */
        private static final byte[] digest_md5(byte[] bytes) {
            try {
                MessageDigest mdTemp = MessageDigest.getInstance("MD5");
                mdTemp.update(bytes);
                return mdTemp.digest();
            } catch (NoSuchAlgorithmException e) {
                Logger.e(e);
                return bytes;
            }
        }
    }

    class Formatter {
        private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        public static byte[] transferString2Byte(String string) {
            int size = TextUtils.isEmpty(string) ? 0 : string.length();
            byte[] bytes = new byte[size/2];
            if(size == 0) return bytes;

            char[] chars = string.toCharArray();
            for(int i = 0;i < bytes.length;i++) {
                char c1 = chars[i * 2];
                int i1 = c1 >= 'a' ? (c1 - 'a' + 10) : (c1 - '0');

                c1 = chars[i * 2 + 1];
                int i2 = c1 >= 'a' ? (c1 - 'a' + 10) : (c1 - '0');
                bytes[i] = (byte) ((i1 << 4) + i2);
            }
            return bytes;
        }

        /**
         * 将byte数组转化为十六进制的字符
         *
         * @param bytes
         * @return
         */
        public static final String transferByte2String(byte[] bytes) {
            int k = 0;
            int size = bytes == null ? 0 : bytes.length;
            char str[] = new char[size * 2];

            for (int i = 0; i < size; i++) {
                byte b = bytes[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        }
        /**
         * 获取字符串的哈希值
         *
         * @param string
         * @return
         */
        public static final String getHashcodeString(String string) {
            if (TextUtils.isEmpty(string)) return "";
            long hashcode = 0;
            int size = string.length();
            for (int i = 0; i < size; i++) {
                hashcode = 131 * hashcode + string.charAt(i);
            }
            return Long.toHexString(hashcode);
        }
    }
}
