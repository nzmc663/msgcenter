package com.engine.msgcenter.cmd.config;

import com.cloudstore.dev.api.bean.MessageGroup;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.bean.WeaCheckbox;
import com.engine.msgcenter.bean.WeaTableItem;
import com.engine.msgcenter.constant.MsgConfigConstant;
import com.engine.msgcenter.dao.UserMsgConfigDao;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GetUserMsgConfigCmd extends AbstractCommonCommand<Map<String, Object>> {

    public GetUserMsgConfigCmd(User user,Map<String, Object> params) {
        this.user=user;
        this.params=params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> apidatas=new HashMap<>();
        String right = Util.null2String(params.get("right"));
        if (StringUtils.isNotBlank(right)){
            if(!HrmUserVarify.checkUserRight("LogView:View", user)){
                apidatas.put("noright", true);
                return apidatas;
            }
        }
        int userid=user.getUID();

        //获取userid参数，为空时使用当前登录id
        String useridString=Util.null2String(params.get("userid"));
        if(StringUtils.isNotBlank(useridString)){
            userid=Util.getIntValue(useridString);
            if(userid<0){
                apidatas.put("ret",false);
                apidatas.put("message","Illegal user, please check 'userid' carefully.");
                return apidatas;
            }
        }
        String moduleid=Util.null2String(params.get("moduleid"));
        apidatas.put("ret",true);
        apidatas.put("userid",userid);
        apidatas.put("datas",this.getUserConfig(userid,moduleid));
        return apidatas;
    }

    public List getUserConfig(int userid,String moduleid){
        RecordSet rs=new RecordSet();
        UserMsgConfigDao dao=new UserMsgConfigDao();

        Map<String,WeaTableItem> allMap= new ConcurrentHashMap<>();

        if(StringUtils.isNotBlank(moduleid)){
            //按模块查询用户配置信息
            rs.executeQuery(dao.getUserDetailConfig(moduleid),userid,moduleid);
            //如果用户无配置信息，则查询默认配置
            if(!rs.next()){
                //按模块查询默认用户配置信息
                rs.executeQuery(dao.getUserDetailConfig(moduleid),MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID,moduleid);
                rs.next();
            }
        }else{
            //查询全部模块用户配置信息
            rs.executeQuery(dao.getUserDetailConfig(),userid,MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID,userid);
            rs.next();
        }

        do {
            WeaTableItem itemGroup= allMap.get(rs.getString("groupid"));

            WeaCheckbox enable =new WeaCheckbox("enable");
            if(MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE.equalsIgnoreCase(rs.getString("enable"))){
                enable.setValue(MsgConfigConstant.MSG_FRONT_CONFIG_ABLED);
            }

            WeaCheckbox enableTray =new WeaCheckbox("enabletray");
            if(MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE.equalsIgnoreCase(rs.getString("enabletray"))){
                enableTray.setValue(MsgConfigConstant.MSG_FRONT_CONFIG_ABLED);
            }

            if(itemGroup==null){
                itemGroup = new WeaTableItem();
                itemGroup.setKey(rs.getString("groupcode")+"_group");
                itemGroup.setTitle(SystemEnv.getHtmlLabelName(rs.getInt("groupname"),user.getLanguage()));
                itemGroup.setOpen(new WeaCheckbox("enable",MsgConfigConstant.MSG_FRONT_CONFIG_ABLED));
                itemGroup.setOpenRight(new WeaCheckbox("enabletray",MsgConfigConstant.MSG_FRONT_CONFIG_ABLED));
                itemGroup.setDetails(MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED);
                Map<Object,Object> otherParams = new HashMap<>();
                otherParams.put("status",rs.getString("status"));
                otherParams.put("isdefault",MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID==(rs.getInt("userid")));
                itemGroup.setOtherParams(otherParams);
                allMap.put(rs.getString("groupid"),itemGroup);
                itemGroup = allMap.get(rs.getString("groupid"));
            }
            WeaTableItem item = new WeaTableItem();
            item.setKey(rs.getString("typeid"));
            item.setTitle(SystemEnv.getHtmlLabelName(rs.getInt("typename"),user.getLanguage()));
            item.setOpen(enable);
            item.setOpenRight(enableTray);
            Map<Object,Object> otherParams = new HashMap<>();
            otherParams.put("id",rs.getString("configid"));
            otherParams.put("category","item");
            otherParams.put("belong",itemGroup.getKey());
            otherParams.put("groupname",itemGroup.getTitle());
            otherParams.put("typename",rs.getInt("typename"));

            String groupcode = rs.getString("groupcode");
            otherParams.put("typeid",rs.getInt("typeid"));
            otherParams.put("typecode",rs.getString("typecode"));
            otherParams.put("include",rs.getString("include"));
            otherParams.put("description",rs.getString("description"));
            otherParams.put("configdetailid",rs.getInt("detailid"));
            otherParams.put("type",rs.getString("type"));
            otherParams.put("path", rs.getString("path"));
            if(MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE.equalsIgnoreCase(rs.getString("hasdetail"))){
                item.setDetails(MsgConfigConstant.MSG_FRONT_CONFIG_ABLED);
                final int workflow= MessageGroup.WORKFLOW.getCode();
                if(workflow==Integer.valueOf(groupcode)){
                    otherParams.put("detatilType",MsgConfigConstant.MSG_DEFAULT_CONFIG_DETAIL_TYPE_WORKFLOW);
                }else{
                    otherParams.put("detatilType",MsgConfigConstant.MSG_DEFAULT_CONFIG_DETAIL_TYPE_OTHER);
                }

            }else{
                item.setDetails(MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED);
            }
            item.setOtherParams(otherParams);
            if(itemGroup.getSubList()==null){
                List<WeaTableItem> subList=new ArrayList<>();
                subList.add(item);
                itemGroup.setSubList(subList);
            }else{
                itemGroup.getSubList().add(item);
            }
        }while(rs.next());

        List<WeaTableItem> hasGroupList = new LinkedList<>();
        List<WeaTableItem> noGroupList = new LinkedList<>();
        for (Map.Entry entry:allMap.entrySet()){
            WeaTableItem item=(WeaTableItem)entry.getValue();
            String status=Util.null2String(item.getOtherParams().get("status"));
            if(item.getSubList()==null){
            }else if(MsgConfigConstant.MSG_DEFAULT_CONFIG_UNENABLE.equals(status)&&item.getSubList().size()==1){
                WeaTableItem subItem = item.getSubList().get(0);
//                if (MessageType.MENTIONS_OF_ME.getCode()==Integer.valueOf(Util.null2String(subItem.getOtherParams().get("typecode")))){
//                    hasGroupList.add(0,subItem);
//                    continue;
//                }
                noGroupList.add(subItem);
            }else{
                boolean allOpen = true;
                boolean allOpenRight = true;
                for (WeaTableItem subItem:item.getSubList()){
                    if(MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED.equalsIgnoreCase(subItem.getOpen().getValue())){
                        allOpen = false;
                    }
                    if(MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED.equalsIgnoreCase(subItem.getOpenRight().getValue())){
                        allOpenRight = false;
                    }
                    if(!allOpen&&!allOpenRight){
                        break;
                    }
                }
                if (!allOpen) {item.getOpen().setValue(MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED);}
                if (!allOpenRight) {item.getOpenRight().setValue(MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED);}
                hasGroupList.add(item);
            }
        }
        hasGroupList.addAll(noGroupList);
        return hasGroupList;
    }
}
