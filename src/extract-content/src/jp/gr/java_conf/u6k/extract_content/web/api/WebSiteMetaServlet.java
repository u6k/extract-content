
package jp.gr.java_conf.u6k.extract_content.web.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.gr.java_conf.u6k.extract_content.domain.WebSiteMetaDomain;
import jp.gr.java_conf.u6k.extract_content.web.exception.WedataException;

@SuppressWarnings("serial")
public class WebSiteMetaServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(WebSiteMetaServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // バージョンを出力する。
            String version = getServletContext().getInitParameter("version");
            resp.addHeader("X-Version", version);

            // メタ情報を全て更新する。
            WebSiteMetaDomain domain = new WebSiteMetaDomain();
            domain.refresh();
        } catch (WedataException e) {
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
