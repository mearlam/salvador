package com.salvador.services;

import com.salvador.pages.Page;
import com.salvador.pages.PageItem;
import com.salvador.runners.TestManager;
import com.salvador.runners.TestSuite;
import com.salvador.runners.progress.PageProgress;
import com.salvador.runners.progress.ScenarioProgress;
import com.salvador.runners.progress.ScenarioTestProgress;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import com.salvador.scenarios.ScenarioTest;
import com.salvador.scenarios.ScenarioTextFormatter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

    @Inject
    transient ScenarioTextFormatter scenarioTextFormatter;


    @GET
    @Path("/passed/{id}/{pageId}/{scenarioId}/{scenarioStepId}/{scenarioTestId}")
    public Response passed(@PathParam("id") String id,
                                @PathParam("pageId") String pageId,
                                @PathParam("scenarioId") String scenarioId,
                                @PathParam("scenarioStepId") String scenarioStepId,
                                @PathParam("scenarioTestId") String scenarioTestId) {
        final TestSuite suite = testManager.getSuites().get(id);

        if (suite != null) {
            suite.getLogReader().addLog("passed-service-update:" + id);
            log.debug("passed {}", scenarioId);

            ScenarioTestProgress testProgress = new ScenarioTestProgress();
            testProgress.setPassed(true);
            testProgress.setComplete(true);

            updateProgress(pageId, scenarioId, scenarioStepId, scenarioTestId, suite, testProgress);
        }

        return Response.ok().build();
    }

    @GET
    @Path("/scenario-complete/{id}/{pageId}/{scenarioId}")
    public Response scenarioComplete(@PathParam("id") String id,
                           @PathParam("pageId") String pageId,
                           @PathParam("scenarioId") String scenarioId) {
        final TestSuite suite = testManager.getSuites().get(id);

        if (suite != null) {
            log.debug("scenario-complete {}", scenarioId);
            suite.setTestsToRun(suite.getTestsToRun()-1);
        }

        return Response.ok().build();
    }

    @GET
    @Path("/{page-start}/{id}/{pageId}")
    public Response scenarioComplete(@PathParam("id") String id,
                                     @PathParam("pageId") String pageId) {
        final TestSuite suite = testManager.getSuites().get(id);

        if (suite != null) {
            log.debug("page-start {}", pageId);

            final Page page = suite.getPage(pageId);
            PageProgress testProgress = new PageProgress();
            testProgress.setPage(page);
            testProgress.setPassed(true);
            testProgress.setComplete(true);
            suite.getProgress().add(testProgress);
        }

        return Response.ok().build();
    }

    @GET
    @Path("/missing/{id}/{pageId}/{scenarioId}/{scenarioStepId}/{scenarioTestId}")
    public Response missingStep(@PathParam("id") String id,
                                @PathParam("pageId") String pageId,
                                @PathParam("scenarioId") String scenarioId,
                                @PathParam("scenarioStepId") String scenarioStepId,
                                @PathParam("scenarioTestId") String scenarioTestId) {
        final TestSuite suite = testManager.getSuites().get(id);

        if (suite != null) {
            suite.getLogReader().addLog("missing-step-service-update:" + id);
            log.debug("missing {}", scenarioId);

            ScenarioTestProgress testProgress = new ScenarioTestProgress();
            testProgress.setPassed(false);
            testProgress.setMissing(true);
            testProgress.setComplete(true);

            updateProgress(pageId, scenarioId, scenarioStepId, scenarioTestId, suite, testProgress);
        }

        return Response.ok().build();
    }

    private void updateProgress(String pageId, String scenarioId,
                                String scenarioStepId,
                                String scenarioTestId, TestSuite suite,
                                ScenarioTestProgress testProgress) {
        final PageItem item = suite.getPage(pageId).getItem(scenarioId);
        if (item != null) {
            Scenario scenario = (Scenario) item;
            final ScenarioStep step = scenario.getStep(scenarioStepId);
            final ScenarioTest test = scenario.getTest(scenarioTestId);
            if (step != null && test != null) {
                ScenarioProgress progress = (ScenarioProgress) suite.getProgress(item.getId());

                if(progress == null) {
                    progress = new ScenarioProgress();
                    progress.setItem(item);
                    suite.getProgress().add(progress);
                }

                // onto the next bunch of steps for the new test data
                if(scenario.getSteps().size() == progress.getStepsComplete()) {
                    progress.setStepsComplete(0);
                    progress.getTests().add(createBlankTestProgress());
                }

                testProgress.setItem(item);
                testProgress.setText(scenarioTextFormatter.getFormattedText(step,test));

                progress.getTests().add(testProgress);
                progress.incrementStepsComplete();
            }
        }
    }

    private ScenarioTestProgress createBlankTestProgress() {
        ScenarioTestProgress progress = new ScenarioTestProgress();
        return progress;
    }



    public static void main(String[] args) {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:8080/rest/runner/passed/1111/2222/3333");

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

    }
}
