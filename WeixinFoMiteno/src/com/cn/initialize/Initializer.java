package com.cn.initialize;

import org.apache.log4j.Logger;
import com.cn.common.DateModule;
import com.cn.common.RandomModule;
import com.cn.dao.util.HibernateDAO;

/**
 * 初始化程序
 * 
 * @author Administrator
 * 
 */
public class Initializer {
	/**
	 * 日志
	 */
	Logger logger = Logger.getLogger(Initializer.class);

	/**
	 * 依赖注入获得实体类操作模块实例
	 */
	public HibernateDAO hibernateDao;

	/**
	 * 依赖注入获得时间操作模块实例
	 */
	public DateModule dateModule;

	/**
	 * 依赖注入获得随机数操作模块实例
	 */
	public RandomModule randomModule;

	public DateModule getDateModule() {
		return dateModule;
	}

	public HibernateDAO getHibernateDao() {
		return hibernateDao;
	}

	public RandomModule getRandomModule() {
		return randomModule;
	}

	/**
	 * 初始化abcd用户
	 */
//	@Transactional(rollbackForClassName = "Exception")
//	public void initAbcd() {
//		List<Authority> authorityList = hibernateDao.listByAll(Authority.class,
//				true);
//		/**
//		 * abcd是全权限用户
//		 */
//		UserBase abcd = new UserBase();
//		abcd.setUserName("abcd");
//		abcd.setPassWord("abcd");
//		abcd.setCreateDate(new Date());
//		abcd.setWritepermission(true);
//		hibernateDao.saveOrUpdate(abcd);
//		for (int i = 0; i < authorityList.size(); ++i) {
//			UserBase_Authority ua = new UserBase_Authority();
//			ua.setUserBase(abcd);
//			ua.setAuthority(authorityList.get(i));
//			hibernateDao.saveOrUpdate(ua);
//		}
//	}

	/**
	 * initAuthority 初始化权限
	 * 
	 * @return String
	 */
//	@Transactional(rollbackFor = Exception.class, value = "transactionManager")
//	public String initAuthority() {
//		Authority authority_city = new Authority();
//		authority_city.setAuthorityString("ROLE_ADMIN");
//		authority_city.setAuthorityDescription("管理员用户");
//		authority_city.setAuthorityExpireDate(new Date());
//		authority_city.setAuthorityStatus(0);
//		hibernateDao.saveOrUpdate(authority_city);
//		
//		Authority authority_school = new Authority();
//		authority_school.setAuthorityString("ROLE_COMMON");
//		authority_school.setAuthorityDescription("普通用户");
//		authority_school.setAuthorityExpireDate(new Date());
//		authority_school.setAuthorityStatus(0);
//		hibernateDao.saveOrUpdate(authority_school);
//				
//		return "initAuthority() Return Success";
//	}

	/**
	 * 初始化用户
	 * 
	 * @param userNumber
	 */
//	@Transactional(rollbackForClassName = "Exception")
//	public void initUser(int userNumber) {
//		List<Authority> authorityList = hibernateDao.listByAll(Authority.class,
//				true);
//
//		for (int i = 0; i < userNumber; ++i) {
//			UserBase user = new UserBase();
//			String username = randomModule.getRandomString(RandomModule.randomData_az, 3)
//					+ randomModule.getRandomString(RandomModule.randomData_09, 3);
//			user.setUserName(username);
//			user.setPassWord(username);
//			user.setWritepermission(randomModule.getRandomBoolean());
//
//			hibernateDao.saveOrUpdate(user);
//			UserBase_Authority ua = new UserBase_Authority();
//			ua.setUserBase(user);
//			ua.setAuthority(authorityList.get(randomModule
//					.getRandomInt(authorityList.size())));
//			hibernateDao.saveOrUpdate(ua);
//		}
//	}

	public void setDateModule(DateModule dateModule) {
		this.dateModule = dateModule;
	}

	public void setHibernateDao(HibernateDAO hibernateDao) {
		this.hibernateDao = hibernateDao;
	}

	public void setRandomModule(RandomModule randomModule) {
		this.randomModule = randomModule;
	}

}
