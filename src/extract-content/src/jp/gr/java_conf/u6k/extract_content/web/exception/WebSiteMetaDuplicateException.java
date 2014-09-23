
package jp.gr.java_conf.u6k.extract_content.web.exception;

@SuppressWarnings("serial")
public class WebSiteMetaDuplicateException extends RuntimeException {

    private String urlPattern;

    public WebSiteMetaDuplicateException(String urlPattern) {
        super("meta duplicate. [urlPattern=" + urlPattern + "]");

        this.urlPattern = urlPattern;
    }

    public String getUrlPattern() {
        return this.urlPattern;
    }

}
