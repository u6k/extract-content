package us.codecraft.xsoup.xevaluator;

import org.jsoup.select.Elements;

import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public interface XElements {

    String get();

    List<String> list();

    public Elements getElements();

}
