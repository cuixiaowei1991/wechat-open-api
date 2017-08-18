package com.cn.dao.daoimpl;

import com.cn.struts2.BaseAction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuixiaowei on 2016/5/31.
 */
public class lotteryTicketDaoimpl extends BaseAction {
    @Transactional(rollbackForClassName = "Exception")
    public <T> String insert(T entity) {
        try {
            hibernateDao.saveOrUpdate(entity);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
    @Transactional(rollbackForClassName = "Exception")
    public <T> List<T> listByCriteria(Class<T> clazz, String order_id,String openid, boolean isCached) {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("LOTTERY_SERIAL_NUM", order_id));
        conj.add(Restrictions.eq("LOTTERY_WETCHAT_OPENID", openid));
        List<T> list = hibernateDao.listByCriteria(clazz, conj, true);
        return list;
    }
    @Transactional(rollbackForClassName = "Exception")
    public <T> List<T> listByCriteria(Class<T> clazz, String order_id,boolean isCached) {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("LOTTERY_SERIAL_NUM", order_id));

        List<T> list = hibernateDao.listByCriteria(clazz, conj, true);
        return list;
    }

}
