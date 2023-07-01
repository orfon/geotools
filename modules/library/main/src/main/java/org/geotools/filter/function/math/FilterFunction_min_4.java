/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function.math;

// this code is autogenerated - you shouldnt be modifying it!

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.geotools.api.filter.capability.FunctionName;

public class FilterFunction_min_4 extends FunctionExpressionImpl {

    // public static FunctionName NAME = new FunctionNameImpl("min_4","int","int");
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "min_4",
                    parameter("minium", Integer.class),
                    parameter("int", Number.class),
                    parameter("int", Number.class));

    public FilterFunction_min_4() {
        super("min_4");
        functionName = NAME;
    }

    @Override
    public Object evaluate(Object feature) {

        Object arg0 = getExpression(0).evaluate(feature);
        Object arg1 = getExpression(1).evaluate(feature);

        if (arg0 == null || arg1 == null) {
            return null;
        }

        arg0 = Converters.convert(arg0, Integer.class, new Hints());
        arg1 = Converters.convert(arg1, Integer.class, new Hints());

        if (arg0 == null) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function min argument #0 - expected type int");
        }
        if (arg1 == null) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function min argument #1 - expected type int");
        }

        return (Math.min((Integer) arg0, (Integer) arg1));
    }
}
