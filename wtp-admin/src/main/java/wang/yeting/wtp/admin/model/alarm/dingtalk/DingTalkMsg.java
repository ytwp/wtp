package wang.yeting.wtp.admin.model.alarm.dingtalk;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-10-29 4:44 下午
 * 订单群机器人 告警消息
 */
@Data
@Accessors(chain = true)
public class DingTalkMsg {

    private At at;

    private Text text;

    /**
     * msgtype
     * String
     * 是
     * 消息类型，此时固定为：text
     */
    private String msgtype = "text";

    @Data
    @Accessors(chain = true)
    public class At {

        /**
         * isAtAll
         * Boolean
         * 否
         * 是否@所有人
         */
        private Boolean isAtAll;

        /**
         * atMobiles
         * Array
         * 否
         * 被@人的手机号（在content里添加@人的手机号）
         */
        private List<String> atMobiles;

    }

    @Data
    @Accessors(chain = true)
    public class Text {

        /**
         * content
         * String
         * 是
         * 消息内容
         */
        private String content;

    }


}
