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
package org.apache.usergrid.chop.webapp.dao;

import com.google.inject.Inject;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.apache.usergrid.chop.api.Module;
import org.apache.usergrid.chop.webapp.dao.model.BasicModule;
import org.apache.usergrid.chop.webapp.elasticsearch.IElasticSearchClient;
import org.apache.usergrid.chop.webapp.elasticsearch.Util;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ModuleDao extends Dao {

    public static final String DAO_INDEX_KEY = "modules";
    public static final String DAO_TYPE_KEY = "module";

    private static final int MAX_RESULT_SIZE = 1000;


    @Inject
    public ModuleDao( IElasticSearchClient elasticSearchClient ) {
        super( elasticSearchClient );
    }


    public boolean save( Module module ) throws Exception {

        IndexResponse response = elasticSearchClient.getClient()
                .prepareIndex( DAO_INDEX_KEY, DAO_TYPE_KEY, module.getId() )
                .setRefresh( true )
                .setSource(
                        jsonBuilder()
                                .startObject()
                                .field( "groupId", module.getGroupId() )
                                .field( "artifactId", module.getArtifactId() )
                                .field( "version", module.getVersion() )
                                .field( "vcsRepoUrl", module.getVcsRepoUrl() )
                                .field( "testPackageBase", module.getTestPackageBase() )
                                .endObject()
                )
                .execute()
                .actionGet();

        return response.isCreated();
    }


    public Module get( String id ) {

        SearchResponse response = elasticSearchClient.getClient()
                .prepareSearch( DAO_INDEX_KEY )
                .setTypes( DAO_TYPE_KEY )
                .setQuery( termQuery( "_id", id ) )
                .execute()
                .actionGet();

        SearchHit hits[] = response.getHits().hits();

        return hits.length > 0 ? toModule( hits[0] ) : null;
    }


    private static Module toModule( SearchHit hit ) {
        Map<String, Object> json = hit.getSource();

        return new BasicModule(
                Util.getString( json, "groupId" ),
                Util.getString( json, "artifactId" ),
                Util.getString( json, "version" ),
                Util.getString( json, "vcsRepoUrl" ),
                Util.getString( json, "testPackageBase" )
        );
    }

    public List<Module> getAll() {

        SearchResponse response = elasticSearchClient.getClient()
                .prepareSearch( DAO_INDEX_KEY )
                .setTypes( DAO_TYPE_KEY )
                .setSize( MAX_RESULT_SIZE )
                .execute()
                .actionGet();

        ArrayList<Module> modules = new ArrayList<Module>();

        for ( SearchHit hit : response.getHits().hits() ) {
            modules.add( toModule( hit ) );
        }

        return modules;
    }
}