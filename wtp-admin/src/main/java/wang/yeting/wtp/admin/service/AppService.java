package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.App;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.vo.AppVo;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:43
 */

public interface AppService {

    PageResponse page(AppVo AppVo, UserBo userBo);

    Boolean create(App app, UserBo userBo);

    PageResponse appOptions(UserBo userBo);

    Boolean update(App app, UserBo userBo);

    App get(AppVo appVo, UserBo userBo);

    Boolean del(AppVo appVo, UserBo userBo);
}
