package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.Cluster;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.vo.ClusterVo;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:44
 */

public interface ClusterService {
    PageResponse options(ClusterVo ClusterVo, UserBo userBo);

    Boolean create(Cluster cluster, UserBo userBo);

    PageResponse page(ClusterVo ClusterVo, UserBo userBo);

    Boolean update(Cluster cluster, UserBo userBo);

    Cluster get(ClusterVo clusterVo, UserBo userBo);
}
