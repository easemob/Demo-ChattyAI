package com.easemob.chattyai.chat.util;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 常用工具类
 *
 * @author God
 */
public class CommonUtil {

    private CommonUtil() {
    }


    /**
     * 从request里获取ip地址
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        // "***.***.***.***".length()
        if (ipAddress != null && ipAddress.length() > 15) {
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


    /**
     * 获取一个uuid
     *
     * @return
     */
    @Deprecated
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 获取一个雪花id
     *
     * @return String
     */
    public static String getSnowflakeId() {
        SnowflakeIdWorker idWorker = SnowflakeIdWorker.getSnowflakerIdWorker();
        return Long.valueOf(idWorker.nextId()).toString();
    }

    /**
     * @description: 获取一个雪花id Long类型
     * @author: alonecoder
     * @date: 2023/5/5 0:22
     * @return: Long
     **/
    public static Long getSnowflakeIdLong() {
        return SnowflakeIdWorker.getSnowflakerIdWorker().nextId();
    }


    /**
     * 生成8位不重复的一个标识
     * 伪随机，因为有一定几率重复，重复的概率比较小而已
     *
     * @return
     */
    public static String getOcode() {
        return getSnowflakeId().substring(0, 8);
    }


    /**
     * 获取当前时间
     * 默认格式为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNow() {
        return getNow("");
    }


    /**
     * 获取date时间格式化成patten的String对象
     *
     * @param date
     * @param patten
     * @return
     */
    public static String getDate(Date date, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        return sdf.format(date);
    }

    /**
     * 获取当前时间格式化成patten的String对象
     *
     * @param patten
     * @return
     */
    public static String getNow(String patten) {
        if (StringUtils.isBlank(patten)) {
            patten = "yyyy-MM-dd HH:mm:ss";
        }
        return getDate(new Date(), patten);
    }

    /**
     * 返回参数的MD5值
     *
     * @param str
     * @return
     */
    public static String MD5(String str) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        return md5.digestHex(str);
    }

    /**
     * 设置百分数精确度2即保留两位小数
     *
     * @param a
     * @param b
     * @return
     */
    public static String percent(float a, float b) {
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(0);
        return nt.format(a / b);
    }


    /**
     * 把list<String>解析为一个字符串
     *
     * @param args
     * @return
     */
    public static String getStrForList(List<String> args) {
        if (args == null || args.size() == 0) {
            return "-";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i));
            if (i < (args.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }


    /**
     * 去掉html标签
     *
     * @param content
     * @return
     */
    public static String delHtmlTag(String content) {
        if (StringUtils.isBlank(content)) {
            return "这里什么都没有啊~";
        }
        return content.replaceAll("\\&[a-zA-Z]{1,10};", "")
                .replaceAll("<[^>]*>", "")
                .replaceAll("[(/>)<]", "");
    }


    public static String getLocalHostAddress() {
        try {
            ApplicationContext applicationContext = SpringUtils.getApplicationContext();
            Environment env = applicationContext.getEnvironment();
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String path = env.getProperty("server.servlet.context-path");
            return "http://" + ip + ":" + port + (StrUtil.isBlank(path)?"":path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
