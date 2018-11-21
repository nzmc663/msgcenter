package com.engine.msgcenter.cmd.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.api.util.Util_DateTime;
import com.engine.SAPIntegration.util.StringUtil;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.constant.MsgConfigConstant;
import com.engine.msgcenter.dao.UserMsgConfigDao;
import com.engine.msgcenter.util.MsgConfigUtil;
import org.apache.commons.lang.StringUtils;
import weaver.conn.DBUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.*;

public class SyncUserMsgConfigCmd extends AbstractCommonCommand<Map<String, Object>> {

    public SyncUserMsgConfigCmd(User user, Map<String, Object> params) {
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> apidatas = new HashMap<>();

        // 验证参数
        if (!this.validateParams(params)) {
            apidatas.put("ret", false);
            apidatas.put("message", "Illegal user, please check args carefully.");
            return apidatas;
        }

        String toUserids=this.getUsers(params);
        // 获取被同步对象配置信息
        int fromUserid = Util.getIntValue(Util.null2String(params.get("from")));
        String dataJsonString=MsgConfigUtil.getUserConfig(fromUserid,user.getLanguage());

        // 获取同步用户列表及参数列表
        Param param = this.parseString2Param(Util.null2String(toUserids),dataJsonString);
        RecordSetTrans rst=new RecordSetTrans();
        UserMsgConfigDao dao=new UserMsgConfigDao();
        try {
            for (Object toUserid : param.getToUserids()) {
                //查询当前同步用户是否已有配置信息
                rst.executeQuery(dao.getUserDetailConfig(), toUserid,MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID,toUserid);
                if(rst.next()){
                    //已有配置信息则取出配置信息，更新参数列表，执行update语句
                    List<List<Object>> cidAndUids=new LinkedList<>();
                    List<List<Object>> didAndCids=new ArrayList<>();
                    do{
                        List<Object> cidAndUid=new ArrayList<>();
                        int configid=rst.getInt("configid");
                        cidAndUid.add(configid);
                        cidAndUid.add(toUserid);
                        cidAndUids.add(cidAndUid);
                        if(MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE.equalsIgnoreCase(rst.getString("hasdetail"))){
                            List<Object> didAndCid=new LinkedList<>();
                            didAndCid.add(rst.getInt("detailid"));
                            didAndCid.add(configid);
                            didAndCid.add(toUserid);
                            didAndCids.add(didAndCid);
                        }
                    }while(rst.next());
                    param.setUpdateConfigParamsCid(cidAndUids);
                    param.setUpdateDetailParamsDid(didAndCids);
                    rst.executeBatchSql(dao.updateConfigBatchSql(),param.getUpdateConfigParams());
                    rst.executeBatchSql(dao.updateConfigDetailBatchSql(),param.getUpdateDetailParams());
                }else{
                    //没有有配置信息，直接更新参数列表，执行insert语句
                    param.setInsertConfigParamsUserid(Util.getIntValue((String)toUserid));
                    param.setInsertDetailParamsUserid(Util.getIntValue((String)toUserid));
                    rst.executeBatchSql(dao.insertConfigBatchSql(),param.getInsertConfigParams());
                    rst.executeBatchSql(dao.insertConfigDetailBatchSql(),param.getInsertDetailParams());
                }
            }
            rst.commit();
            apidatas.put("ret",true);
            apidatas.put("message","successful operation.");
        } catch (Exception e) {
            e.printStackTrace();
            rst.rollback();
            apidatas.put("ret", false);
            apidatas.put("message",e.getMessage());
            return apidatas;
        }
        return apidatas;
    }

