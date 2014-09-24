
package jp.gr.java_conf.u6k.extract_content.web.api.dao;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.WebSiteMetaDuplicateException;
import jp.gr.java_conf.u6k.extract_content.web.exception.WebSiteMetaNotFoundException;

public class WebSiteMetaDao {

    private PersistenceManager pm;

    public WebSiteMetaDao() {
        this.pm = PMF.get().getPersistenceManager();
    }

    /**
     * リソースを閉じる。
     */
    public void close() {
        this.pm.close();
    }

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
     *             urlPattern引数、contentStartPattern引数、contentEndPattern引数がnullまたは空文字列の場合。
     * @throws WebSiteMetaDuplicateException
     *             登録しようとしたURLパターンが既に登録されている場合。
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
        List<WebSiteMeta> result = findByUrlPattern(urlPattern);

        if (result.size() > 0) {
            throw new WebSiteMetaDuplicateException(urlPattern);
        }

        // 登録。
        WebSiteMeta meta = new WebSiteMeta(urlPattern, contentStartPattern, contentEndPattern);
        this.pm.makePersistent(meta);
    }

    public void update(String id, String urlPattern, String contentStartPattern, String contentEndPattern) {
        // パラメータを検証する。
        if (StringUtil.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("id is empty.");
        }

        // URLパターンの重複チェック。
        List<WebSiteMeta> metaList = findByUrlPattern(urlPattern);
        if (metaList.size() > 0) {
            throw new WebSiteMetaDuplicateException(urlPattern);
        }

        // 存在チェック。
        WebSiteMeta meta = findById(id);
        if (meta == null) {
            throw new WebSiteMetaNotFoundException(id);
        }

        // 更新。
        if (!StringUtil.isNullOrEmpty(urlPattern)) {
            meta.setUrlPattern(urlPattern);
        }
        if (!StringUtil.isNullOrEmpty(contentStartPattern)) {
            meta.setContentStartPattern(contentStartPattern);
        }
        if (!StringUtil.isNullOrEmpty(contentEndPattern)) {
            meta.setContentEndPattern(contentEndPattern);
        }
    }

    /**
     * キー文字列を指定して、Webサイトメタ情報を削除する。
     * 
     * @param id
     *            削除するWebサイトメタ情報のキー文字列。
     * @return 削除に成功した場合はtrue、失敗した場合はfalse。
     */
    public boolean delete(String id) {
        WebSiteMeta meta = findById(id);
        if (meta == null) {
            return false;
        }

        this.pm.deletePersistent(meta);

        return true;
    }

    /**
     * 全てのWebサイトメタ情報を取得する。
     * 
     * @return 全てのWebサイトメタ情報。該当するデータが存在しない場合、0件のリストを返す。
     */
    public List<WebSiteMeta> findAll() {
        Query query = this.pm.newQuery(WebSiteMeta.class);
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
     * @throws IllegalArgumentException
     *             id引数がnullまたは空文字列の場合。
     */
    public WebSiteMeta findById(String id) {
        if (StringUtil.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("id is empty.");
        }

        WebSiteMeta meta;
        try {
            meta = this.pm.getObjectById(WebSiteMeta.class, id);
        } catch (JDOObjectNotFoundException e) {
            meta = null;
        }

        return meta;
    }

    private List<WebSiteMeta> findByUrlPattern(String urlPattern) {
        Query query = this.pm.newQuery(WebSiteMeta.class);
        query.setFilter("urlPattern == pUrlPattern");
        query.declareParameters("String pUrlPattern");
        @SuppressWarnings("unchecked")
        List<WebSiteMeta> result = (List<WebSiteMeta>) query.execute(urlPattern);

        return result;
    }

}
