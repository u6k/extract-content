
package jp.gr.java_conf.u6k.extract_content.web.exception;

@SuppressWarnings("serial")
public class WedataException extends RuntimeException {

    public WedataException(Throwable cause) {
        super(cause);
    }

    public WedataException(int statusCode) {
        super("statusCode=" + statusCode);
    }

}
