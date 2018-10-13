package br.ufal.ic.academico.ApplicationClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Path("kkeaemen")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    private final TestDAO tdao;

    @GET
    @UnitOfWork
    public Response teste(){
        log.info("kkeaemen");
        return Response.ok(tdao.t()).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class testDTO {

        private String name;
        private int number;
    }

}
