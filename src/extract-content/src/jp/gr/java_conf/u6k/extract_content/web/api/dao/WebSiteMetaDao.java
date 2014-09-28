
package jp.gr.java_conf.u6k.extract_content.web.api.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;

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
     * @param wedataId
     *            wedataのリソースURL。
     * @param urlPattern
     *            URLパターン。
     * @param pageElement
     *            本文部分のXPath式。
     * @throws IllegalArgumentException
     *             wedataId引数、urlPattern引数、pageElement引数がnullまたは空文字列の場合。
     */
    public void create(String wedataId, String urlPattern, String pageElement) {
        // パラメータを検証。
        if (StringUtil.isNullOrEmpty(wedataId)) {
            throw new IllegalArgumentException("wedataId is empty.");
        }
        if (StringUtil.isNullOrEmpty(urlPattern)) {
            throw new IllegalArgumentException("urlPattern is empty.");
        }
        if (StringUtil.isNullOrEmpty(pageElement)) {
            throw new IllegalArgumentException("pageElement is empty.");
        }

        // 登録。
        WebSiteMeta meta = new WebSiteMeta(wedataId, urlPattern, pageElement);
        this.pm.makePersistent(meta);
    }

    /**
     * 全てのWebサイトメタ情報を削除する。
     */
    public void deleteAll() {
        while (true) {
            Query query = this.pm.newQuery(WebSiteMeta.class);
            @SuppressWarnings("unchecked")
            List<WebSiteMeta> l = (List<WebSiteMeta>) query.execute();

            if (l.size() == 0) {
                break;
            }

            this.pm.deletePersistentAll(l);
        }
    }

}
