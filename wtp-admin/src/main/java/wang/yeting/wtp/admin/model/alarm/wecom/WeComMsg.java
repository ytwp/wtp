package wang.yeting.wtp.admin.model.alarm.wecom;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-10-29 6:09 下午
 */
@Data
@Accessors(chain = true)
public class WeComMsg {

    private String msgtype = "text";

    private Msg text;

    @Data
    @Accessors(chain = true)
    public class Msg {

        private String content;

        private List<String> mentioned_list;

        private List<String> mentioned_mobile_list;

        public Msg ofMentionedList(String... mentioneds) {
            this.mentioned_list = CollectionUtil.toList(mentioneds);
            return this;
        }

        public Msg ofMentionedMobileList(String... mentionedMobiles) {
            this.mentioned_mobile_list = CollectionUtil.toList(mentionedMobiles);
            return this;
        }
    }

}
