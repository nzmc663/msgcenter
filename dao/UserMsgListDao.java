package com.engine.msgcenter.dao;

import weaver.conn.RecordSet;

import java.util.List;

public class UserMsgListDao {
    /**
     *
     * @param msgTypeList 消息类型列表
     * @return 根据消息创建日期和创建时间降序排列属于该消息类型列表的未读消息（分页）
     */
    public String getMsgList(List<Object> msgTypeList){
        RecordSet rs = new RecordSet();
        StringBuilder sql =new StringBuilder("");
        if ("sqlserver".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from (select row_number() over (order by createdate desc,createtime desc) as rowid,* from ECOLOGY_MESSAGE_INFO  where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(")) a  where rowid between ? and ?");
        }
        else if ("oracle".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from (select ROWNUM rn,a.* from ECOLOGY_MESSAGE_INFO a where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(") order by createdate desc,createtime desc ) where rn between ? and ? ");


        }
        else if ("mysql".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from ECOLOGY_MESSAGE_INFO where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(") order by createdate desc,createtime desc limit ?,?");
        }
        else if ("dm".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from (select ROWNUM rn,a.* from ECOLOGY_MESSAGE_INFO a where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(") order by createdate desc,createtime desc ) where rn between ? and ? ");
        }
        return sql.toString();
    }

    /**
     *
     * @param msgTypeList 消息类型列表
     * @return 根据消息创建日期和创建时间降序排列属于该消息类型列表的已读消息（分页）
     */
    public String getReadMsgList(List<Object> msgTypeList){
        RecordSet rs = new RecordSet();
        StringBuilder sql =new StringBuilder("");
        if ("sqlserver".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from (select row_number() over (order by createdate desc,createtime desc) as rowid,* from ECOLOGY_MESSAGE_INFO_READ  where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(")) a  where rowid between ? and ?");
        }
        else if ("oracle".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from (select ROWNUM rn,a.* from ECOLOGY_MESSAGE_INFO_READ a where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(") order by createdate desc,createtime desc ) where rn between ? and ? ");


        }
        else if ("mysql".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from ECOLOGY_MESSAGE_INFO_READ where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(") order by createdate desc,createtime desc limit ?,?");
        }
        else if ("dm".equalsIgnoreCase(rs.getDBType())) {
            sql.append("select * from (select ROWNUM rn,a.* from ECOLOGY_MESSAGE_INFO_READ a where userid = ? and messagetype in (");
            for(int i = 0;i<msgTypeList.size();i++){
                if(i == msgTypeList.size() - 1) {
                    sql.append(msgTypeList.get(i));
                    break;
                }
                sql.append(msgTypeList.get(i)).append(",");
            }
            sql.append(") order by createdate desc,createtime desc ) where rn between ? and ? ");
        }
        return sql.toString();
    }

}
