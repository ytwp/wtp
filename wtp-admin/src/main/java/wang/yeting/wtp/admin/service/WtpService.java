package wang.yeting.wtp.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.vo.WtpVo;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:43
 */

public interface WtpService {
    Boolean create(Wtp wtp, UserBo userBo);

    PageResponse page(WtpVo WtpVo, UserBo userBo);

    Boolean update(Wtp wtp, UserBo userBo);

    Wtp get(WtpVo WtpVo, UserBo userBo);

    /**
     * ↓ 内部接口
     */
    Wtp info(WtpVo WtpVo);

    List<Wtp> initConfigFactory();

    Boolean add(Wtp wtp);

    Wtp get(LambdaQueryWrapper<Wtp> lambdaQueryWrapper);

    Boolean del(Wtp wtp, UserBo userBo);
}
