package autoservice.model.repository;

import autoservice.model.entities.GarageSpot;
import autoservice.model.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GarageSpotRepository extends HibernateAbstractDAO<GarageSpot, Long> {
    public GarageSpotRepository() {
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