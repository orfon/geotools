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
package org.geotools.feature.collection;

import java.io.IOException;
import java.util.Collection;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;

/**
 * A FeatureCollection which completely delegates to another FeatureCollection.
 *
 * <p>This class should be subclasses by classes which must somehow decorate another
 * SimpleFeatureCollection and override the relevant methods.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @since 2.5
 */
public class DecoratingFeatureCollection<T extends FeatureType, F extends Feature>
        implements FeatureCollection<T, F> {

    /** the delegate */
    protected FeatureCollection<T, F> delegate;

    protected DecoratingFeatureCollection(FeatureCollection<T, F> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void accepts(
            org.geotools.api.feature.FeatureVisitor visitor, org.geotools.api.util.ProgressListener progress)
            throws IOException {
        if (canDelegate(visitor)) {
            delegate.accepts(visitor, progress);
        } else {
            DataUtilities.visit(this, visitor, progress);
        }
    }

    /**
     * Methods for subclass to override in order to determine if the supplied visitor can be passed
     * to the delegate collection.
     *
     * <p>The default is false and the visitor receives the decoraeted features.
     */
    protected boolean canDelegate(FeatureVisitor visitor) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public FeatureIterator<F> features() {
        return delegate.features();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        return delegate.getBounds();
    }

    @Override
    public T getSchema() {
        return delegate.getSchema();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public FeatureCollection<T, F> sort(SortBy order) {
        return delegate.sort(order);
    }

    @Override
    public FeatureCollection<T, F> subCollection(Filter filter) {
        return delegate.subCollection(filter);
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <O> O[] toArray(O[] a) {
        return delegate.toArray(a);
    }

    @Override
    public String getID() {
        return delegate.getID();
    }
}
