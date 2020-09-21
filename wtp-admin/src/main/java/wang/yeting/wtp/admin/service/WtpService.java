package wang.yeting.wtp.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.WtpDto;
import wang.yeting.wtp.admin.model.vo.WtpVo;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:43
 */

public interface WtpService {
    Boolean create(Wtp wtp, UserBo userBo);

    PageResponse<WtpDto> page(WtpVo wtpVo, UserBo userBo);

    Boolean update(Wtp wtp, UserBo userBo);

    Wtp get(WtpVo wtpVo, UserBo userBo);

    /**
     * ↓ 内部接口
     */
    Wtp info(WtpVo wtpVo);

    List<Wtp> initConfigFactory();

    Boolean add(Wtp wtp);

    Wtp get(LambdaQueryWrapper<Wtp> lambdaQueryWrapper);

    Boolean del(Wtp wtp, UserBo userBo);

    Boolean syncConfig(Wtp wtp, String clusterIds, UserBo userBo);
}
