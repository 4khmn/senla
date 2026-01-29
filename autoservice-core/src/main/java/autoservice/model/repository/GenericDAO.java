package autoservice.model.repository;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO <T, PK extends Serializable> {

    PK save(T entity);

    T findById(PK id);

    void delete(PK id);

    List<T> findAll();

    void update(T entity);

    Long count();


}
