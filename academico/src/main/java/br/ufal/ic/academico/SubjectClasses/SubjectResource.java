package br.ufal.ic.academico.SubjectClasses;

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

@Path("subjects")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SubjectResource {

    private final SubjectDAO sdao;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll");
        return Response.ok(sdao.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {

        log.info("getById: id={}", id);

        Subject s = sdao.get(id);

        return Response.ok(s).build();
    }


    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(SubjectDTO entity) {

        log.info("save: {}", entity);

        Subject p = new Subject(
                entity.getName(), entity.getPrerequisites(), entity.getCourseID(), entity.getDepartamentId(),
                entity.getCredits(), entity.getMinCredits(), entity.getProfessor()
        );

        return Response.ok(sdao.persist(p)).build();
    }

//    @PUT
//    @Path("/{id}")
//    @UnitOfWork
//    @Consumes("application/json")
//    public Response update(@PathParam("id") Long id, SubjectDTO entity) {
//
//        log.info("update: id={}, {}", id, entity);
//
//        Subject p = sdao.get(id);
//        System.out.println(p);
//        return Response.ok(entity).build();
//    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete: id={}", id);
        sdao.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubjectDTO {

        //private Long id;
        private String name;
        private int[] prerequisites;
        private int courseID;
        private int departamentId;
        private int credits;
        private int minCredits;
        private int professor;
    }


}
