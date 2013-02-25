package com.salvador.services;

import com.salvador.pages.PageItem;
import com.salvador.runners.TestManager;
import com.salvador.runners.TestSuite;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 25/02/13
 * Time: 14:49
 */
@Path("/runner")
@Produces("application/json")
@RequestScoped
public class TestRunnerService {

    final Logger log = LoggerFactory.getLogger(TestRunnerService.class);

    @Inject
    transient TestManager testManager;

    @GET
    @Path("/passed/{id}/{scenarioId}/{scenarioStepId}")
    public Response passed(@PathParam("id") String id,
                           @PathParam("scenarioId") String scenarioId,
                           @PathParam("scenarioStepId") String scenarioStepId) {
        final TestSuite suite = testManager.getSuites().get(id);

        if (suite != null) {
            log.debug("passed {}", scenarioId);
            final PageItem item = suite.getPage().getItem(scenarioId);
            if (item != null) {
                Scenario scenario = (Scenario) item;
                final ScenarioStep step = scenario.getStep(scenarioStepId);
                if (step != null) {
                    step.setComplete(true);
                    step.setPassed(true);
                }
            }
        }

        return Response.ok().build();
    }

    public static void main(String[] args) {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:8080/rest/runner/passed/1111/2222/3333");

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

    }
}
