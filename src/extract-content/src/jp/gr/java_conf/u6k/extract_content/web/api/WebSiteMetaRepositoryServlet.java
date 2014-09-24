
package jp.gr.java_conf.u6k.extract_content.web.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMeta;
import jp.gr.java_conf.u6k.extract_content.web.api.dao.WebSiteMetaDao;
import jp.gr.java_conf.u6k.extract_content.web.api.util.StringUtil;
import jp.gr.java_conf.u6k.extract_content.web.exception.WebSiteMetaDuplicateException;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class WebSiteMetaRepositoryServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(WebSiteMetaRepositoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // バージョンを出力する。
            String version = getServletContext().getInitParameter("version");
            resp.addHeader("X-Version", version);

            // パラメータを取得する。
            String id = StringUtil.trim(req.getParameter("id"));

            LOG.finer("param: id=" + id);

            if (id.length() == 0) {
                // メタ情報を全検索する。
                WebSiteMetaDao dao = new WebSiteMetaDao();
                List<WebSiteMeta> metaList = dao.findAll();

                // 検索結果を出力する。
                resp.setContentType("text/plain");
                PrintWriter w = resp.getWriter();

                for (WebSiteMeta meta : metaList) {
                    w.write(KeyFactory.keyToString(meta.getKey()) + " " + meta.getUrlPattern() + "\n");
                }

                w.flush();
            } else {
                // メタ情報を取得する。
                WebSiteMeta meta;
                WebSiteMetaDao dao = new WebSiteMetaDao();
                try {
                    meta = dao.findById(id);
                } finally {
                    dao.close();
                }

                // 検索結果を出力する。
                resp.setContentType("text/plain");
                PrintWriter w = resp.getWriter();

                if (meta != null) {
                    w.write("id=" + KeyFactory.keyToString(meta.getKey()) + "\n");
                    w.write("urlPattern=" + meta.getUrlPattern() + "\n");
                    w.write("contentStartPattern=" + meta.getContentStartPattern() + "\n");
                    w.write("contentEndPattern=" + meta.getContentEndPattern() + "\n");
                    w.flush();
                } else {
                    resp.setStatus(404);
                }
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
            } finally {
                dao.close();
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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // バージョンを出力する。
            String version = getServletContext().getInitParameter("version");
            resp.addHeader("X-Version", version);

            // パラメータを取得する。
            String id = StringUtil.trim(req.getParameter("id"));

            LOG.finer("param: id=" + id);

            // DBから削除する。検証はDAOに任せる。
            boolean result;

            WebSiteMetaDao dao = new WebSiteMetaDao();
            try {
                result = dao.delete(id);
            } finally {
                dao.close();
            }

            if (!result) {
                resp.setStatus(404);
                return;
            }
        } catch (IllegalArgumentException e) {
            LOG.log(Level.WARNING, "error", e);
            resp.setStatus(400);
            resp.setContentType("text/plain");

            PrintWriter w = resp.getWriter();
            w.write(e.toString());
            w.flush();

            return;
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
