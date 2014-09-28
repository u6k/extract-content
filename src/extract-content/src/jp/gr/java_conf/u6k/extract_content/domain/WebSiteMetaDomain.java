
package jp.gr.java_conf.u6k.extract_content.domain;

import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMeta;

public class WebSiteMetaDomain {

    public void refresh() {
        // TODO
    }

    public List<WebSiteMeta> findAll() {
        return Collections.emptyList();
    }

    public WebSiteMeta findByUrl(String url) {
        // TODO
        WebSiteMeta meta = new WebSiteMeta();
        meta.setPageElement("//div[@class=\"article-outer-2\"]");

        return meta;
    }

}
