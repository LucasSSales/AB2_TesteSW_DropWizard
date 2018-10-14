package br.ufal.ic.academico.CourseClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("courses")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

    private final CourseDAO courseDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll");
        return Response.ok(courseDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
        log.info("getById: id={}", id);
        Course c = courseDAO.get(id);
        return Response.ok(c).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(CourseDTO entity) {

        log.info("save: {}", entity);

        Course c = new Course(entity.getName(), entity.getDepartamentId(), entity.getSecretaryId(), entity.getSubjectIds());
        c.setStudents(entity.getStudents());

        return Response.ok(courseDAO.persist(c)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, CourseDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Course c = courseDAO.get(id);
        c.setStudents(entity.getStudents());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {

        log.info("delete: id={}", id);
        courseDAO.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CourseDTO {
        private String name;
        private int departamentId;
        private int secretaryId;
        private int[] subjectIds;
        private long[] students;
    }



}
