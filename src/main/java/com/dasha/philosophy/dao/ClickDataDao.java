package com.dasha.philosophy.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.dasha.philosophy.model.ClickData;
import com.dasha.philosophy.util.HibernateUtil;

@Repository("clickDataDao")
public class ClickDataDao {

	public List<ClickData> listClickData() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();

		List<ClickData> clickData = session.createQuery("from ClickData").list();
		session.close();
		return clickData;
	}
	
	public ClickData saveClickDataInDatabase(ClickData clickData) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();

		Integer id = (Integer) session.save(clickData);
		clickData.setId(id);
			
		session.getTransaction().commit();
			
		session.close();

		return clickData;
	}
}