    /**
     * 校验参数
     */
    private boolean validateParams(Map<String, Object> params) {
        String fromUserid = Util.null2String(params.get("from"));
        if (!StringUtil.isNumber(fromUserid) || Util.getIntValue(fromUserid) <= 0) {
            return false;
        }
        String synctype = Util.null2String(params.get("synctype"));
        if (!StringUtil.isNumber(synctype) || Util.getIntValue(synctype) < 0) {
            return false;
        }
        return true;
    }
    /**
     * 获取需要被同步的人员id
     */
    private String getUsers(Map<String, Object> params){
        String sync=Util.null2String(params.get("synctype"));
        String toUser=Util.null2String(params.get("to"));
        UserMsgConfigDao dao=new UserMsgConfigDao();
        List<String> list=new ArrayList<>();
        Object[] objects=DBUtil.transListIn(toUser,list);
        String sql="";
        switch(sync){
            case "0"://人力资源
                return toUser;
            case "1"://分部
                sql=dao.getUseridByBrowser("subcompanyid1",Util.null2String(objects[0]));
                break;
            case "2"://部门
                sql=dao.getUseridByBrowser("departmentid",Util.null2String(objects[0]));
                break;
            case "3"://所有人
                sql=dao.getUseridByBrowser(null,null);
                break;
        }
        RecordSet rs=new RecordSet();
        if(StringUtils.isNotBlank(toUser)){
            rs.executeQuery(sql,list);
        }else{
            rs.executeQuery(sql);
        }
        StringBuilder sb=new StringBuilder(",");
        while(rs.next()){
            sb.append(rs.getString("id")).append(",");
        }
        return sb.toString();
    }

    /**
     * 将配置信息及用户id转为参数列表
     * @param paramString 同步用户id（多个用户之间半角逗号分隔）
     * @param configJsonString 被同步用户配置信息
     */
    private Param parseString2Param(String paramString,String configJsonString) {

        Param param = new Param();
        List<String> stringList = Util.splitString2List(Util.null2String(paramString), ",");
        //使用hashset将重复id去掉
        HashSet<String> toUserids = new HashSet<>();
        for (String string : stringList) {
            if (string.isEmpty())continue;
            if (StringUtil.isNumber(string)) {
                toUserids.add(string);
            }
        }
        param.setToUserids(new ArrayList<Object>(toUserids));

        JSONArray jsonArray=JSONArray.parseArray(Util.null2String(configJsonString));
        List<List<Object>> insertConfigParams=new ArrayList<>();//新增时配置主表参数
        List<List<Object>> insertDetailParams=new ArrayList<>();//新增时明细表参数
        List<List<Object>> updateConfigParams=new ArrayList<>();//更新时配置主表参数
        List<List<Object>> updateDetailParams=new ArrayList<>();//更新时明细表参数
        for(int i=0;i<jsonArray.size();i++){
            JSONObject obj=jsonArray.getJSONObject(i);

            List<Object> updateConfigParam=new LinkedList<>();//更新时配置主表参数
            int configid =obj.getIntValue("id");
            int messagetypeid=obj.getIntValue("messagetypeid");
            String include=obj.getString("include");
            String hasdetail=obj.getString("hasdetail");
            String enable=obj.getString("enable");
            String enabletray=obj.getString("enabletray");
            String description=obj.getString("description");

            updateConfigParam.add(include);
            updateConfigParam.add(hasdetail);
            updateConfigParam.add(enable);
            updateConfigParam.add(enabletray);
            updateConfigParam.add(configid);
            updateConfigParam.add(user.getUID());
            updateConfigParam.add(messagetypeid);
            updateConfigParams.add(updateConfigParam);

            List<Object> insertConfigParam=new LinkedList<>();//插入时配置主表参数
            insertConfigParam.addAll(updateConfigParam);
            insertConfigParam.remove(4);
            insertConfigParam.add(description);
            insertConfigParam.add(user.getUID());
            insertConfigParam.add(Util_DateTime.getNowDate());
            insertConfigParam.add(Util_DateTime.getNowTime());
            insertConfigParams.add(insertConfigParam);

            //如果有明细，则取出对应明细表中的数据
            if(MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE.equals(hasdetail)){

                List<Object> updateDetailParam=new LinkedList<>();//更新时明细表参数
                int detailid=obj.getIntValue("configdetailid");
                String type=obj.getString("type");
                String path=obj.getString("path");
                if(MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL.equals(type)){
                    path=MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL_VALUE;
                }
                if(StringUtils.isBlank(path)){
                    type=MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL;
                    path=MsgConfigConstant.MSG_DEFAULT_CONFIG_PATH_ALL_VALUE;
                }
                updateDetailParam.add(type);
                updateDetailParam.add(path);
                updateDetailParam.add(detailid);
                updateDetailParam.add(configid);
                updateDetailParam.add(user.getUID());
                updateDetailParams.add(updateDetailParam);

                List<Object> insertDetailParam=new LinkedList<>();//插入时明细表参数
                insertDetailParam.addAll(updateDetailParam);
                insertDetailParam.remove(3);
                insertDetailParam.set(2,messagetypeid);
                insertDetailParam.add(user.getUID());
                insertDetailParam.add(Util_DateTime.getNowDate());
                insertDetailParam.add(Util_DateTime.getNowTime());
                insertDetailParams.add(insertDetailParam);
            }
        }
        param.setInsertConfigParams(insertConfigParams);
        param.setInsertDetailParams(insertDetailParams);
        param.setUpdateConfigParams(updateConfigParams);
        param.setUpdateDetailParams(updateDetailParams);
        return param;
    }

