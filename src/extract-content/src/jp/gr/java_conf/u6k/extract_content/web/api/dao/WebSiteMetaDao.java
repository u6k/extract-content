
package jp.gr.java_conf.u6k.extract_content.web.api.dao;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.WebSiteMetaDuplicateException;

public class WebSiteMetaDao {

    /**
     * Webサイトメタ情報を登録する。
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

        List<WebSiteMeta> result = findByUrlPattern(pm, urlPattern);

        if (result.size() > 0) {
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

    /**
     * 全てのWebサイトメタ情報を取得する。
     * 
     * @return 全てのWebサイトメタ情報。該当するデータが存在しない場合、0件のリストを返す。
     */
    public List<WebSiteMeta> findAll() {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(WebSiteMeta.class);
        @SuppressWarnings("unchecked")
        List<WebSiteMeta> result = (List<WebSiteMeta>) query.execute();

        return result;
    }

    /**
     * キー文字列を指定して、Webサイトメタ情報を取得する。
     * 
     * @param id
     *            キー文字列。
     * @return Webサイトメタ情報。該当するデータが存在しない場合、nullを返す。
     */
    public WebSiteMeta findById(String id) {
        WebSiteMeta meta;

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            meta = pm.getObjectById(WebSiteMeta.class, id);
        } catch (JDOObjectNotFoundException e) {
            meta = null;
        }

        return meta;
    }

    private List<WebSiteMeta> findByUrlPattern(PersistenceManager pm, String urlPattern) {
        Query query = pm.newQuery(WebSiteMeta.class);
        query.setFilter("urlPattern == pUrlPattern");
        query.declareParameters("String pUrlPattern");
        @SuppressWarnings("unchecked")
        List<WebSiteMeta> result = (List<WebSiteMeta>) query.execute(urlPattern);

        return result;
    }

}
