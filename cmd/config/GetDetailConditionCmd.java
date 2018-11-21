package com.engine.msgcenter.cmd.config;

import com.cloudstore.dev.api.util.Util_Serializer;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.constant.MsgConfigConstant;
import weaver.conn.DBUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetDetailConditionCmd extends AbstractCommonCommand<Map<String, Object>> {

    public GetDetailConditionCmd(User user, Map<String, Object> params) {
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
        String type=Util.null2String(params.get("type")).trim();
        apidatas.put("options",makeSelectOptions(type));
        String idString=Util.null2String(params.get("path"));
        apidatas.put("selectLinkageDatas",makeSelectLinkageDatas(type,idString));
        return apidatas;
    }

    private List makeSelectOptions(String type){
        List<Map> options=new ArrayList<>(3);
        HashMap<String,Object> optionAll =new HashMap<>();
        optionAll.put("key",MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL);
        optionAll.put("showname",SystemEnv.getHtmlLabelName(332,user.getLanguage()));
        HashMap<String,Object> optionDesig =new HashMap<>();
        optionDesig.put("key",MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG);
        optionDesig.put("showname",SystemEnv.getHtmlLabelName(172,user.getLanguage()));
        HashMap<String,Object> optionExclude =new HashMap<>();
        optionExclude.put("key",MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE);
        optionExclude.put("showname",SystemEnv.getHtmlLabelName(126833,user.getLanguage()));
        if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL.equals(type)){
            optionAll.put("selected",true);
        }else if (MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG.equals(type)){
            optionDesig.put("selected",true);
        }else if (MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE.equals(type)){
            optionExclude.put("selected",true);
        }
        options.add(optionAll);
        options.add(optionDesig);
        options.add(optionExclude);
        return options;
    }
    @SuppressWarnings("unchecked")
    private Map makeSelectLinkageDatas(String type,String idString){
        Map<String,Object> selectLinkageDatas=new HashMap<>(2);
        Map<String,Object> value1Map=new HashMap<>(4);
        value1Map.put("conditionType","BROWSER");
        value1Map.put("domkey","path");

        Map<String,Object> otherParams=new HashMap<>(4);
        otherParams.put("type","-99991");
        otherParams.put("isSingle",false);
        otherParams.put("title",SystemEnv.getHtmlLabelName(385227,user.getLanguage()));
        otherParams.put("icon","icon-coms-workflow");
        otherParams.put("viewAttr",3);
        value1Map.put("otherParams",otherParams);

        Map<String,Object> browserConditionParam=new HashMap<>(1);
        if(!MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL_VALUE.equalsIgnoreCase(idString)){
            List<String> params=new ArrayList<>();
            Object[] objects=DBUtil.transListIn(idString,params);
            String sql="select id,workflowname from workflow_base where id in ("+objects[0]+")";
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql,params);
            List<Map<Object,Object>> replaceDatas = new ArrayList<>();
            while(rs.next()){
                Map<Object,Object> map = new HashMap<>();
                map.put("id",rs.getInt("id"));
                map.put("name",rs.getString("workflowname"));
                replaceDatas.add(map);
            }
            browserConditionParam.put("replaceDatas",replaceDatas);
        }

        Map<String,Object> value2Map=new HashMap<>();
        try {
            byte[] data=Util_Serializer.serialize(value1Map);
            value2Map= (Map<String, Object>) Util_Serializer.deserialize(data);
        } catch (Exception e) {
            value2Map.putAll(value1Map);
        }

        if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG.equals(type)){
            value1Map.put("browserConditionParam",browserConditionParam);
        }else if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE.equals(type)){
            value2Map.put("browserConditionParam",browserConditionParam);
        }

        selectLinkageDatas.put(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG,value1Map);
        selectLinkageDatas.put(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE,value2Map);
        return selectLinkageDatas;
    }

}

