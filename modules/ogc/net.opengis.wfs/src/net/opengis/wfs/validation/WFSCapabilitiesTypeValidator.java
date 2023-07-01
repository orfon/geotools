/**
 *
 * $Id$
 */
package net.opengis.wfs.validation;

import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.GMLObjectTypeListType;

import org.geotools.api.filter.capability.FilterCapabilities;

/**
 * A sample validator interface for {@link net.opengis.wfs.WFSCapabilitiesType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface WFSCapabilitiesTypeValidator {
  boolean validate();

  boolean validateFeatureTypeList(FeatureTypeListType value);
  boolean validateServesGMLObjectTypeList(GMLObjectTypeListType value);
  boolean validateSupportsGMLObjectTypeList(GMLObjectTypeListType value);
  boolean validateFilterCapabilities(FilterCapabilities value);
}
