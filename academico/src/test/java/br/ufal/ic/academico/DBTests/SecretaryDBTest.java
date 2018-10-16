package br.ufal.ic.academico.DBTests;


import br.ufal.ic.academico.SecretaryClasses.Secretary;
import br.ufal.ic.academico.SecretaryClasses.SecretaryDAO;
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
public class SecretaryDBTest {

    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Secretary.class).build();

    private SecretaryDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("setUp");
        dao = new SecretaryDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCRUD() {

        System.out.println("testCRUD");

        Secretary c1 = new Secretary("Computação", true, new Long(1));
        ArrayList<Long> subjIds = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            subjIds.add(new Long(i));
        c1.setSubjects(subjIds);

        Secretary saved = dbTesting.inTransaction(() -> dao.persist(c1));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(c1.getName(), saved.getName());
        assertEquals(c1.getDepartamentId(), saved.getDepartamentId());
        assertEquals(c1.isPostgraduate(), saved.isPostgraduate());
        assertEquals(c1.getSubjects(), saved.getSubjects());

    }

}
