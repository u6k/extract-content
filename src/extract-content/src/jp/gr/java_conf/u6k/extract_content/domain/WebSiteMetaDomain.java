
package jp.gr.java_conf.u6k.extract_content.domain;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import jp.gr.java_conf.u6k.extract_content.domain.gson.WedataBaseInfo;
import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMeta;
import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMetaDao;
import jp.gr.java_conf.u6k.extract_content.web.exception.WedataException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class WebSiteMetaDomain {

    private static final Logger LOG = Logger.getLogger(WebSiteMetaDomain.class.getName());

    /** wedataのURL。 */
    private static final String WEDATA_URL = "http://wedata.net/databases/AutoPagerize/items.json";

    /**
     * Webサイトメタ情報を全て更新する。
     * 
     * @throws WedataException
     *             wedataからデータを取得できなかった場合。
     */
    public void refresh() {
        // wedataからJSONデータを取得する。
        LOG.finer("request wedata.");

        String wedataJson;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(WEDATA_URL);
            CloseableHttpResponse httpResp = httpClient.execute(httpGet);
            try {
                if (httpResp.getStatusLine().getStatusCode() == 200) {
                    wedataJson = EntityUtils.toString(httpResp.getEntity());
                } else {
                    throw new WedataException(httpResp.getStatusLine().getStatusCode());
                }
            } finally {
                httpResp.close();
            }
        } catch (ParseException | IOException e) {
            throw new WedataException(e);
        }

        Gson gson = new Gson();
        WedataBaseInfo[] baseInfos = gson.fromJson(wedataJson, WedataBaseInfo[].class);

        // メタ情報をDBから全削除する。
        LOG.finer("delete all meta.");

        WebSiteMetaDao dao = new WebSiteMetaDao();
        try {
            dao.deleteAll();
        } finally {
            dao.close();
        }

        // メタ情報をDBに登録する。
        LOG.finer("create all meta.");

        dao = new WebSiteMetaDao();
        try {
            for (WedataBaseInfo baseInfo : baseInfos) {
                dao.create(baseInfo.getResource_url(), baseInfo.getData().getUrl(), baseInfo.getData().getPageElement());
            }
        } finally {
            dao.close();
        }
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
