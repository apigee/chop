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
package org.apache.usergrid.chop.stack;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.usergrid.chop.api.Commit;
import org.apache.usergrid.chop.api.Module;
import org.apache.usergrid.chop.api.Runner;


/**
 * A Stack implementation used to decorate a standard Stack with runtime
 * information used by the coordinator to control and manage it.
 */
public class CoordinatedStack implements ICoordinatedStack {

    private final Stack delegate;
    private final List<CoordinatedCluster> clusters;
    private final Commit commit;
    private final Module module;
    private final User user;
    private IpRuleSet ruleSet;
    private String dataCenter;

    private StackState state = StackState.INACTIVE;
    private Set<Runner> runners;
    private Collection<Instance> runnerInstances = new LinkedList<Instance>();


    public CoordinatedStack( Stack delegate, User user, Commit commit, Module module ) {
        this.delegate = delegate;
        this.clusters = new ArrayList<CoordinatedCluster>( delegate.getClusters().size() );
        this.user = user;
        this.commit = commit;
        this.module = module;
        this.dataCenter =  delegate.getDataCenter();
        this.ruleSet = delegate.getIpRuleSet();

        for ( Cluster cluster : delegate.getClusters() ) {
            this.clusters.add( new CoordinatedCluster( cluster ) );
        }
    }


    @Override
    public String getName() {
        return delegate.getName();
    }


    @Override
    public UUID getId() {
        return delegate.getId();
    }


    @Override
    public List<? extends ICoordinatedCluster> getClusters() {
        return clusters;
    }


    @Override
    public Commit getCommit() {
        return commit;
    }


    @Override
    public Module getModule() {
        return module;
    }


    @Override
    public User getUser() {
        return user;
    }


    @Override
    public StackState getState() {
        return state;
    }


    @Override
    public Collection<Runner> getRunners() {
        return runners;
    }


    @Override
    public IpRuleSet getIpRuleSet() {
        return ruleSet;
    }


    @Override
    public Collection<Instance> getRunnerInstances() {
        return runnerInstances;
    }


    public CoordinatedStack setIpRuleSet( final IpRuleSet ruleSet ) {
        this.ruleSet = ruleSet;
        return this;
    }


    public CoordinatedStack addInboundRule( IpRule rule ) {
        ruleSet.getInboundRules().add( rule );
        return this;
    }


    public CoordinatedStack addOutboundRule( IpRule rule ) {
        ruleSet.getOutboundRules().add( rule );
        return this;
    }


    public void addRunnerInstance( Instance instance ) {
        runnerInstances.add( instance );
    }

    @Override
    public String getDataCenter() {
        return dataCenter;
    }


    public CoordinatedStack setDataCenter( final String dataCenter ) {
        this.dataCenter = dataCenter;
        return this;
    }
}
