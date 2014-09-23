
package jp.gr.java_conf.u6k.extract_content.web.api.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static final boolean isNullOrEmpty(String s) {
        return (s == null || s.length() == 0);
    }

    public static final String trim(String s) {
        if (s == null) {
            return "";
        } else {
            return s.trim();
        }
    }

}
