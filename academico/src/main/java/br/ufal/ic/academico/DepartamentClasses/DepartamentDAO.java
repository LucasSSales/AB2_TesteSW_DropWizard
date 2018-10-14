package br.ufal.ic.academico.DepartamentClasses;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class DepartamentDAO extends AbstractDAO<Departament> {

    public DepartamentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Departament get(Serializable id) throws HibernateException {
        log.info("getting person: id={}", id);
        return super.get(id);
    }

    public List<Departament> list() throws HibernateException {
        log.info("getting persons");
        return super.list(query("from Departament"));
    }

    @Override
    public Departament persist(Departament entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(Serializable id) {
        currentSession().delete(this.get(id));
    }
}
