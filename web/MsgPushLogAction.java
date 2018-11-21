package com.engine.msgcenter.web;

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.msgcenter.service.MsgPushLogService;
import com.engine.msgcenter.service.impl.MsgPushLogServiceImpl;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/*
 * @ Description：消息推送日志Action
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 */

public class MsgPushLogAction {

    private MsgPushLogService getService(HttpServletRequest request, HttpServletResponse response) {
        User user = HrmUserVarify.getUser(request, response);// 需要增加的代码
        return getService(user);
    }

    private MsgPushLogService getService(User user) {
        return (MsgPushLogServiceImpl) ServiceUtil.getService(MsgPushLogServiceImpl.class, user);
    }
    private MsgPushLogService getService(){
        return ServiceUtil.getService(MsgPushLogServiceImpl.class);
    }

    /**
     * @description ：高级搜索条件
     */
    @GET
    @Path("/getCondition")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCondition(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String, Object>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            //实例化Service并调用业务类处理
            apidatas.putAll(getService(user).getCondition(ParamUtil.request2Map(request),user));
            apidatas.put("api_status", true);
        }catch(Exception e){
            e.printStackTrace();
            apidatas.put("api_status",false);
            apidatas.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * @description ：显示接收人名字
     */
    @POST
    @Path("/showUsername")
    @Produces(MediaType.TEXT_PLAIN)
    public String showUsername(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String, Object>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            //实例化Service并调用业务类处理
            apidatas.putAll(getService(user).showUsername(ParamUtil.request2Map(request),user));
            apidatas.put("api_status", true);
        }catch(Exception e){
            e.printStackTrace();
            apidatas.put("api_status",false);
            apidatas.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return apidatas.get("usernames").toString();
    }

    /**
     * @description ：查询消息日志
     */
    @POST
    @Path("/queryMsgLog")
    @Produces(MediaType.TEXT_PLAIN)
    public String querymsglog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String, Object>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            //实例化Service并调用业务类处理
            apidatas.putAll(getService().queryMsgLog(ParamUtil.request2Map(request),user));
        }catch(Exception e){
            e.printStackTrace();
            apidatas.put("api_status",false);
            apidatas.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * @description ：修改消息日志
     */
    @POST
    @Path("/updateMsgLog")
    @Produces(MediaType.TEXT_PLAIN)
    public String updateMsgLog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try{
            User user = HrmUserVarify.getUser(request,response);
            apidatas.putAll(getService().updateMsgLog(ParamUtil.request2Map(request),user));
        }catch (Exception e){
            e.printStackTrace();
            apidatas.put("api_status",false);
            apidatas.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * @description ：删除消息日志
     */
    @GET
    @Path("/deleteMsgLog")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteMsgLog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            map.putAll(getService().deleteMsgLog(ParamUtil.request2Map(request),user));
        }catch (Exception e){
            e.printStackTrace();
            map.put("api_status",false);
            map.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * @description ：添加消息日志
     */
    @POST
    @Path("/addMsgLog")
    @Produces(MediaType.TEXT_PLAIN)
    public String addMsgLog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            map.putAll(getService().addMsgLog(ParamUtil.request2Map(request),user));
        }catch (Exception e){
            e.printStackTrace();
            map.put("api_status",false);
            map.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * @description ：查询消息对外推送日志
     */
    @POST
    @Path("/queryMsgSubLog")
    @Produces(MediaType.TEXT_PLAIN)
    public String queryMsgSubLog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String, Object>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            //实例化Service并调用业务类处理
            apidatas.putAll(getService().queryMsgSubLog(ParamUtil.request2Map(request),user));
        }catch(Exception e){
            e.printStackTrace();
            apidatas.put("api_status",false);
            apidatas.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }
}
