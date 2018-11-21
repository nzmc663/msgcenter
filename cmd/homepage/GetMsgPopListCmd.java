package com.engine.msgcenter.cmd.homepage;

import com.alibaba.fastjson.JSONObject;
import com.cloudstore.api.util.Util_Redis;
import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.util.Util_Message;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.util.MsgTypeUtil;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：汪路军
 * @ Date       ：Created in 10:01 2018/10/29
 * @ Description：消息中心右下角弹出提醒
 * @ Modified By：
 * @Version:     1.0
 */
public class GetMsgPopListCmd extends AbstractCommonCommand<Map<String, Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }
    public GetMsgPopListCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }
    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            List<Map> typeList = new ArrayList<>();//保存缓存和数据库中指定字段的消息
            Map<String,Object> typeDetailMap = new HashMap<>();//封装每条消息的指定字段信息
            List<Object> paramsValuesList = new ArrayList<>();//消息的params字段中的value值的集合
            String messagename = "";//消息显示名
            int code = 0;//消息类型;
            if(user!=null){
                List<Map> mapList = MsgTypeUtil.getMapList(user);
                Map<Integer, String> nameMap = (HashMap<Integer, String>)mapList.get(0); //key：消息类型code value：消息类型显示名 （此处返回的消息类型代表已经被启用了）
                if (Util_Redis.getIstance()!=null){
                    List<MessageBean> allList = Util_Message.getMessageListNotHdel(String.valueOf(user.getUID()));//所有消息的列表
                    //遍历缓存中的消息列表
                    for (MessageBean messageBean : allList) {
                        code = messageBean.getMessageType().getCode();//获取该消息类型的id
                        //判断该消息类型是否启用,并且该消息类型是属于选择要查看的消息类型
                        if (nameMap.containsKey(code)) {
                            messagename = nameMap.get(code);
                            typeDetailMap.put("name",messagename);
                            typeDetailMap.put("title",messageBean.getTitle());
                            for (Map.Entry<String,Object> entry : messageBean.getParams().entrySet())
                                paramsValuesList.add(entry.getValue());
                            typeDetailMap.put("params", paramsValuesList);
                            typeDetailMap.put("link",messageBean.getLinkUrl());

                            typeList.add(typeDetailMap); //将封装好的Map加入到typeList中
                            typeDetailMap = new HashMap<>();
                            paramsValuesList = new ArrayList<>();
                        }
                    }
                }

                apidatas.put("data", typeList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return apidatas;
    }
}
