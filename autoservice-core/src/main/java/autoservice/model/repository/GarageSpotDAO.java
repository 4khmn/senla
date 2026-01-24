package autoservice.model.repository;

import autoservice.model.entities.GarageSpot;
import autoservice.model.utils.HibernateUtil;
import config.annotation.Component;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Component
public class GarageSpotDAO extends HibernateAbstractDAO<GarageSpot, Long> {
    public GarageSpotDAO() {
        super(GarageSpot.class);
    }

    @Override
    public Long count() {
        String hql = "select count(*) from GarageSpot";
        Session session = HibernateUtil.getSession();
        Query<Long> query = session.createQuery(hql);
        return query.getSingleResult();
    }
}