package com.engine.msgcenter.util;

import com.alibaba.druid.util.StringUtils;
import com.engine.msgcenter.constant.MsgConfigConstant;
import com.weaver.base.msgcenter.constant.WeaChannelType;
import weaver.general.Util;
import weaver.systeminfo.SystemEnv;
import java.util.ArrayList;

/*
 * @ Description：transmethod
 * @ Date       ：Created by mhw in 2018/10/23 17:27
 */
public class MsgTransmethod {

    /**
     * @description ：将状态更改为多语言文本——消息中心
     */
    public static String getStatus(String status,String languageId){
        int labelId = 0;
        String result = "";
        int languageid = Integer.parseInt(languageId);
        if(org.apache.commons.lang.StringUtils.equals(status,"y")){
            labelId = 18095;
        }else if(org.apache.commons.lang.StringUtils.equals(status,"n")){
            labelId = 18096;
        }
        result = SystemEnv.getHtmlLabelName(labelId,languageid);
        return result;
    }

    /**
     * @description ：消息订阅设置——操作的显示隐藏
     */
    public static ArrayList getOpratePopedomWithStatus(String requestid, String status) throws Exception {
        ArrayList returnList = new ArrayList();
        String edit = "true";
        String enable = "false";
        String disable = "false";

        if(org.apache.commons.lang.StringUtils.equals(status,MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE)){
            disable = "true";//启用状态显示禁用
        }else {
            enable = "true";//禁用状态显示启用
        }
        returnList.add(edit);
        returnList.add(enable);
        returnList.add(disable);
        return returnList;
    }

    /**
     * @description ：获取消息数据类型
     */
    public static String getDataType(String datatype, String languageId){
        int labelid = 0;
        if(StringUtils.equals(datatype,"0")){
            labelid = 608;//文本类型
        }else if(StringUtils.equals(datatype,"1")){
            labelid = 388642;//二进制类型
        }
        return SystemEnv.getHtmlLabelName(labelid,Integer.parseInt(languageId));
    }

    /**
     * @description ：获取提醒种类名称
     */
    public static String getRemindType(String typename, String languageId){
        return SystemEnv.getHtmlLabelName(Integer.parseInt(typename),Integer.parseInt(languageId));
    }

    /**
     * 显示接收人
     */
    public String getReceivers(String requestid, String para) {

        String[] tempStr = Util.splitString(para, "+");
        String returnStr = "";
        if(0 < tempStr.length){
            String userLanguage = Util.null2String(tempStr[0]);
            if(2 < tempStr.length){
                String mainsubuserid = Util.null2String(tempStr[2]);
                returnStr = "<div id='"  + requestid + "div'>";
                returnStr += "<span style='cursor:hand;text-decoration: underline' onClick=showallreceived('" + mainsubuserid + "','" + requestid + "') >" + SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage)) + "</span>";
                returnStr += "</div>";
            }
        }
        return returnStr;
    }

    /**
     * @description ：消息订阅设置返回通道类型名称
     */
    public String getChannelType(String code){
        return WeaChannelType.getType(code).getName();
    }
}
