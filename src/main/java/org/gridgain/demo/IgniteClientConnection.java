/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gridgain.demo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

@Singleton
public class IgniteClientConnection {

    private IgniteClient client;

    @PostConstruct
    public void init() {
        ClientConfiguration cfg = new ClientConfiguration();

        cfg.setAddresses(Application.IGNITE_SERVER_ADDRESS);
        cfg.setPartitionAwarenessEnabled(true);

        client = Ignition.startClient(cfg);
    }

    public IgniteClient getClient() {
        return client;
    }

    @PreDestroy
    public void close() {
        try {
            client.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
