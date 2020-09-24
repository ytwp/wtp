package wang.yeting.wtp.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wang.yeting.wtp.admin.bean.User;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.UserDto;
import wang.yeting.wtp.admin.model.vo.UserVo;

/**
 * @author : weipeng
 * @date : 2020-08-19 3:34 下午
 */
public interface UserService extends IService<User> {
    PageResponse<UserDto> page(UserVo userVo, UserBo userBo);

    Boolean update(User user, UserBo userBo);

    Boolean create(User user, UserBo userBo);
}
