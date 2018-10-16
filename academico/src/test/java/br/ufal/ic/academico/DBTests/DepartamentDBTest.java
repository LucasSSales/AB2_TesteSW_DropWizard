package br.ufal.ic.academico.DBTests;


import br.ufal.ic.academico.DepartamentClasses.Departament;
import br.ufal.ic.academico.DepartamentClasses.DepartamentDAO;
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
public class DepartamentDBTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Departament.class).build();

    private DepartamentDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("setUp");
        dao = new DepartamentDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCRUD() {

        System.out.println("testCRUD");

        Departament c1 = new Departament("Matemática");
        c1.setPosGradSec(new Long(-1));
        c1.setGradSec(new Long(300));

        Departament saved = dbTesting.inTransaction(() -> dao.persist(c1));
        ArrayList<Long> cIds = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            cIds.add(new Long(i));
        c1.setCoursesIds(cIds);

        ArrayList<Long> profIds = new ArrayList<Long>();
        for (int i = 0; i < 4; i++)
            profIds.add(new Long(i));
        c1.setProfessors(profIds);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(c1.getName(), saved.getName());
        assertEquals(c1.getPosGradSec(), saved.getPosGradSec());
        assertEquals(c1.getGradSec(), saved.getGradSec());
        assertEquals(c1.getProfessors(), saved.getProfessors());
        assertEquals(c1.getCoursesIds(), saved.getCoursesIds());
    }
}
