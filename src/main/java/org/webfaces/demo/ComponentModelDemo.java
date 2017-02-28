package org.webfaces.demo;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;

/**
 * @author Kito D. Mann
 */

@FacesComponent(value = "org.webfaces.ComponentModelDemo")
public class ComponentModelDemo extends UINamingContainer {

    public void preRender(ComponentSystemEvent event) {
        System.out.println("Inside preRender event");
    }

    public void handleActionEvent(ActionEvent e) {
        this.setMessage("Action event fired by " + e.getComponent().getClientId());
    }

    public String getMessage() {
        return (String)super.getStateHelper().get("message");
    }

    public void setMessage(String message) {
        super.getStateHelper().put("message", message);
    }

    public String getWidgetVar() {
        return (String)super.getStateHelper().get("widgetVar");
    }

    public void setWidgetVar(String widgetVar) {
        super.getStateHelper().put("widgetVar", widgetVar);
    }
}
