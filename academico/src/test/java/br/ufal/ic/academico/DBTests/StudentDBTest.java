package br.ufal.ic.academico.DBTests;

import br.ufal.ic.academico.StudentClasses.Student;
import br.ufal.ic.academico.StudentClasses.StudentDAO;
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
public class StudentDBTest {

    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Student.class).build();

    private StudentDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("setUp");
        dao = new StudentDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCRUD() {

        System.out.println("testCRUD");

        Student c1 = new Student("Koichi Hirose", new Long(1), new Long(1), false);
        ArrayList<Long> studying = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            studying.add(new Long(i));
        c1.setStudying(studying);

        ArrayList<Long> ap = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            ap.add(new Long(i));
        c1.setApproved(ap);

        ArrayList<Long> disap = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            disap.add(new Long(i));
        c1.setDisapproved(disap);

        Student saved = dbTesting.inTransaction(() -> dao.persist(c1));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(c1.getName(), saved.getName());
        assertEquals(c1.getScore(), saved.getScore());
        assertEquals(c1.getDepartamentID(), saved.getDepartamentID());
        assertEquals(c1.getCourseID(), saved.getCourseID());
        assertEquals(c1.isPostgraduate(), saved.isPostgraduate());
        assertEquals(c1.getStudying(), saved.getStudying());
        assertEquals(c1.getDisapproved(), saved.getDisapproved());
        assertEquals(c1.getApproved(), saved.getApproved());


    }

}
