package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.Cluster;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.ClusterDto;
import wang.yeting.wtp.admin.model.vo.ClusterVo;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:44
 */

public interface ClusterService {
    PageResponse<Cluster> options(ClusterVo clusterVo, UserBo userBo);

    Boolean create(Cluster cluster, UserBo userBo);

    PageResponse<ClusterDto> page(ClusterVo clusterVo, UserBo userBo);

    Boolean update(Cluster cluster, UserBo userBo);

    Cluster get(ClusterVo clusterVo, UserBo userBo);

    Boolean del(ClusterVo clusterVo, UserBo userBo);
}
