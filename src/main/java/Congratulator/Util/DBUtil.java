package Congratulator.Util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by User2 on 05.03.2018.
 */
public class DBUtil {
    private static SessionFactory ourSessionFactory;
    private static Session session;

    private DBUtil() {

    }

    public static Session getSession() throws HibernateException {
        Logger.log(Level.INFO, "Getting Hibernate session", true);

        if (session == null) {
            Logger.log(Level.INFO, "Starting Hibernate session", true);

            try {
                String url = Settings.getInstance().getDbURL();
                String login = Settings.getDbLogin();
                String pass = Settings.getDbPass();

                Configuration configuration = new Configuration();
                configuration.configure();

                configuration.setProperty("hibernate.connection.url", url);
                configuration.setProperty("hibernate.connection.username", login);
                configuration.setProperty("hibernate.connection.password", pass);

                ourSessionFactory = configuration.buildSessionFactory();
                session = ourSessionFactory.openSession();
            } catch (Throwable ex) {
                Logger.log(Level.SEVERE, "Starting Hibernate session fail. Additional info " + ex.toString(), true);
            }
        }

        return session;
    }

    public static void writeEntity(Object object, boolean evict) {
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
        if (evict) evictEntity(object);
    }

    public static void evictEntity(Object object) {
        getSession().evict(object);
    }

    public static <T> T getEntityById(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    public static List<Object[]> getClientsForBDay(int day, int month) {
        Logger.log(Level.INFO, "Loading clientlist for " + day + "." + month + " from DB", true);

        return getSession().createQuery(String.format(
                "from KlientinfoEntity k, EmailEntity e where " +
                        "k.id = e.parentId and MONTH(k.dateOld)=%d and DAY(k.dateOld)=%d and k.saveDateOld=1", month, day)).list();
    }

    public static LinkedList<String> getEmailsForBDay(int day, int month) {
        Logger.log(Level.INFO, "Loading email list for clients with BDay in " + day + "." + month + " from DB", true);

        LinkedList<String> result = new LinkedList<String>();

        result.addAll(getSession().createQuery(String.format(
                "select e.email from KlientinfoEntity k, EmailEntity e " +
                        "where k.id = e.parentId and MONTH(k.dateOld)=%d and DAY(k.dateOld)=%d and k.saveDateOld=1", month, day)).list());

        return result;
    }

    public static void closeSession() {
        Logger.log(Level.INFO, "Enter DBUtil.closeSession()", true);

        if (session != null) {
            Logger.log(Level.INFO, "Closing Hibernate session", true);
            getSession().close();
        }
    }
}
