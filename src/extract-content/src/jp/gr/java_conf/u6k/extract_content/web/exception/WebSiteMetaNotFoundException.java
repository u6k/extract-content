
package jp.gr.java_conf.u6k.extract_content.web.exception;

@SuppressWarnings("serial")
public class WebSiteMetaNotFoundException extends RuntimeException {

    private String id;

    public WebSiteMetaNotFoundException(String id) {
        super("id=" + id);

        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}
