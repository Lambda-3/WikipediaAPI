/*
 * ==========================License-Start=============================
 * wikipedia-rest : WikipediaRESTServer
 *
 * Copyright © 2017 Lambda³
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End==============================
 */

package org.lambda3.data.wikipedia.rest;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;


/**
 *
 */
class WikipediaRESTServer {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Server server;

    WikipediaRESTServer() {
        Config config = ConfigFactory.load()
                .withFallback(ConfigFactory.load("application.conf"))
                .withFallback(ConfigFactory.load("reference.conf"));

        log.debug("Initializing Wikipedia REST Server");


        ResourceConfig rc = generateResourceConfig(config);

        String uriString = config.getString("wiki-api.server.url");

        this.server = JettyHttpContainerFactory.createServer(
                URI.create(uriString), rc, false);

        log.info("Server successfully initialized, waiting for start");

    }

    static ResourceConfig generateResourceConfig(Config config) {
        ResourceConfig rc = new ResourceConfig();

        rc.property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
        rc.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        // basic features
        rc.register(JsonFeature.class);
        rc.register(ValidationFeature.class);

        rc.register(new WikipediaResource(config));

        return rc;
    }

    void start() {
        log.debug("Trying to start WikipediaRESTServer");
        try {
            server.start();
            log.info("WikipediaRESTServer should be started");
        } catch (Exception e) {
            log.error("During starting the server the following exception was thrown: ", e);
        }
    }

    void stop() {
        if (server != null) {
            log.info("Trying to shutdown the server...");
            try {
                server.stop();
                log.info("Server was shut down.");
            } catch (Exception e) {
                log.error("There was an error during shutting down the server: {}", e.getMessage());
            }
        } else {
            log.error("Server should be stopped, but wasn't initialized yet.");
        }
    }
}
