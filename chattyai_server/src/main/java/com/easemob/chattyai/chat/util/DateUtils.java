package com.easemob.chattyai.chat.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.util
 * @Author: alonecoder
 * @CreateTime: 2023-11-30  19:56
 * @Description: 日期工具类
 * @Version: 1.0
 */
public class DateUtils {

    private DateUtils(){}


    /**
     * 获取当前时间段是早上还是中午还是晚上
     * 0:00 - 9:59   早上
     * 10:00 - 13:59 中午
     * 14:00 - 18:59 下午
     * 17:00 - 23:59 晚上
     * @return 0 代表早上 1 代表中午 2 代表下午 3 代表晚上
     */
    public static int getCurrentTimePeriod(){
        int hour = DateUtil.hour(new Date(), true);
        if(hour < 10){
            return 0;
        }else if(hour < 14){
            return 1;
        }else if(hour < 19){
            return 2;
        }else{
            return 3;
        }
    }



}
