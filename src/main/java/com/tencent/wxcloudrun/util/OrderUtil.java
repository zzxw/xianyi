package com.tencent.wxcloudrun.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class OrderUtil {
    public static String getUUID() {
        String replaceUUID = UUID.randomUUID().toString().replace("-", "");

        return replaceUUID;
    }

    public static String getIDByTime() {
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String localDate = LocalDateTime.now().format(ofPattern);
        //随机数
        //String randomNumeric = RandomStringUtils.randomNumeric(8);
        Random random = new Random();
        int randomNum = random.nextInt(10000000) + 1;
        String uuid = localDate + randomNum;
        return uuid;
    }

    public static void main(String[] args) {
        String uuid = getUUID();
        System.out.println(uuid.length());;
        System.out.println(uuid);;

        String idByTime = getIDByTime();
        System.out.println(idByTime.length());
        System.out.println(idByTime);
    }
}
