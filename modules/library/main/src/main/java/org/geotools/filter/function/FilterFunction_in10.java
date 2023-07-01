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
package org.geotools.filter.function;

// this code is autogenerated - you shouldnt be modifying it!

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.api.filter.capability.FunctionName;

public class FilterFunction_in10 extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "in10",
                    Boolean.class,
                    parameter("value", Object.class),
                    parameter("in1", Object.class),
                    parameter("in2", Object.class),
                    parameter("in3", Object.class),
                    parameter("in4", Object.class),
                    parameter("in5", Object.class),
                    parameter("in6", Object.class),
                    parameter("in7", Object.class),
                    parameter("in8", Object.class),
                    parameter("in9", Object.class),
                    parameter("in10", Object.class));

    public FilterFunction_in10() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Object arg0;
        Object arg1;
        Object arg2;
        Object arg3;
        Object arg4;
        Object arg5;
        Object arg6;
        Object arg7;
        Object arg8;
        Object arg9;
        Object arg10;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #0 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg1 = getExpression(1).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #1 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg2 = getExpression(2).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #2 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg3 = getExpression(3).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #3 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg4 = getExpression(4).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #4 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg5 = getExpression(5).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #5 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg6 = getExpression(6).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #6 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg7 = getExpression(7).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #7 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg8 = getExpression(8).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #8 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg9 = getExpression(9).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #9 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg10 = getExpression(10).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function in10 argument #10 - expected type Object");
        }

        return Boolean.valueOf(
                StaticGeometry.in10(
                        arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10));
    }
}
