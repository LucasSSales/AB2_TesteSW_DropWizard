package br.ufal.ic.academico;

import br.ufal.ic.academico.SecretaryClasses.Secretary;
import br.ufal.ic.academico.SecretaryClasses.SecretaryDAO;
import br.ufal.ic.academico.SecretaryClasses.SecretaryResources;
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
public class SecretaryTest {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private SecretaryDAO secDao = mock(SecretaryDAO.class);

    private final SecretaryResources secResource = new SecretaryResources(secDao);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";

    @Test
    public void testDelete(){

        List<Secretary> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        assertTrue(saved.size() > 0, "Lista está vazia");

        Long id = saved.get(0).getId();
        int size = saved.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries/%d", RULE.getLocalPort(), acad, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted, "Ids não batem");

        List<Secretary> saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        int newSize = saved2.size();

        assertEquals(newSize, size-1, "Tamanhos não batem");

    }

    @Test
    public void testSave() {

        Secretary s = new Secretary("Computacao", true, new Long(80));
        s.setSubjects(new ArrayList<Long>());

        Secretary saved = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Secretary.class);

        assertNotNull(saved, "O objeto está null");
        assertNotNull(saved.getId(), "O objeto está null");

        List<Secretary> list = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        assertTrue(list.size() > 0, "A lista está vazia");

        //GET COM ID
        Long id = list.get(0).getId();

        Secretary getWthId = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries/%d", RULE.getLocalPort(), acad, id))
                .request()
                .get(new GenericType<Secretary>() {});

        assertNotNull(getWthId, "Objeto retornou null");
        assertEquals(id, getWthId.getId(), "Os Ids não batem");


    }

    @Test
    public void testList() {

        List<Secretary> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        assertTrue(saved.size() > 0);
    }

    @Test
    public void testPost(){
        Secretary s = new Secretary("Computacao", true, new Long(80));
        s.setSubjects(new ArrayList<Long>());

        Secretary saved = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Secretary.class);

        assertAll(
                () -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getId());
                    assertNotNull(saved.getDepartamentId());
                },
                () -> {
                    assertEquals("Computacao", saved.getName());
                    assertEquals(new Long(80), saved.getDepartamentId());
                },
                () -> { assertTrue(saved.isPostgraduate()); },
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
        Secretary s = new Secretary("Computacao", true, new Long(80));
        s.setSubjects(new ArrayList<Long>());

        Secretary post = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Secretary.class);

        //FAZENDO O UPDATE
        assertNotNull(post, "Objeto retornado está null");
        assertNotNull(post.getId());
        Long id = post.getId();

        ArrayList<Long> subjectsIds = new ArrayList<Long>();
        subjectsIds.add(new Long(78));
        subjectsIds.add(new Long(79));
        subjectsIds.add(new Long(80));
        subjectsIds.add(new Long(81));

        s.setSubjects(subjectsIds);

        Secretary saved = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries/%d", RULE.getLocalPort(), acad, id))
                .request()
                .put(Entity.json(s), Secretary.class);

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
                }
        );
    }

}
