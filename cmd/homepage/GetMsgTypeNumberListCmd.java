package com.engine.msgcenter.cmd.homepage;

import com.cloudstore.api.util.Util_Redis;
import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.util.Util_Message;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.util.MsgTypeUtil;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.*;
/**
 * @ Author     ：汪路军
 * @ Date       ：Created in 10:01 2018/09/04
 * @ Description：消息中心主页面消息类型和数量
 * @ Modified By：
 * @Version:     1.0
 */
public class GetMsgTypeNumberListCmd extends AbstractCommonCommand<Map<String, Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    /**
     * 构造方法
     *
     * @param params
     * @param user
     */
    public GetMsgTypeNumberListCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        List<Map<String, String>> msgTypeNumberList = new ArrayList<>();//存放所有返回消息的消息类型、数量、id的list
        HashMap<String, Integer> typeMap = new HashMap<String, Integer>(); //key: 消息类型显示名 value：消息数量
        HashMap<String, String> msgTypeNumberMap = new HashMap<String, String>(); //封装每个消息类型、数量、id
        int code = 0;//消息类型id
        int sum = 0;//消息数量
        int number = 0;//全部消息数
        String namekey = "";//类型或类型组的名称
        List<Map> mapList = MsgTypeUtil.getMapList(user);
        Map<Integer, String> namemap = (HashMap<Integer, String>)mapList.get(0); //key：消息类型code value：消息类型显示名 （此处返回的消息类型代表已经被启用了）
        Map<String, String> nameIdMap = (HashMap<String, String>)mapList.get(2);//key:消息类型显示名称 value:消息类型名的唯一标识
        //初始化key:消息类型显示名 value：数量0
        for (Map.Entry<Integer, String> entry : namemap.entrySet())
            typeMap.put(entry.getValue(), 0);

            if (Util_Redis.getIstance()!=null) {
                List<MessageBean> list = null;//从缓存中获取所有消息列表
                try {
                    list = Util_Message.getMessageListNotHdel(String.valueOf(user.getUID()));
                    for (MessageBean messageBean : list) {
                        code = messageBean.getMessageType().getCode();
                        //被启用
                        if (namemap.containsKey(code)) {
                            namekey = namemap.get(code);//获取名称
                            sum = typeMap.get(namekey) + 1;
                            typeMap.put(namekey, sum);//显示名称和数量
                            number++;
                        }
                    }

                    msgTypeNumberMap.put("name", SystemEnv.getHtmlLabelName(82857, user.getLanguage()));//全部
                    msgTypeNumberMap.put("count", String.valueOf(0));
                    msgTypeNumberMap.put("id", "0");
                    msgTypeNumberList.add(msgTypeNumberMap);

                    //遍历有序的map,将消息类型名、消息数量、消息类型名的唯一标识封装在msgTypeNumberMap中
                    for (Map.Entry<String, String> entry : nameIdMap.entrySet()) {
                        msgTypeNumberMap = new HashMap<>();
                        msgTypeNumberMap.put("name", entry.getKey());//消息显示名
                        msgTypeNumberMap.put("count", String.valueOf(typeMap.get(entry.getKey())));//从typeMap中根据消息显示名获取数量
                        msgTypeNumberMap.put("id", entry.getValue());//消息类型名的唯一标识,（消息类型组，消息类型），合并显示的只有消息类型组id

                        msgTypeNumberList.add(msgTypeNumberMap);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                msgTypeNumberMap.put("name", SystemEnv.getHtmlLabelName(82857, user.getLanguage()));//全部
                msgTypeNumberMap.put("count", String.valueOf(0));
                msgTypeNumberMap.put("id", "0");
                msgTypeNumberList.add(msgTypeNumberMap);

                //遍历有序的map,将消息类型名、消息数量、消息类型名的唯一标识封装在msgTypeNumberMap中
                for (Map.Entry<String, String> entry : nameIdMap.entrySet()) {
                    msgTypeNumberMap = new HashMap<>();
                    msgTypeNumberMap.put("name", entry.getKey());//消息显示名
                    msgTypeNumberMap.put("count", String.valueOf(0));//从typeMap中根据消息显示名获取数量
                    msgTypeNumberMap.put("id", entry.getValue());//消息类型名的唯一标识,（消息类型组，消息类型），合并显示的只有消息类型组id

                    msgTypeNumberList.add(msgTypeNumberMap);

                }
            }

                //按照消息类型组和消息类型排序
                Collections.sort(msgTypeNumberList,new Comparator(){
                    @Override
                    public int compare(Object o1, Object o2) {
                        String u1 = (String) ((Map) o1).get("id");
                        String u2 = (String) ((Map) o2).get("id");
                        String[] mergeId1 = u1.split(",");//(消息类型组id,消息类型id)
                        String[] mergeId2 = u2.split(",");//(消息类型组id,消息类型id)
                        if(mergeId1[0].equals(mergeId2[0]))
                            return Integer.valueOf(mergeId1[1]).compareTo(Integer.valueOf(mergeId2[1]));
                        return Integer.valueOf(mergeId1[0]).compareTo(Integer.valueOf(mergeId2[0]));
                    }
                });

        apidatas.put("count",  String.valueOf(number));
        apidatas.put("item", msgTypeNumberList);
        return apidatas;
    }

}
