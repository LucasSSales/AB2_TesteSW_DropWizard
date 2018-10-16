package br.ufal.ic.academico;

import br.ufal.ic.academico.DepartamentClasses.Departament;
import br.ufal.ic.academico.ProfessorClasses.Professor;
import br.ufal.ic.academico.SecretaryClasses.Secretary;
import br.ufal.ic.academico.SecretaryClasses.SecretaryDAO;
import br.ufal.ic.academico.SecretaryClasses.SecretaryResources;
import br.ufal.ic.academico.StudentClasses.Student;
import br.ufal.ic.academico.SubjectClasses.Subject;
import br.ufal.ic.academico.SubjectClasses.SubjectInfos;
import ch.qos.logback.classic.Level;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
public class RequirementsTest {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

//    private SecretaryDAO secDao = mock(SecretaryDAO.class);
//
//    private final SecretaryResources secResource = new SecretaryResources(secDao);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";



    @Test
    public void testList() {

        //POST CASO O BANCO ESTEJA VAZIO
        Student s = new Student("Dio Brando", new Long(10), new Long(15), false);
        s.setScore(0);

        Student post = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Student.class);

        List<Student> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/drca/matricula", RULE.getLocalPort(), acad))
                .request()
                .get(new GenericType<List<Student>>() {});

