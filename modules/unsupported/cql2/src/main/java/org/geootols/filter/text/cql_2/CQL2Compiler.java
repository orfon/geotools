/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geootols.filter.text.cql_2;

import java.io.StringReader;
import java.util.List;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.text.commons.IToken;
import org.geotools.filter.text.commons.Result;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql_2.parsers.CQL2Parser;
import org.geotools.filter.text.cql_2.parsers.Node;
import org.geotools.filter.text.cql_2.parsers.ParseException;
import org.geotools.filter.text.cql_2.parsers.TokenMgrError;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.During;

/**
 * CQL2 compiler
 *
 * <p>Builds the filter, expression or arguments related with the visited node of syntax tree
 *
 * @author Jody Garnett
 * @author Mauricio Pazos (Axios Engineering)
 */
public class CQL2Compiler extends CQL2Parser implements org.geotools.filter.text.commons.ICompiler {

    private static final String ATTRIBUTE_PATH_SEPARATOR = "/";

    /** cql expression to compile */
    private final String source;

    private final CQL2FilterBuilder builder;

    /** new instance of TXTCompiler */
    public CQL2Compiler(final String txtSource, final FilterFactory filterFactory) {
        super(new StringReader(txtSource));

        assert filterFactory != null : "filterFactory cannot be null";

        this.source = txtSource;
        this.builder = new CQL2FilterBuilder(txtSource, filterFactory);
    }

    /**
     * compile source to produce a Filter. The filter result must be retrieved with {@link
     * #getFilter()}.
     */
    @Override
    public void compileFilter() throws CQLException {
        try {
            super.FilterCompilationUnit();
        } catch (TokenMgrError tokenError) {
            throw new CQLException(tokenError.getMessage(), getTokenInPosition(0), this.source);
        } catch (ParseException e) {
            throw new CQLException(
                    e.getMessage(), getTokenInPosition(0), e.getCause(), this.source);
        }
    }

    /** compiles source to produce a Expression */
    @Override
    public void compileExpression() throws CQLException {
        try {
            super.ExpressionCompilationUnit();
        } catch (TokenMgrError tokenError) {
            throw new CQLException(tokenError.getMessage(), getTokenInPosition(0), this.source);
        } catch (ParseException e) {
            throw new CQLException(
                    e.getMessage(), getTokenInPosition(0), e.getCause(), this.source);
        }
    }

    /** Compiles a list of filters */
    @Override
    public void compileFilterList() throws CQLException {
        try {
            super.FilterListCompilationUnit();
        } catch (TokenMgrError tokenError) {
            throw new CQLException(tokenError.getMessage(), getTokenInPosition(0), this.source);
        } catch (ParseException e) {
            throw new CQLException(
                    e.getMessage(), getTokenInPosition(0), e.getCause(), this.source);
        }
    }

    /** @return the CQL2 source */
    @Override
    public final String getSource() {
        return this.source;
    }

    /**
     * Return the filter resultant of compiling process
     *
     * @return Filter
     */
    @Override
    public final Filter getFilter() throws CQLException {
        return this.builder.getFilter();
    }
    /**
     * Return the expression resultant of compiling process
     *
     * @return Expression
     */
    @Override
    public final Expression getExpression() throws CQLException {

        return this.builder.getExpression();
    }

    @Override
    public IToken getTokenInPosition(int index) {
        return TokenAdapter.newAdapterFor(super.getToken(index));
    }

    /**
     * Returns the list of Filters built as the result of calling {@link
     * #FilterListCompilationUnit()()}
     *
     * @return List<Filter>
     * @throws CQLException if a ClassCastException occurs while casting a built item to a Filter.
     */
    @Override
    public List<Filter> getFilterList() throws CQLException {
        return this.builder.getFilterList();
    }

    @Override
    public final void jjtreeOpenNodeScope(Node n) {}

