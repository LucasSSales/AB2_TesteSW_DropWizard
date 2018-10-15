package br.ufal.ic.academico;

import br.ufal.ic.academico.CourseClasses.Course;
import br.ufal.ic.academico.CourseClasses.CourseDAO;
import br.ufal.ic.academico.CourseClasses.CourseResource;
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
public class CourseTests {
    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private CourseDAO courseDAO = mock(CourseDAO.class);

    private final CourseResource courseResource = new CourseResource(courseDAO);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";

    @Test
    public void testDelete(){

        List<Course> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Course>>() {});

        assertTrue(saved.size() > 0, "Lista está vazia");

        Long id = saved.get(0).getId();
        int size = saved.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/courses/%d", RULE.getLocalPort(), acad, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted, "Ids não batem");

        List<Course> saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Course>>() {});

        int newSize = saved2.size();

        assertEquals(newSize, size-1, "Tamanhos não batem");

    }

    @Test
    public void testSave() {

        Course c = new Course("Ciencia da Computacao", new Long(80), new Long(77));
        c.setSubjectIds(new ArrayList<Long>());
        c.setStudents(new ArrayList<Long>());

        Course saved = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(c), Course.class);

        assertNotNull(saved, "O objeto está null");
        assertNotNull(saved.getId(), "O objeto está null");

        List<Course> list = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Course>>() {});

        assertTrue(list.size() > 0, "A lista está vazia");

        //GET COM ID
        Long id = list.get(0).getId();

        Course getWthId = RULE.client().target(
                String.format("http://localhost:%d/%s/courses/%d", RULE.getLocalPort(), acad, id))
                .request()
                .get(new GenericType<Course>() {});

        assertNotNull(getWthId, "Objeto retornou null");
        assertEquals(id, getWthId.getId(), "Os Ids não batem");


    }

    @Test
    public void testList() {

        List<Course> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Course>>() {});

        assertTrue(saved.size() > 0);
    }

    @Test
    public void testPost(){
        Course c = new Course("Ciencia da Computacao", new Long(80), new Long(77));
        c.setSubjectIds(new ArrayList<Long>());
        c.setStudents(new ArrayList<Long>());

        Course saved = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(c), Course.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getId());
                    assertNotNull(saved.getDepartamentId());
                },
                () -> {
                    assertEquals("Ciencia da Computacao", saved.getName());
                    assertEquals(new Long(80), saved.getDepartamentId());
                    assertEquals(new Long(77), saved.getSecretaryId());
                },
                () -> {
                    assertNotNull(saved.getSubjectIds());
                    assertEquals(0, saved.getSubjectIds().size());
                },
                () -> {
                    assertNotNull(saved.getStudents());
                    assertEquals(0, saved.getStudents().size());
                }
        );
    }

    @Test
    public void testPut(){
        //FAZENDO POST
        Course c = new Course("Ciencia da Computacao", new Long(80), new Long(77));
        c.setSubjectIds(new ArrayList<Long>());
        c.setStudents(new ArrayList<Long>());

        Course post = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(c), Course.class);

        //FAZENDO O UPDATE
        assertNotNull(post, "Objeto retornado está null");
        assertNotNull(post.getId());
        Long id = post.getId();

        ArrayList<Long> subjectsIds = new ArrayList<Long>();
        subjectsIds.add(new Long(78));
        subjectsIds.add(new Long(79));
        subjectsIds.add(new Long(80));
        subjectsIds.add(new Long(81));
        c.setSubjectIds(subjectsIds);

        ArrayList<Long> studentsIds = new ArrayList<Long>();
        studentsIds.add(new Long(100));
        studentsIds.add(new Long(110));
        studentsIds.add(new Long(120));
        studentsIds.add(new Long(130));
        studentsIds.add(new Long(140));
        c.setStudents(studentsIds);

        Course saved = RULE.client().target(
                String.format("http://localhost:%d/%s/courses/%d", RULE.getLocalPort(), acad, id))
                .request()
                .put(Entity.json(c), Course.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getStudents());
                    assertNotNull(saved.getSubjectIds());
                },
                () -> {
                    assertEquals(4, saved.getSubjectIds().size());
                    assertEquals(new Long(78), saved.getSubjectIds().get(0));
                    assertEquals(new Long(79), saved.getSubjectIds().get(1));
                    assertEquals(new Long(80), saved.getSubjectIds().get(2));
                    assertEquals(new Long(81), saved.getSubjectIds().get(3));
                },
                () -> {
                    assertEquals(5, saved.getStudents().size());
                    assertEquals(new Long(100), saved.getStudents().get(0));
                    assertEquals(new Long(110), saved.getStudents().get(1));
                    assertEquals(new Long(120), saved.getStudents().get(2));
                    assertEquals(new Long(130), saved.getStudents().get(3));
                    assertEquals(new Long(140), saved.getStudents().get(4));
                }
        );
    }
}
