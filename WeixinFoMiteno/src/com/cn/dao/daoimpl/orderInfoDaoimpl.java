package com.cn.dao.daoimpl;

import com.cn.struts2.BaseAction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuixiaowei on 2016/5/12.
 */
public class orderInfoDaoimpl extends BaseAction {

    @Transactional(rollbackForClassName = "Exception")
    public <T> List<T> listByCriteria(Class<T> clazz, String out_trade_no, boolean isCached) {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("orderId", out_trade_no));
        List<T> list = hibernateDao.listByCriteria(clazz, conj, true);
        return list;
    }

    @Transactional(rollbackForClassName = "Exception")
    public <T> String insert(T entity) {
        try {
            hibernateDao.saveOrUpdate(entity);

            return "success";
        }catch(Exception e)
        {
            return "fail";
        }
    }
}
