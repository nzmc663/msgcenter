package com.engine.msgcenter.cmd.msgpushlog;

import com.api.browser.bean.SearchConditionGroup;
import com.api.browser.bean.SearchConditionItem;
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
 * @ Description：高级搜索条件
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 */

public class DoSearchConditionCmd extends AbstractCommonCommand<Map<String,Object>> {


    public DoSearchConditionCmd(Map<String,Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> apidatas  = new HashMap<String, Object>();
        ConditionFactory conditionFactory = new ConditionFactory(user);
        List<SearchConditionGroup> addGroups = new ArrayList<SearchConditionGroup>();
        List<SearchConditionItem> addItems = new ArrayList<SearchConditionItem>();

        //提醒种类
        SearchConditionItem searchConditionItemType = conditionFactory.createCondition(ConditionType.INPUT, 15390, "remindTypes");
        addItems.add(searchConditionItemType);

        //提醒内容
        SearchConditionItem searchConditionItemText = conditionFactory.createCondition(ConditionType.INPUT, 15772 , "contexts");
        addItems.add(searchConditionItemText);

        //提醒接收人
        SearchConditionItem searchConditionItemUser = conditionFactory.createCondition(ConditionType.BROWSER, 15772 , "remindUsers");
        addItems.add(searchConditionItemUser);

        addGroups.add(new SearchConditionGroup(SystemEnv.getHtmlLabelNames("15774",user.getLanguage()),true,addItems));
        apidatas.put("condition",addGroups);
        return apidatas;
    }
}
