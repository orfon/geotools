/**
 *
 * $Id$
 */
package net.opengis.wfs.validation;

import java.net.URI;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.geotools.api.filter.Filter;

/**
 * A sample validator interface for {@link net.opengis.wfs.UpdateElementType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface UpdateElementTypeValidator {
  boolean validate();

  boolean validateProperty(EList value);
  boolean validateFilter(Filter value);
  boolean validateHandle(String value);
  boolean validateInputFormat(String value);
  boolean validateSrsName(URI value);
  boolean validateTypeName(QName value);
}
