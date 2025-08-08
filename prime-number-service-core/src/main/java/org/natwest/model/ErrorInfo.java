package org.natwest.model;

import javax.xml.bind.annotation.XmlRootElement;

/*
Error class, in case of any error, we will be wrapping the error message as per the below definition and then send it back to the client.
 */
@XmlRootElement
public class ErrorInfo {

    private final String url;
    private final String message;

    public ErrorInfo(final String url, final String message) {
        this.url = url;
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }

}
