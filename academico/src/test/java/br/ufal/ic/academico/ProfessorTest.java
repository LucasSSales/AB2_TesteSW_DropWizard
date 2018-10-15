package br.ufal.ic.academico;

import br.ufal.ic.academico.ProfessorClasses.Professor;
import br.ufal.ic.academico.ProfessorClasses.ProfessorDAO;
import br.ufal.ic.academico.ProfessorClasses.ProfessorResource;
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
public class ProfessorTest {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private ProfessorDAO profDao = mock(ProfessorDAO.class);

    private final ProfessorResource profResource = new ProfessorResource(profDao);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";

    @Test
    public void testDelete(){

        List<Professor> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Professor>>() {});

        assertTrue(saved.size() > 0, "Lista está vazia");

        Long id = saved.get(0).getId();
        int size = saved.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/professors/%d", RULE.getLocalPort(), acad, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted, "Ids não batem");

        List<Professor> saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Professor>>() {});

        int newSize = saved2.size();

        assertEquals(newSize, size-1, "Tamanhos não batem");

    }

    @Test
    public void testSave() {

        Professor p = new Professor("Computacao", new Long(80));
        p.setSubjects(new ArrayList<Long>());

        Professor saved = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(p), Professor.class);

        assertNotNull(saved, "O objeto está null");
        assertNotNull(saved.getId(), "O objeto está null");

        List<Professor> list = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Professor>>() {});

        assertTrue(list.size() > 0, "A lista está vazia");

        //GET COM ID
        Long id = list.get(0).getId();

        Professor getWthId = RULE.client().target(
                String.format("http://localhost:%d/%s/professors/%d", RULE.getLocalPort(), acad, id))
                .request()
                .get(new GenericType<Professor>() {});

        assertNotNull(getWthId, "Objeto retornou null");
        assertEquals(id, getWthId.getId(), "Os Ids não batem");


    }

    @Test
    public void testList() {

        List<Professor> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Professor>>() {});

        assertTrue(saved.size() > 0);
    }

    @Test
    public void testPost(){
        Professor p = new Professor("Willy Tiengo", new Long(80));
        p.setSubjects(new ArrayList<Long>());

        Professor saved = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(p), Professor.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getId());
                    assertNotNull(saved.getDepartamentId());
                },
                () -> {
                    assertEquals("Willy Tiengo", saved.getName());
                    assertEquals(new Long(80), saved.getDepartamentId());
                },
                () -> {
                    assertNotNull(saved.getSubjects());
                    assertEquals(0, saved.getSubjects().size());
                },
                () -> {

                }
        );
    }

    @Test
    public void testPut(){
        //FAZENDO POST
        Professor p = new Professor("Computacao", new Long(80));
        p.setSubjects(new ArrayList<Long>());

        Professor post = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(p), Professor.class);

        //FAZENDO O UPDATE
        assertNotNull(post, "Objeto retornado está null");
        assertNotNull(post.getId());
        Long id = post.getId();

        ArrayList<Long> subjectsIds = new ArrayList<Long>();
        subjectsIds.add(new Long(78));
        subjectsIds.add(new Long(79));
        subjectsIds.add(new Long(80));
        subjectsIds.add(new Long(81));

        p.setSubjects(subjectsIds);

        Professor saved = RULE.client().target(
                String.format("http://localhost:%d/%s/professors/%d", RULE.getLocalPort(), acad, id))
                .request()
                .put(Entity.json(p), Professor.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getSubjects());
                },
                () -> {
                    assertEquals(4, saved.getSubjects().size());
                    assertEquals(new Long(78), saved.getSubjects().get(0));
                    assertEquals(new Long(79), saved.getSubjects().get(1));
                    assertEquals(new Long(80), saved.getSubjects().get(2));
                    assertEquals(new Long(81), saved.getSubjects().get(3));
                }
        );
    }

}
