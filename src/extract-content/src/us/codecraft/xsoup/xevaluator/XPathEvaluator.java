package us.codecraft.xsoup.xevaluator;

import org.jsoup.nodes.Element;

/**
 * @author code4crafter@gmail.com
 */
public interface XPathEvaluator {

    XElements evaluate(Element element);

    boolean hasAttribute();

}
