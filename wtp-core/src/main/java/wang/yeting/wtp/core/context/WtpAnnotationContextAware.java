package wang.yeting.wtp.core.context;

import org.springframework.beans.factory.Aware;

/**
 * @author : weipeng
 * @date : 2020-09-10 12:05
 * Interface to be implemented by any bean that wishes to be notified
 */

public interface WtpAnnotationContextAware extends Aware {

    /**
     * Set the {@link WtpAnnotationContext} that this component runs in.
     *
     * @param wtpAnnotationContext
     */
    void setWtpAnnotationContext(WtpAnnotationContext wtpAnnotationContext);
}
