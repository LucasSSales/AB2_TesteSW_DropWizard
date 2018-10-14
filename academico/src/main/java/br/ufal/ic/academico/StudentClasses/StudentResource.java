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

        Student s = new Student(entity.getName());
        s.setScore(entity.getScore());
        s.setApproved(entity.getApproved());
        s.setDisapproved(entity.getDisapproved());
        s.setStudying(entity.getStudying());

        return Response.ok(studentDAO.persist(s)).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StudentDTO {

        private String name;
        private int score;
        private int[] studying;
        private int[] approved;
        private int[] disapproved;
    }


}
