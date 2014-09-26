
package jp.gr.java_conf.u6k.extract_content.web.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.gr.java_conf.u6k.extract_content.web.api.dao.ApiKeyDao;

@SuppressWarnings("serial")
public class ApiKeyServlet extends HttpServlet {

    private Logger LOG = Logger.getLogger(ApiKeyServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // バージョンを出力する。
            String version = getServletContext().getInitParameter("version");
            resp.addHeader("X-Version", version);

            // APIキーを出力する。
            ApiKeyDao dao = new ApiKeyDao();
            String apiKey = dao.getApiKey();

            resp.setContentType("text/plain");

            PrintWriter w = resp.getWriter();
            w.write(apiKey);
            w.flush();

            return;
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "error.", e);
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
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

}
