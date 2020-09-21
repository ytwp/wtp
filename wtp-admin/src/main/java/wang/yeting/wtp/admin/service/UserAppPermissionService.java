package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.UserAppPermission;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.UserAppPermissionDto;
import wang.yeting.wtp.admin.model.vo.UserAppPermissionVo;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-08-19 21:41
 */

public interface UserAppPermissionService {
    List<UserAppPermission> getByUserId(Integer userId);

    PageResponse<UserAppPermissionDto> find(UserAppPermissionVo userAppPermissionVo, UserBo userBo);

    Boolean create(UserAppPermissionVo userAppPermissionVo, UserBo userBo);

    Boolean del(UserAppPermissionVo userAppPermissionVo, UserBo userBo);

    Boolean add(UserAppPermission userAppPermission);
}
