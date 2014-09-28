
package jp.gr.java_conf.u6k.extract_content.web.exception;

@SuppressWarnings("serial")
public class PageRequestException extends RuntimeException {

    public PageRequestException(String url, Throwable e) {
        super("url=" + url, e);
    }

}
