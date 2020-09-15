package wang.yeting.wtp.core.biz.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : weipeng
 * @date : 2020-07-24 9:58
 */
@Data
@Accessors(chain = true)
public class ConfigEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConcurrentMap<String, Config> configConcurrentMap;

    public ConfigEvent(){}

    /**
     * Constructor.
     *
     * @param configConcurrentMap the actual changes
     */
    public ConfigEvent(ConcurrentMap<String, Config> configConcurrentMap) {
        this.configConcurrentMap = configConcurrentMap;
    }

    /**
     * Get the keys changed.
     *
     * @return the list of the keys
     */
    public Set<String> changedKeys() {
        return configConcurrentMap.keySet();
    }

    /**
     * Get a specific change instance for the key specified.
     *
     * @param key the changed key
     * @return the change instance
     */
    public Config getChange(String key) {
        return configConcurrentMap.get(key);
    }

    /**
     * Check whether the specified key is changed
     *
     * @param key the key
     * @return true if the key is changed, false otherwise.
     */
    public boolean isChanged(String key) {
        return configConcurrentMap.containsKey(key);
    }

}
