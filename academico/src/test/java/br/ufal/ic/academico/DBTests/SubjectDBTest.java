package br.ufal.ic.academico.DBTests;

import br.ufal.ic.academico.SubjectClasses.Subject;
import br.ufal.ic.academico.SubjectClasses.SubjectDAO;
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
public class SubjectDBTest {

    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Subject.class).build();

    private SubjectDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("setUp");
        dao = new SubjectDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCRUD() {

        System.out.println("testCRUD");

        Subject c1 = new Subject("Teste de Software", new Long(1), new Long(1), new Long(1), 100, 100, new Long(1), false);
        ArrayList<Long> stuIds = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            stuIds.add(new Long(i));
        c1.setStudentsId(stuIds);

        Subject saved = dbTesting.inTransaction(() -> dao.persist(c1));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(c1.getName(), saved.getName());
        assertEquals(c1.getPrerequisites(), saved.getPrerequisites());
        assertEquals(c1.getCourseID(), saved.getCourseID());
        assertEquals(c1.getDepartamentId(), saved.getDepartamentId());
        assertEquals(c1.getCredits(), saved.getCredits());
        assertEquals(c1.getMinCredits(), saved.getMinCredits());
        assertEquals(c1.getProfessor(), saved.getProfessor());
        assertEquals(c1.getStudentsId(), saved.getStudentsId());

    }

}
