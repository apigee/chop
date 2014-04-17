/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.usergrid.chop.webapp.elasticsearch;

import com.google.inject.Inject;
import org.apache.usergrid.chop.webapp.elasticsearch.ElasticSearchClient;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.usergrid.chop.webapp.ChopUiModule;

import static junit.framework.TestCase.assertNotNull;

@RunWith(JukitoRunner.class)
@UseModules(ChopUiModule.class)
public class ElasticSearchClientTest {

    @Inject
    ElasticSearchClient elasticSearchClient;

    @Test
    public void test() {
        assertNotNull(elasticSearchClient);
        assertNotNull(elasticSearchClient.getClient());
    }
}