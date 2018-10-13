package br.ufal.ic.academico.ApplicationClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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


//    @Getter
//    @RequiredArgsConstructor
//    @AllArgsConstructor
//    @ToString
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class SubjectDTO {
//
//        private String name;
//        private int number;
//    }


}
