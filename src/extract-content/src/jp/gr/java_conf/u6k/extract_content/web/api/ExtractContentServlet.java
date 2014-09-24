
package jp.gr.java_conf.u6k.extract_content.web.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMeta;
import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMetaDao;
import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.ContentExtractFailException;
import jp.gr.java_conf.u6k.extract_content.web.exception.UrlNotMatchException;

@SuppressWarnings("serial")
public class ExtractContentServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ExtractContentServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // バージョンを出力する。
            String version = getServletContext().getInitParameter("version");
            resp.addHeader("X-Version", version);

            // リクエストパラメータを取得する。
            String reqUrl = StringUtil.trim(req.getParameter("url"));
            LOG.info("url=" + reqUrl);

            // パラメータを検証する。
            if (StringUtil.isNullOrEmpty(reqUrl)) {
                throw new IllegalArgumentException("url is empty.");
            }

            // 指定したWebページを取得する。
            String html;
            String contentType;

            reqUrl = URLDecoder.decode(reqUrl, "UTF-8");
            URL url = new URL(reqUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            try {
                urlCon.setRequestProperty("Content-Type", "text/html");
                urlCon.setRequestMethod("GET");
                urlCon.setConnectTimeout(10000);
                InputStream in = urlCon.getInputStream();
                try {
                    contentType = urlCon.getContentType();
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();

                    int len;
                    byte[] buf = new byte[1024 * 1024];
                    while ((len = in.read(buf)) != -1) {
                        bout.write(buf, 0, len);
                    }

                    // TODO レスポンスのContent-Typeから文字セットを取得する。
                    html = bout.toString("UTF-8");
                } finally {
                    in.close();
                }
            } finally {
                urlCon.disconnect();
            }

            // HTMLを出力する。
            WebSiteMetaDao dao = new WebSiteMetaDao();
            List<?> metaList = dao.findAll();

            boolean isMatch = false;

            for (int i = 0; i < metaList.size(); i++) {
                WebSiteMeta meta = (WebSiteMeta) metaList.get(i);

                Pattern pattern = Pattern.compile(meta.getUrlPattern());
                Matcher matcher = pattern.matcher(reqUrl);
                if (matcher.matches()) {
                    isMatch = true;

                    pattern = Pattern.compile(meta.getContentStartPattern());
                    matcher = pattern.matcher(html);
                    if (!matcher.find()) {
                        throw new ContentExtractFailException("content start not match.", meta.getUrlPattern());
                    }
                    int contentStartIndex = matcher.start();

                    pattern = Pattern.compile(meta.getContentEndPattern());
                    matcher = pattern.matcher(html);
                    if (!matcher.find()) {
                        throw new ContentExtractFailException("content end not match.", meta.getUrlPattern());
                    }
                    int contentEndIndex = matcher.end();

                    String content = html.substring(contentStartIndex, contentEndIndex);

                    resp.setContentType(contentType);
                    PrintWriter w = resp.getWriter();
                    w.write(content);
                    w.flush();

                    break;
                }
            }

            if (!isMatch) {
                throw new UrlNotMatchException(reqUrl);
            }
        } catch (IllegalArgumentException | UrlNotMatchException | ContentExtractFailException e) {
            LOG.log(Level.WARNING, "meta create failure.", e);
            resp.setStatus(400);
            resp.setContentType("text/plain");

            PrintWriter w = resp.getWriter();
            w.write(e.toString());
            w.flush();

            return;
        } catch (RuntimeException e) {
            LOG.log(Level.WARNING, "meta create failure.", e);
            resp.setStatus(500);
            resp.setContentType("text/plain");

            PrintWriter w = resp.getWriter();
            w.write(e.toString());
            w.flush();

            return;
        }
    }

}
