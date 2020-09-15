package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.vo.LoginVo;

/**
 * @author : weipeng
 * @date : 2020-08-19 3:34 下午
 */
public interface LoginService {

    Result login(LoginVo loginVo);

    Result info(String token);
}
