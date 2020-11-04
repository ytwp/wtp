package wang.yeting.wtp.core.context.support;

import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.context.WtpAnnotationContext;
import wang.yeting.wtp.core.handler.WtpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : weipeng
 * @date : 2020-09-10 13:23
 */

public class AbstractWtpAnnotationContext implements WtpAnnotationContext {

    private final Map<String, List<WtpHandler>> wtpHandlerMap;

    public AbstractWtpAnnotationContext() {
        wtpHandlerMap = new HashMap<>();
    }

    /**
     * Put wtpHandler into WtpAnnotationContext
     *
     * @param name       {@link String}
     * @param wtpHandler {@link WtpHandler}
     */
    @Override
    public void setWtpHandler(String name, WtpHandler wtpHandler) {
        List<WtpHandler> wtpHandlers = wtpHandlerMap.computeIfAbsent(name, k -> new ArrayList<>());
        wtpHandlers.add(wtpHandler);
    }

    /**
     * Get wtpHandler according to name
     *
     * @param name {@link Wtp}
     * @return {@link WtpHandler}
     */
    @Override
    public List<WtpHandler> getWtpHandler(String name) {
        return wtpHandlerMap.get(name);
    }

    /**
     * Get multiple wtpHandler according to name
     *
     * @param names {@link Wtp}
     * @return {@link List <WtpHandler>}
     */
    @Override
    public Map<String, List<WtpHandler>> getWtpHandler(String... names) {
        Map<String, List<WtpHandler>> wtpHandlerMapTemp = new HashMap<>(names.length);
        for (String name : names) {
            List<WtpHandler> wtpHandlerList = wtpHandlerMap.get(name);
            wtpHandlerMapTemp.put(name, wtpHandlerList);
        }
        return wtpHandlerMapTemp;
    }

    /**
     * Get all wtpHandler
     *
     * @return {@link List<WtpHandler>}
     */
    @Override
    public Map<String, List<WtpHandler>> getWtpHandler() {
        return wtpHandlerMap;
    }
}
