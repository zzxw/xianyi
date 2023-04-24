package com.tencent.wxcloudrun.util;

import com.alibaba.fastjson.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;

public class Util {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    public static String generateRandomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String getTimeStamp() {
        long timeStamp = System.currentTimeMillis() / 1000l;
        return String.valueOf(timeStamp);
    }

    public static String getTimeStamp(String time) {
        ZonedDateTime dateTime = ZonedDateTime.parse(time);
        return String.valueOf(dateTime.toInstant().toEpochMilli());
    }

    public static double getRealFee(int num1, int num2) {
        BigDecimal b1 = new BigDecimal(num1);
        BigDecimal b2 = new BigDecimal(num2);
        BigDecimal b3 = b1.divide(b2, 2, RoundingMode.HALF_UP);
        return b3.doubleValue();
    }

    public static double getDefaultRealFee(int num1) {
        return getRealFee(num1,100);
    }


    public static String decode(String notifyData)  {
        Cipher cipher = null;
        String str = "";
        try{
            JSONObject result = JSONObject.parseObject(notifyData);
            JSONObject resource = result.getJSONObject("resource");
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            String key = "yZwjC7OH9DK3jQzOAf9D3RwHqwfoGmDz";
            String associatedData = resource.getString("associated_data");
            String nonce = resource.getString("nonce");
            String content = resource.getString("ciphertext");

            byte[] decode = Base64.getDecoder().decode(content);

            cipher.init(2, new SecretKeySpec(key.getBytes(), "AES"), new GCMParameterSpec(128, nonce.getBytes()));
            if (associatedData != null && associatedData.trim().length() > 0) {
                cipher.updateAAD(associatedData.getBytes());
            }
            str = new String(cipher.doFinal(decode), StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }


        return str;
    }
}
