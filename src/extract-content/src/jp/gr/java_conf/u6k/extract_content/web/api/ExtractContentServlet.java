
package jp.gr.java_conf.u6k.extract_content.web.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ExtractContentServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ExtractContentServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOG.entering(getClass().getName(), "doGet");

        // バージョンを出力する。
        String version = getServletContext().getInitParameter("version");
        resp.addHeader("X-Version", version);

        // リクエストパラメータを取得する。
        String reqUrl = req.getParameter("url");
        String strUrl = URLDecoder.decode(reqUrl, "UTF-8");

        LOG.info("url=" + reqUrl);
        LOG.info("url(decode)=" + strUrl);

        // 指定したWebページを取得する。
        String html;
        String contentType;

        URL url = new URL(strUrl);
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

                LOG.info("none:" + bout.toString());
                LOG.info("UTF-8:" + bout.toString("UTF-8"));

                html = bout.toString();
            } finally {
                in.close();
            }
        } finally {
            urlCon.disconnect();
        }

        // HTMLを出力する。

        // Pattern pattern = Pattern.compile("<body.*?>");
        // Matcher matcher = pattern.matcher(html);
        // System.out.println(matcher.find());
        // System.out.println(matcher.group());
        // int bodyStart = matcher.start();

        // pattern = Pattern.compile("</body>");
        // matcher = pattern.matcher(html);
        // System.out.println(matcher.find());
        // System.out.println(matcher.group());
        // int bodyEnd = matcher.start();

        Pattern pattern = Pattern.compile("<div class=\"article-header\">");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        matcher.group();
        int articleStart = matcher.start();

        pattern = Pattern.compile("<!-- articleOption End -->");
        matcher = pattern.matcher(html);
        matcher.find();
        matcher.group();
        int articleEnd = matcher.start();

        String article = html.substring(articleStart, articleEnd);

        String resultHtml = "<html>";
        resultHtml += "<head>";
        resultHtml += "<meta http-equiv=\"Content-Type\" content=\"" + contentType + "\"/>";
        resultHtml += "</head>";
        resultHtml += "<body>";
        resultHtml += "<div style=\"font-size: 100%\">" + article + "</div>";
        resultHtml += "<div style=\"font-size: 100%\">original: <a href=\"" + strUrl + "\">" + strUrl + "</a></div>";
        resultHtml += "</body>";
        resultHtml += "</html>";

        resp.setContentType(contentType);
        PrintWriter w = resp.getWriter();
        w.write(resultHtml);
        w.close();
    }

}
