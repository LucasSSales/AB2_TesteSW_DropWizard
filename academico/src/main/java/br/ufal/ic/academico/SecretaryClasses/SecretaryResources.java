package br.ufal.ic.academico.SecretaryClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("secretaries")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SecretaryResources {
    private final SecretaryDAO secretaryDAO;

    @GET
    @UnitOfWork
    public Response getAll() {

        log.info("getAll");
        return Response.ok(secretaryDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {

        log.info("getById: id={}", id);
        Secretary p = secretaryDAO.get(id);
        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(SecretaryDTO entity) {

        log.info("save: {}", entity);
        Secretary s = new Secretary(entity.getName());
        s.setSubjects(entity.getSubjects());
        s.setDepartamentId(entity.getDepartamentId());
        return Response.ok(secretaryDAO.persist(s)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, SecretaryDTO entity) {
        log.info("update: id={}, {}", id, entity);
        Secretary s = secretaryDAO.get(id);
        s.setSubjects(entity.getSubjects());
        s.setDepartamentId(entity.getDepartamentId());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {

        log.info("delete: id={}", id);
        secretaryDAO.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SecretaryDTO {

        private String name;
        private int[] subjects;
        private int departamentId;
    }
}
