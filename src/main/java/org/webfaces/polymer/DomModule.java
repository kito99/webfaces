package org.webfaces.polymer;

import org.webfaces.Template;

import javax.faces.component.FacesComponent;

/**
 * JSF representation of the Polymer dom-module tag.
 *
 * @author Kito D. Mann
 */

@FacesComponent(tagName = "dom-module", namespace = "uri:webfaces", createTag = true)
public class DomModule  extends Template {

    @Override
    protected String getElementName() {
        return "dom-module";
    }
}
