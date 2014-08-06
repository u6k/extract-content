
package jp.gr.java_conf.u6k.extract_content.sample_extract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Main {

    public static void main(String[] args) throws Exception {
        String url = "http://ayamevip.com/archives/20105548.html";

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResp = httpClient.execute(httpGet);
        String html = EntityUtils.toString(httpResp.getEntity());

        writeFile("/Users/u6k/Downloads/test.html", html);

        Pattern pattern = Pattern.compile("<body.*?>");
        Matcher matcher = pattern.matcher(html);
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        int bodyStart = matcher.start();

        pattern = Pattern.compile("</body>");
        matcher = pattern.matcher(html);
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        int bodyEnd = matcher.start();

        pattern = Pattern.compile("<div class=\"article-body entry-content\">");
        matcher = pattern.matcher(html);
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        int articleStart = matcher.start();

        pattern = Pattern.compile("<!-- articleBody End -->");
        matcher = pattern.matcher(html);
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        int articleEnd = matcher.start();

        String article = html.substring(articleStart, articleEnd);

        writeFile("/Users/u6k/Downloads/article.html", article);
    }

    private static void writeFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fout = new FileOutputStream(file);
        try {
            fout.write(content.getBytes());
        } finally {
            fout.close();
        }
    }

}
