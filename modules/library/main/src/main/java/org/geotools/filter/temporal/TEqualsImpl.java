/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.filter.temporal;

import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.temporal.RelativePosition;

public class TEqualsImpl extends BinaryTemporalOperatorImpl implements TEquals {

    public TEqualsImpl(Expression e1, Expression e2) {
        super(e1, e2);
    }

    public TEqualsImpl(Expression e1, Expression e2, MatchAction matchAction) {
        super(e1, e2, matchAction);
    }

    @Override
    protected boolean doEvaluate(RelativePosition pos) {
        return pos == RelativePosition.EQUALS;
    }

    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
