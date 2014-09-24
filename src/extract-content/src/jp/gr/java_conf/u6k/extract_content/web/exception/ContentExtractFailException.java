
package jp.gr.java_conf.u6k.extract_content.web.exception;

@SuppressWarnings("serial")
public class ContentExtractFailException extends RuntimeException {

    private String urlPattern;

    public ContentExtractFailException(String message, String urlPattern) {
        super(message + " [urlPattern=" + urlPattern + "]");

        this.urlPattern = urlPattern;
    }

    public String getUrlPattern() {
        return this.urlPattern;
    }

}
