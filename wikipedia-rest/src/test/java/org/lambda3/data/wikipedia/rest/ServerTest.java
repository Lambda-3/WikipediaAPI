/*
 * ==========================License-Start=============================
 * wikipedia-rest : ServerTest
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
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTestNg;

import javax.ws.rs.core.Application;

/**
 *
 */
public abstract class ServerTest extends JerseyTestNg.ContainerPerClassTest {

    @Override
    protected Application configure() {

        Config config = ConfigFactory
                .load("wiki-api.local.conf")
                .resolveWith(ConfigFactory.load())
                .withFallback(ConfigFactory.load("reference.conf"));

        return WikipediaRESTServer.generateResourceConfig(config);
    }

    @Override
    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(JsonFeature.class);
        super.configureClient(clientConfig);
    }
}
