package br.ufal.ic.academico;

import br.ufal.ic.academico.StudentClasses.StudentDAO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("drca")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class RequirementsResources {

    private final StudentDAO studentDAO;

    @GET
    @UnitOfWork
    public Response getAll() {

        log.info("getAll");

        return Response.ok(studentDAO.list()).build();
    }
}
