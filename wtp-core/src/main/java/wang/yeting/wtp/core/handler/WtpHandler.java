package wang.yeting.wtp.core.handler;

import lombok.Data;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;

/**
 * @author : weipeng
 * @date : 2020-07-23 15:12
 */

@Data
public abstract class WtpHandler {

    public Wtp wtp;

    /**
     * 赋值
     *
     * @param threadPool {@link WtpThreadPoolExecutor} 值
     * @return {@link Boolean} 赋值结果
     */
    public abstract Boolean assignment(WtpThreadPoolExecutor threadPool);

}
