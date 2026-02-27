package autoservice.model.repository;

import autoservice.model.entities.GarageSpot;
import org.springframework.stereotype.Repository;

@Repository
public class GarageSpotRepository extends HibernateAbstractDAO<GarageSpot, Long> {
    public GarageSpotRepository() {
        super(GarageSpot.class);
    }

    @Override
    public Long count() {
        String hql = "select count(*) from GarageSpot";
        return getSession().createQuery(hql, Long.class).getSingleResult();
    }
}