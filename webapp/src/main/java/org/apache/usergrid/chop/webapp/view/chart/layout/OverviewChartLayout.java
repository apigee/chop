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
package org.apache.usergrid.chop.webapp.view.chart.layout;

import org.apache.usergrid.chop.webapp.service.chart.builder.ChartBuilder;
import org.apache.usergrid.chop.webapp.view.chart.format.PointRadius;
import org.apache.usergrid.chop.webapp.view.main.Breadcrumb;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class OverviewChartLayout extends ChartLayout {

    public OverviewChartLayout(ChartLayoutContext layoutContext, ChartBuilder chartBuilder, ChartLayout nextLayout, Breadcrumb breadcrumb) {
        super( new Config(
                layoutContext,
                chartBuilder,
                nextLayout,
                "overviewChart",
                "overviewChartCallback",
                "js/overview-chart.js",
                new PointRadius(),
                breadcrumb
        ) );

        addNextChartButton();
    }

    @Override
    protected void pointClicked(JSONObject json) throws JSONException {
        super.pointClicked(json);

        String caption = "Commit: " + StringUtils.abbreviate(json.getString("commitId"), 10);
        nextChartButton.setCaption(caption);
    }

    @Override
    protected void handleBreadcrumb() {
        config.getBreadcrumb().setItem(this, "Overview", 0);
    }
}
