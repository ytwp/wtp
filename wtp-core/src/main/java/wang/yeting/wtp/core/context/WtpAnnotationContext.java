package wang.yeting.wtp.core.context;

import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.handler.WtpHandler;

import java.util.List;
import java.util.Map;

/**
 * @author : weipeng
 * @date : 2020-09-10 12:07
 */

public interface WtpAnnotationContext {

    /**
     * Put wtpHandler into WtpAnnotationContext
     *
     * @param name
     * @param wtpHandler
     */
    void setWtpHandler(String name, WtpHandler wtpHandler);

    /**
     * Get wtpHandler according to name
     *
     * @param name {@link Wtp}
     * @return {@link WtpHandler}
     */
    List<WtpHandler> getWtpHandler(String name);

    /**
     * Get multiple wtpHandler according to name
     *
     * @param names {@link Wtp}
     * @return {@link List<WtpHandler>}
     */
    Map<String, List<WtpHandler>> getWtpHandler(String... names);

    /**
     * Get all wtpHandler
     *
     * @return {@link List<WtpHandler>}
     */
    Map<String, List<WtpHandler>> getWtpHandler();
}