    /** called by parser when the node is closed. */
    @Override
    public final void jjtreeCloseNodeScope(Node n) throws ParseException {

        try {
            Object built = build(n);

            IToken tokenAdapter = TokenAdapter.newAdapterFor(this.token);
            Result r = new Result(built, tokenAdapter, n.getType());
            this.builder.pushResult(r);
        } catch (CQLException e) {
            ParseException pe = new ParseException(e.getMessage());
            pe.initCause(e);
            throw pe;
        } finally {
            n.dispose();
        }
    }
    /**
     * This method is called when the parser close a node. Here is built the filters an expressions
     * recognized in the parsing process.
     *
     * @param n a Node instance
     * @return Filter or Expression
     */
    private Object build(Node n) throws CQLException {

        switch (n.getType()) {

                // ----------------------------------------
                // (+|-) Integer and Float
                // ----------------------------------------
            case JJTINTEGERNODE:
                return this.builder.buildLiteralInteger(getTokenInPosition(0).toString());
            case JJTFLOATINGNODE:
                return this.builder.buildLiteralDouble(getTokenInPosition(0).toString());
            case JJTNEGATIVENUMBER_NODE:
                return this.builder.bulidNegativeNumber();

                // ----------------------------------------
                // String
                // ----------------------------------------
            case JJTSTRINGNODE:
                return this.builder.buildLiteralString(getTokenInPosition(0).toString());

                // ----------------------------------------
                // Identifier
                // ----------------------------------------
            case JJTIDENTIFIER_NODE:
                return this.builder.buildIdentifier(JJTIDENTIFIER_PART_NODE);

            case JJTIDENTIFIER_PART_NODE:
                return this.builder.buildIdentifierPart(getTokenInPosition(0));

                // ----------------------------------------
                // attribute
                // ----------------------------------------
            case JJTSIMPLE_ATTRIBUTE_NODE:
                return this.builder.buildSimpleAttribute();

            case JJTCOMPOUND_ATTRIBUTE_NODE:
                return this.builder.buildCompoundAttribute(
                        JJTSIMPLE_ATTRIBUTE_NODE, ATTRIBUTE_PATH_SEPARATOR);

                // ----------------------------------------
                // function
                // ----------------------------------------
            case JJTFUNCTION_NODE:
                return this.builder.buildFunction(JJTFUNCTIONNAME_NODE);

            case JJTFUNCTIONNAME_NODE:
                return n; // used as mark of function name in stack

            case JJTFUNCTIONARG_NODE:
                return n; // used as mark of args in stack

                // Math Nodes
            case JJTADDNODE:
            case JJTSUBTRACTNODE:
            case JJTMULNODE:
            case JJTDIVNODE:
                return buildBinaryExpression(n.getType());

                // Boolean expression
            case JJTBOOLEAN_AND_NODE:
                return buildLogicFilter(JJTBOOLEAN_AND_NODE);

            case JJTBOOLEAN_OR_NODE:
                return buildLogicFilter(JJTBOOLEAN_OR_NODE);

            case JJTBOOLEAN_NOT_NODE:
                return buildLogicFilter(JJTBOOLEAN_NOT_NODE);

                // ----------------------------------------
                // between predicate actions
                // ----------------------------------------
            case JJTBETWEEN_NODE:
                return this.builder.buildBetween();

            case JJTNOT_BETWEEN_NODE:
                return this.builder.buildNotBetween();

                // ----------------------------------------
                // Compare predicate actions
                // ----------------------------------------
            case JJTCOMPARISONPREDICATE_EQ_NODE:
            case JJTCOMPARISONPREDICATE_GT_NODE:
            case JJTCOMPARISONPREDICATE_LT_NODE:
            case JJTCOMPARISONPREDICATE_GTE_NODE:
            case JJTCOMPARISONPREDICATE_LTE_NODE:
                return buildBinaryComparasionOperator(n.getType());

            case JJTCOMPARISONPREDICATE_NOT_EQUAL_NODE:
                Filter eq = buildBinaryComparasionOperator(JJTCOMPARISONPREDICATE_EQ_NODE);
                Not notFilter = this.builder.buildNotFilter(eq);

                return notFilter;

                // ----------------------------------------
                // Text predicate (Like)
                // ----------------------------------------
            case JJTLIKE_NODE:
                return this.builder.buildLikeFilter(true);

            case JJTNOT_LIKE_NODE:
                return this.builder.buildNotLikeFilter(true);

                // ----------------------------------------
                // Null predicate
                // ----------------------------------------
            case JJTNULLPREDICATENODE:
                return this.builder.buildPropertyIsNull();

            case JJTNOTNULLPREDICATENODE:
                return this.builder.buildPorpertyNotIsNull();

                // ----------------------------------------
                // temporal predicate actions
                // ----------------------------------------
            case JJTDATE_NODE:
                return this.builder.buildDateExpression(getTokenInPosition(0));

            case JJTDATETIME_NODE:
                return this.builder.buildDateTimeExpression(getTokenInPosition(0));

            case JJTINTERVAL:
                // TODO: this likely needs to be a function instead, see:
                //
                return builder.buildPeriodBetweenDates();

            case JJTTPTEQUALS_DATETIME_NODE:
                return this.builder.buildTEquals();

            case JJTTPBEFORE_DATETIME_NODE:
                return buildBefore();

            case JJTTPAFTER_DATETIME_NODE:
                return buildAfterPredicate();

            case JJTTPDURING_PERIOD_NODE:
                return buildDuring();

                // ----------------------------------------
                // routine invocation Geo Operation
                // ----------------------------------------
            case JJTROUTINEINVOCATION_GEOOP_EQUAL_NODE:
            case JJTROUTINEINVOCATION_GEOOP_DISJOINT_NODE:
            case JJTROUTINEINVOCATION_GEOOP_INTERSECT_NODE:
            case JJTROUTINEINVOCATION_GEOOP_TOUCH_NODE:
            case JJTROUTINEINVOCATION_GEOOP_CROSS_NODE:
            case JJTROUTINEINVOCATION_GEOOP_WITHIN_NODE:
            case JJTROUTINEINVOCATION_GEOOP_CONTAIN_NODE:
            case JJTROUTINEINVOCATION_GEOOP_OVERLAP_NODE:
                return buildBinarySpatialOperator(n.getType());

                // ----------------------------------------
                // Geometries:
                // ----------------------------------------
            case JJTPOINT_NODE:
                return this.builder.buildCoordinate();

            case JJTPOINTTEXT_NODE:
                return this.builder.buildPointText();

            case JJTLINESTRINGTEXT_NODE:
                return this.builder.buildLineString(JJTPOINT_NODE);

            case JJTPOLYGONTEXT_NODE:
                return this.builder.buildPolygon(JJTLINESTRINGTEXT_NODE);

            case JJTMULTIPOINTTEXT_NODE:
                return this.builder.buildMultiPoint(JJTPOINTTEXT_NODE);

            case JJTMULTILINESTRINGTEXT_NODE:
                return this.builder.buildMultiLineString(JJTLINESTRINGTEXT_NODE);

            case JJTMULTIPOLYGONTEXT_NODE:
                return this.builder.buildMultiPolygon(JJTPOLYGONTEXT_NODE);

            case JJTGEOMETRYLITERAL:
                return this.builder.buildGeometryLiteral();

            case JJTGEOMETRYCOLLECTIONTEXT_NODE:
                return this.builder.buildGeometryCollection(JJTGEOMETRYLITERAL);

            case JJTWKTNODE:
                return this.builder.buildGeometry();

            case JJTENVELOPETAGGEDTEXT_NODE:
                return this.builder.buildEnvelope(TokenAdapter.newAdapterFor(n.getToken()));

            case JJTTRUENODE:
                return this.builder.buildTrueLiteral();

            case JJTFALSENODE:
                return this.builder.buildFalseLiteral();

                // ----------------------------------------
                //  IN Predicate
                // ----------------------------------------
            case JJTIN_PREDICATE_NODE:
                return this.builder.buildInPredicate(JJTEXPRESSION_IN_LIST_NODE);

            case JJTNOT_IN_PREDICATE_NODE:
                Or orFilter = this.builder.buildInPredicate(JJTEXPRESSION_IN_LIST_NODE);
                Not notOrFilter = this.builder.buildNotFilter(orFilter);

                return notOrFilter;
        }

        return null;
    }

