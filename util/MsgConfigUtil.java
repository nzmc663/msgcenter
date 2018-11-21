package com.engine.msgcenter.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.engine.msgcenter.constant.MsgConfigConstant;
import com.engine.msgcenter.dao.UserMsgConfigDao;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tianzefa on 2018/08/31.
 * 消息配置工具类
 */
public class MsgConfigUtil {

    public static String getUserConfig(int userid,int language){
        RecordSet rs=new RecordSet();
        UserMsgConfigDao dao=new UserMsgConfigDao();
        //查询用户配置信息
        rs.executeQuery(dao.getUserDetailConfig(),userid,MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID,userid);
        //如果用户无配置信息，则查询默认配置
        if(!rs.next()){
            //查询库中的默认配置信息
            rs.executeQuery(dao.getUserDetailConfig(),MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID);
            rs.next();
        }
        JSONArray jsonArray=new JSONArray();
        do {
            JSONObject obj=new JSONObject();
            obj.put("id", rs.getInt("configid"));
            obj.put("userid",userid);
            obj.put("hasdetail",rs.getString("hasdetail"));
            obj.put("enable",rs.getString("enable"));
            obj.put("enabletray",rs.getString("enabletray"));
            obj.put("include",rs.getString("include"));

            obj.put("configdetailid",rs.getInt("detailid"));
            obj.put("type",rs.getString("type"));
            obj.put("path", rs.getString("path"));

            obj.put("messagetypeid",rs.getInt("typeid"));
            obj.put("messagetypename",SystemEnv.getHtmlLabelName(rs.getInt("typename"),language));
            obj.put("typecode",rs.getString("typecode"));

            obj.put("messagegroupid",rs.getString("groupid"));
            obj.put("messagegroupname",SystemEnv.getHtmlLabelName(rs.getInt("groupname"),language));
            obj.put("groupcode",rs.getString("groupcode"));
            obj.put("messagegroupstatus",rs.getString("status"));

            jsonArray.add(obj);
        }while(rs.next());
        return jsonArray.toJSONString();
    }
    /**
     * 获取用户配置中某字段是否开启的所有消息类型
     * @param userid 用户id
     * @param field 数据库字段名（ex:"enable"、"enableTray"、"hasdetail"）
     * @param isEnabled 是否开启（y:开启 n:关闭）
     * @param hasInclude 是否需要包含include中所有消息类型
     */
    public static List<Object> getUserMsgTypeEnabled(int userid,String field,String isEnabled,boolean hasInclude){
        RecordSet rs=new RecordSet();
        Set<Object> enableTypeSet=new HashSet<>();
        UserMsgConfigDao dao=new UserMsgConfigDao();
        //查询用户配置信息
        rs.executeQuery(dao.getUserDetailConfig(),userid,MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID,userid);
        //如果用户无配置信息，则查询默认配置
        if(!rs.next()){
            //查询库中的默认配置信息
            rs.executeQuery(dao.getUserConfig(),MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID);
            rs.next();
        }
        do{
            String enableField=rs.getString(field);
            if(Util.null2String(isEnabled).equalsIgnoreCase(enableField)){
                enableTypeSet.add(rs.getInt("messagetypeid"));
                if(hasInclude&&StringUtils.isNotBlank(rs.getString("include"))){
                    List<String> includeMsgType=Util.splitString2List(rs.getString("include"),MsgConfigConstant.MSG_DEFAULT_CONFIG_SEPARATOR);
                    enableTypeSet.addAll(includeMsgType);
                }
            }
        }while(rs.next());
        RecordSet rs1=new RecordSet();
        StringBuilder sql =new StringBuilder("");
        ArrayList<Object> arrayList = new ArrayList<>(enableTypeSet);
        sql.append("select typecode from ECOLOGY_MESSAGE_TYPE where id in (");
        for(int i = 0;i<arrayList.size();i++){
            if(i == arrayList.size() - 1) {
                sql.append(arrayList.get(i));
                break;
            }
            sql.append(arrayList.get(i)).append(",");
        }
        sql.append(")");
        rs1.executeQuery(sql.toString());
        ArrayList<Object> arrayList2 = new ArrayList<>();
        while(rs1.next()){
            arrayList2.add(rs1.getString("typecode"));
        }
        return arrayList2;
    }



