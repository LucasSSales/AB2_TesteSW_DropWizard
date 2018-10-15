package br.ufal.ic.academico;

import br.ufal.ic.academico.SubjectClasses.Subject;
import br.ufal.ic.academico.SubjectClasses.SubjectDAO;
import br.ufal.ic.academico.SubjectClasses.SubjectResource;
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
public class SubjectTest {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private SubjectDAO subjDao = mock(SubjectDAO.class);

    private final SubjectResource subjResource = new SubjectResource(subjDao);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";

    @Test
    public void testDelete(){

        List<Subject> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Subject>>() {});

        assertTrue(saved.size() > 0, "Lista está vazia");

        Long id = saved.get(0).getId();
        int size = saved.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects/%d", RULE.getLocalPort(), acad, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted, "Ids não batem");

        List<Subject> saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Subject>>() {});

        int newSize = saved2.size();

        assertEquals(newSize, size-1, "Tamanhos não batem");

    }

    @Test
    public void testSave() {

        Subject s = new Subject("Teste de Software", new Long(150), new Long(80), new Long(17), 200, 100, new Long(270), true);
        s.setStudentsId(new ArrayList<Long>());

        Subject saved = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);

        assertNotNull(saved, "O objeto está null");
        assertNotNull(saved.getId(), "O objeto está null");

        List<Subject> list = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Subject>>() {});

        assertTrue(list.size() > 0, "A lista está vazia");

        //GET COM ID
        Long id = list.get(0).getId();

        Subject getWthId = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects/%d", RULE.getLocalPort(), acad, id))
                .request()
                .get(new GenericType<Subject>() {});

        assertNotNull(getWthId, "Objeto retornou null");
        assertEquals(id, getWthId.getId(), "Os Ids não batem");


    }

    @Test
    public void testList() {

        List<Subject> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Subject>>() {});

        assertTrue(saved.size() > 0);
    }

    @Test
    public void testPost(){
        Subject s = new Subject("Teste de Software", new Long(150), new Long(80), new Long(17), 200, 100, new Long(270), true);
        s.setStudentsId(new ArrayList<Long>());
        Subject saved = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getId());
                },
                () -> {
                    assertEquals("Teste de Software", saved.getName());
                    assertEquals(new Long(150), saved.getPrerequisites());
                    assertEquals(new Long(80), s.getCourseID());
                    assertEquals(new Long(17), saved.getDepartamentId());
                    assertEquals(200, saved.getCredits());
                    assertEquals(100, saved.getMinCredits());
                    assertEquals(new Long(270), saved.getProfessor());
                },
                () -> { assertTrue(saved.isPostgraduate()); },
                () -> {
                    assertNotNull(saved.getStudentsId());
                    assertEquals(0, saved.getStudentsId().size());
                }
        );
    }

    @Test
    public void testPut(){
        //FAZENDO POST
        Subject s = new Subject("Teste de Software", new Long(150), new Long(80), new Long(17), 200, 100, new Long(270), true);
        s.setStudentsId(new ArrayList<Long>());

        Subject post = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);

        //FAZENDO O UPDATE
        assertNotNull(post, "Objeto retornado está null");
        assertNotNull(post.getId());
        Long id = post.getId();

        ArrayList<Long> studentsId = new ArrayList<Long>();
        studentsId.add(new Long(78));
        studentsId.add(new Long(79));
        studentsId.add(new Long(80));
        s.setStudentsId(studentsId);

        Subject saved = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects/%d", RULE.getLocalPort(), acad, id))
                .request()
                .put(Entity.json(s), Subject.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getStudentsId());
                },
                () -> {
                    assertEquals(3, saved.getStudentsId().size());
                    assertEquals(new Long(78), saved.getStudentsId().get(0));
                    assertEquals(new Long(79), saved.getStudentsId().get(1));
                    assertEquals(new Long(80), saved.getStudentsId().get(2));
                }
        );
    }

}