package com.dasha.philosophy.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();
	 
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration()
            		.configure()
                    .buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