        assertTrue(saved.size() > 0);
    }

    @Test
    public void testGet2() {
        //POST DE DEPARTAMENTO
        Departament d = new Departament("Computacao");
        d.setProfessors(new ArrayList<Long>());
        d.setGradSec(new Long(69));
        d.setPosGradSec(new Long(-1));
        d.setCoursesIds(new ArrayList<Long>());
        Departament post = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(d), Departament.class);

        //ID DO DEPARTAMENTO CRIADO
        Long depId = post.getId();

        //CRIANDO MATERIAS PARA O DEPARTAMENTO
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        Subject s = new Subject("Teste de Software", new Long(150), new Long(80), depId, 200, 100, new Long(270), false);
        s.setStudentsId(new ArrayList<Long>());
        Subject saved1 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);
        subjects.add(saved1);

        Subject s1 = new Subject("Compiladores", new Long(340), new Long(80), depId, 200, 1500, new Long(270), true);
        s1.setStudentsId(new ArrayList<Long>());
        Subject saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s1), Subject.class);
        subjects.add(saved2);

        Subject s2 = new Subject("Redes 01", new Long(200), new Long(80), depId+2, 200, 100, new Long(270), false);
        s2.setStudentsId(new ArrayList<Long>());
        Subject saved3 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s2), Subject.class);

        Subject s3 = new Subject("Sistemas Operacionais", new Long(200), new Long(80), depId, 200, 100, new Long(270), false);
        s3.setStudentsId(new ArrayList<Long>());
        Subject saved4 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s3), Subject.class);
        subjects.add(saved4);

        //CRIANDO ESTUDANTE
        Student st = new Student("Guido Mista", new Long(4), depId, false);
        st.setScore(0);
        Student post1 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st), Student.class);


        //TESTANDO O CASO QUE FUNCIONA
        List<Subject> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/drca/matricula/%d/%d", RULE.getLocalPort(), acad, post1.getId(), depId))
                .request()
                .get(new GenericType<List<Subject>>() {});

        int i = 0;
        assertNotNull(saved);
        for (Subject subj: saved ) {
            assertNotNull(subj);
            assertEquals(subjects.get(i).getName(), subj.getName(), "Nomes não batem");
            assertEquals(subjects.get(i).getStudentsId(), subj.getStudentsId(), "StudentIds nao batem");
            assertEquals(subjects.get(i).getProfessor(), subj.getProfessor(), "StudentIds nao batem");
            assertEquals(subjects.get(i).getPrerequisites(), subj.getPrerequisites(), "StudentIds nao batem");
            assertEquals(subjects.get(i).getDepartamentId(), subj.getDepartamentId(), "StudentIds nao batem");
            assertEquals(subjects.get(i).getCourseID(), subj.getCourseID(), "StudentIds nao batem");
            i++;
        }

        assertTrue(saved.size() > 0);

        //TESTANDO CASO DE ERRO
        assertThrows(InternalServerErrorException.class,
                () -> {
                    List<Subject> savedError1 = RULE.client().target(
                            String.format("http://localhost:%d/%s/drca/matricula/1500/%d", RULE.getLocalPort(), acad, depId))
                            .request()
                            .get(new GenericType<List<Subject>>() {});
                }, "Não lançou a excessao");

        assertThrows(InternalServerErrorException.class,
                () -> {
                    List<Subject> savedError2 = RULE.client().target(
                            String.format("http://localhost:%d/%s/drca/matricula/%d/30000", RULE.getLocalPort(), acad, post1.getId()))
                            .request()
                            .get(new GenericType<List<Subject>>() {});
                }, "Não lançou a excessao");

    }

    //CASOS NORMAIS
    @Test
    public void testPut1(){
        //DEPARTAMENTO
        Departament d = new Departament("Computacao");
        d.setProfessors(new ArrayList<Long>());
        d.setGradSec(new Long(69));
        d.setPosGradSec(new Long(-1));
        d.setCoursesIds(new ArrayList<Long>());
        Departament post = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(d), Departament.class);

        //MATERIA
        Subject s = new Subject("Teste de Software", new Long(-1), new Long(80),
                post.getId(), 200, 0, new Long(270), false);
        s.setStudentsId(new ArrayList<Long>());
        Subject saved1 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);

        //ALUNO
        Student st = new Student("Caesar Zeppelli", new Long(4), post.getId(), false);
        st.setScore(0);
        Student post1 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st), Student.class);

        //TESTANDO O CASO QUE FUNCIONA
        String saved = RULE.client().target( //                                                                id aluno/id dept/id materia
                String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), post.getId(), saved1.getId()))
                .request()
                .put(Entity.json(saved1.getId()), String.class);

        assertEquals("Sucesso", saved.toString());

        assertThrows(InternalServerErrorException.class,
                () -> {
                    String fail = RULE.client().target( //
                            String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, 0, post.getId(), saved1.getId()))
                            .request()
                            .put(Entity.json(saved1.getId()), String.class);
                });

        assertThrows(InternalServerErrorException.class,
                () -> {
                    String fail = RULE.client().target( //
                            String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), 0, saved1.getId()))
                            .request()
                            .put(Entity.json(saved1.getId()), String.class);
                });

        assertThrows(InternalServerErrorException.class,
                () -> {
                    String fail = RULE.client().target( //
                            String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), post.getId(), 0))
                            .request()
                            .put(Entity.json(saved1.getId()), String.class);
                });



    }


    @Test
    public void testPut2() {
        //DEPARTAMENTO
        Departament d = new Departament("Computacao");
        d.setProfessors(new ArrayList<Long>());
        d.setGradSec(new Long(69));
        d.setPosGradSec(new Long(-1));
        d.setCoursesIds(new ArrayList<Long>());
        Departament post = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(d), Departament.class);

        //MATERIA
        Subject s = new Subject("Teste de Software", new Long(-1), new Long(80),
                post.getId(), 200, 0, new Long(270), true);
        s.setStudentsId(new ArrayList<Long>());
        Subject saved1 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);

        //ALUNO DE POS
        Student st = new Student("Caesar Zeppelli", new Long(4), post.getId(), true);
        st.setScore(0);
        Student post1 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st), Student.class);

        //TESTANDO O CASO QUE FUNCIONA
        String saved = RULE.client().target( //                                                                id aluno/id dept/id materia
                String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), post.getId(), saved1.getId()))
                .request()
                .put(Entity.json(saved1.getId()), String.class);

        assertEquals("Sucesso", saved.toString());
    }

    @Test
    public void testProofOfEnrollment(){
        //CRIANDO ESTUDANTE
        Student st = new Student("Jean-Pierre Pollnaref", new Long(4), new Long(10), false);
        st.setScore(0);
        st.setStudying(new ArrayList<>());


        //CRIANDO MATERIAS PARA O DEPARTAMENTO
        ArrayList<Long> subjects = new ArrayList<>();
        ArrayList<String> subjNames = new ArrayList<>();

        Subject s = new Subject("Teste de Software", new Long(-1), new Long(80), new Long(10), 200, 0, new Long(270), false);
        s.setStudentsId(new ArrayList<Long>());
        Subject saved1 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);
        subjects.add(saved1.getId());
        subjNames.add(saved1.getName());

        Subject s1 = new Subject("Compiladores", new Long(340), new Long(80), new Long(10), 200, 150, new Long(270), true);
        s1.setStudentsId(new ArrayList<Long>());
        Subject saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s1), Subject.class);
        subjects.add(saved2.getId());
        subjNames.add(saved2.getName());

        st.setStudying(subjects);
        Student post = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st), Student.class);

        //EMITINDO COMPROVANTE
        ProofOfEnrollment saved = RULE.client().target(
                String.format("http://localhost:%d/%s/drca/comprovante/%d", RULE.getLocalPort(), acad, post.getId()))
                .request()
                .get(new GenericType<ProofOfEnrollment>() {});

        assertNotNull(saved);


        ArrayList<String> longs = saved.getSubjects();
        assertAll(
                () -> {
                    assertNotNull(saved.getStudent());
                    assertEquals(st.getName(), saved.getStudent().getName());
                    assertEquals(st.getScore(), saved.getStudent().getScore());
                    assertEquals(st.getDepartamentID(), saved.getStudent().getDepartamentID());
                    assertEquals(st.getCourseID(), saved.getStudent().getCourseID());
                },
                () -> {
                    assertNull(saved.getStudent().getApproved());
                    assertNull(saved.getStudent().getDisapproved());
                    assertNotNull(saved.getStudent().getStudying());
                    int i = 0;
                    for (Long l : saved.getStudent().getStudying()) {
                        assertNotNull(l);
                        assertEquals(subjects.get(i), l);
                        i++;
                    }
                },
                () -> {
                    ArrayList<String> studying = saved.getSubjects();
                    assertNotNull(studying);
                    int i = 0;
                    for (String str : studying) {
                        assertNotNull(str);
                        assertEquals(subjNames.get(i), str);
                        i++;
                    }
                    System.out.println(i);

                }
        );
    }

    @Test
    public void testSecList(){
        Long depId = new Long(350);

        //CRIANDO MATERIAS PARA  A SECRETARIA
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        Subject s = new Subject("Teste de Software", new Long(150), new Long(80), depId, 200, 100, new Long(270), false);
        s.setStudentsId(new ArrayList<Long>());
        Subject saved1 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);
        subjects.add(saved1);

        Subject s1 = new Subject("Compiladores", new Long(340), new Long(80), depId-1, 200, 1500, new Long(270), true);
        s1.setStudentsId(new ArrayList<Long>());
        Subject saved2 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s1), Subject.class);
        subjects.add(saved2);

        Subject s2 = new Subject("Redes 01", new Long(200), new Long(80), depId, 200, 100, new Long(270), false);
        s2.setStudentsId(new ArrayList<Long>());
        Subject saved3 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s2), Subject.class);
        subjects.add(saved3);

        Subject s3 = new Subject("Sistemas Operacionais", new Long(200), new Long(80), depId, 200, 100, new Long(270), false);
        s3.setStudentsId(new ArrayList<Long>());
        Subject saved4 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s3), Subject.class);



        //POST DA SECRETARIA
        Secretary sec = new Secretary("Computacao", true, depId);
        ArrayList<Long> subjs = new ArrayList<Long>();
        subjs.add(saved1.getId());
        subjs.add(saved2.getId());
        subjs.add(saved3.getId());
        sec.setSubjects(subjs);

        Secretary post = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(sec), Secretary.class);

        //ID DA SECRETARIA
        Long secId = post.getId();

        //EMITINDO LISTA DE MATERIAS
        List<Subject> saved = RULE.client().target(
                String.format("http://localhost:%d/%s/drca/ofertas/%d", RULE.getLocalPort(), acad, secId))
                .request()
                .get(new GenericType<List<Subject>>() {});

        assertAll(
                () -> { assertNotNull(saved); },
                () -> {
                    int i = 0;
                    for (Subject sub : saved){
                        assertNotNull(sub);
                        assertEquals(subjects.get(i).getName(), sub.getName());
                        assertEquals(subjects.get(i).getDepartamentId(), sub.getDepartamentId());
                        assertEquals(subjects.get(i).getPrerequisites(), sub.getPrerequisites());
                        assertEquals(subjects.get(i).getProfessor(), sub.getProfessor());
                        assertEquals(subjects.get(i).getCourseID(), sub.getCourseID());
                        assertEquals(subjects.get(i).getMinCredits(), sub.getMinCredits());
                        assertEquals(subjects.get(i).getCredits(), sub.getCredits());
                        for ( Long l: sub.getStudentsId() ) {
                            assertEquals(subjects.get(i).getStudentsId(), l);
                        }
                        i++;
                    }
                }
        );

    }

    @Test
    public void testSubjInfos(){
        //CRIANDO PROFESSOR
        Professor p = new Professor("WIlly Tiengo", new Long(123));
        p.setSubjects(new ArrayList<Long>());
        Professor pSaved = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(p), Professor.class);

        //CRIANDO MATERIA
        Subject s = new Subject("Teste de Software", new Long(150), new Long(80), new Long(123), 200, 100, pSaved.getId(), false);

        assertEquals(pSaved.getId(), s.getProfessor());

        //CRIANDO ALUNOS
        ArrayList<String> stuNames = new ArrayList<String>();
        ArrayList<Long> stuIds = new ArrayList<Long>();

        Student st1 = new Student("Noriaki Kakyoin", new Long(4), new Long(123), false);
        st1.setScore(0);
        Student post1 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st1), Student.class);
        stuNames.add(st1.getName());
        stuIds.add(post1.getId());

        Student st2 = new Student("Mohammed Avdol", new Long(4), new Long(123), false);
        st2.setScore(0);
        Student post2 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st2), Student.class);
        stuNames.add(st2.getName());
        stuIds.add(post2.getId());

        Student st3 = new Student("Hol Horse", new Long(4), new Long(123), false);
        st3.setScore(0);
        Student post3 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st3), Student.class);
        stuNames.add(st3.getName());
        stuIds.add(post3.getId());

        Student st4 = new Student("Iggy", new Long(4), new Long(123), false);
        st4.setScore(0);
        Student post4 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st4), Student.class);
        stuNames.add(st4.getName());
        stuIds.add(post4.getId());

        //FAZENDO POST DA MATERIA
        s.setStudentsId(stuIds);
        Subject sSaved = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);

        //EMITINDO INFORMAÇÕES DA MATERIA
        SubjectInfos siSaved = RULE.client().target(
                String.format("http://localhost:%d/%s/drca/materiaInfo/%d", RULE.getLocalPort(), acad, sSaved.getId()))
                .request()
                .get(new GenericType<SubjectInfos>() {});

        assertAll(
                () -> { assertNotNull(siSaved); },
                () ->{
                    assertNotNull(siSaved.getSubject());
                    assertEquals(s.getName(), siSaved.getSubject().getName());
                    assertEquals(s.getCredits(), siSaved.getSubject().getCredits());
                    assertEquals(s.getMinCredits(), siSaved.getSubject().getMinCredits());
                    assertEquals(s.getProfessor(), siSaved.getSubject().getProfessor());
                    assertEquals(s.getDepartamentId(), siSaved.getSubject().getDepartamentId());
                    assertEquals(s.getCourseID(), siSaved.getSubject().getCourseID());
                    assertEquals(s.getPrerequisites(), siSaved.getSubject().getPrerequisites());
                    assertFalse(siSaved.getSubject().isPostgraduate());
                    System.out.println(siSaved.getSubject().getName());
                },
                () -> {
                    assertNotNull(siSaved.getProfName());
                    assertEquals(p.getName(), siSaved.getProfName());
                },
                () -> {
                    assertNotNull(siSaved.getStudentNames());
                    int i = 0;
                    for (String name: siSaved.getStudentNames()) {
                        assertNotNull(name);
                        assertEquals(stuNames.get(i), name);
                        i++;
                    }
                    assertNotEquals(0, i);
                }

        );

    }

}
