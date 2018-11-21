package com.engine.msgcenter.cmd.msgsubconfig;

import com.api.browser.bean.SearchConditionGroup;
import com.api.browser.bean.SearchConditionItem;
import com.api.browser.bean.SearchConditionOption;
import com.api.browser.util.ConditionFactory;
import com.api.browser.util.ConditionType;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @ Description：高级搜索
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 13:16
 */
public class SearchCondition extends AbstractCommonCommand<Map<String,Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public SearchCondition(Map<String,Object> params,User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {

        Map<String,Object> apidatas  = new HashMap<String, Object>();
        ConditionFactory conditionFactory = new ConditionFactory(user);
        List<SearchConditionGroup> addGroups = new ArrayList<SearchConditionGroup>();
        List<SearchConditionItem> addItems = new ArrayList<SearchConditionItem>();

        //订阅名称——输入框
        SearchConditionItem subnameItem = conditionFactory.createCondition(ConditionType.INPUT, 388299, "name");
        addItems.add(subnameItem);

        //消息数据类型——选择框
        List<SearchConditionOption> datatypeList = new ArrayList<>();
        datatypeList.add(new SearchConditionOption("text",SystemEnv.getHtmlLabelName(608,user.getLanguage())));//文本 608
        datatypeList.add(new SearchConditionOption("binary",SystemEnv.getHtmlLabelName(388642,user.getLanguage())));//二进制 388642
        SearchConditionItem datatypeItem = conditionFactory.createCondition(ConditionType.SELECT, 388295, "datatype",datatypeList);
        addItems.add(datatypeItem);

        //第三方注册类——输入框
        SearchConditionItem classnameItem = conditionFactory.createCondition(ConditionType.INPUT, 388296, "classname");
        addItems.add(classnameItem);

        //通道类型——选择框
        List<SearchConditionOption> channeltypeList = new ArrayList<>();
        channeltypeList.add(new SearchConditionOption("Direct","Direct"));
        channeltypeList.add(new SearchConditionOption("Redis","Redis"));
        channeltypeList.add(new SearchConditionOption("ActiveMQ","ActiveMQ"));
        channeltypeList.add(new SearchConditionOption("RabbitMQ","RabbitMQ"));
        channeltypeList.add(new SearchConditionOption("DB","DB"));
        channeltypeList.add(new SearchConditionOption("Restful","Restful"));
        SearchConditionItem channeltypeItem = conditionFactory.createCondition(ConditionType.SELECT, 388298 , "channeltype",channeltypeList);
        addItems.add(channeltypeItem);

        //状态——选择框
        List<SearchConditionOption> statusList = new ArrayList<>();
        statusList.add(new SearchConditionOption("allStatus",SystemEnv.getHtmlLabelName(332,user.getLanguage()),true));
        statusList.add(new SearchConditionOption("enabled",SystemEnv.getHtmlLabelName(18095,user.getLanguage())));
        statusList.add(new SearchConditionOption("disabled",SystemEnv.getHtmlLabelName(18096,user.getLanguage())));
        SearchConditionItem statusItem = conditionFactory.createCondition(ConditionType.SELECT, 602 , "status",statusList);
        addItems.add(statusItem);

        addGroups.add(new SearchConditionGroup(SystemEnv.getHtmlLabelNames("388300",user.getLanguage()),true,addItems));
        apidatas.put("condition",addGroups);
        return apidatas;
    }
}