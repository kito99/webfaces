package org.webfaces;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created: 14 Sep 2016
 *
 * @author Kito D. Mann
 */
@FacesComponent(tagName = "template", namespace = "uri:webfaces", createTag = true)
public class Template extends RenderOnce {

    private static Logger logger = Logger.getLogger(Template.class.toString());

    public static String COMPONENT_FAMILY = "org.webfaces";

    public Template() {
        this.setGroup("template");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!hasBeenRendered()) {
            logger.info("No marker found; rendering");
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement("template", this);
            writer.writeAttribute("id", this.getId(), "id");
            for (String attribute : this.getAttributes().keySet()) {
                if (!attribute.equals("id") && !attribute.startsWith("com.")) {
                    writer.writeAttribute(attribute, this.getAttributes().get(attribute), null);
                }
            }
            super.encodeBegin(context);

        } else {
            logger.info("Marker found, rendering skipped");
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        if (!hasBeenRendered()) {
            logger.info("No marker found; rendering");
            super.encodeEnd(context);
            context.getResponseWriter().endElement("template");
            markAsRendered();
        } else {
            logger.info("Marker found, rendering skipped");
        }
    }

    protected String getMarkerKey() {
        return "webfaces.template." + this.getGroup();
    }

    protected String getIdToRenderOnce() {
        return this.getId();
    }

}
