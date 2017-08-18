package com.cn.dao.daoimpl;

import com.cn.struts2.BaseAction;
import org.hibernate.criterion.Criterion;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuixiaowei on 2016/9/20.
 */
public class AdvertOrderDaoImpl extends BaseAction {
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
    @Transactional(rollbackForClassName = "Exception")
    public <T> List<T> listByCriteria(Class<T> clazz, Criterion criterion, boolean isCached) {
        List<T> AutoMessageList=hibernateDao.listByCriteria(clazz,
                criterion,true);
        return AutoMessageList;

    }
    @Transactional(rollbackForClassName = "Exception")
    public <T> List<T> listPageByCriteria(Class<T> clazz, Criterion criterion,int rows,int page,boolean isCached) {
        List<T> authList = hibernateDao.listByPageByCriteria(clazz, criterion, rows, page
                , true);
        return authList;
    }
    @Transactional(rollbackForClassName = "Exception")
    public <T> int listByCriteriaCount(Class<T> clazz, Criterion criterion, boolean isCached)
    {
        int listcount = hibernateDao.countByCriteria(clazz,
                criterion, true);
        return listcount;
    }

}
