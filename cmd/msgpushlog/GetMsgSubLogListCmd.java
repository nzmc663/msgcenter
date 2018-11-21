package com.engine.msgcenter.cmd.msgpushlog;

import com.cloudstore.eccom.constant.WeaBoolAttr;
import com.cloudstore.eccom.pc.table.WeaTable;
import com.cloudstore.eccom.pc.table.WeaTableColumn;
import com.cloudstore.eccom.result.WeaResultMsg;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
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
 * @ Description：
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/11 10:20
 */
public class GetMsgSubLogListCmd extends AbstractCommonCommand<Map<String,Object>> {


    public GetMsgSubLogListCmd(Map<String,Object> params,User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> map = new HashMap<>();
        WeaResultMsg weaResultMsg = new WeaResultMsg(false);
        WeaTable weaTable = new WeaTable();
        boolean hasLogRight = HrmUserVarify.checkUserRight("LogView:View", user);
        if(!hasLogRight){
            map.put("noright", true);
            return map;
        }
        try{
            String pageUid = "msg" + PageUidConstant.MSG_SUB_LOG_PAGEUID;
            String pageId = pageUid + "_" + user.getUID();
            String pageSize = PageIdConst.getPageSize(pageId,user.getUID());

            RecordSet rs = new RecordSet();
            String backFields = "ID,ECOLOGY_MESSAGE_SUBSCRIBE,CONTEXTS,DATATYPE,CLASSNAME,MESSAGETYPE,CHANNELTYPE,CREATERID,concat(concat(t.CREATEDATE, ' '),t.CREATETIME) as cretime,concat(concat(t.MODIFYDATE, ' '),t.MODIFYTIME) as modtime";
            if(DBConstant.DB_TYPE_SQLSERVER.equalsIgnoreCase(rs.getDBType())){
                backFields = "ID,ECOLOGY_MESSAGE_SUBSCRIBE,CONTEXTS,DATATYPE,CLASSNAME,MESSAGETYPE,CHANNELTYPE,CREATERID,t.CREATEDATE+' '+t.CREATETIME as cretime,t.MODIFYDATE+' '+t.MODIFYTIME as modtime";
            }

            String sqlForm = "ECOLOGY_MESSAGE_SUBSCRIBE_LOG t";
            String sqlWhere = " 1=1 ";

            //搜索条件
            String fromDate = "";//默认今天
            String toDate = "";
            String dataType = Util.null2String(params.get("datatype"));
            String contexts = Util.null2String(params.get("contexts"));
            String className = Util.null2String(params.get("classname"));
            String messageType = Util.null2String(params.get("messagetype"));
            String channelType = Util.null2String(params.get("channeltype"));

            //1、消息内容
            if(StringUtils.isNotBlank(contexts)){
                sqlWhere += " and contexts like '%" + contexts + "%'";
            }
            //2、消息数据类型
            if(StringUtils.isNotBlank(dataType)){
                sqlWhere += " and datatype like '%" + dataType + "%'";
            }
            //3、第三方注册类
            if(StringUtils.isNotBlank(className)){
                sqlWhere += " and classname like '%" + className + "%'";
            }
            //4、消息类型
            if(StringUtils.isNotBlank(messageType)){
                sqlWhere += " and messagetype like '%" + messageType + "%'";
            }
            //5、通道类型
            if(StringUtils.isNotBlank(channelType)){
                sqlWhere += " and channeltype like '%" + channelType + "%'";
            }
            //6、创建时间范围
            String fromDatetmp = Util.null2String(params.get("fromDate"));
            String toDatetmp = Util.null2String(params.get("toDate"));
            if(StringUtils.isNotBlank(fromDatetmp) && StringUtils.isNotBlank(toDatetmp)){
                fromDate = fromDatetmp;
                toDate = toDatetmp;
            }
            if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
                sqlWhere += " and createdate>='" + fromDate + "' and createdate<='" + toDate + "'";
            }

            weaTable.setPageUID(pageUid);
            weaTable.setPageID(pageId);
            weaTable.setPagesize(pageSize);
            weaTable.setCheckboxList(null);
            weaTable.setCheckboxpopedom(null);
            weaTable.setBackfields(backFields);
            weaTable.setSqlform(sqlForm);
            weaTable.setSqlwhere(sqlWhere);
            weaTable.setSqlorderby("modtime,id");
            weaTable.setSqlprimarykey("id");
            weaTable.setSqlisdistinct("false");
            weaTable.setSqlsortway("desc");

            String languageId = "" + user.getLanguage();
            weaTable.getColumns().add(new WeaTableColumn("id").setDisplay(WeaBoolAttr.FALSE));
            weaTable.getColumns().add(new WeaTableColumn("ecology_message_subscribe").setDisplay(WeaBoolAttr.FALSE));
            weaTable.getColumns().add(new WeaTableColumn("15%",SystemEnv.getHtmlLabelName(26456,user.getLanguage()),"CONTEXTS"));
            weaTable.getColumns().add(new WeaTableColumn("15%",SystemEnv.getHtmlLabelName(388295,user.getLanguage()),"DATATYPE","DATATYPE","com.engine.msgcenter.util.MsgTransmethod.getDataType",languageId));
            weaTable.getColumns().add(new WeaTableColumn("15%",SystemEnv.getHtmlLabelName(388296,user.getLanguage()),"CLASSNAME"));
            weaTable.getColumns().add(new WeaTableColumn("15%",SystemEnv.getHtmlLabelName(388297,user.getLanguage()),"MESSAGETYPE"));
            weaTable.getColumns().add(new WeaTableColumn("15%",SystemEnv.getHtmlLabelName(388298,user.getLanguage()),"CHANNELTYPE","","com.engine.msgcenter.util.MsgTransmethod.getChannelType"));
            weaTable.getColumns().add(new WeaTableColumn("15%",SystemEnv.getHtmlLabelName(1339,user.getLanguage()),"cretime"));
            weaTable.getColumns().add(new WeaTableColumn("15%",SystemEnv.getHtmlLabelName(19520,user.getLanguage()),"modtime"));

            weaResultMsg.putAll(weaTable.makeDataResult());
            weaResultMsg.success();
            map = weaResultMsg.getResultMap();
        }catch (Exception e){
            e.printStackTrace();
            return weaResultMsg.fail(e.getMessage()).getResultMap();
        }
        return map;
    }
}
