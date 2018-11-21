
package com.engine.msgcenter.cmd.homepage;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.bean.WeaSelect;
import com.engine.msgcenter.constant.MsgConfigConstant;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.hrm.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取消息中心消息模块
 *
 * tianzefa add by 2018/11/20
 */
public class GetModuleCmd extends AbstractCommonCommand<Map<String, Object>> {
    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public GetModuleCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }


    @Override
    public Map<String, Object> execute(CommandContext commandContext) {

        Map<String, Object> apidatas = new HashMap<>();
        List<WeaSelect> options=new ArrayList<>();
        RecordSet rs =new RecordSet();
        rs.executeQuery("select g.groupcode,h.labelname from ECOLOGY_MESSAGE_GROUP g left join HtmlLabelInfo h on g.groupname=h.indexid where h.languageid=? and g.status=?",user.getLanguage(),MsgConfigConstant.MSG_DEFAULT_CONFIG_ENABLE);
        WeaSelect weaSelect = new WeaSelect(StringUtils.EMPTY,StringUtils.EMPTY,true);
        options.add(weaSelect);
        while(rs.next()){
            String key=rs.getString("groupcode");
            String showname=rs.getString("labelname");
            weaSelect=new WeaSelect(key,showname);
            options.add(weaSelect);
        }
        apidatas.put("options",options);
        return apidatas;
    }
}

