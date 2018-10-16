package br.ufal.ic.academico;

import br.ufal.ic.academico.StudentClasses.Student;
import br.ufal.ic.academico.StudentClasses.StudentDAO;
import br.ufal.ic.academico.StudentClasses.StudentResource;
import br.ufal.ic.academico.exemplos.MyResource;
import br.ufal.ic.academico.exemplos.Person;
import br.ufal.ic.academico.exemplos.PersonDAO;
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
import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentTest {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private StudentDAO stuDao = mock(StudentDAO.class);

    private final StudentResource stuResource = new StudentResource(stuDao);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";

    @Test
    public void testDelete(){

        List<Student> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Student>>() {});

        assertTrue(saved.size() > 0);

        Long id = saved.get(0).getId();
        int size = saved.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/students/%d", RULE.getLocalPort(), acad, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted);

        List<Student> saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Student>>() {});

        int newSize = saved2.size();

        assertEquals(newSize, size-1);

    }

    @Test
    public void testSave() {

        Student s = new Student("Joseph Joestar", new Long(1), new Long(2), false);

        Student saved = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Student.class);

        assertNotNull(saved.getId());

        List<Student> list = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Student>>() {});

        assertTrue(list.size() > 0);

        //GET COM ID
        Long id = list.get(0).getId();

        Student getWthId = RULE.client().target(
                String.format("http://localhost:%d/%s/students/%d", RULE.getLocalPort(), acad, id))
                .request()
                .get(new GenericType<Student>() {});

        assertNotNull(getWthId);
        assertEquals(id, getWthId.getId());


    }

    @Test
    public void testList() {

        List<Student> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Student>>() {});

        assertTrue(saved.size() > 0);
    }

    @Test
    public void testPost(){
        Student s = new Student("Jotaro Kujo", new Long(10), new Long(15), false);
        s.setScore(0);
        s.setStudying(new ArrayList<Long>());
        s.setApproved(new ArrayList<Long>());
        s.setDisapproved(new ArrayList<Long>());
        Student saved = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Student.class);

        assertAll(
                () -> {
                    assertNotNull(saved.getId());
                    assertEquals("Jotaro Kujo", saved.getName());
                    assertEquals(new Long(10), s.getCourseID());
                    assertEquals(new Long(15), saved.getDepartamentID());
                    assertFalse(saved.isPostgraduate());
                },
                () -> {
                    assertNotNull(saved.getScore());
                    assertEquals(0, saved.getScore());
                },
                () -> {
                    assertNotNull(saved.getStudying());
                    assertEquals(0, saved.getStudying().size());
                },
                () -> {
                    assertNotNull(saved.getApproved());
                    assertEquals(0, saved.getApproved().size());},
                () -> {
                    assertNotNull(saved.getDisapproved());
                    assertEquals(0, saved.getDisapproved().size());
                }
        );
    }

    @Test
    public void testPut(){
        //FAZENDO POST
        Student s = new Student("Giorno Giovanna", new Long(10), new Long(15), false);
        s.setScore(0);
        s.setStudying(new ArrayList<Long>());
        s.setApproved(new ArrayList<Long>());
        s.setDisapproved(new ArrayList<Long>());
        Student post = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Student.class);

        //FAZENDO O UPDATE
        assertNotNull(post.getId());
        Long id = post.getId();
        s.setScore(100);

        ArrayList<Long> studying = new ArrayList<Long>();
        studying.add(new Long(60));
        s.setStudying(studying);

        ArrayList<Long> approved = new ArrayList<Long>();
        approved.add(new Long(78));
        approved.add(new Long(79));
        s.setApproved(approved);

        Student saved = RULE.client().target(
                String.format("http://localhost:%d/%s/students/%d", RULE.getLocalPort(), acad, id))
                .request()
                .put(Entity.json(s), Student.class);

        assertAll(
                () -> {
                    assertNotNull(saved.getScore());
                    assertEquals(100, saved.getScore());
                },
                () -> {
                    assertNotNull(saved.getStudying());
                    assertEquals(1, saved.getStudying().size());
                    assertEquals(new Long(60), saved.getStudying().get(0));
                },
                () -> {
                    assertNotNull(saved.getApproved());
                    assertEquals(2, saved.getApproved().size());},
                () -> {
                    assertNotNull(saved.getDisapproved());
                    assertEquals(0, saved.getDisapproved().size());
                }
        );
    }



}
