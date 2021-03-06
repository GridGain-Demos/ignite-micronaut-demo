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

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;

@Controller("/cities")
public class CityController {
    @Inject
    IgniteClientConnection client;

    @Get
    @Produces(MediaType.TEXT_JSON)
    public List<City> listCities(int population) {
        if (population <= 0)
            return null;

        FieldsQueryCursor<List<?>> cursor = client.getClient().query(
            new SqlFieldsQuery("SELECT name, countryCode, population FROM City WHERE population >= ? ORDER BY population DESC")
                .setSchema("PUBLIC")
                .setArgs(population));

        List<City> response = new ArrayList<>(cursor.getColumnsCount());

        for (List<?> row: cursor.getAll()) {
            City city = new City();

            city.setName((String)row.get(0));
            city.setCountryCode((String)row.get(1));
            city.setPopulation((Integer)row.get(2));

            response.add(city);
        }

        return response;
    }
}
