package br.ufal.ic.academico;

import br.ufal.ic.academico.DepartamentClasses.Departament;
import br.ufal.ic.academico.DepartamentClasses.DepartamentDAO;
import br.ufal.ic.academico.ProfessorClasses.Professor;
import br.ufal.ic.academico.ProfessorClasses.ProfessorDAO;
import br.ufal.ic.academico.SecretaryClasses.Secretary;
import br.ufal.ic.academico.SecretaryClasses.SecretaryDAO;
import br.ufal.ic.academico.StudentClasses.Student;
import br.ufal.ic.academico.StudentClasses.StudentDAO;
import br.ufal.ic.academico.SubjectClasses.Subject;
import br.ufal.ic.academico.SubjectClasses.SubjectDAO;
import br.ufal.ic.academico.SubjectClasses.SubjectInfos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Path("drca")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class RequirementsResources {

    private final StudentDAO studentDAO;
    private final DepartamentDAO departamentDAO;
    private final SubjectDAO subjectDAO;
    private final SecretaryDAO secretaryDAO;
    private final ProfessorDAO professorDAO;

    @GET
    @Path("/matricula")
    @UnitOfWork
    public Response getAll() {

        log.info("getAll students");

        return Response.ok(studentDAO.list()).build();
    }

    @GET
    @Path("/matricula/{id}/{depId}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id, @PathParam("depId") Long depId) {

        log.info("getById: id={}", id);

        Student s = studentDAO.get(id);
        //VERIFICA SE O ID DO ALUNO EXISTE
        if(s.equals(null))
            return Response.status(404).build();

        Departament d = departamentDAO.get(depId);
        //VERIFICA SE O depId EXISTE/SE O DEPARTAMENTO ESTÁ ASSOCIADO AO ALUNO
        if(d.equals(null) || (s.getDepartamentID() != depId))
            return Response.status(404).build();

        List<Subject> subs = subjectDAO.list();
        ArrayList<Subject> exit =  new ArrayList<Subject>();
        for(Subject sub : subs){
            if(sub.getDepartamentId() == depId)
                exit.add(sub);
        }

        return Response.ok(exit).build();
    }

    @PUT
    @Path("/matricula/{id}/{depId}/{subjId}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, @PathParam("depId") Long depId, @PathParam("subjId") Long subjId, RequirementDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Student student = studentDAO.get(id);
        //VERIFICA SE O ID DO ALUNO EXISTE
        if(student.equals(null))
            return Response.status(404).build();

        Departament d = departamentDAO.get(depId);
        //VERIFICA SE O depId EXISTE/SE O DEPARTAMENTO ESTÁ ASSOCIADO AO ALUNO
        if(d.equals(null) || (student.getDepartamentID() != depId))
            return Response.status(404).build();

        Subject subject = subjectDAO.get(subjId);
        //VERIFICA SE O ID DA MATERIA EXISTE
        if(subject.equals(null))
            return Response.status(404).build();

        //VERIFICA SE POSSUI CREDITOS O BASTANTE PARA A MATERIA
        if(subject.getMinCredits() > student.getScore())
            return Response.status(403).build();

        //VERIFICA SE ALUNO DE GRADUACAO PD CURSAR MATERIA DE POS
        if(subject.isPostgraduate() && !student.isPostgraduate() && student.getScore() < 170)
            return Response.status(403).build();

        //IMPEDE ALUNO DE POS D CURSAR MATERIA DE GRADUACO
        if(!subject.isPostgraduate() && student.isPostgraduate())
            return Response.status(403).build();

        //ArrayList<Long> disapproved = student.getDisapproved();
        ArrayList<Long> studying = student.getStudying();

        for (Long i: studying ) {
            if(i.equals(subjId))
                return Response.status(403).build();
        }

        //PRE REQUISITOS VERIFICAR SE POSSUI
        if(subject.getPrerequisites() != -1){
            ArrayList<Long> approved = student.getApproved();
            Long preRequisite = subject.getPrerequisites();

            for (Long i : approved) {
                if(i.equals(preRequisite))
                    return Response.ok(entity).build();
            }
            return Response.status(403).build();
        }

        ArrayList<Long> exit = student.getStudying();
        exit.add(subjId);
        student.setStudying(exit);

        return Response.ok(entity).build();


    }


    //COMPROVANTE DE MATRICULA
    @GET
    @Path("/comprovante/{id}")
    @UnitOfWork
    public Response getProofEnrollment(@PathParam("id") Long id) {

        log.info("getById: id={}", id);

        Student s = studentDAO.get(id);
        //VERIFICA SE O ID DO ALUNO EXISTE
        if(s.equals(null))
            return Response.status(404).build();

        //LISTA DE MATERIAS NO BANCO
        ArrayList<Subject> subject = (ArrayList) subjectDAO.list();
        //LISTA DE IDS DE MATRIAS DO ALUNO
        ArrayList<Long> subIds = s.getStudying();
        //COMPROVANTE
        ProofOfEnrollment proof = new ProofOfEnrollment(s);
        //LISTA DE NOMES DE MATERIAS QUE O ALUNO ESTA CURSANDO
        ArrayList<String> exit = new ArrayList<String>();


        for (Subject subj : subject) {
            if(subIds.contains(subj.getId()))
                exit.add(subj.getName());
        }

        proof.setSubjects(exit);

        return Response.ok(proof).build();
    }

    //LISTA DA SECRETARIA
    @GET
    @Path("/ofertas/{secId}")
    @UnitOfWork
    public Response secretaryList(@PathParam("secId") Long id) {

        log.info("getById: id={}", id);

        Secretary sec = secretaryDAO.get(id);
        if(sec.equals(null))
            return Response.status(404).build();

        //LISTA DE MATERIAS NO BANCO
        ArrayList<Subject> subject = (ArrayList) subjectDAO.list();
        //LISTA DE IDS DE MATERIAS DA SECRETARIA
        ArrayList<Long> subIds = sec.getSubjects();
        //LISTA DE MATERIAS OFERTADAS PELO CURSO
        ArrayList<Subject> exit = new ArrayList<Subject>();

        for (Subject subj : subject) {
            if(subIds.contains(subj.getId()))
                exit.add(subj);
        }

        return Response.ok(exit).build();
    }

    //INFORMAÇOES DA MATERIA
    @GET
    @Path("/materiaInfo/{subjId}")
    @UnitOfWork
    public Response subjectInfos (@PathParam("subjId") Long id) {

        log.info("getById: id={}", id);

        Subject subject = subjectDAO.get(id);
        if(subject.equals(null))
            return Response.status(404).build();

        //LISTA DE PROFESSORES NO BANCO
        ArrayList<Professor> profs = (ArrayList) professorDAO.list();
        String profName = new String();

        if(profs.contains(subject.getProfessor()))
            profName = profs.get(profs.indexOf( subject.getProfessor() )).getName();

        //LISTA DE ALUNOS NO BANCO
        ArrayList<Student> student = (ArrayList) studentDAO.list();
        //LISTA DE IDS DOS ALUNOS MATRICULADOS
        ArrayList<Long> stuIds = subject.getStudentsId();
        //LISTA QUE IRA RECEBER OS NOMES DOS ALUNOS NO CURSO
        ArrayList<String> stuNames = new ArrayList<String>();

        for (Student stu: student) {
            if(stuIds.contains(stu.getId()))
                stuNames.add(stu.getName());
        }

        //SubjectInfos si = new SubjectInfos(subject, stuNames, profName);
        return Response.ok().build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequirementDTO {
        private Long subjId;
    }





}
