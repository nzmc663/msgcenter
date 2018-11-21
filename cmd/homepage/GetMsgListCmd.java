
package com.engine.msgcenter.cmd.homepage;
/**
 * @ Author     ：汪路军
 * @ Date       ：Created in 10:01 2018/09/21
 * @ Description：消息中心主页面返回分页消息列表
 * @ Modified By：
 * @Version:     1.0
 */
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.api.util.Util_Redis;
import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.util.Util_Message;
import com.cloudstore.eccom.common.WeaIndexManager;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.dao.UserMsgListDao;
import com.engine.msgcenter.util.MsgConfigUtil;
import com.engine.msgcenter.util.MsgTypeUtil;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;
import java.util.*;

/**
 * 获取所有的消息或者对应类型的消息
 */
public class GetMsgListCmd extends AbstractCommonCommand<Map<String, Object>> {
    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public GetMsgListCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }


    @Override
    public Map<String, Object> execute(CommandContext commandContext) {

        Map<String, Object> apidatas = new HashMap<String, Object>();

        try {
            String currentDateString = TimeUtil.getCurrentTimeString();
            String dateStr = "";//当前日期
            String timeStr = "";//当前时间
            String paramsStr = "";//消息的params字段
            List<Map> typeList = new ArrayList<>();//保存缓存和数据库中指定字段的消息
            Map<String,Object> typeDetailMap = new HashMap<>();//封装每条消息的指定字段信息
            List<Object> paramsValuesList = new ArrayList<>();//消息的params字段中的value值的集合
            String id = Util.null2String(params.get("id"));//选择要查看的消息类型
            String offset = Util.null2String(params.get("offset"));//分页偏移量
            String[] mergeId = id.split(",");//(消息类型组id,消息类型id)
            int pagesize =Util.getIntValue(Util.null2String(params.get("pagesize")));//分页，一页显示pagesize条消息

            int sum = 0;//用于记录缓存中消息数
            String messagename = "";//消息显示名
            int code = 0;//消息类型id
            boolean flag = false;

            List<Object> enableList = new ArrayList<>();
            List<Map> mapList = MsgTypeUtil.getMapList(user);
            Map<Integer, String> nameMap = (HashMap<Integer, String>)mapList.get(0); //key：消息类型code value：消息类型显示名 （此处返回的消息类型代表已经被启用了）
            Map<Integer,Integer> idMap = (HashMap<Integer, Integer>)mapList.get(1); //key：消息类型id value：消息类型组id （此处返回的消息类型代表已经被启用了）
            if (Util_Redis.getIstance()!=null){
                List<MessageBean> allList = Util_Message.getMessageListNotHdel(String.valueOf(user.getUID()));//所有消息的列表
                //遍历缓存中的消息列表
                for (MessageBean messageBean : allList) {
                    code = messageBean.getMessageType().getCode();//获取该消息类型的id
                    //判断该消息类型是否启用,并且该消息类型是属于选择要查看的消息类型
                    if (nameMap.containsKey(code)) {
                        messagename = nameMap.get(code);
                        if("0".equals(mergeId[0])) //0表示全部
                            flag = true;
                        else if(mergeId.length == 1)//比较类型组id，是“提到我的”和“相关交流”
                            flag = mergeId[0].equals(String.valueOf(idMap.get(code)) );
                        else
                            flag = mergeId[1].equals(String.valueOf(code));//比较消息类型id

                        if (flag) {
                            typeDetailMap.put("time",messageBean.getDate()+" " +messageBean.getTime());
                            typeDetailMap.put("name",messagename);
                            typeDetailMap.put("title",messageBean.getTitle());
                            for (Map.Entry<String,Object> entry : messageBean.getParams().entrySet())
                                paramsValuesList.add(entry.getValue());
                            typeDetailMap.put("params", paramsValuesList);
                            typeDetailMap.put("link",messageBean.getLinkUrl());

                            typeList.add(typeDetailMap); //将封装好的Map加入到typeList中
                            typeDetailMap = new HashMap<>();
                            paramsValuesList = new ArrayList<>();
                            dateStr = currentDateString.substring(0,10);
                            timeStr = currentDateString.substring(11);
                            saveReadMessage( messageBean,dateStr,timeStr);//查看完消息之后删除，并插入到数据库中
                            if(++sum == pagesize)
                                break;
                        }
                    }
                }
            }
            //当未读表中的消息不足pagesize，需要从已读表中获取
            if(sum < pagesize){
                if("0".equals(mergeId[0])) //0表示全部
                    enableList = MsgConfigUtil.getUserMsgTypeEnabled(user.getUID(),"enable","y",true);//获取全部开启的消息类型id列表
                else if(mergeId.length == 1)//比较类型组id，是“提到我的”和“相关交流”
                    enableList = MsgConfigUtil.getUserMsgTypeGroup(user.getUID(),Integer.parseInt(mergeId[0]),"y");//获取类型组下开启的消息类型id列表
                else
                    enableList = MsgConfigUtil.getUserMsgTypeInclude(user.getUID(),Integer.parseInt(mergeId[1]),"y",true);//获取类型及其包含的类型id列表

                UserMsgListDao userMsgListDao =  new UserMsgListDao();//获取对应数据库的查询语句
                RecordSet rs = new RecordSet();
                if(!enableList.isEmpty()){
                        rs.executeQuery(userMsgListDao.getReadMsgList(enableList),user.getUID(),(Integer.parseInt(offset)*pagesize + sum +1),(Integer.parseInt(offset)*pagesize+ pagesize));//查询已读表中消息类型属于enableList中的消息，并按降序排列和分页数据
                        //将rs中查到的数据添加到list中
                        while (rs.next()) {
                            typeDetailMap.put("time",rs.getString("createdate") + " " + rs.getString("createtime"));
                            typeDetailMap.put("name",nameMap.get(rs.getInt("messagetype")));//添加消息类型
                            typeDetailMap.put("title",rs.getString("title"));//添加消息标题
                            if(StringUtils.isNotBlank(paramsStr=rs.getString("params"))){
                                for (Map.Entry<String,Object> entry : ((Map<String,Object>)JSONObject.parse(paramsStr)).entrySet())
                                    paramsValuesList.add(entry.getValue());
                            }
                            typeDetailMap.put("params", paramsValuesList);
                            typeDetailMap.put("link",rs.getString("linkurl"));

                            typeList.add(typeDetailMap);//将封装好的Map 加入到typeList中
                            typeDetailMap = new HashMap<>();
                            paramsValuesList = new ArrayList<>();
                        }
                }

            }

            //按照消息时间从小到大排序
            Collections.sort(typeList,new Comparator(){
                @Override
                public int compare(Object o1, Object o2) {
                    String u1 = (String) ((Map) o1).get("time");
                    String u2 = (String) ((Map) o2).get("time");
                    return u1.compareTo(u2);
                }
            });
            apidatas.put("data", typeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apidatas;
    }

    /**
     * 删除消息时，保存已读表中
     * @param messageBean 消息
     * @param readdate 读取日期
     * @param readtime 读取时间
     * @return
     */
    public boolean saveReadMessage(MessageBean messageBean,String readdate,String readtime) {
        RecordSet rs = new RecordSet();
        boolean flag = rs.executeUpdate("insert into ECOLOGY_MESSAGE_INFO_READ (" +
                        "id," +
                        "messageid," +
                        "messagetype," +
                        "userid," +
                        "targetid," +
                        "targetname," +
                        "title," +
                        "contexts," +
                        "creater," +
                        "description," +
                        "linkurl," +
                        "params," +
                        "clientip," +
                        "createdate," +
                        "createtime," +
                        "readdate," +
                        "readtime) values" +
                        " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                messageBean.getMessageId(),
                messageBean.getMessageUnitId(),
                messageBean.getMessageType().getCode(),
                user.getUID(),
                messageBean.getTargetId(),
                messageBean.getTargetName(),
                messageBean.getTitle(),
                messageBean.getContext(),
                messageBean.getCreater(),
                messageBean.getDesc(),
                messageBean.getLinkUrl(),
                JSONObject.toJSONString(messageBean.getParams()),
                messageBean.getClientIp(),
                messageBean.getDate(),
                messageBean.getTime(),
                readdate,
                readtime);
        try {
            long data = Util_Message.delMessage(messageBean);
            if(flag&&(data!=0))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