    /**
     * 根据消息类型获取用户配置，若该消息类型未开启，则返回null，否则返回该消息类型以及其包含的类型
     * @param userid 用户id
     * @param messagetype 消息类型id
     * @param isEnabled 是否开启（y:开启 n:关闭）
     * @param hasInclude 是否需要包含include中所有消息类型
     */
    public static List<Object> getUserMsgTypeInclude(int userid,int messagetype,String isEnabled,boolean hasInclude){
        RecordSet rs=new RecordSet();
        Set<Object> enableTypeSet=new HashSet<>();
        UserMsgConfigDao dao=new UserMsgConfigDao();
        //查询用户配置信息
        rs.executeQuery(dao.getUserDetailConfig(),userid,MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID,userid);
        //如果用户无配置信息，则查询默认配置
        if(!rs.next()){
            //查询库中的默认配置信息
            rs.executeQuery(dao.getUserConfig(),MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID);
            rs.next();
        }

        do{
            int messagetypeid=rs.getInt("messagetypeid");//获取消息类型id
            //该消息类型开启了并且与传入的消息类型相等
            if(Util.null2String(isEnabled).equals(rs.getString("enable")) && messagetype==messagetypeid ){
                enableTypeSet.add(messagetypeid);
                //查看包含的消息类型
                if(hasInclude&&StringUtils.isNotBlank(rs.getString("include"))){
                    List<String> includeMsgType=Util.splitString2List(rs.getString("include"),MsgConfigConstant.MSG_DEFAULT_CONFIG_SEPARATOR);
                    enableTypeSet.addAll(includeMsgType);
                }
            }
        }while(rs.next());
        RecordSet rs1=new RecordSet();
        StringBuilder sql =new StringBuilder("");
        ArrayList<Object> arrayList = new ArrayList<>(enableTypeSet);
        sql.append("select typecode from ECOLOGY_MESSAGE_TYPE where id in (");
        for(int i = 0;i<arrayList.size();i++){
            if(i == arrayList.size() - 1) {
                sql.append(arrayList.get(i));
                break;
            }
            sql.append(arrayList.get(i)).append(",");
        }
        sql.append(")");
        rs1.executeQuery(sql.toString());
        ArrayList<Object> arrayList2 = new ArrayList<>();
        while(rs1.next()){
            arrayList2.add(rs1.getString("typecode"));
        }
        return arrayList2;
    }


    /**
     * 根据消息类型组获取用户配置，若该消息类型未开启，则返回null，否则返回其包含的类型
     * @param userid 用户id
     * @param message_groupid 消息类型组id
     * @param isEnabled 是否开启（y:开启 n:关闭）
     */
    public static List<Object> getUserMsgTypeGroup(int userid,int message_groupid,String isEnabled){
        RecordSet rs=new RecordSet();
        Set<Object> enableTypeSet=new HashSet<>();
        UserMsgConfigDao dao=new UserMsgConfigDao();
        //查询用户配置信息
        rs.executeQuery(dao.getUserDetailConfig(),userid,MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID,userid);
        //如果用户无配置信息，则查询默认配置
        if(!rs.next()){
            //查询库中的默认配置信息
            rs.executeQuery(dao.getUserConfig(),MsgConfigConstant.MSG_DEFAULT_CONFIG_USERID);
            rs.next();
        }
        List<Integer> allTypeList = MsgTypeUtil.getBelongGroupAllType(message_groupid);
        do{
            int messagetypeid=rs.getInt("messagetypeid");//获取消息类型id
            //查看开启还是关闭的消息类型
            if(Util.null2String(isEnabled).equals(rs.getString("enable")) ){
                //满足条件并且是属于该消息类型组
                if(allTypeList.contains(messagetypeid))
                    enableTypeSet.add(messagetypeid);
            }
        }while(rs.next());
        RecordSet rs1=new RecordSet();
        StringBuilder sql =new StringBuilder("");
        ArrayList<Object> arrayList = new ArrayList<>(enableTypeSet);
        sql.append("select typecode from ECOLOGY_MESSAGE_TYPE  where id in (");
        for(int i = 0;i<arrayList.size();i++){
            if(i == arrayList.size() - 1) {
                sql.append(arrayList.get(i));
                break;
            }
            sql.append(arrayList.get(i)).append(",");
        }
        sql.append(")");
        rs1.executeQuery(sql.toString());
        ArrayList<Object> arrayList2 = new ArrayList<>();
        while(rs1.next()){
            arrayList2.add(rs1.getString("typecode"));
        }
        return arrayList2;
    }
}
