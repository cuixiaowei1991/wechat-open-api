package test;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class TestBase extends TestCase{
	@Resource
	public SessionFactory sessionFactory;
	public Session session;

	@Before
	public void setUp() throws Exception {
		session = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory,
				new SessionHolder(session));
	} 

	@After
	public void tearDown() throws Exception {
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
				.getResource(sessionFactory);
		session = holder.getSession();
		session.flush();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
	}

}
