package autoservice.model.utils;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    private static Session session;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Master.class)
                    .addAnnotatedClass(Order.class)
                    .addAnnotatedClass(GarageSpot.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        if (session != null && session.isOpen()) {
            return session;
        }
        session = sessionFactory.openSession();
        return session;
    }

    public static void shutdown() {
        if (session != null && session.isOpen()) {
            session.close();
        }
        sessionFactory.close();
    }

}
