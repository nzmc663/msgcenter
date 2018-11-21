package com.engine.msgcenter.web;

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.msgcenter.service.MsgPushLogService;
import com.engine.msgcenter.service.MsgSubConfigService;
import com.engine.msgcenter.service.impl.MsgPushLogServiceImpl;
import com.engine.msgcenter.service.impl.MsgSubConfigSerImpl;
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
 * @ Description：消息订阅设置
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 13:06
 */
public class MsgSubConfigAction {
    private MsgSubConfigService getService(HttpServletRequest request, HttpServletResponse response) {
        User user = HrmUserVarify.getUser(request, response);// 需要增加的代码
        return getService(user);
    }

    private MsgSubConfigService getService(User user) {
        return (MsgSubConfigSerImpl) ServiceUtil.getService(MsgSubConfigSerImpl.class, user);
    }
    private MsgSubConfigService getService(){
        return ServiceUtil.getService(MsgSubConfigSerImpl.class);
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
     * @description ：查询消息日志
     */
    @POST
    @Path("/queryMsgSubConfig")
    @Produces(MediaType.TEXT_PLAIN)
    public String querymsglog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String, Object>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            //实例化Service并调用业务类处理
            apidatas.putAll(getService().queryMsgSubConfig(ParamUtil.request2Map(request),user));
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
    @Path("/updateMsgSubConfig")
    @Produces(MediaType.TEXT_PLAIN)
    public String updateMsgLog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try{
            User user = HrmUserVarify.getUser(request,response);
            apidatas.putAll(getService().updateMsgSubConfig(ParamUtil.request2Map(request),user));
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
    @Path("/deleteMsgSubConfig")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteMsgLog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            map.putAll(getService().deleteMsgSubConfig(ParamUtil.request2Map(request),user));
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
    @Path("/addMsgSubConfig")
    @Produces(MediaType.TEXT_PLAIN)
    public String addMsgLog(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            map.putAll(getService().addMsgSubConfig(ParamUtil.request2Map(request),user));
        }catch (Exception e){
            e.printStackTrace();
            map.put("api_status",false);
            map.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * @description ：更改状态
     * @param       ：[request, response]
     * @return      ：java.lang.String
     */
    @GET
    @Path("/changeStatus")
    @Produces(MediaType.TEXT_PLAIN)
    public String changeStatus(@Context HttpServletRequest request,@Context HttpServletResponse response){
//        response.setContentType("text/html;charset=utf-8");
        Map<String,Object> map = new HashMap<>();
        try{
            User user = HrmUserVarify.getUser(request, response);
            map = getService().changeStatus(ParamUtil.request2Map(request),user);
        }catch (Exception e){
            e.printStackTrace();
            map.put("api_status",false);
            map.put("api_errormsg","catch exception : "+e.getMessage());
        }
        return JSONObject.toJSONString(map);
    }

}
