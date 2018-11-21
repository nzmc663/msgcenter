package com.engine.msgcenter.util;


import weaver.conn.RecordSet;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.*;

public class MsgTypeUtil {


    /**
     * @param message_groupid 消息类型组id
     * @return 获取消息类型组下的所有消息类型
     */
    public static List<Integer> getBelongGroupAllType(int message_groupid){
        RecordSet rs=new RecordSet();
        List<Integer> typeList = new ArrayList<>();
        rs.executeQuery("select * from ECOLOGY_MESSAGE_TYPE a inner join ECOLOGY_MESSAGE_GROUP b on ecology_message_groupid = b.id and b.id = ?",message_groupid);
        while(rs.next()){
            typeList.add(rs.getInt("id"));
        }
        return typeList;

    }

    /**
     * @param user
     * @return
     * namemap：key：消息类型id value：消息类型显示名s
     * idmap：key：消息类型id value：所属消息类型组id
     * nameIdMap key：消息类型显示名 value：消息类型名的唯一标识
     * 返回的map均是查询用户配置后，开启的消息类型
     */
    public static ArrayList<Map> getMapList(User user) {
        int userid = user.getUID();
        RecordSet rs1=new RecordSet();
        rs1.executeQuery("select *  from ECOLOGY_MESSAGE_CONFIG where userid = ? ",userid);
        //如果用户无配置信息，则查询默认配置
        if(!rs1.next())
            userid = -1;

        ArrayList<Map> arrayList = new ArrayList<>();
        Map<Integer, String> namemap = new HashMap<Integer, String>();//key:消息id ， value: 消息类型显示名称
        Map<Integer, Integer> idmap = new HashMap<Integer, Integer>();//key:消息类型id  value：所属消息类型组的id
        Map<String, String> nameIdMap = new HashMap<>();//key:消息类型显示名称 value:消息类型名的唯一标识

        RecordSet rs = new RecordSet();
        int typename = 0;//存放多语言标签id
        String htmlLabelName = "";//多语言标签名称
        String include = " ";
        rs.executeQuery("select typename,typecode,groupname,status,messagetypeid,enable,include,ecology_message_groupid from ECOLOGY_MESSAGE_TYPE a inner join ECOLOGY_MESSAGE_GROUP b on ecology_message_groupid = b.id inner join ECOLOGY_MESSAGE_CONFIG c on a.id = c.messagetypeid and userid = ? ", userid);
        while (rs.next()) {
            //是否被启用
            if ("y".equals(rs.getString("enable"))) {
                //判断是否合并显示，若是则显示消息组名，若不是显示消息类型名
                if ("y".equals(rs.getString("status"))) {
                    typename = rs.getInt("groupname");
                    nameIdMap.put(SystemEnv.getHtmlLabelName(typename, user.getLanguage()),rs.getString("ecology_message_groupid"));//若是合并显示，则消息类型名的唯一标识是类型组id
                } else {
                    typename = rs.getInt("typename");
                    nameIdMap.put(SystemEnv.getHtmlLabelName(typename, user.getLanguage()) ,(rs.getString("ecology_message_groupid") + ","+rs.getString("messagetypeid")));//若不是合并显示，则消息类型名的唯一标识是（消息类型组id，消息类型id）
                }
                htmlLabelName = SystemEnv.getHtmlLabelName(typename, user.getLanguage());
                //该类型是否包含其他类型，若是则将其他类型id加入，名称为该类型名
                include = rs.getString("include");
                if(!("".equals(include)) &&  !(null == include)) {
                    String[] includeId = include.split(",");//消息类型组id+消息类型id
                    for(int i = 0 ; i < includeId.length;i++) {
                        RecordSet rs2= new RecordSet();
                        rs2.executeQuery("select id ,typecode from  ECOLOGY_MESSAGE_TYPE where id = ?", includeId[i] );
                        rs2.next();
                        if(!namemap.containsKey(rs2.getInt("typecode")))
                        namemap.put(rs2.getInt("typecode"), htmlLabelName);
                    }
                }
                namemap.put(rs.getInt("typecode"), htmlLabelName);
                idmap.put(rs.getInt("messagetypeid"), rs.getInt("ecology_message_groupid"));
            }
        }
        arrayList.add(namemap);
        arrayList.add(idmap);
        arrayList.add(nameIdMap);
        return arrayList;
    }




}
