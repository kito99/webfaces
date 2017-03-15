package org.webfaces.jsf;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceWrapper that parses EL expressions.
 * @author Thomas Andraschko
 */
public class ValueExpressionFilterResource extends ResourceWrapper {

    private Resource resource;

    public ValueExpressionFilterResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Resource getWrapped() {
        return this.resource;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ValueExpressionFilterInputStream(getWrapped().getInputStream());
    }

    @Override
    public String getRequestPath() {
        return getWrapped().getRequestPath();
    }

    @Override
    public String getContentType() {
        return getWrapped().getContentType();
    }

    @Override
    public String getLibraryName() {
        return getWrapped().getLibraryName();
    }

    @Override
    public String getResourceName() {
        return getWrapped().getResourceName();
    }

    @Override
    public void setContentType(String contentType) {
        getWrapped().setContentType(contentType);
    }

    @Override
    public void setLibraryName(String libraryName) {
        getWrapped().setLibraryName(libraryName);
    }

    @Override
    public void setResourceName(String resourceName) {
        getWrapped().setResourceName(resourceName);
    }

    @Override
    public String toString() {
        return getWrapped().toString();
    }


    // copied from myfaces
    private class ValueExpressionFilterInputStream extends InputStream {
        private PushbackInputStream delegate;

        public ValueExpressionFilterInputStream(InputStream in) {
            super();
            delegate = new PushbackInputStream(in, 255);
        }

        @Override
        public int read() throws IOException {
            int c1 = delegate.read();

            if (c1 == -1) return -1;

            if (((char) c1) == '#') {
                int c2 = delegate.read();
                if (c2 == -1) return -1;
                if (((char) c2) == '{') {
                    //It is a value expression. We need
                    //to look for a occurrence of } to
                    //extract the expression and evaluate it,
                    //the result should be unread.
                    List<Integer> expressionList = new ArrayList<Integer>();
                    int c3 = delegate.read();
                    while (c3 != -1 && ((char) c3) != '}') {
                        expressionList.add(c3);
                        c3 = delegate.read();
                    }

                    if (c3 == -1) {
                        //get back the data, because we can't
                        //extract any value expression
                        for (int i = 0; i < expressionList.size(); i++) {
                            delegate.unread(expressionList.get(i));
                        }
                        delegate.unread(c2);
                        return c1;
                    } else {
                        //EL expression found. Evaluate it and pushback
                        //the result into the stream
                        FacesContext context = FacesContext.getCurrentInstance();
                        ELContext elContext = context.getELContext();
                        ValueExpression ve = context.getApplication().
                                getExpressionFactory().createValueExpression(
                                elContext,
                                "#{" + convertToExpression(expressionList) + "}",
                                String.class);

                        String value = (String) ve.getValue(elContext);

                        for (int i = value.length() - 1; i >= 0; i--) {
                            delegate.unread((int) value.charAt(i));
                        }
                        //read again
                        return delegate.read();
                    }
                } else {
                    delegate.unread(c2);
                    return c1;
                }
            } else {
                //just continue
                return c1;
            }
        }

        private String convertToExpression(List<Integer> expressionList) {
            char[] exprArray = new char[expressionList.size()];

            for (int i = 0; i < expressionList.size(); i++) {
                exprArray[i] = (char) expressionList.get(i).intValue();
            }
            return String.valueOf(exprArray);
        }
    }
}