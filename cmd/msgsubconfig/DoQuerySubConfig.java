package com.engine.msgcenter.cmd.msgsubconfig;

import com.cloudstore.eccom.constant.WeaBoolAttr;
import com.cloudstore.eccom.pc.table.*;
import com.cloudstore.eccom.result.WeaResultMsg;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.constant.PageUidConstant;
import com.engine.systeminfo.constant.em.EmConstant;
import com.engine.workflow.constant.PageUidConst;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.conn.constant.DBConstant;
import weaver.general.PageIdConst;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @ Description：消息订阅设置查询
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 13:04
 */
public class DoQuerySubConfig extends AbstractCommonCommand<Map<String,Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public DoQuerySubConfig(Map<String,Object> params,User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> map = new HashMap<>();
        if(!HrmUserVarify.checkUserRight("LogView:View", user)){
            map.put("noright", true);
            return map;
        }
        WeaResultMsg result = new WeaResultMsg(false);
            try {
                String pageUid = "sub" + PageUidConstant.MSG_SUB_CONFIG_PAGEUID;
                String pageID = pageUid + "_" + user.getUID();
                String pageSize = PageIdConst.getPageSize(pageID, user.getUID());
                String sqlwhere = " 1=1 ";

                WeaTable table = new WeaTable();
                table.setPageUID(pageUid);
                table.setPageID(pageID);
                table.setPagesize(pageSize);
                RecordSet rs=new RecordSet();
                String fields = " id, t.status as tmpstatus, name, datatype, status, classname, channeltype, concat(concat(T .CREATEDATE, ' '),T .CREATETIME) AS tcretime, concat(concat(T .MODIFYDATE, ' '),T .MODIFYTIME) AS tmodtime";
                if(DBConstant.DB_TYPE_SQLSERVER.equalsIgnoreCase(rs.getDBType())){
                    fields = " id, t.status as tmpstatus, name, datatype, status, classname, channeltype, T .CREATEDATE + ' ' + T .CREATETIME AS tcretime, T .MODIFYDATE + ' ' + T .MODIFYTIME AS tmodtime";
                }

                table.setBackfields(fields);

                //搜索条件
                //名称
                String name = Util.null2String(params.get("name"));
                if (StringUtils.isNotBlank(name)) {
                    sqlwhere += " and name like '%" + name + "%' ";
                }
                //消息数据类型
                String datatype = Util.null2String(params.get("datatype"));
                if (StringUtils.equals(datatype,"text")) {
                    sqlwhere += " and datatype='0'";
                }else if(StringUtils.equals(datatype,"binary")){
                    sqlwhere += " and datatype='1'";
                }
                //第三方注册类
                String classname = Util.null2String(params.get("classname"));
                if (StringUtils.isNotBlank(classname)) {
                    sqlwhere += " and classname like '%" + classname + "%' ";
                }
                //通道类型
                String channeltype = Util.null2String(params.get("channeltype"));
                if (StringUtils.isNotBlank(channeltype)) {
                    sqlwhere += " and channeltype='" + channeltype + "' ";
                }
                //状态
                String status = Util.null2String(params.get("status"));
                if (StringUtils.isNotBlank(status)) {
                    if (StringUtils.equals(status, "enabled")) {
                        sqlwhere += " and status='y' ";
                    } else if (StringUtils.equals(status, "disabled")) {
                        sqlwhere += " and status='n' ";
                    }
                }
                //创建时间 fromDate toDate
                String fromDate = Util.null2String(params.get("fromDate"));
                String toDate = Util.null2String(params.get("toDate"));
                if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
                    sqlwhere += " and createdate>='" + fromDate + "' and createdate<='" + toDate + "' ";
                }

                table.setSqlform("ecology_message_subscribe t");
                table.setSqlwhere(sqlwhere);
                table.setSqlorderby("tmodtime,id");
                table.setSqlprimarykey("id");
                table.setSqlisdistinct("false");
                table.setSqlsortway("DESC");
                String languageId = "" + user.getLanguage();
                table.getColumns().add(new WeaTableColumn("id").setDisplay(WeaBoolAttr.FALSE));
                table.getColumns().add(new WeaTableColumn("20%", SystemEnv.getHtmlLabelName(388299, user.getLanguage()), "name","name"));
                table.getColumns().add(new WeaTableColumn("20%", SystemEnv.getHtmlLabelName(388295, user.getLanguage()), "datatype","datatype","com.engine.msgcenter.util.MsgTransmethod.getDataType",languageId));
                table.getColumns().add(new WeaTableColumn("15%", SystemEnv.getHtmlLabelName(1339, user.getLanguage()), "tcretime","tcretime").setDisplay(WeaBoolAttr.FALSE));
                table.getColumns().add(new WeaTableColumn("15%", SystemEnv.getHtmlLabelName(19520, user.getLanguage()), "tmodtime","tmodtime").setDisplay(WeaBoolAttr.FALSE));
                table.getColumns().add(new WeaTableColumn("20%", SystemEnv.getHtmlLabelName(388296,user.getLanguage()),"classname","classname"));
                table.getColumns().add(new WeaTableColumn("20%", SystemEnv.getHtmlLabelName(388298,user.getLanguage()),"channeltype","channeltype","com.engine.msgcenter.util.MsgTransmethod.getChannelType"));
                table.getColumns().add(new WeaTableColumn("10%", SystemEnv.getHtmlLabelName(602, user.getLanguage()), "status", "status", "com.engine.msgcenter.util.MsgTransmethod.getStatus", languageId));
                WeaTableColumn weaTableColumn = new WeaTableColumn("5%",SystemEnv.getHtmlLabelName(602,user.getLanguage()),"tmpstatus","tmpstatus","com.engine.msgcenter.util.MsgTransmethod.getOpratePopedomWithStatus");
                weaTableColumn.setDisplay(WeaBoolAttr.FALSE);
                weaTableColumn.setTransMethodForce("true");
                table.getColumns().add(weaTableColumn);

                WeaTableOperates weaTableOperates = new WeaTableOperates();
                List<WeaTableOperate> operateList = new ArrayList<>();
                WeaTableOperate edit = new WeaTableOperate(SystemEnv.getHtmlLabelName(93, user.getLanguage()), "", "0");
                edit.setIsalwaysshow("true");

                WeaTableOperate enabled = new WeaTableOperate(SystemEnv.getHtmlLabelName(18095, user.getLanguage()), "", "1");

                WeaTableOperate disabled = new WeaTableOperate(SystemEnv.getHtmlLabelName(18096, user.getLanguage()), "", "2");

                operateList.add(edit);
                operateList.add(enabled);
                operateList.add(disabled);

                WeaTablePopedom popedom = new WeaTablePopedom();
                popedom.setTransmethod("com.engine.systeminfo.util.DataUtils.getOpratePopedomWithStatus");
                popedom.setOtherpara("column:tmpstatus");
                weaTableOperates.setPopedom(popedom);
                weaTableOperates.setOperate(operateList);
                table.setOperates(weaTableOperates);

                result.putAll(table.makeDataResult());
                result.success();
                map = result.getResultMap();

            } catch (Exception e) {
                e.printStackTrace();
                map.put("status",false);
                map.put("msg",e.getMessage());
            }
        return map;
    }
}
