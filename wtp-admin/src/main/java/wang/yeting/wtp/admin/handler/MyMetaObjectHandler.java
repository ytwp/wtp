package wang.yeting.wtp.admin.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
/**
 * @author : weipeng
 * @date : 2020-07-04 16:33
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("created", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("modified", System.currentTimeMillis(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modified", System.currentTimeMillis(), metaObject);
    }
}
