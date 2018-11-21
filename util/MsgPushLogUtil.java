package com.engine.msgcenter.util;

import weaver.conn.RecordSet;
import weaver.systeminfo.SystemEnv;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 * @ Description：消息推送日志处理公共接口
 * @ Modified By：
 * @Version:     1.0
 */

public class MsgPushLogUtil {

    /**
     * @Date        ：Created in 14:00 2018/9/10
     * @Description ：查询提醒接收人
     */
    public static String getReceivers1(String userid, String languageId){

        StringBuilder stringBuilder = new StringBuilder();
        String returnStr = "";
            try {
                RecordSet rs = new RecordSet();
                String result = "";
                String sql = "select top 1 (select pername + ' ' from ( SELECT DISTINCT(pername) FROM split2(?,DEFAULT) ) AS tmp  for xml path('')) as username from ECOLOGY_MESSAGE_LOG";
                rs.executeQuery(sql, userid);
                while (rs.next()) {
                    returnStr = rs.getString("username");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return returnStr;
    }

    /**
     * @Date        ：Created in 14:00 2018/9/10
     * @Description ：查询消息种类
     */
    public static String getTypeName(String MESSAGEID,String languageId){
        RecordSet recordSet = new RecordSet();
        String sql = "select TYPENAME from ECOLOGY_MESSAGE_TYPE where ID=" + MESSAGEID;
        recordSet.execute(sql);
        recordSet.next();
        int typeName = recordSet.getInt("TYPENAME");
        int language = Integer.parseInt(languageId);
        return SystemEnv.getHtmlLabelName(typeName,language);
    }

    /**
     * @Date        ：Created in 14:01 2018/9/10
     * @Description ：查询推送时间
     */
    public static String getPushTime(String transmethod){
        String result = "";
        RecordSet recordSet = new RecordSet();
        String id = transmethod;
        String sql = "select CREATEDATE,CREATETIME from ECOLOGY_MESSAGE_LOG where ID=?";
        try{
            recordSet.executeQuery(sql,id);
            result = result + recordSet.getString("CREATEDATE ") + recordSet.getString("CREATETIME");
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 日期转字符串
     */
    public static String date2string1(Date time){

        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = sdf.format(time);
        return result;
    }

    /**
     * 日期转字符串
     */
    public static String date2string2(Date time){

        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        result = sdf.format(time);
        return result;
    }

    /**
     * 字符串转日期
     */
    public static Date string2date(String time){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            date = sdf.parse(time);
        }catch(Exception e){
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取现在时间
     */
    public static String getNowTime() {
        Date currentTime = new Date();
        String cTime = date2string1(currentTime);
        String [] strs = cTime.split(" ");
        String result = strs[1];
        return result;
    }

    /**
     * 获取现在时间
     */
    public static String getNowDate() {
        Date currentTime = new Date();
        String result = date2string2(currentTime);
        return result;
    }

    /**
     * 获取本周周一
     */
    public static String getMonday() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_WEEK, 2);// 老外将周日定位第一天，周一取第二天
        return date2string2(cDay.getTime());
    }

    /**
     * 获取本周周日
     */
    public static String getSunday() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        if (Calendar.DAY_OF_WEEK == cDay.getFirstDayOfWeek()) { // 如果刚好是周日，直接返回
            return date2string2(date);
        } else {// 如果不是周日，加一周计算
            cDay.add(Calendar.DAY_OF_YEAR, 7);
            cDay.set(Calendar.DAY_OF_WEEK, 1);
            return date2string2(cDay.getTime());
        }
    }

    /**
     * 得到上个月第一天的日期
     */
    public static String getFirstDayOfLastMonth() {
        Calendar cDay = Calendar.getInstance();
        cDay.add(Calendar.MONTH,-1);
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        return date2string2(cDay.getTime());
    }

    /**
     * 得到上个月最后一天的日期
     */
    public static String getLastDayOfLastMonth() {
        Calendar cDay = Calendar.getInstance();
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        cDay.add(Calendar.DATE,-1);
        return date2string2(cDay.getTime());
    }

    /**
     * 得到本月第一天的日期
     */
    public static String getFirstDayOfMonth() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        return date2string2(cDay.getTime());
    }

    /**
     * 得到本月最后一天的日期
     */
    public static String getLastDayOfMonth() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, cDay
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        return date2string2(cDay.getTime());
    }

    /**
     * 得到本季度第一天的日期
     */
    public static String getFirstDayOfQuarter() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
            cDay.set(Calendar.MONTH, Calendar.JANUARY);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
            cDay.set(Calendar.MONTH, Calendar.APRIL);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) {
            cDay.set(Calendar.MONTH, Calendar.JULY);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
            cDay.set(Calendar.MONTH, Calendar.OCTOBER);
        }
        cDay.set(Calendar.DAY_OF_MONTH, cDay
                .getActualMinimum(Calendar.DAY_OF_MONTH));
        return date2string2(cDay.getTime());
    }

    /**
     * 得到本季度最后一天的日期
     */
    public static String getLastDayOfQuarter() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
            cDay.set(Calendar.MONTH, Calendar.MARCH);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
            cDay.set(Calendar.MONTH, Calendar.JUNE);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) {
            cDay.set(Calendar.MONTH, Calendar.SEPTEMBER);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
            cDay.set(Calendar.MONTH, Calendar.DECEMBER);
        }
        cDay.set(Calendar.DAY_OF_MONTH, cDay
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        return date2string2(cDay.getTime());
    }

    /**
     * 获取本年的第一天
     */
    public static String getYearDateStart() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int year = cDay.get(Calendar.YEAR);
        return year + "-01-01";
    }

    /**
     * 获取本年的最后一天
     */
    public static String getYearDateEnd() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int year = cDay.get(Calendar.YEAR);
        return year + "-12-31";
    }

    /**
     * 获取去年的第一天
     */
    public static String getLastYearDateStart() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int year = cDay.get(Calendar.YEAR)-1;

        return year + "-01-01";
    }

    /**
     * 获取去年的最后一天
     */
    public static String getLastYearDateEnd() {
        Date date = new Date();
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int year = cDay.get(Calendar.YEAR)-1;
        return year + "-12-31";
    }


}