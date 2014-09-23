
package jp.gr.java_conf.u6k.extract_content.web.api.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.WebSiteMetaDuplicateException;

public class WebSiteMetaDao {

    /**
     * Webページのメタ情報を登録する。
     * 
     * @param urlPattern
     *            URLパターン。
     * @param contentStartPattern
     *            本文開始位置。
     * @param contentEndPattern
     *            本文終了位置。
     * @throws IllegalArgumentException
     *             パラメータが間違えている場合。
     */
    public void create(String urlPattern, String contentStartPattern, String contentEndPattern) {
        // パラメータを検証。
        if (StringUtil.isNullOrEmpty(urlPattern)) {
            throw new IllegalArgumentException("urlPattern is empty.");
        }
        if (StringUtil.isNullOrEmpty(contentStartPattern)) {
            throw new IllegalArgumentException("contentStartPattern is empty.");
        }
        if (StringUtil.isNullOrEmpty(contentEndPattern)) {
            throw new IllegalArgumentException("contentEndPattern is empty.");
        }

        // URLパターンの重複チェック。
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(WebSiteMeta.class);
        query.setFilter("urlPattern == pUrlPattern");
        query.declareParameters("String pUrlPattern");
        List<?> resultList = (List<?>) query.execute(urlPattern);

        if (resultList.size() > 0) {
            throw new WebSiteMetaDuplicateException(urlPattern);
        }

        // 登録。
        WebSiteMeta meta = new WebSiteMeta(urlPattern, contentStartPattern, contentEndPattern);
        try {
            pm.makePersistent(meta);
        } finally {
            pm.close();
        }
    }

}
