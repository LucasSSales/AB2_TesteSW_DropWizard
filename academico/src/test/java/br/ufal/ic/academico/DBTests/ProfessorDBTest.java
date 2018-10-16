package br.ufal.ic.academico.DBTests;


import br.ufal.ic.academico.ProfessorClasses.Professor;
import br.ufal.ic.academico.ProfessorClasses.ProfessorDAO;
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
public class ProfessorDBTest {

    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Professor.class).build();

    private ProfessorDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("setUp");
        dao = new ProfessorDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCRUD() {

        System.out.println("testCRUD");

        Professor c1 = new Professor("Willy Tiengo", new Long(1));
        ArrayList<Long> subjIds = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            subjIds.add(new Long(i));
        c1.setSubjects(subjIds);

        Professor saved = dbTesting.inTransaction(() -> dao.persist(c1));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(c1.getName(), saved.getName());
        assertEquals(c1.getDepartamentId(), saved.getDepartamentId());
        assertEquals(c1.getSubjects(), saved.getSubjects());

    }

}