    private org.geotools.api.filter.expression.BinaryExpression buildBinaryExpression(int nodeType)
            throws CQLException {

        org.geotools.api.filter.expression.BinaryExpression expr = null;

        switch (nodeType) {
            case JJTADDNODE:
                expr = this.builder.buildAddExpression();

                break;

            case JJTSUBTRACTNODE:
                expr = this.builder.buildSubtractExression();

                break;

            case JJTMULNODE:
                expr = this.builder.buildMultiplyExpression();
                break;

            case JJTDIVNODE:
                expr = this.builder.buildDivideExpression();
                break;

            default:
                break;
        }

        return expr;
    }

    private Filter buildLogicFilter(int nodeType) throws CQLException {
        try {
            Filter logicFilter;

            switch (nodeType) {
                case JJTBOOLEAN_AND_NODE:
                    logicFilter = this.builder.buildAndFilter();
                    break;

                case JJTBOOLEAN_OR_NODE:
                    logicFilter = this.builder.buildOrFilter();
                    break;

                case JJTBOOLEAN_NOT_NODE:
                    logicFilter = this.builder.buildNotFilter();
                    break;

                default:
                    throw new CQLException(
                            "Expression not supported. And, Or, Not is required",
                            getTokenInPosition(0),
                            this.source);
            }

            return logicFilter;
        } catch (IllegalFilterException ife) {
            throw new CQLException(
                    "Exception building LogicFilter", getTokenInPosition(0), ife, this.source);
        }
    }

