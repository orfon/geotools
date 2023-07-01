/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import org.geotools.filter.ConstantExpression;
import org.geotools.api.filter.expression.Expression;

/**
 * An AnchorPoint identifies the location inside a label or graphic to use as an "anchor" for
 * positioning it relative to a point geometry.
 *
 * @author Ian Turton
 * @version $Id$
 */
public interface AnchorPoint extends org.geotools.api.style.AnchorPoint {
    // TODO: add AnchorPoint to GeoAPI
    /**
     * get the x coordinate of the anchor point
     *
     * @return the expression which represents the X coordinate
     */
    @Override
    Expression getAnchorPointX();

    static final AnchorPoint DEFAULT =
            new AnchorPoint() {
                private void cannotModifyConstant() {
                    throw new UnsupportedOperationException("Constant Stroke may not be modified");
                }

                @Override
                public void setAnchorPointX(Expression x) {
                    cannotModifyConstant();
                }

                @Override
                public void setAnchorPointY(Expression y) {
                    cannotModifyConstant();
                }

                @Override
                public void accept(org.geotools.styling.StyleVisitor visitor) {
                    cannotModifyConstant();
                }

                @Override
                public Object accept(org.geotools.api.style.StyleVisitor visitor, Object data) {
                    cannotModifyConstant();
                    return null;
                }

                @Override
                public Expression getAnchorPointX() {
                    return ConstantExpression.constant(0.5);
                }

                @Override
                public Expression getAnchorPointY() {
                    return ConstantExpression.constant(0.5);
                }
            };

    /**
     * Set the X coordinate for the anchor point
     *
     * @param x an expression which represents the X coordinate
     */
    void setAnchorPointX(Expression x);

    /**
     * set the Y coordinate for the anchor point
     *
     * @param y an expression which represents the Y coordinate
     */
    void setAnchorPointY(Expression y);

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    void accept(org.geotools.styling.StyleVisitor visitor);
}
