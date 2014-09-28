
package jp.gr.java_conf.u6k.extract_content.web.api.dao;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class WebSiteMeta {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String wedataId;

    @Persistent
    private Text urlPattern;

    @Persistent
    private Text pageElement;

    public WebSiteMeta() {
    }

    public WebSiteMeta(String wedataId, String urlPattern, String pageElement) {
        this.wedataId = wedataId;
        setUrlPattern(urlPattern);
        setPageElement(pageElement);
    }

    public String getWedataId() {
        return wedataId;
    }

    public void setWedataId(String wedataId) {
        this.wedataId = wedataId;
    }

    public String getUrlPattern() {
        if (this.urlPattern != null) {
            return this.urlPattern.getValue();
        } else {
            return null;
        }
    }

    public void setUrlPattern(String urlPattern) {
        if (urlPattern != null) {
            this.urlPattern = new Text(urlPattern);
        } else {
            this.urlPattern = null;
        }
    }

    public String getPageElement() {
        if (this.pageElement != null) {
            return this.pageElement.getValue();
        } else {
            return null;
        }
    }

    public void setPageElement(String pageElement) {
        if (pageElement != null) {
            this.pageElement = new Text(pageElement);
        } else {
            this.pageElement = null;
        }
    }

    public Key getKey() {
        return key;
    }

}
