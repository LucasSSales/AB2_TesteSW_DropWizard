package br.ufal.ic.academico;

import br.ufal.ic.academico.StudentClasses.Student;
import br.ufal.ic.academico.StudentClasses.StudentDAO;
import br.ufal.ic.academico.StudentClasses.StudentResource;
import br.ufal.ic.academico.SubjectClasses.Subject;
import br.ufal.ic.academico.SubjectClasses.SubjectDAO;
import br.ufal.ic.academico.SubjectClasses.SubjectResource;
import br.ufal.ic.academico.exemplos.MyResource;
import br.ufal.ic.academico.exemplos.Person;
import br.ufal.ic.academico.exemplos.PersonDAO;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Willy
 */
@Slf4j
public class AcademicoApp extends Application<ConfigApp> {

    public static void main(String[] args) throws Exception {
        new AcademicoApp().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<ConfigApp> bootstrap) {
        log.info("initialize");
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ConfigApp config, Environment environment) {
        
        final PersonDAO dao = new PersonDAO(hibernate.getSessionFactory()); //
        final SubjectDAO subjdao = new SubjectDAO(hibernate.getSessionFactory());
        final StudentDAO studao = new StudentDAO(hibernate.getSessionFactory());


        final MyResource resource = new MyResource(dao); //
        final SubjectResource subjResource = new SubjectResource(subjdao);
        final StudentResource studentResource = new StudentResource(studao);

        
        environment.jersey().register(resource); //
        environment.jersey().register(subjResource);
        environment.jersey().register(studentResource);

    }

    private final HibernateBundle<ConfigApp> hibernate
            = new HibernateBundle<ConfigApp>(Person.class, Subject.class, Student.class) {
        
        @Override
        public DataSourceFactory getDataSourceFactory(ConfigApp configuration) {
            return configuration.getDatabase();
        }
    };
}
