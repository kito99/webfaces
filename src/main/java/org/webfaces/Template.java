package org.webfaces;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created: 14 Sep 2016
 *
 * @author Kito D. Mann
 */
@FacesComponent(tagName = "template", namespace = "uri:webfaces", createTag = true)
public class Template extends UIComponentBase {

    public static String COMPONENT_FAMILY = "jsfwc";
    public static String MARKER_KEY = "jsfwc.template";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        if (hasBeenRendered(this.getClientId())) {
            System.out.println("No marker found; rendering");
            super.encodeAll(context);
        } else {
            System.out.println("Marker found, rendering skipped");
        }
    }


    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("template", this);
        writer.writeAttribute("id", this.getId(), "id");

        //TOOD: Write out other attributes
//        this.getAttributes().keySet().stream().forEach((attribute) -> {
//                    if (attribute.equals("id")) {
//                        writer.writeAttribute(attribute, this.getAttributes().get(attribute), null);
//                    }
//                }
//        ));

        super.encodeBegin(context);

}

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);
        context.getResponseWriter().endElement("template");
        markAsRendered(this.getClientId());
    }

    protected void markAsRendered(String id) {
        Map<String, Object> viewMap = this.getFacesContext().getViewRoot().getViewMap();
        List<String> renderedTemplateIds =
                (List<String>) viewMap.get(MARKER_KEY);
        if (renderedTemplateIds == null) {
            renderedTemplateIds = new ArrayList<String>();
            viewMap.put(MARKER_KEY, renderedTemplateIds);
        }
        if (!renderedTemplateIds.contains(id)) {
            System.out.println("Storing marker id " + this.getClientId());
            renderedTemplateIds.add(id);
        }
    }

    protected boolean hasBeenRendered(String id) {
        Map<String, Object> viewMap = this.getFacesContext().getViewRoot().getViewMap();
        List<String> renderedTemplateIds =
                (List<String>) viewMap.get(MARKER_KEY);
        boolean found = renderedTemplateIds != null && renderedTemplateIds.contains(id);
        System.out.println("Looking for marker id " + this.getClientId() + ", found =" + found);
        return found;
    }
}
