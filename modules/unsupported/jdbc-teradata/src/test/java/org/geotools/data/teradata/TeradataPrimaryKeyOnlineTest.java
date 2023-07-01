/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.teradata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.AutoGeneratedPrimaryKeyColumn;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JDBCPrimaryKeyOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyTestSetup;
import org.geotools.jdbc.NonIncrementingPrimaryKeyColumn;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

public class TeradataPrimaryKeyOnlineTest extends JDBCPrimaryKeyOnlineTest {

    @Override
    protected JDBCPrimaryKeyTestSetup createTestSetup() {
        return new TeradataPrimaryKeyTestSetup(new TeradataTestSetup());
    }

    @Test
    public void testUniqueGeneratedPrimaryKey() throws Exception {
        JDBCFeatureStore fs =
                (JDBCFeatureStore) dataStore.getFeatureSource(tname("uniquetablenotgenerated"));

        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(
                fs.getPrimaryKey().getColumns().get(0) instanceof NonIncrementingPrimaryKeyColumn);

        ContentFeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);

        SimpleFeatureType featureType = fs.getSchema();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);
        b.add("four");
        b.add(new GeometryFactory().createPoint(new Coordinate(4, 4)));

        SimpleFeature f = b.buildFeature(null);
        fs.addFeatures(DataUtilities.collection(f));

        // pattern match to handle the multi primary key case
        assertTrue(
                ((String) f.getUserData().get("fid"))
                        .matches(tname(featureType.getTypeName()) + ".4(\\..*)?"));

        assertPrimaryKeyValues(features, 4);
    }

    @Test
    public void testUniqueNonGeneratedPrimaryKey() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("uniquetable"));

        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof AutoGeneratedPrimaryKeyColumn);

        ContentFeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
        addFeature(fs.getSchema(), fs);
        assertPrimaryKeyValues(features, 4);
    }

    @Override
    public void testSequencedPrimaryKey() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("seq"));

        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof AutoGeneratedPrimaryKeyColumn);

        ContentFeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
        addFeature(fs.getSchema(), fs);
        assertPrimaryKeyValues(features, 4);
    }
}
