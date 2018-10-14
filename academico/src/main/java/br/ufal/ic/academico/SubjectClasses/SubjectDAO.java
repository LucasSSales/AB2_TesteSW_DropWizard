package br.ufal.ic.academico.SubjectClasses;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class SubjectDAO extends AbstractDAO<Subject> {

    public SubjectDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Subject get(Serializable id) throws HibernateException {
        log.info("getting subject: id={}", id);
        return super.get(id);
    }

    public List<Subject> list() throws HibernateException {
        log.info("getting subjects");
        return super.list(query("from Subject"));
    }


    @Override
    public Subject persist(Subject entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(Serializable id) {
        currentSession().delete(this.get(id));
    }


}
