/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.h2gis.geotools;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import org.geotools.jdbc.ColumnMetadata;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.io.WKBWriter;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;

/**
 * jdbc-h2gis is an extension to connect H2GIS a spatial library that brings spatial support to the
 * H2 Java database.
 *
 * <p>H2GIS dialect based on prepared statements.
 *
 * @author Erwan Bocher
 */
public class H2GISPSDialect extends PreparedStatementSQLDialect {

    private final H2GISDialect delegate;

    public H2GISPSDialect(JDBCDataStore store, H2GISDialect delegate) {
        super(store);
        this.delegate = delegate;
    }

    @Override
    public boolean isAggregatedSortSupported(String function) {
        return "distinct".equalsIgnoreCase(function);
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx) {
        return delegate.includeTable(schemaName, tableName, cx);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException {
        return delegate.decodeGeometryEnvelope(rs, column, cx);
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            int column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        return delegate.decodeGeometryValue(descriptor, rs, column, factory, cx, hints);
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        return delegate.decodeGeometryValue(descriptor, rs, column, factory, cx, hints);
    }

    @Override
    public void encodeGeometryColumn(
            GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        delegate.encodeGeometryColumn(gatt, prefix, srid, hints, sql);
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        delegate.encodeGeometryEnvelope(tableName, geometryColumn, sql);
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        delegate.encodePrimaryKey(column, sql);
    }

    @Override
    public Integer getGeometrySRID(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getGeometrySRID(schemaName, tableName, columnName, cx);
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return delegate.getGeometryTypeName(type);
    }

    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx) throws SQLException {
        return delegate.getMapping(columnMetaData, cx);
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return delegate.lookupGeneratedValuesPostInsert();
    }

    @Override
    public Object getNextAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getNextAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    @Override
    public Object getLastAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getLastAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx)
            throws SQLException {
        return delegate.getNextSequenceValue(schemaName, sequenceName, cx);
    }

    @Override
    public String encodeNextSequenceValue(String schemaName, String sequenceName) {
        return delegate.encodeNextSequenceValue(schemaName, sequenceName);
    }

    @Override
    public String getSequenceForColumn(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getSequenceForColumn(schemaName, tableName, columnName, cx);
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        delegate.postCreateTable(schemaName, featureType, cx);
    }

    @Override
    public void preDropTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        delegate.preDropTable(schemaName, featureType, cx);
    }

    @Override
    public void postDropTable(String schemaName, SimpleFeatureType featureType, Connection cx) {
        delegate.postDropTable(schemaName, featureType, cx);
    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        delegate.registerClassToSqlMappings(mappings);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        delegate.registerSqlTypeNameToClassMappings(mappings);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        delegate.registerSqlTypeToSqlTypeNameOverrides(overrides);
    }

    @Override
    public void handleUserDefinedType(
            ResultSet columnMetaData, ColumnMetadata metadata, Connection cx) throws SQLException {
        delegate.handleUserDefinedType(columnMetaData, metadata, cx);
    }

    @Override
    public void prepareGeometryValue(
            Class<? extends Geometry> gClass,
            int dimension,
            int srid,
            Class binding,
            StringBuffer sql) {
        if (gClass != null) {
            sql.append("ST_GeomFromWKB(?, ").append(srid).append(")");
        } else {
            sql.append("?");
        }
    }

    @Override
    public void setGeometryValue(
            Geometry g, int dimension, int srid, Class binding, PreparedStatement ps, int column)
            throws SQLException {
        if (g != null && !g.isEmpty()) {
            if (g instanceof LinearRing) {
                // h2gis does not handle linear rings, convert to just a line string
                g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
            }

            byte[] bytes = new WKBWriter(dimension).write(g);
            ps.setBytes(column, bytes);
        } else {
            ps.setNull(column, Types.OTHER, "Geometry");
        }
    }

    @Override
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        H2GISPSFilterToSql fts = new H2GISPSFilterToSql(this);
        fts.setFunctionEncodingEnabled(delegate.isFunctionEncodingEnabled());
        return fts;
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return delegate.isLimitOffsetSupported();
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        delegate.applyLimitOffset(sql, limit, offset);
    }

    @Override
    public int getGeometryDimension(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getGeometryDimension(schemaName, tableName, columnName, cx);
    }

    @Override
    protected String getArrayComponentTypeName(AttributeDescriptor att) throws SQLException {
        if (att == null) {
            return null;
        }
        String name = (String) att.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME);
        if (name != null && name.startsWith("_")) {
            return name.substring(1);
        }
        return super.getArrayComponentTypeName(att);
    }

    @Override
    public void encodeGeometryColumnSimplified(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        delegate.encodeGeometryColumnSimplified(gatt, prefix, srid, sql, distance);
    }

    @Override
    public void encodeGeometryColumnGeneralized(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        delegate.encodeGeometryColumnGeneralized(gatt, prefix, srid, sql, distance);
    }

    H2GISDialect getDelegate() {
        return delegate;
    }

    @Override
    public void initializeConnection(Connection cx) throws SQLException {
        delegate.initializeConnection(cx);
    }

    @Override
    public boolean canSimplifyPoints() {
        return delegate.canSimplifyPoints();
    }
}
