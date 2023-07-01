/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

// this was autogenerated and then hand modified to implement better support for geometry
// transformations in SLD

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Geometry;
import org.geotools.api.filter.capability.FunctionName;

public class FilterFunction_offset extends FunctionExpressionImpl
        implements GeometryTransformation {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "offset",
                    Geometry.class,
                    parameter("geometry", Geometry.class),
                    parameter("offsetX", Double.class),
                    parameter("offsetY", Double.class));

    public FilterFunction_offset() {
        super(NAME);
    }

    public int getArgCount() {
        return 3;
    }

    @Override
    public Object evaluate(Object feature) {
        Geometry geom = getExpression(0).evaluate(feature, Geometry.class);
        Double offsetX = getExpression(1).evaluate(feature, Double.class);
        if (offsetX == null) offsetX = 0d;
        Double offsetY = getExpression(2).evaluate(feature, Double.class);
        if (offsetY == null) offsetY = 0d;

        if (geom != null) {
            Geometry offseted = geom.copy();
            offseted.apply(new OffsetOrdinateFilter(offsetX, offsetY));
            return offseted;
        } else {
            return null;
        }
    }

    /**
     * Returns an translated rendering envelope if the offsets are not using feature attributes. If
     * the offsets are feature dependent the user will have to expand the rendering area via the
     * renderer buffer parameter
     */
    @Override
    public ReferencedEnvelope invert(ReferencedEnvelope renderingEnvelope) {
        Double offsetX = getExpression(1).evaluate(null, Double.class);
        Double offsetY = getExpression(2).evaluate(null, Double.class);

        if (offsetX != null && offsetY != null) {
            ReferencedEnvelope offseted = new ReferencedEnvelope(renderingEnvelope);
            offseted.translate(-offsetX, -offsetY);
            return offseted;
        } else {
            return null;
        }
    }

    /** Applies an offset to the X and Y coordinates */
    public static class OffsetOrdinateFilter implements CoordinateSequenceFilter {
        double offsetX;
        double offsetY;

        public OffsetOrdinateFilter(double offsetX, double offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        @Override
        public void filter(CoordinateSequence seq, int i) {
            seq.setOrdinate(i, 0, seq.getOrdinate(i, 0) + offsetX);
            seq.setOrdinate(i, 1, seq.getOrdinate(i, 1) + offsetY);
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public boolean isGeometryChanged() {
            return true;
        }
    }
}
