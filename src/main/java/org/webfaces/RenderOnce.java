package org.webfaces;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents a component that renders only one time, even if it's included several times in a page.
 *
 * @author Kito D. Mann
 */
@FacesComponent(tagName = "renderOnce", namespace = "uri:webfaces", createTag = true)
public class RenderOnce extends UIComponentBase {

    private static Logger logger = Logger.getLogger(RenderOnce.class.toString());

    public static String COMPONENT_FAMILY = "org.webfaces";

    public String getGroup() {
        return (String)super.getStateHelper().get("group");
    }

    public void setGroup(String group) {
        super.getStateHelper().put("group", group);
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
    public void encodeAll(FacesContext context) throws IOException {
        if (!hasBeenRendered()) {
            logger.info("No marker found; rendering");
            super.encodeAll(context);
        } else {
            logger.info("Marker found, rendering skipped");
        }
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!hasBeenRendered()) {
            super.encodeBegin(context);
        } else {
            logger.info("Marker found, rendering skipped");
        }
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        if (!hasBeenRendered()) {
            logger.info("No marker found; rendering");
            super.encodeChildren(context);
        } else {
            logger.info("Marker found, rendering skipped");
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        if (!hasBeenRendered()) {
            logger.info("No marker found; rendering");
            super.encodeEnd(context);
            markAsRendered();
        } else {
            logger.info("Marker found, rendering skipped");
        }
    }

    protected void markAsRendered() {
        String id = this.getIdToRenderOnce();
        Map<String, Object> viewMap = this.getFacesContext().getViewRoot().getViewMap();
        List<String> renderedIds = getRenderedIds(viewMap);
        if (renderedIds == null) {
            renderedIds = new ArrayList<String>();
            viewMap.put(getMarkerKey(), renderedIds);
        }
        if (!renderedIds.contains(id)) {
            logger.info("Storing marker id " + id);
            renderedIds.add(id);
        }
    }

    protected boolean hasBeenRendered() {
        String id = this.getIdToRenderOnce();
        Map<String, Object> viewMap = this.getFacesContext().getViewRoot().getViewMap();
        List<String> renderedIds = getRenderedIds(viewMap);
        boolean found = renderedIds != null && renderedIds.contains(id);
        logger.info("Looking for marker id " + id + ", found=" + found);
        return found;
    }

    protected List<String> getRenderedIds(Map<String, Object> viewMap) {
        return (List<String>) viewMap.get(getMarkerKey());
    }

    protected String getMarkerKey() {
        return "webfaces.renderOnce." + this.getGroup();
    }

    protected String getIdToRenderOnce() {
        if (this.getChildCount() > 0) {
            return this.getChildren().get(0).getId();
        } else {
            logger.warning("No child component found.");
        }
        return null;
    }

}
