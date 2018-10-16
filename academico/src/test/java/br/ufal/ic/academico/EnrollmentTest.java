package br.ufal.ic.academico;

import br.ufal.ic.academico.DepartamentClasses.Departament;
import br.ufal.ic.academico.StudentClasses.Student;
import br.ufal.ic.academico.SubjectClasses.Subject;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
public class EnrollmentTest {

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String acad = "academicotest";

    //FALHAS NA MATRICULA

    private Long depId;

    public Departament postDep(){
        Departament d = new Departament("Computacao");
        d.setProfessors(new ArrayList<Long>());
        d.setGradSec(new Long(69));
        d.setPosGradSec(new Long(-1));
        d.setCoursesIds(new ArrayList<Long>());
        Departament post = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(d), Departament.class);
        this.depId = post.getId();
        return post;
    }

    public Subject postSubj(boolean postgraduate, int minCredits){
        Subject s = new Subject("Teste de Software", new Long(-1), new Long(80),
                depId, 200, minCredits, new Long(270), postgraduate);
        s.setStudentsId(new ArrayList<Long>());
        Subject saved1 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(s), Subject.class);
        return saved1;
    }

    public Student postStu(boolean postgraduate, int score, ArrayList<Long> studying, ArrayList<Long> approved){
        Student st = new Student("Caesar Zeppelli", new Long(4), this.depId, postgraduate);
        st.setScore(score);
        st.setStudying(studying);
        st.setApproved(approved);
        Student post1 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), acad))
                .request()
                .post(Entity.json(st), Student.class);
        return post1;
    }


    //ALUNO DE POS EM MATERIA DE GRADUAÇAO
    @Test
    public void testPut() {
        //DEPARTAMENTO
        Departament post = this.postDep();

        //MATERIA
        Subject saved1 = this.postSubj(false, 0);

        //ALUNO DE POS
        Student post1 = this.postStu(true, 100, new ArrayList<Long>(), new ArrayList<Long>());

        String saved = RULE.client().target( //                                                                id aluno/id dept/id materia
                String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), post.getId(), saved1.getId()))
                .request()
                .put(Entity.json(saved1.getId()), String.class);

        assertEquals("Falha", saved.toString());
    }

    //ALUNO DE GRADUAÇAO EM MATERIA DE POS SEM SCORE NECESSARIO
    @Test
    public void testPut2() {
        //DEPARTAMENTO
        Departament post = this.postDep();

        //MATERIA
        Subject saved1 = this.postSubj(true, 0);

        //ALUNO DE POS
        Student post1 = this.postStu(false, 100, new ArrayList<Long>(), new ArrayList<Long>());

        String saved = RULE.client().target( //                                                                id aluno/id dept/id materia
                String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), post.getId(), saved1.getId()))
                .request()
                .put(Entity.json(saved1.getId()), String.class);

        assertEquals("Falha", saved.toString());
    }

    //ALUNO DE SEM CREDITOS SUFICIENTES
    @Test
    public void testPut3() {
        //DEPARTAMENTO
        Departament post = this.postDep();

        //MATERIA
        Subject saved1 = this.postSubj(true, 100);

        //ALUNO DE POS
        Student post1 = this.postStu(false, 0, new ArrayList<Long>(), new ArrayList<Long>());

        String saved = RULE.client().target( //                                                                id aluno/id dept/id materia
                String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), post.getId(), saved1.getId()))
                .request()
                .put(Entity.json(saved1.getId()), String.class);

        assertEquals("Falha", saved.toString());
    }

    //ALUNO DE JA MATRICULADO
    @Test
    public void testPut4() {
        //DEPARTAMENTO
        Departament post = this.postDep();

        //MATERIA
        Subject saved1 = this.postSubj(false, 0);

        //ALUNO DE POS
        ArrayList<Long> std = new ArrayList<Long>();
        std.add(saved1.getId());
        Student post1 = this.postStu(false, 0, std, new ArrayList<Long>());


        String saved = RULE.client().target( //                                                                id aluno/id dept/id materia
                String.format("http://localhost:%d/%s/drca/matricula/%d/%d/%d", RULE.getLocalPort(), acad, post1.getId(), post.getId(), saved1.getId()))
                .request()
                .put(Entity.json(saved1.getId()), String.class);

        assertEquals("Falha!", saved.toString());
    }
}
