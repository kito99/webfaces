package org.webfaces.jsf;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

/**
 * Custom ResourceHandler to resolve EL expressions also in htm and html files.
 * This is currently only supported in the specs in css files.
 */
public class ValueExpressionFilterResourceHandler extends ResourceHandlerWrapper {
    private ResourceHandler wrapped;

    public ValueExpressionFilterResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        Resource resource = super.createResource(resourceName, libraryName);

        // only handle resources which does not have a "faces" keyword in it's library name
        // so we automatically exclude primefaces, icefaces, richfaces and other *faces

        // we currently also only filter .htm and .html files
        if (resource != null
                && libraryName != null
                && !libraryName.contains("faces")
                && (resourceName.endsWith(".htm") || resourceName.endsWith(".html"))) {
            return new ValueExpressionFilterResource(resource);
        } else {
            return resource;
        }
    }
}