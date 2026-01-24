package autoservice.model.repository;

import autoservice.model.entities.Master;
import autoservice.model.exceptions.DBException;
import autoservice.model.utils.HibernateUtil;
import config.annotation.Component;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;
@Component
@Slf4j
public class MasterDAO extends HibernateAbstractDAO<Master, Long> {

    public MasterDAO() {
        super(Master.class);
    }
    public List<Master> mastersSortByName() {
        try {
            Session session = HibernateUtil.getSession();
            String hql = "FROM Master m ORDER BY m.name";
            Query<Master> query = session.createQuery(hql);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error getting sorted masters by name", e);
            throw new DBException("Error getting sorted masters by name", e);
        }
    }

    @Override
    public Long count() {
        String  hql = "select count(*) from Master m";
        Session session = HibernateUtil.getSession();
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.getSingleResult();
    }

}
