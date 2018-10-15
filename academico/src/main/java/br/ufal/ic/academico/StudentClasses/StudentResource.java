package br.ufal.ic.academico.StudentClasses;

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
import java.util.ArrayList;

@Path("students")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    private final StudentDAO studentDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll");
        return Response.ok(studentDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
        log.info("getById: id={}", id);
        Student p = studentDAO.get(id);
        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(StudentDTO entity) {

        log.info("save: {}", entity);

        Student s = new Student(entity.getName(), entity.getCourseID(), entity.getDepartamentID(), entity.isPostgraduate());
        s.setScore(entity.getScore());
        s.setApproved(entity.getApproved());
        s.setDisapproved(entity.getDisapproved());
        s.setStudying(entity.getStudying());

        return Response.ok(studentDAO.persist(s)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, StudentDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Student p = studentDAO.get(id);
        System.out.println(p);
        p.setScore(entity.getScore());
        p.setStudying(entity.getStudying());
        p.setApproved(entity.getApproved());
        p.setDisapproved(entity.getDisapproved());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete: id={}", id);
        studentDAO.delete(id);
        return Response.ok(id).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StudentDTO {

        private String name;
        private int courseID;
        private int departamentID;
        private int score;
        private boolean postgraduate;
        private ArrayList<Long> studying;
        private ArrayList<Long> approved;
        private ArrayList<Long> disapproved;
    }


}
