
package jp.gr.java_conf.u6k.extract_content.domain;

import java.io.IOException;

import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMeta;
import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.ContentExtractFailException;
import jp.gr.java_conf.u6k.extract_content.web.exception.PageRequestException;
import jp.gr.java_conf.u6k.extract_content.web.exception.UrlNotMatchException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import us.codecraft.xsoup.Xsoup;

public class ExtractContentDomain {

    /**
     * URLからWebページを取得して、メタ情報を使用して本文部分を返す。
     * 
     * @param url
     *            取得するWebページのURL。
     * @return Webページの本文部分のHTML。
     * @throws IllegalArgumentException
     *             url引数が空の場合。
     * @throws PageRequestException
     *             Webページを取得できなかった場合。
     * @throws UrlNotMatchException
     *             URLに一致するメタ情報が存在しない場合。
     * @throws ContentExtractFailException
     *             Webページに本文部分が存在しない場合。
     */
    public String extract(String url) {
        if (StringUtil.isNullOrEmpty(url)) {
            throw new IllegalArgumentException("url is empty.");
        }

        // HTMLを取得する。
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(10000).get();
        } catch (IOException e) {
            throw new PageRequestException(url, e);
        }

        // メタ情報管理から取得する。
        WebSiteMetaDomain webSiteMeta = new WebSiteMetaDomain();
        WebSiteMeta meta = webSiteMeta.findByUrl(url);
        if (meta == null) {
            throw new UrlNotMatchException(url);
        }

        // 本文部分を抽出する。
        String result = Xsoup.compile(meta.getPageElement()).evaluate(doc).get();
        if (result == null) {
            throw new ContentExtractFailException("content not found.", meta.getUrlPattern());
        }

        return result;
    }

}
