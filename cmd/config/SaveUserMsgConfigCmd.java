package com.engine.msgcenter.cmd.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.api.util.Util_DateTime;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.constant.MsgConfigConstant;
import com.engine.msgcenter.dao.UserMsgConfigDao;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSetTrans;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.*;

public class SaveUserMsgConfigCmd extends AbstractCommonCommand<Map<String, Object>> {

    public SaveUserMsgConfigCmd(User user, Map<String, Object> params) {
        this.user=user;
        this.params=params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> apidatas  = new HashMap<>();

        //获取用户id,用户id异常时直接返回错误信息
        int userid=Util.getIntValue(Util.null2String(params.get("userid"))); //获取用户信息
        if(userid==-1){
            apidatas.put("ret",false);
            apidatas.put("message","Illegal user, please check 'userid' carefully.");
            return apidatas;
        }

        //判断用户是否已有配置信息
        RecordSetTrans rst=new RecordSetTrans();
        rst.setAutoCommit(false);
        UserMsgConfigDao dao=new UserMsgConfigDao();
        boolean isDefaultConfig=true;
        try {
            rst.executeQuery(dao.getUserConfig(),userid);
            if (rst.next()){
                isDefaultConfig=false;
            }
        }catch (Exception e){
            apidatas.put("ret",false);
            apidatas.put("message",e.getMessage());
            return apidatas;
        }

        //获取表单用户配置数据
        Param param=this.parseString2Param(Util.null2String(params.get("datas")),isDefaultConfig,userid);

        //根据是否默认配置分别执行insert脚本或者update脚本
        try {
             if (isDefaultConfig){
                 rst.executeBatchSql(dao.insertConfigBatchSql(),param.getConfigParams());
                 rst.executeBatchSql(dao.insertConfigDetailBatchSql(),param.getDetailParams());
                 apidatas.put("ret",true);
                 apidatas.put("message","successful operation.");
                 rst.commit();
             }  else{
                 rst.executeBatchSql(dao.updateConfigBatchSql(), param.getConfigParams());
                 rst.executeBatchSql(dao.updateConfigDetailBatchSql(), param.getDetailParams());
                 apidatas.put("ret", true);
                 apidatas.put("message", "successful operation.");
                 rst.commit();
             }

        } catch (Exception e) {
                rst.rollback();
                apidatas.put("ret",false);
                apidatas.put("message",e.getMessage());
                rst.commit();
                return apidatas;
        }

        return apidatas;
    }

    private class Param{

        private List<List<Object>> configParams;//配置主表参数
        private List<List<Object>> detailParams;//明细表参数

        public List<List<Object>> getConfigParams() {
            return configParams;
        }

        public void setConfigParams(List<List<Object>> configParams) {
            this.configParams = configParams;
        }

        public List<List<Object>> getDetailParams() {
            return detailParams;
        }

        public void setDetailParams(List<List<Object>> detailParams) {
            this.detailParams = detailParams;
        }
    }
    /**
     * 将paramString转换为批量执行sql脚本的参数列表
     *    if isDefaultConfig is true  生成insert脚本参数列表
     *    if isDefaultConfig is false 生成update脚本参数列表
     * @param paramString json参数String类型
     * @param isDefaultConfig 是否默认配置
     */
    private Param parseString2Param(String paramString,boolean isDefaultConfig,int userid){
        Param param=new Param();
        JSONArray jsonArray=JSONArray.parseArray(Util.null2String(paramString));
        List<List<Object>> configParams=new ArrayList<>();
        List<List<Object>> detailParams=new ArrayList<>();
        JSONArray subNullArray =new JSONArray(1);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject superObject=jsonArray.getJSONObject(i);
            JSONArray subArray=superObject.getJSONArray("subList");
            if (subArray==null||subArray.size()==0){
                subArray=subNullArray;
                subArray.set(0,superObject);
            }
            for (int j=0;j<subArray.size();j++){
                JSONObject obj=subArray.getJSONObject(j);
                JSONObject open=obj.getJSONObject("open");
                JSONObject openRight=obj.getJSONObject("openRight");
                JSONObject otherParams=obj.getJSONObject("otherParams");

                List<Object> configParam=new LinkedList<>();//配置主表参数
                int configid =Integer.valueOf(otherParams.getString("id"));
                int messagetypeid=otherParams.getIntValue("typeid");
                String include=otherParams.getString("include");
                String hasdetail=isEnableWithVal(obj.getString("details"));
                String enable=isEnableWithVal(open.getString("value"));
                String enabletray=isEnableWithVal(openRight.getString("value"));
                configParam.add(include);
                configParam.add(hasdetail);
                configParam.add(enable);
                configParam.add(enabletray);
                configParam.add(configid);
                configParam.add(userid);
                configParam.add(messagetypeid);
                if (isDefaultConfig){
                    configParam.remove(4);
                    configParam.add(otherParams.getString("description"));
                    configParam.add(user.getUID());
                    configParam.add(Util_DateTime.getNowDate());
                    configParam.add(Util_DateTime.getNowTime());
                }
                configParams.add(configParam);
                if(MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE.equals(hasdetail)){
                    List<Object> detailParam=new LinkedList<>();//明细表参数
                    int detailid=otherParams.getIntValue("configdetailid");
                    String type=otherParams.getString("type");
                    String path=otherParams.getString("path");
                    if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL.equals(type)){
                        path=MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL_VALUE;
                    }
                    if(StringUtils.isBlank(path)){
                        type=MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL;
                        path=MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL_VALUE;
                    }
                    detailParam.add(type);
                    detailParam.add(path);
                    detailParam.add(detailid);
                    detailParam.add(configid);
                    detailParam.add(userid);
                    if (isDefaultConfig){
                        detailParam.remove(3);
                        detailParam.set(2,messagetypeid);
                        detailParam.add(user.getUID());
                        detailParam.add(Util_DateTime.getNowDate());
                        detailParam.add(Util_DateTime.getNowTime());
                    }
                    detailParams.add(detailParam);
                }
            }

        }
        param.setConfigParams(configParams);
        param.setDetailParams(detailParams);
        return param;
    }

    private static String isEnableWithVal(String val){
        if(MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED.equalsIgnoreCase(val)){
            return MsgConfigConstant.MSG_DEFAULT_CONFIG_UNENABLE;
        }else if(MsgConfigConstant.MSG_FRONT_CONFIG_ABLED.equalsIgnoreCase(val)){
            return MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE;
        }
        return null;
    }
}
