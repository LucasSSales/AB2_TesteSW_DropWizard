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


    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(SubjectDTO entity) {

        log.info("save: {}", entity);

        Subject p = new Subject(entity.getName(), entity.getPrerequisites());
        p.setMinCredits(entity.getMinCredits());

        return Response.ok(sdao.persist(p)).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubjectDTO {

        private Long id;
        private String name;
        private int minCredits;
        private int[] prerequisites;
    }


}
