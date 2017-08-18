package com.cn.dao.daoimpl;

import com.cn.struts2.BaseAction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuixiaowei on 2016/5/26.
 */
public class getWeiXinTuiguangByWXTDDaoimpl extends BaseAction {
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
    public <T> List<T> listByCriteria(Class<T> clazz, String weixin_num,String tuiguang_num, boolean isCached) {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("wexin_num", weixin_num));
        conj.add(Restrictions.eq("tuiguang_num", tuiguang_num));
        List<T> list = hibernateDao.listByCriteria(clazz, conj, true);
        return list;
    }
}
