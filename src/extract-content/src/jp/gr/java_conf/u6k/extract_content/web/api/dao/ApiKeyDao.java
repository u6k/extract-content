
package jp.gr.java_conf.u6k.extract_content.web.api.dao;

import java.util.List;
import java.util.UUID;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class ApiKeyDao {

    private PersistenceManager pm;

    public ApiKeyDao() {
        this.pm = PMF.get().getPersistenceManager();
    }

    /**
     * リソースを閉じる。
     */
    public void close() {
        this.pm.close();
    }

    /**
     * APIキーを更新する。生成されていない場合、生成する。
     */
    public void refresh() {
        Query query = this.pm.newQuery(ApiKey.class);
        @SuppressWarnings("unchecked")
        List<ApiKey> l = (List<ApiKey>) query.execute();

        this.pm.deletePersistentAll(l);

        ApiKey apiKey = new ApiKey(UUID.randomUUID().toString());
        this.pm.makePersistent(apiKey);
    }

    /**
     * APIキーを取得する。生成されていない場合、生成してから取得する。
     * 
     * @return APIキー。
     */
    public String getApiKey() {
        Query query = this.pm.newQuery(ApiKey.class);
        @SuppressWarnings("unchecked")
        List<ApiKey> l = (List<ApiKey>) query.execute();

        ApiKey apiKey;
        if (l.size() > 0) {
            apiKey = l.get(0);
        } else {
            apiKey = new ApiKey(UUID.randomUUID().toString());
            this.pm.makePersistent(apiKey);
        }

        return apiKey.getApiKey();
    }

}
