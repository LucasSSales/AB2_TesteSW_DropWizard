package br.ufal.ic.academico.DBTests;


import br.ufal.ic.academico.CourseClasses.Course;
import br.ufal.ic.academico.CourseClasses.CourseDAO;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CourseDBTest {

    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Course.class).build();

    private CourseDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("setUp");
        dao = new CourseDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCRUD() {

        System.out.println("testCRUD");

        Course c1 = new Course("Koichi Hirose", new Long(1), new Long(1));
        ArrayList<Long> subjIds = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            subjIds.add(new Long(i));
        c1.setSubjectIds(subjIds);

        ArrayList<Long> stuIds = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            stuIds.add(new Long(i));
        c1.setSubjectIds(stuIds);



        Course saved = dbTesting.inTransaction(() -> dao.persist(c1));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(c1.getName(), saved.getName());
        assertEquals(c1.getDepartamentId(), saved.getDepartamentId());
        assertEquals(c1.getSecretaryId(), saved.getSecretaryId());
        assertEquals(c1.getSubjectIds(), saved.getSubjectIds());
        assertEquals(c1.getStudents(), saved.getStudents());

    }

}
