package com.cn.dao.daoimpl;

import com.cn.struts2.BaseAction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuixiaowei on 2016/4/26.
 */
public class getAuthorizeByAuthappidDaoimpl extends BaseAction {

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
    public <T> List<T> listByCriteria(Class<T> clazz, String authorizerAppid, boolean isCached) {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("weixin_Authorizer_Appid", authorizerAppid));
        List<T> list = hibernateDao.listByCriteria(clazz, conj, true);
        return list;
    }

    @Transactional(rollbackForClassName = "Exception")
    public <T> List<T> listByCriteriaByShopId(Class<T> clazz, String shopid, boolean isCached) {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("shop_id", shopid));
        List<T> list = hibernateDao.listByCriteria(clazz, conj, true);
        return list;
    }


}
