package br.ufal.ic.academico;

import br.ufal.ic.academico.DepartamentClasses.Departament;
import br.ufal.ic.academico.DepartamentClasses.DepartamentDAO;
import br.ufal.ic.academico.DepartamentClasses.DepartamentResources;
import ch.qos.logback.classic.Level;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
public class DepartamentTest {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private DepartamentDAO deptDao = mock(DepartamentDAO.class);

    private final DepartamentResources profResource = new DepartamentResources(deptDao);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";

    @Test
    public void testDelete(){

        List<Departament> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Departament>>() {});

        assertTrue(saved.size() > 0, "Lista está vazia");

        Long id = saved.get(0).getId();
        int size = saved.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments/%d", RULE.getLocalPort(), acad, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted, "Ids não batem");

        List<Departament> saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Departament>>() {});

        int newSize = saved2.size();

        assertEquals(newSize, size-1, "Tamanhos não batem");

    }

    @Test
    public void testSave() {

        Departament d = new Departament("Computacao");
        d.setProfessors(new ArrayList<Long>());
        d.setGradSec(new Long(69));
        d.setPosGradSec(new Long(-1));
        d.setCoursesIds(new ArrayList<Long>());

        Departament saved = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(d), Departament.class);

        assertNotNull(saved, "O objeto está null");
        assertNotNull(saved.getId(), "O objeto está null");

        List<Departament> list = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Departament>>() {});

        assertTrue(list.size() > 0, "A lista está vazia");

        //GET COM ID
        Long id = list.get(0).getId();

        Departament getWthId = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments/%d", RULE.getLocalPort(), acad, id))
                .request()
                .get(new GenericType<Departament>() {});

        assertNotNull(getWthId, "Objeto retornou null");
        assertEquals(id, getWthId.getId(), "Os Ids não batem");


    }

    @Test
    public void testList() {

        List<Departament> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Departament>>() {});

        assertTrue(saved.size() > 0);
    }

    @Test
    public void testPost(){
        Departament d = new Departament("Computacao");
        d.setProfessors(new ArrayList<Long>());
        d.setGradSec(new Long(69));
        d.setPosGradSec(new Long(-1));
        d.setCoursesIds(new ArrayList<Long>());

        Departament saved = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(d), Departament.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getId());
                },
                () -> {
                    assertEquals("Computacao", saved.getName());
                },
                () -> {
                    assertNotNull(saved.getCoursesIds());
                    assertEquals(0, saved.getCoursesIds().size());
                },
                () -> {
                    assertNotNull(saved.getProfessors());
                    assertEquals(0, saved.getProfessors().size());
                },
                () -> {
                    assertNotNull(saved.getGradSec());
                    assertEquals(new Long(69), d.getGradSec());
                    assertNotNull(saved.getPosGradSec());
                    assertEquals(new Long(-1), d.getPosGradSec());
                }
        );
    }

    @Test
    public void testPut(){
        //FAZENDO POST
        Departament d = new Departament("Computacao");
        d.setProfessors(new ArrayList<Long>());
        d.setGradSec(new Long(69));
        d.setPosGradSec(new Long(-1));
        d.setCoursesIds(new ArrayList<Long>());

        Departament post = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(d), Departament.class);

        //FAZENDO O UPDATE
        assertNotNull(post, "Objeto retornado está null");
        assertNotNull(post.getId());
        Long id = post.getId();

        ArrayList<Long> coursesIds = new ArrayList<Long>();
        coursesIds.add(new Long(78));
        coursesIds.add(new Long(79));
        coursesIds.add(new Long(80));
        coursesIds.add(new Long(81));
        d.setCoursesIds(coursesIds);

        ArrayList<Long> profIds = new ArrayList<Long>();
        profIds.add(new Long(33));
        profIds.add(new Long(36));
        profIds.add(new Long(39));
        d.setProfessors(profIds);

        d.setPosGradSec(new Long(56));


        Departament saved = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments/%d", RULE.getLocalPort(), acad, id))
                .request()
                .put(Entity.json(d), Departament.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getGradSec());
                    assertNotNull(saved.getPosGradSec());
                    assertEquals(new Long(56), saved.getPosGradSec());
                },
                () -> {
                    assertEquals(4, saved.getCoursesIds().size());
                    assertEquals(new Long(78), saved.getCoursesIds().get(0));
                    assertEquals(new Long(79), saved.getCoursesIds().get(1));
                    assertEquals(new Long(80), saved.getCoursesIds().get(2));
                    assertEquals(new Long(81), saved.getCoursesIds().get(3));
                },
                () -> {
                    assertEquals(3, saved.getProfessors().size());
                    assertEquals(new Long(33), saved.getProfessors().get(0));
                    assertEquals(new Long(36), saved.getProfessors().get(1));
                    assertEquals(new Long(39), saved.getProfessors().get(2));
                }
        );
    }

}
