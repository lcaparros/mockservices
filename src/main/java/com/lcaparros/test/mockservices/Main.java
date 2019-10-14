package com.lcaparros.test.mockservices;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author lcaparros
 */

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);

        int exposedPort = 2000;

        List<String> portBindings = new ArrayList<String>();
        portBindings.add(exposedPort + ":1080");
        MockServerContainer localMockServer = new MockServerContainer();
        localMockServer.setPortBindings(portBindings);
        localMockServer.withCreateContainerCmdModifier(createContainerCmd -> ((CreateContainerCmd)createContainerCmd).withName("mockServer"));
        localMockServer.start();

        logger.info("MockServer is listenning to http://" + localMockServer.getContainerIpAddress() + ":" + localMockServer.getServerPort());


        /*  Expectation inicialization
         *  Define here the wanted expectations as the defined one
         */
        MockServerClient mockServerClient = new MockServerClient("localhost", localMockServer.getServerPort());

        mockServerClient.when(
                request()
                        .withMethod("GET")
                        .withPath("/ping")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody("pong")
        );
    }
}
