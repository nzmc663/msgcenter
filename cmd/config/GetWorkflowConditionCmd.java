package com.engine.msgcenter.cmd.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.browser.bean.BrowserBean;
import com.api.browser.bean.SearchConditionItem;
import com.api.browser.bean.SearchConditionOption;
import com.api.browser.util.BrowserConstant;
import com.api.browser.util.BrowserInitUtil;
import com.api.browser.util.ConditionFactory;
import com.api.browser.util.ConditionType;
import com.api.formmode.page.util.Util;
import com.cloudstore.dev.api.bean.MessageType;
import com.cloudstore.dev.api.util.Util_Serializer;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.constant.MsgConfigCanContainType;
import com.engine.msgcenter.constant.MsgConfigConstant;
import org.apache.commons.lang.StringUtils;
import weaver.conn.DBUtil;
import weaver.conn.RecordSet;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.*;


public class GetWorkflowConditionCmd extends AbstractCommonCommand<Map<String, Object>> {

    public GetWorkflowConditionCmd(User user, Map<String, Object> params) {
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
        JSONObject jsonObject=null;
        JSONObject otherParams=null;
        List<SearchConditionItem> conditions=new ArrayList<>();
        ConditionFactory conditionFactory = new ConditionFactory(user);

        String items=Util.null2String(params.get("items"));
        JSONArray jsonArray=JSONArray.parseArray(items);
        Iterator<Object> iterator=jsonArray.iterator();
        String includeValue="";
        while(iterator.hasNext()){
            jsonObject=(JSONObject) iterator.next();
            otherParams = jsonObject.getJSONObject("otherParams");
            String typeid=otherParams.getString("typeid");
            String typecode=otherParams.getString("typecode");
            String type=otherParams.getString("type");
            String path=otherParams.getString("path");
            String include=otherParams.getString("include");
            if (StringUtils.isNotBlank(include)&&MessageType.WF_NEW_ARRIVAL.getCode()==Integer.valueOf(typecode)){
                includeValue=include;
            }

            List<SearchConditionOption> options=new ArrayList<>();
            SearchConditionOption all=new SearchConditionOption(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL, SystemEnv.getHtmlLabelName(332,user.getLanguage()));
            SearchConditionOption desig=new SearchConditionOption(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG, SystemEnv.getHtmlLabelName(172,user.getLanguage()));
            SearchConditionOption exclude=new SearchConditionOption(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE, SystemEnv.getHtmlLabelName(126833,user.getLanguage()));
            if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL.equals(type)){
                all.setSelected(true);
            }else if (MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG.equals(type)){
                desig.setSelected(true);
            }else if (MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE.equals(type)){
                exclude.setSelected(true);
            }
            options.add(all);
            options.add(desig);
            options.add(exclude);
            SearchConditionItem selectItem=conditionFactory.createCondition(ConditionType.SELECT_LINKAGE,otherParams.getIntValue("typename"),"-"+typeid,options);

            Map<String,SearchConditionItem> selectLinkDatas=new HashMap<>();

            BrowserInitUtil browserInitUtil = new BrowserInitUtil();
            BrowserBean browserBean = new BrowserBean("-99991");
            browserBean.setIsSingle(false);
            //browserBean.setViewAttr(3); 暂时取消必填提示
            browserInitUtil.initBrowser(browserBean, user.getLanguage());

            SearchConditionItem desigBrowserItem=new SearchConditionItem(ConditionType.BROWSER,"",new String[]{typeid},browserBean);
            SearchConditionItem excludeBrowserItem = null;
            try {
                byte[] data=Util_Serializer.serialize(desigBrowserItem);
                excludeBrowserItem= (SearchConditionItem) Util_Serializer.deserialize(data);
            } catch (Exception e) {
                excludeBrowserItem=new SearchConditionItem(ConditionType.BROWSER,"",new String[]{typeid},browserBean);
            }

            if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG.equals(type)){
                desigBrowserItem.getBrowserConditionParam().setReplaceDatas(this.getReplaceDatas(path));
            }else if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE.equals(type)){
                excludeBrowserItem.getBrowserConditionParam().setReplaceDatas(this.getReplaceDatas(path));
            }

            selectLinkDatas.put(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_DESIG,desigBrowserItem);
            selectLinkDatas.put(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_EXCLUDE,excludeBrowserItem);

            selectItem.setSelectLinkageDatas(selectLinkDatas);
            selectItem.setColSpan(1);
            selectItem.setLabelcol(8);
            selectItem.setFieldcol(12);

            conditions.add(selectItem);
        }
        List<Map> cond=new ArrayList<>();
        Map<String,Object> data=new HashMap<>();
        data.put("title","");
        data.put("defaultshow",true);
        data.put("items",conditions);
        data.putAll(this.getSelectParams(jsonArray,includeValue));
        cond.add(data);
        apidatas.put(BrowserConstant.BROWSER_RESULT_CONDITIONS, cond);
        return apidatas;
    }

    private  List<Map<String,Object>> getReplaceDatas(String idString){
        List<Map<String,Object>> replaceDatas = new ArrayList<>();
        if(!MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL_VALUE.equalsIgnoreCase(idString)){
            List<String> params=new ArrayList<>();
            Object[] objects=DBUtil.transListIn(idString,params);
            String sql="select id,workflowname from workflow_base where id in ("+objects[0]+")";
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql,params);
            while(rs.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("id",rs.getInt("id"));
                map.put("name",rs.getString("workflowname"));
                replaceDatas.add(map);
            }
        }
        return replaceDatas;
    }

    private Map<String,Object> getSelectParams(JSONArray jsonArray,String include){
        StringBuilder sb=new StringBuilder();
        JSONObject jsonObject=null;
        JSONObject otherParams=null;
        List<Map<String,Object>> options=new ArrayList<>();
        List<Map<String,Object>> syncSelectOptions=new ArrayList<>();
        Map<String,Object> selectParams=new HashMap<>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject=jsonArray.getJSONObject(i);
            otherParams = jsonObject.getJSONObject("otherParams");
            String typecode=otherParams.getString("typecode");
            String typeid=otherParams.getString("typeid");
            int typecodeValue=Integer.valueOf(typecode);
            if(MessageType.WF_RETURN.getCode()==typecodeValue||MessageType.WF_FORWARD.getCode()==typecodeValue
                    ||MessageType.WF_COPY.getCode()==typecodeValue||MessageType.WF_INQUIRY.getCode()==typecodeValue){
                Map<String,Object> selectMap=new HashMap<>();
                selectMap.put("key",typeid);
                selectMap.put("showname",this.showContainDefaultTitle(typecode));
                options.add(selectMap);
            }
            if(MessageType.WF_NEW_ARRIVAL.getCode()==typecodeValue){
                selectParams.put("newArrivalKey","-"+typeid);
            }

            Map<String,Object> syncSelectMap=new HashMap<>();
            syncSelectMap.put("key","-"+typeid);
            syncSelectMap.put("showname",jsonObject.getString("title"));
            syncSelectOptions.add(syncSelectMap);
            sb.append("-"+typeid).append(MsgConfigConstant.MSG_DEFAULT_CONFIG_SEPARATOR);
        }
        if (include.startsWith(",")){
            include=include.substring(1);
        }
        if (include.endsWith(",")){
            include=include.substring(0,include.length()-1);
        }
        selectParams.put("options",options);
        selectParams.put("value",include);
        selectParams.put("syncOptions",syncSelectOptions);
        selectParams.put("syncValue",sb.toString());
        return selectParams;
    }

    private String showContainDefaultTitle(String typecode){
        return SystemEnv.getHtmlLabelName( MsgConfigCanContainType.getLabelid(Integer.valueOf(typecode)),user.getLanguage());
    }

}

