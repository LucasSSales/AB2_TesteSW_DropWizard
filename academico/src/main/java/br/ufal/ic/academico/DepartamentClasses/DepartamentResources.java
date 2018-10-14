package br.ufal.ic.academico.DepartamentClasses;

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

@Path("departaments")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class DepartamentResources {

    private final DepartamentDAO departamentDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll");
        return Response.ok(departamentDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {

        log.info("getById: id={}", id);
        Departament p = departamentDAO.get(id);
        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(DepartamentDTO entity) {

        log.info("save: {}", entity);

        Departament d = new Departament(entity.getName(), entity.getCoursesIds());
        d.setGradSec(entity.getGradSec());
        d.setPosGradSec(entity.getPosGradSec());
        d.setProfessors(entity.getProfessors());
        return Response.ok(departamentDAO.persist(d)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, DepartamentDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Departament d = departamentDAO.get(id);
        d.setGradSec(entity.getGradSec());
        d.setPosGradSec(entity.getPosGradSec());
        d.setProfessors(entity.getProfessors());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete: id={}", id);
        departamentDAO.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DepartamentDTO {
        private String name;
        private int[] coursesIds;
        private int gradSec;
        private int posGradSec;
        private int[] professors;
    }


}
