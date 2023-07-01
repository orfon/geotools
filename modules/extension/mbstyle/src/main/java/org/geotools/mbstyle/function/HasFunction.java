/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.mbstyle.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.json.simple.JSONObject;
import org.geotools.api.filter.capability.FunctionName;

/** Evaluate to TRUE if a JSONObject contains a given key value or FALSE if it does not. */
public class HasFunction extends FunctionExpressionImpl {
    public static final FunctionName NAME =
            new FunctionNameImpl(
                    "has",
                    parameter("value", String.class),
                    parameter("object", JSONObject.class),
                    parameter("fallback", Object.class));

    public HasFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        String arg0;
        JSONObject arg1;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature, String.class);

        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"has\" argument #1 - expected type String");
        }
        try { // attempt to get value and perform conversion
            arg1 = getExpression(1).evaluate(feature, JSONObject.class);

        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"has\" argument #0 - expected type JSONObject");
        }

        return arg1.containsKey(arg0);
    }
}
