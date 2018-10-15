package br.ufal.ic.academico.ProfessorClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("professors")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class ProfessorResource {
    private final ProfessorDAO professorDAO;

    @GET
    @UnitOfWork
    public Response getAll() {

        log.info("getAll");
        return Response.ok(professorDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {

        log.info("getById: id={}", id);
        Professor p = professorDAO.get(id);
        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(ProfessorDTO entity) {

        log.info("save: {}", entity);
        Professor p = new Professor(entity.getName(), entity.getDepartamentId());
        p.setSubjects(entity.getSubjects());
        return Response.ok(professorDAO.persist(p)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, ProfessorDTO entity) {

        log.info("update: id={}, {}", id, entity);
        Professor p = professorDAO.get(id);
        System.out.println(p);
        p.setSubjects(entity.getSubjects());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {

        log.info("delete: id={}", id);
        Long delId = professorDAO.get(id).getId();
        professorDAO.delete(id);
        return Response.ok(delId).build();
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProfessorDTO {

        private String name;
        private Long departamentId;
        private ArrayList<Long> subjects;
    }
}
