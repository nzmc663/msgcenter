package com.engine.msgcenter.cmd.config;

import com.api.browser.bean.BrowserBean;
import com.api.browser.bean.SearchConditionItem;
import com.api.browser.bean.SearchConditionOption;
import com.api.browser.util.BrowserConstant;
import com.api.browser.util.ConditionFactory;
import com.api.browser.util.ConditionType;
import com.api.formmode.page.util.Util;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetSyncFormConditionCmd extends AbstractCommonCommand<Map<String, Object>> {

    public GetSyncFormConditionCmd(User user, Map<String, Object> params) {
        this.user=user;
        this.params=params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> apidatas=new HashMap<>();
        String userid= Util.null2String(params.get("userid"));
        List<SearchConditionItem> conditions=new ArrayList<>();

        ConditionFactory conditionFactory = new ConditionFactory(user);

        SearchConditionItem fromUseridItem=conditionFactory.createCondition(ConditionType.INPUT,731, "from");
        fromUseridItem.setColSpan(1);
        fromUseridItem.setValue(userid);
        fromUseridItem.setLabelcol(6);
        fromUseridItem.setFieldcol(12);
        conditions.add(fromUseridItem);

        List<SearchConditionOption> options=new ArrayList<>();
        options.add(new SearchConditionOption("0", SystemEnv.getHtmlLabelName(179,user.getLanguage()),true));
        options.add(new SearchConditionOption("1", SystemEnv.getHtmlLabelName(141,user.getLanguage())));
        options.add(new SearchConditionOption("2", SystemEnv.getHtmlLabelName(124,user.getLanguage())));
        options.add(new SearchConditionOption("3", SystemEnv.getHtmlLabelName(1340,user.getLanguage())));
        SearchConditionItem syncTypeItem=conditionFactory.createCondition(ConditionType.SELECT, 388551, "synctype",options);
        syncTypeItem.setColSpan(1);
        syncTypeItem.setLabelcol(6);
        syncTypeItem.setFieldcol(12);
        conditions.add(syncTypeItem);

        SearchConditionItem syncUserItem=conditionFactory.createCondition(ConditionType.BROWSER, 106, "to");
        BrowserBean browser=new BrowserBean();
        browser.init();
        browser.setViewAttr(3);
        browser.setType("17");
        browser.setIsSingle(false);
        browser.setTitle(SystemEnv.getHtmlLabelName(179,user.getLanguage()));
        syncUserItem.setBrowserConditionParam(browser);
        syncUserItem.setColSpan(1);
        syncUserItem.setViewAttr(3);
        syncUserItem.setLabelcol(6);
        syncUserItem.setFieldcol(12);
        conditions.add(syncUserItem);

        List<Map> cond=new ArrayList<>();
        Map<String,Object> data=new HashMap<>();
        data.put("title","");
        data.put("defaultshow",true);
        data.put("items",conditions);
        cond.add(data);
        apidatas.put(BrowserConstant.BROWSER_RESULT_CONDITIONS, cond);

        return apidatas;
    }

}