    private class Param {

        private List<Object> toUserids;
        private List<List<Object>> insertConfigParams;//新增时配置主表参数
        private List<List<Object>> insertDetailParams;//新增时明细表参数
        private List<List<Object>> updateConfigParams;//更新时配置主表参数
        private List<List<Object>> updateDetailParams;//更新时明细表参数

        public List<Object> getToUserids() {
            return toUserids;
        }

        public void setToUserids(List<Object> toUserids) {
            this.toUserids = toUserids;
        }

        public List<List<Object>> getInsertConfigParams() {
            return insertConfigParams;
        }

        public void setInsertConfigParams(List<List<Object>> insertConfigParams) {
            this.insertConfigParams = insertConfigParams;
        }

        public List<List<Object>> getInsertDetailParams() {
            return insertDetailParams;
        }

        public void setInsertDetailParams(List<List<Object>> insertDetailParams) {
            this.insertDetailParams = insertDetailParams;
        }

        public List<List<Object>> getUpdateConfigParams() {
            return updateConfigParams;
        }

        public void setUpdateConfigParams(List<List<Object>> updateConfigParams) {
            this.updateConfigParams = updateConfigParams;
        }

        public List<List<Object>> getUpdateDetailParams() {
            return updateDetailParams;
        }

        public void setUpdateDetailParams(List<List<Object>> updateDetailParams) {
            this.updateDetailParams = updateDetailParams;
        }

        /**
         * 插入配置列表设置新的用户id
         */
        public void setInsertConfigParamsUserid(int userid){
            for(List<Object> list:insertConfigParams){
                list.set(4,userid);
                list.set(8,Util_DateTime.getNowDate());
                list.set(9,Util_DateTime.getNowTime());
            }
        }

        /**
         * 插入明细列表设置新的用户id
         */
        public void setInsertDetailParamsUserid(int userid){
            for(List<Object> list:insertDetailParams){
                list.set(3,userid);
                list.set(5,Util_DateTime.getNowDate());
                list.set(6,Util_DateTime.getNowTime());
            }
        }

        /**
         * 更新配置列表设置新的配置id
         */
        public void setUpdateConfigParamsCid(List<List<Object>> cidAndUids){
            for(int i=0;i<updateConfigParams.size();i++){
                List<Object> list=updateConfigParams.get(i);
                List<Object> cidAndUid=cidAndUids.get(i);
                list.set(4,cidAndUid.get(0));//主键configid
                list.set(5,cidAndUid.get(1));//userid
            }
        }
        /**
         * 更新明细列表设置新的明细id
         */
        public void setUpdateDetailParamsDid(List<List<Object>> didAndCids){
            for (int i=0;i<updateDetailParams.size();i++){
                List<Object> list=updateDetailParams.get(i);
                List<Object> didAndCid=didAndCids.get(i);
                list.set(2,didAndCid.get(0));//主键detailid
                list.set(3,didAndCid.get(1));//外键configid
                list.set(4,didAndCid.get(2));//用户id
            }
        }
    }

}
