
package jp.gr.java_conf.u6k.extract_content.web.exception;

@SuppressWarnings("serial")
public class UrlNotMatchException extends RuntimeException {

    private String url;

    public UrlNotMatchException(String url) {
        super("url=" + url);

        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

}
