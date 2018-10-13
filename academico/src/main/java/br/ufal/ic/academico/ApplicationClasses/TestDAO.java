package br.ufal.ic.academico.ApplicationClasses;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class TestDAO extends AbstractDAO<AppTest> {

    public TestDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public AppTest get(Serializable id) throws HibernateException {
        log.info("getting person: id={}", id);
        return super.get(id);
    }

    public List<AppTest> list() throws HibernateException {
        log.info("getting persons");
        return super.list(query("from Person"));
    }

    public String t () throws HibernateException{
        return "kkeaemen";
    }

    @Override
    public AppTest persist(AppTest entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(Serializable id) {
        currentSession().delete(this.get(id));
    }

}
