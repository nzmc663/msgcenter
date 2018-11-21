package com.engine.msgcenter.cmd.msgpushlog;

import com.cloudstore.eccom.constant.WeaBoolAttr;
import com.cloudstore.eccom.pc.table.WeaTable;
import com.cloudstore.eccom.pc.table.WeaTableColumn;
import com.cloudstore.eccom.result.WeaResultMsg;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.constant.MsgPLConstant;
import com.engine.msgcenter.constant.PageUidConstant;
import com.engine.msgcenter.util.MsgPushLogUtil;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.conn.constant.DBConstant;
import weaver.general.PageIdConst;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.HashMap;
import java.util.Map;

/*
 * @ Description：查询消息推送日志
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 */

public class DoQueryMsgPushLogCmd extends AbstractCommonCommand<Map<String,Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public DoQueryMsgPushLogCmd(Map<String,Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String,Object> execute(CommandContext commandContext) {
        Map<String, Object> map = new HashMap<>();
        WeaResultMsg result = new WeaResultMsg(false);
        boolean hasLogRight = HrmUserVarify.checkUserRight("LogView:View", user);
        if(!hasLogRight){
            map.put("hasRight", "false");
            map.put("status", "0");
            return map;
        }
        try {
            try {
                String pageUid = "msg" + PageUidConstant.MSG_LOG_PAGEUID;
                String pageID = pageUid + "_" + user.getUID();
                String pageSize = PageIdConst.getPageSize(pageID, user.getUID());
                String sqlwhere = " 1=1 ";

                String timeType = Util.null2String(params.get("timeType"));//1、时间范围
                String remindType = Util.null2String(params.get("remindType"));//2、提醒种类
                String contexts = Util.null2String(params.get("contexts"));//3、提醒内容
                String remindUsers = Util.null2String(params.get("remindUsers"));//4、提醒接收人
                //1、时间范围
                String fromDate = MsgPushLogUtil.getNowDate();//默认为今天
                String toDate = MsgPushLogUtil.getNowDate();
                if(StringUtils.equals(timeType,MsgPLConstant.WEEK)){
                    fromDate = MsgPushLogUtil.getMonday();//周一
                    toDate = MsgPushLogUtil.getSunday();//周末
                }else if(StringUtils.equals(timeType,MsgPLConstant.MONTH)){
                    fromDate = MsgPushLogUtil.getFirstDayOfMonth();//月初
                    toDate = MsgPushLogUtil.getLastDayOfMonth();//月末
                }else if(StringUtils.equals(timeType,MsgPLConstant.LAST_MONTH)){
                    fromDate = MsgPushLogUtil.getFirstDayOfLastMonth();//上月初
                    toDate = MsgPushLogUtil.getLastDayOfLastMonth();//上月末
                }else if(StringUtils.equals(timeType,MsgPLConstant.QUARTER)){
                    fromDate = MsgPushLogUtil.getFirstDayOfQuarter();//季初
                    toDate = MsgPushLogUtil.getLastDayOfQuarter();//季末
                }else if(StringUtils.equals(timeType,MsgPLConstant.YEAR)){
                    fromDate = MsgPushLogUtil.getYearDateStart();//年初
                    toDate = MsgPushLogUtil.getYearDateEnd();//年末
                }else if(StringUtils.equals(timeType,MsgPLConstant.LAST_YEAR)){
                    fromDate = MsgPushLogUtil.getLastYearDateStart();//去年初
                    toDate = MsgPushLogUtil.getLastYearDateEnd();//去年末
                }else if(StringUtils.equals(timeType,MsgPLConstant.SPECIFIED_DATE)){
                    String fromDateTemp = Util.null2String(params.get("fromDate"));//指定开始日期
                    String toDateTemp = Util.null2String(params.get("toDate"));//指定结束日期
                    if(StringUtils.isNotBlank(fromDateTemp)&&StringUtils.isNotBlank(toDateTemp)){
                        fromDate=fromDateTemp;
                        toDate=toDateTemp;
                    }
                }
                if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
                    sqlwhere = sqlwhere + " and t1.CREATEDATE>='" + fromDate + "' and t1.CREATEDATE<='" + toDate + "'";
                }
                //2、提醒种类
                if(StringUtils.equals(remindType,MsgPLConstant.SPECIFIED_TYPE)){//默认全部种类不拼接sqlwhere
                    String chooseTypes = Util.null2String(params.get("chooseTypes"));//指定种类
                    if(StringUtils.isNotBlank(chooseTypes)){
                        sqlwhere += " and t1.MESSAGETYPE='" + chooseTypes + "'";
                    }
                }
                //3、提醒内容
                if(StringUtils.isNotBlank(contexts)){//
                    sqlwhere += " and t1.CONTEXTS like '%" + contexts + "%'";
                }
                //4、提醒接收人
                if(StringUtils.isNotBlank(remindUsers)){
                    sqlwhere += " and t1.USERID like '%," + remindUsers + ",%'";
                }

                //消息日志
                WeaTable table = new WeaTable();
                table.setPageUID(pageUid);
                table.setPageID(pageID);
                table.setPagesize(pageSize);
                table.setCheckboxList(null);
                table.setCheckboxpopedom(null);

                RecordSet rs = new RecordSet();
                String backfields2 = "t1.ID  as tid,CONTEXTS,t1.USERID as userid,t1.USERID as requestid,t1.CREATEDATE as t1date,t1.CREATETIME as t1time,concat(concat(t1.CREATEDATE, ' '),t1.CREATETIME) as ttime,MESSAGETYPE,t2.id as t2id,t2.typename as typename ";
                if(DBConstant.DB_TYPE_SQLSERVER.equalsIgnoreCase(rs.getDBType())){
                    backfields2 = "t1.ID  as tid,CONTEXTS,t1.USERID as userid,t1.USERID as requestid,t1.CREATEDATE as t1date,t1.CREATETIME as t1time,t1.CREATEDATE + ' ' + t1.CREATETIME as ttime,MESSAGETYPE,t2.id as t2id,t2.typename as typename ";
                }

                table.setBackfields(backfields2);
                table.setSqlform("ECOLOGY_MESSAGE_LOG t1 left join ecology_message_type t2 on t1.MESSAGETYPE=t2.id ");
                table.setSqlwhere(sqlwhere);
                table.setSqlorderby("ttime,tid");
                table.setSqlprimarykey("ECOLOGY_MESSAGE_LOG.ID");
                table.setSqlisdistinct("false");
                table.setSqlsortway("DESC");

                String langugeId = String.valueOf(user.getLanguage());
                String param = user.getLanguage() + "+" + user.getUID() + "+column:userid";
                table.getColumns().add(new WeaTableColumn("tid").setDisplay(WeaBoolAttr.FALSE));
                table.getColumns().add(new WeaTableColumn("userid").setDisplay(WeaBoolAttr.FALSE));
                table.getColumns().add(new WeaTableColumn("30%", SystemEnv.getHtmlLabelName(388218,user.getLanguage()), "ttime","ttime"));
                table.getColumns().add(new WeaTableColumn("20%", SystemEnv.getHtmlLabelName(388219,user.getLanguage()), "typename","typename","com.engine.msgcenter.util.MsgTransmethod.getRemindType",langugeId));
                table.getColumns().add(new WeaTableColumn("20%", SystemEnv.getHtmlLabelName(27415,user.getLanguage()), "CONTEXTS","CONTEXTS"));
                table.getColumns().add(new WeaTableColumn("30%", SystemEnv.getHtmlLabelName(388220,user.getLanguage()), "tid","tid","com.engine.msgcenter.util.MsgTransmethod.getReceivers",param));

                result.putAll(table.makeDataResult());
                result.success();
                map = result.getResultMap();
            } catch (Exception e) {
                e.printStackTrace();
                return result.fail(e.getMessage()).getResultMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",false);
            map.put("msg","catch exception：" + e.getMessage());
        }
        return map;
    }

}
