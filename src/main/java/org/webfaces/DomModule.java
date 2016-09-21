package org.webfaces;

import javax.faces.component.FacesComponent;

/**
 * Created: 21 Sep 2016
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
