
package jp.gr.java_conf.u6k.extract_content.domain.gson;

public class WedataBaseInfo {

    private String resource_url;

    private WedataSiteInfo data;

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public WedataSiteInfo getData() {
        return data;
    }

    public void setData(WedataSiteInfo data) {
        this.data = data;
    }

}
