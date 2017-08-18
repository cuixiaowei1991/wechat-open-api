package com.cn.dao.daoimpl;

import com.cn.struts2.BaseAction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuixiaowei on 2016/4/28.
 */
public class getTicketInfoByidDaoimpl extends BaseAction
{

    @Transactional(rollbackForClassName = "Exception")
    public List getTicketInfoListNoPage(String ticketid){
        String sql="select ti.TICKET_TITLE,ti.TICKET_ACTIVE";
        return hibernateDao.listBySQL(sql, false);
    }
}
