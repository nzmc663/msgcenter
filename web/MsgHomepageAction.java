package com.engine.msgcenter.web;

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.integration.gconst.IntegrationConstant;
import com.engine.msgcenter.service.MsgHomepageService;
import com.engine.msgcenter.service.impl.MsgHomepageServiceImpl;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
/**
 * @ Author     ：汪路军
 * @ Date       ：Created in 10:01 2018/09/04
 * @ Description：消息中心主页面Action
 * @ Modified By：
 * @Version:     1.0
 */
public class MsgHomepageAction {
    private MsgHomepageService getService(){
        return ServiceUtil.getService(MsgHomepageServiceImpl.class);
    }

    /**
     * 获取消息中心首页右侧消息类型及消息数量
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/getMsgTypeNumberList")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMsgTypeNumberList(@Context HttpServletRequest request, @Context HttpServletResponse response){

        Map<String, Object> apidatas = new HashMap<>();
        User user = HrmUserVarify.checkUser(request, response);
        Map<String,Object> params = ParamUtil.request2Map(request);
        //获取数据
        apidatas.putAll(getService().getMsgTypeNumberList(params,user));

        //设置返回状态正常
        apidatas.put(IntegrationConstant.INTEGRATION_RESULT_STATUS, IntegrationConstant.INTEGRATION_RESULT_STATUS_NORMAL);
        return JSONObject.toJSONString(apidatas);

    }


    /**
     * 获取消息中心全部消息
     * @param request
     * @param response
     * @param id
     * @param offset
     * @param pagesize
     * @return
     */
    @POST
    @Path("/getMsgList")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMsgList(@Context HttpServletRequest request, @Context HttpServletResponse response,@FormParam("id") String id,@FormParam("offset") String offset,@FormParam("pagesize") String pagesize){
        Map<String, Object> apidatas = new HashMap<>();

        User user = HrmUserVarify.checkUser(request, response);
        Map<String,Object> params = ParamUtil.request2Map(request);
        //获取数据
        apidatas.putAll(getService().getMsgList(params,user));

        //设置返回状态正常
        apidatas.put(IntegrationConstant.INTEGRATION_RESULT_STATUS, IntegrationConstant.INTEGRATION_RESULT_STATUS_NORMAL);
        return JSONObject.toJSONString(apidatas);

    }

    /**
     * 获取消息中心 右下角消息弹出列表
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/getPopList")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPopList(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String, Object> apidatas = new HashMap<>();

        User user = HrmUserVarify.checkUser(request, response);
        Map<String,Object> params = ParamUtil.request2Map(request);
        //获取数据
        apidatas.putAll(getService().getPopList(params,user));

        //设置返回状态正常
        apidatas.put(IntegrationConstant.INTEGRATION_RESULT_STATUS, IntegrationConstant.INTEGRATION_RESULT_STATUS_NORMAL);
        return JSONObject.toJSONString(apidatas);

    }

    /**
     * 获取消息中心消息模块
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/getModule")
    @Produces(MediaType.TEXT_PLAIN)
    public String getModule(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String, Object> apidatas = new HashMap<>();

        User user = HrmUserVarify.checkUser(request, response);
        Map<String,Object> params = ParamUtil.request2Map(request);
        //获取数据
        apidatas.putAll(getService().getModule(params,user));

        //设置返回状态正常
        apidatas.put(IntegrationConstant.INTEGRATION_RESULT_STATUS, IntegrationConstant.INTEGRATION_RESULT_STATUS_NORMAL);
        return JSONObject.toJSONString(apidatas);

    }
}
