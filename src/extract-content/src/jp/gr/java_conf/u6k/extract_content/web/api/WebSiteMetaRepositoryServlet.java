
package jp.gr.java_conf.u6k.extract_content.web.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMetaDao;
import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.WebSiteMetaDuplicateException;

@SuppressWarnings("serial")
public class WebSiteMetaRepositoryServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(WebSiteMetaRepositoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // バージョンを出力する。
            String version = getServletContext().getInitParameter("version");
            resp.addHeader("X-Version", version);

            // パラメータを取得する。
            String urlPattern = StringUtil.trim(req.getParameter("urlPattern"));
            String contentStartPattern = StringUtil.trim(req.getParameter("contentStartPattern"));
            String contentEndPattern = StringUtil.trim(req.getParameter("contentEndPattern"));

            LOG.finer("param: urlPattern=" + urlPattern);
            LOG.finer("param: contentStartPattern=" + contentStartPattern);
            LOG.finer("param: contentEndPattern=" + contentEndPattern);

            // DBに登録する。もろもろの検証はDAOに任せる。
            WebSiteMetaDao dao = new WebSiteMetaDao();
            try {
                dao.create(urlPattern, contentStartPattern, contentEndPattern);
            } catch (IllegalArgumentException | WebSiteMetaDuplicateException e) {
                LOG.log(Level.WARNING, "meta create failure.", e);
                resp.setStatus(400);
                resp.setContentType("text/plain");

                PrintWriter w = resp.getWriter();
                w.write(e.toString());
                w.flush();

                return;
            }
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "error", e);
            resp.setStatus(500);
            resp.setContentType("text/plain");

            PrintWriter w = resp.getWriter();
            w.write(e.toString());
            w.flush();

            return;
        }
    }

}
