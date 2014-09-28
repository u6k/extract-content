
package jp.gr.java_conf.u6k.extract_content.web.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.gr.java_conf.u6k.extract_content.domain.ExtractContentDomain;
import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.ContentExtractFailException;
import jp.gr.java_conf.u6k.extract_content.web.exception.PageRequestException;
import jp.gr.java_conf.u6k.extract_content.web.exception.UrlNotMatchException;

@SuppressWarnings("serial")
public class ExtractContentServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ExtractContentServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // バージョンを出力する。
            String version = getServletContext().getInitParameter("version");
            resp.addHeader("X-Version", version);

            // パラメータを取得する。
            String url = StringUtil.trim(req.getParameter("url"));

            LOG.finer("param: url=" + url);

            // Webページを取得して、本文部分を抽出する。
            ExtractContentDomain extractContent = new ExtractContentDomain();
            String content = extractContent.extract(url);

            // TODO Content-Typeを正確に出力する。もしくは、UTF-8にするために出力文字列を変換する。
            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter w = resp.getWriter();
            w.write(content);
            w.flush();
        } catch (IllegalArgumentException | PageRequestException | UrlNotMatchException | ContentExtractFailException e) {
            LOG.log(Level.WARNING, "warning", e);

            resp.setContentType("text/plain");
            resp.setStatus(400);
            PrintWriter w = resp.getWriter();
            w.write(e.toString());
            w.flush();
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "error", e);

            resp.setContentType("text/plain");
            resp.setStatus(500);
            PrintWriter w = resp.getWriter();
            w.write(e.toString());
            w.flush();
        }
    }

}
