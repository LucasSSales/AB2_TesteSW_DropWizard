package br.ufal.ic.academico;

import br.ufal.ic.academico.CourseClasses.Course;
import br.ufal.ic.academico.CourseClasses.CourseDAO;
import br.ufal.ic.academico.CourseClasses.CourseResource;
import br.ufal.ic.academico.DepartamentClasses.Departament;
import br.ufal.ic.academico.DepartamentClasses.DepartamentDAO;
import br.ufal.ic.academico.DepartamentClasses.DepartamentResources;
import br.ufal.ic.academico.ProfessorClasses.Professor;
import br.ufal.ic.academico.ProfessorClasses.ProfessorDAO;
import br.ufal.ic.academico.ProfessorClasses.ProfessorResource;
import br.ufal.ic.academico.SecretaryClasses.Secretary;
import br.ufal.ic.academico.SecretaryClasses.SecretaryDAO;
import br.ufal.ic.academico.SecretaryClasses.SecretaryResources;
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

        //DAOs
        final SubjectDAO subjdao = new SubjectDAO(hibernate.getSessionFactory());
        final StudentDAO studao = new StudentDAO(hibernate.getSessionFactory());
        final CourseDAO cdao = new CourseDAO(hibernate.getSessionFactory());
        final DepartamentDAO ddao = new DepartamentDAO(hibernate.getSessionFactory());
        final SecretaryDAO secdao = new SecretaryDAO(hibernate.getSessionFactory());
        final ProfessorDAO pdao = new ProfessorDAO(hibernate.getSessionFactory());

        //RESOURCES
        final SubjectResource subjResource = new SubjectResource(subjdao);
        final StudentResource studentResource = new StudentResource(studao);
        final CourseResource courseResource = new CourseResource(cdao);
        final DepartamentResources departamentResources = new DepartamentResources(ddao);
        final SecretaryResources secretaryResources = new SecretaryResources(secdao);
        final ProfessorResource professorResource = new ProfessorResource(pdao);

        final RequirementsResources req = new RequirementsResources(studao, ddao, subjdao);

        //JERSEY REGISTER
        environment.jersey().register(subjResource);
        environment.jersey().register(studentResource);
        environment.jersey().register(courseResource);
        environment.jersey().register(departamentResources);
        environment.jersey().register(secretaryResources);
        environment.jersey().register(professorResource);

        environment.jersey().register(req);

    }

    private final HibernateBundle<ConfigApp> hibernate
            = new HibernateBundle<ConfigApp>(Subject.class,
                                    Student.class,
                                    Course.class,
                                    Departament.class,
                                    Secretary.class,
                                    Professor.class) {
        
        @Override
        public DataSourceFactory getDataSourceFactory(ConfigApp configuration) {
            return configuration.getDatabase();
        }
    };
}
