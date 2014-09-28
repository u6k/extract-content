
package jp.gr.java_conf.u6k.extract_content.web.api.dao;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class WebSiteMeta {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String wedataId;

    @Persistent
    private String urlPattern;

    @Persistent
    private String pageElement;

    // TODO 削除する。
    @Persistent
    private String contentStartPattern;

    // TODO 削除する。
    @Persistent
    private String contentEndPattern;

    public WebSiteMeta() {
    }

    public WebSiteMeta(String urlPattern, String contentStartPattern, String contentEndPattern) {
        this.urlPattern = urlPattern;
        this.contentStartPattern = contentStartPattern;
        this.contentEndPattern = contentEndPattern;
    }

    public Key getKey() {
        return key;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getContentStartPattern() {
        return contentStartPattern;
    }

    public void setContentStartPattern(String contentStartPattern) {
        this.contentStartPattern = contentStartPattern;
    }

    public String getContentEndPattern() {
        return contentEndPattern;
    }

    public void setContentEndPattern(String contentEndPattern) {
        this.contentEndPattern = contentEndPattern;
    }

    public String getWedataId() {
        return wedataId;
    }

    public void setWedataId(String wedataId) {
        this.wedataId = wedataId;
    }

    public String getPageElement() {
        return pageElement;
    }

    public void setPageElement(String pageElement) {
        this.pageElement = pageElement;
    }

}
