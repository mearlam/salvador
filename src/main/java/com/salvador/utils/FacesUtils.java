package com.salvador.utils;

import com.sun.deploy.net.HttpRequest;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 12:04
 */
public abstract class FacesUtils {

    public static String getParam(String param) {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String value = "";
        if (externalContext.getRequestParameterMap().containsKey(param)) {
            value = externalContext.getRequestParameterMap().get(param);
        }

        return value;
    }

    public static String getHttpHeader(String header) {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String value = "";
        if (externalContext.getRequestHeaderMap().containsKey(header)) {
            value = externalContext.getRequestHeaderMap().get(header);
        }

        return value;
    }

    public static String getDestinationPage() {
        String destinationPage = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
                get("javax.servlet.forward.request_uri");
        if(destinationPage != null) {
            destinationPage.replace("+","-");
        }
        return destinationPage;
    }

    public static void redirect(String page) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            final ExternalContext externalContext = facesContext.getExternalContext();
            if (externalContext != null) {

                try {
                    externalContext.redirect(externalContext.encodeResourceURL(page));
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void refreshPage() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            final ExternalContext externalContext = facesContext.getExternalContext();
            if (externalContext != null) {
                final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
                if (request != null) {
                    try {
                        externalContext.redirect(externalContext.encodeResourceURL(request.getRequestURI()));
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }
}
