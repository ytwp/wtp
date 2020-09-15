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

    public abstract Boolean assignment(WtpThreadPoolExecutor threadPool);

}
