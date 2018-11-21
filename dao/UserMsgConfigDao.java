package com.engine.msgcenter.dao;

import org.apache.commons.lang.StringUtils;

/**
 *消息中心用户配置dao类
 */
public class UserMsgConfigDao {

    private StringBuffer stringBuffer;

    private final String BLANK = " ";
    private final String SELECT = "SELECT";
    private final String UPDATE = "UPDATE";
    private final String DELETE = "DELETE";
    private final String INSERT = "INSERT";
    private final String ORDEYBY = "ORDER BY";

    public UserMsgConfigDao(){
        this.stringBuffer=new StringBuffer();
    }

    public String getCustomSelectSql(String backfield,String fromsql,String sqlwhere,String orderby,String sortway){
        stringBuffer.setLength(0);
        stringBuffer.append(SELECT).append(BLANK).append(backfield).append(BLANK).append(fromsql).append(BLANK).append(sqlwhere).append(BLANK).append(ORDEYBY).append(BLANK).append(orderby);
        if(StringUtils.isNotBlank(sortway)){
            stringBuffer.append(BLANK).append(sortway);
        }
        String sql =stringBuffer.toString();
        stringBuffer.setLength(0);
        return sql;
    }

    /**
     * 通过用户id查询用户详细配置
     */
    public String getUserDetailConfig(){

        String backfield="config.id as configid,userid,messagetypeid,hasdetail,include,enable,enabletray,detail.id as detailid,type,path," +
                "type.id as typeid,typename,typecode,groups.id as groupid,groupname,groupcode,status,config.description";

        String fromsql= "from ECOLOGY_MESSAGE_CONFIG config left join ECOLOGY_MESSAGE_CONFIG_DETAIL detail " +
                " on config.id=detail.ecology_message_config_id ,ECOLOGY_MESSAGE_TYPE type, ECOLOGY_MESSAGE_GROUP groups";

        String sqlwhere="where config.messagetypeid=type.id and type.ecology_message_groupid=groups.id and (userid=? or (config.userid=? and config.messagetypeid not in ( select messagetypeid from ECOLOGY_MESSAGE_CONFIG where userid=?))) ";

        String orderby="groupid,typeid";

        return getCustomSelectSql(backfield,fromsql,sqlwhere,orderby,null);

    }

    /**
     * 通过用户id查询用户详细配置
     */
    public String getUserDetailConfig(String moduleid){
        String backfield="config.id as configid,userid,messagetypeid,hasdetail,include,enable,enabletray,detail.id as detailid,type,path," +
                "type.id as typeid,typename,typecode,groups.id as groupid,groupname,groupcode,status,config.description";

        String fromsql= "from ECOLOGY_MESSAGE_CONFIG config left join ECOLOGY_MESSAGE_CONFIG_DETAIL detail " +
                " on config.id=detail.ecology_message_config_id ,ECOLOGY_MESSAGE_TYPE type, ECOLOGY_MESSAGE_GROUP groups";

        String sqlwhere="where config.messagetypeid=type.id and type.ecology_message_groupid=groups.id and config.userid=? and groups.groupcode=?";

        String orderby="groupid,typeid";

        return getCustomSelectSql(backfield,fromsql,sqlwhere,orderby,null);

    }
    /**
     * 通过用户id查询用户配置主表信息
     */
    public String getUserConfig(){
        return "select * from ECOLOGY_MESSAGE_CONFIG where userid=?";
    }

    public String insertConfigBatchSql(){
        return "insert into ECOLOGY_MESSAGE_CONFIG (include,hasdetail,enable,enabletray,userid,messagetypeid,description,creater,createdate,createtime) "+
                "values(?,?,?,?,?,?,?,?,?,?)";
    }

    public String insertConfigDetailBatchSql(){
        return "insert into ECOLOGY_MESSAGE_CONFIG_DETAIL (type,path,ecology_message_config_id,creater,createdate,createtime) "+
                "values(?,?,(select id from ECOLOGY_MESSAGE_CONFIG where messagetypeid=? and userid=?),?,?,?)";
    }

    public String updateConfigBatchSql(){
        return "update ECOLOGY_MESSAGE_CONFIG set include=?,hasdetail=?,enable=?,enabletray=? where id=? and userid=? and messagetypeid=?";
    }

    public String updateConfigDetailBatchSql(){
        return "update ECOLOGY_MESSAGE_CONFIG_DETAIL set type=?,path=? where id=? and (select userid from ECOLOGY_MESSAGE_CONFIG where id=?)=?";
    }

    public String deleteConfigDetailBatchSql(){
        return "delete from ECOLOGY_MESSAGE_CONFIG_DETAIL where ecology_message_config_id in (select id from ECOLOGY_MESSAGE_CONFIG where userid=?)";
    }

    public String deleteConfigBatchSql(){
        return "delete from ECOLOGY_MESSAGE_CONFIG where userid=?";
    }

    public String insertSyncConfigBatchSql(){
        return "insert into ECOLOGY_MESSAGE_CONFIG(userid,messagetypeid,include,hasdetail,enable,enabletray,creater,createdate,createtime) " +
                "(select ? as userid , messagetypeid,include,hasdetail,enable,enabletray,? as creater,? as createdate,? as createtime FROM ECOLOGY_MESSAGE_CONFIG where userid=?)";
    }

    public String insertSyncConfigDetailBatchSql(){
        return "insert into ECOLOGY_MESSAGE_CONFIG(userid,messagetypeid,include,hasdetail,enable,enabletray,creater,createdate,createtime) " +
                "(select ? as userid , messagetypeid,include,hasdetail,enable,enabletray,? as creater,? as createdate,? as createtime FROM ECOLOGY_MESSAGE_CONFIG where userid=?)";
    }

    public String getUserConfgDetailSql(){
        return "select userid,messagetypeid,detail.* from ECOLOGY_MESSAGE_CONFIG config,ECOLOGY_MESSAGE_CONFIG_DETAIL detail where detail.ecology_message_config_id=config.id and userid=?";
    }

    public String getUseridByBrowser(String field,String ids){
        String sql="select t1.id from HrmResource t1 where (t1.status = 0 or t1.status = 1 or t1.status = 2 or t1.status = 3)";
        if(StringUtils.isBlank(field)&&StringUtils.isBlank(ids)){
            return sql;
        }else{
            sql=new StringBuilder(sql).append(" and ").append(field).append(" in ( ").append(ids).append(" )").toString();
        }
        return sql;
    }

}