    /**
     * Creates Binary Spatial Operator
     *
     * @return BinarySpatialOperator
     */
    private BinarySpatialOperator buildBinarySpatialOperator(final int nodeType)
            throws CQLException {

        BinarySpatialOperator filter = null;

        switch (nodeType) {
            case JJTROUTINEINVOCATION_GEOOP_EQUAL_NODE:
                filter = this.builder.buildSpatialEqualFilter();
                break;
            case JJTROUTINEINVOCATION_GEOOP_DISJOINT_NODE:
                filter = this.builder.buildSpatialDisjointFilter();
                break;
            case JJTROUTINEINVOCATION_GEOOP_INTERSECT_NODE:
                filter = this.builder.buildSpatialIntersectsFilter();
                break;
            case JJTROUTINEINVOCATION_GEOOP_TOUCH_NODE:
                filter = this.builder.buildSpatialTouchesFilter();
                break;
            case JJTROUTINEINVOCATION_GEOOP_CROSS_NODE:
                filter = this.builder.buildSpatialCrossesFilter();
                break;
            case JJTROUTINEINVOCATION_GEOOP_WITHIN_NODE:
                filter = this.builder.buildSpatialWithinFilter();
                break;
            case JJTROUTINEINVOCATION_GEOOP_CONTAIN_NODE:
                filter = this.builder.buildSpatialContainsFilter();
                break;
            case JJTROUTINEINVOCATION_GEOOP_OVERLAP_NODE:
                filter = this.builder.buildSpatialOverlapsFilter();
                break;
            default:
                throw new CQLException("Binary spatial operator unexpected");
        }

        return filter;
    }

    /**
     * Build the convenient filter for before date and before period filters
     *
     * @return Filter
     */
    private Before buildBefore() throws CQLException {
        return builder.buildBeforeDate();
    }

    /**
     * Build the convenient filter for during period filters
     *
     * @return Filter
     */
    private During buildDuring() throws CQLException {
        return this.builder.buildDuringPeriod();
    }

    /**
     * build filter for after date and after period
     *
     * @return a filter
     */
    private After buildAfterPredicate() throws CQLException {
        return this.builder.buildAfterDate();
    }

    /**
     * Builds a compare filter
     *
     * @return BinaryComparisonOperator
     */
    private BinaryComparisonOperator buildBinaryComparasionOperator(int filterType)
            throws CQLException {

        switch (filterType) {
            case JJTCOMPARISONPREDICATE_EQ_NODE:
                return this.builder.buildEquals();

            case JJTCOMPARISONPREDICATE_GT_NODE:
                return this.builder.buildGreater();

            case JJTCOMPARISONPREDICATE_LT_NODE:
                return this.builder.buildLess();

            case JJTCOMPARISONPREDICATE_GTE_NODE:
                return this.builder.buildGreaterOrEqual();

            case JJTCOMPARISONPREDICATE_LTE_NODE:
                return this.builder.buildLessOrEqual();

            default:
                throw new CQLException("unexpeted filter type.");
        }
    }
}
