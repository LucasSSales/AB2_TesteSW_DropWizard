package br.ufal.ic.academico.SecretaryClasses;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class SecretaryDAO extends AbstractDAO<Secretary> {

    public SecretaryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Secretary get(Serializable id) throws HibernateException {
        log.info("getting person: id={}", id);
        return super.get(id);
    }

    public List<Secretary> list() throws HibernateException {
        log.info("getting persons");
        return super.list(query("from Secretary"));
    }

    @Override
    public Secretary persist(Secretary entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(Serializable id) {
        currentSession().delete(this.get(id));
    }
}
