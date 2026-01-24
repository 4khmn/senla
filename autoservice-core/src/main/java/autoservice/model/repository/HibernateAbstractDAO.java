package autoservice.model.repository;

import autoservice.model.utils.HibernateUtil;

import java.io.Serializable;
import java.util.List;

public abstract class HibernateAbstractDAO<T, PK extends Serializable> implements GenericDAO<T, PK> {
    protected Class<T> type;


    protected HibernateAbstractDAO(Class<T> type) {
        this.type = type;
    }


    @Override
    public PK save(T entity) {
        return (PK) HibernateUtil.getSession().save(entity);
    }

    @Override
    public T findById(PK id) {
        return HibernateUtil.getSession().get(type, id);
    }

    @Override
    public void delete(PK id) {
        T entity = HibernateUtil.getSession().get(type, id);
        if (entity != null) {
            HibernateUtil.getSession().delete(entity);
        }
    }

    @Override
    public List<T> findAll() {
        return HibernateUtil.getSession()
                .createQuery("from " + type.getName(), type)
                .getResultList();
    }

    @Override
    public void update(T entity) {
        HibernateUtil.getSession().update(entity);
    }

    @Override
    public abstract Long count();
}
