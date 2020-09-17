package wang.yeting.wtp.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.UserAppPermission;
import wang.yeting.wtp.admin.mapper.UserAppPermissionMapper;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.UserAppPermissionDto;
import wang.yeting.wtp.admin.model.enums.PermissionEnum;
import wang.yeting.wtp.admin.model.vo.UserAppPermissionVo;
import wang.yeting.wtp.admin.service.UserAppPermissionService;
import wang.yeting.wtp.admin.util.TokenUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : weipeng
 * @date : 2020-08-19 21:41
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAppPermissionServiceImpl extends ServiceImpl<UserAppPermissionMapper, UserAppPermission> implements UserAppPermissionService {

    private final TokenUtils tokenUtils;

    @Override
    public List<UserAppPermission> getByUserId(Integer userId) {
        return list(new LambdaQueryWrapper<UserAppPermission>().eq(UserAppPermission::getUserId, userId));
    }

    @Override
    public PageResponse find(UserAppPermissionVo userAppPermissionVo, UserBo userBo) {
        PageResponse pageResponse = new PageResponse();
        Integer userId = userAppPermissionVo.getUserId() != null ? userAppPermissionVo.getUserId() : userBo.getUserId();
        LambdaQueryWrapper<UserAppPermission> lambdaQueryWrapper = new LambdaQueryWrapper<UserAppPermission>().eq(UserAppPermission::getUserId, userId);
        if (!tokenUtils.checkSuperAdmin(userBo)) {
            List<UserAppPermission> userAppPermissionList = userBo.getUserAppPermissionList();
            Set<String> permissionSet = userAppPermissionList.stream().map(UserAppPermission::getAppId).collect(Collectors.toSet());
            if (CollectionUtil.isEmpty(permissionSet)) {
                return new PageResponse().setList(null);
            }
            lambdaQueryWrapper.in(UserAppPermission::getAppId, permissionSet);
        }
        List<UserAppPermission> userAppPermissionList = list(lambdaQueryWrapper);
        Map<String, List<UserAppPermission>> permissionListMap = new HashMap<>();
        for (UserAppPermission userAppPermission : userAppPermissionList) {
            List<UserAppPermission> appPermissionList = permissionListMap.computeIfAbsent(userAppPermission.getAppId(), k -> new ArrayList<>());
            appPermissionList.add(userAppPermission);
        }
        List<UserAppPermissionDto> userAppPermissionDtoList = new ArrayList<>();
        List<String> checked = new ArrayList<>();
        for (Map.Entry<String, List<UserAppPermission>> permissionListEntry : permissionListMap.entrySet()) {
            String appId = permissionListEntry.getKey();
            List<UserAppPermission> appPermissionList = permissionListEntry.getValue();
            UserAppPermissionDto userAppPermissionDto = new UserAppPermissionDto();
            userAppPermissionDto.setUserId(appPermissionList.get(0).getUserId());
            userAppPermissionDto.setAppId(appId);
            userAppPermissionDto.setId(appId);
            userAppPermissionDto.setLabel(appId);
            getChildren(appId, userAppPermissionDto, checked, appPermissionList);
            userAppPermissionDtoList.add(userAppPermissionDto);
        }
        pageResponse.setList(userAppPermissionDtoList);
        pageResponse.setData(checked);
        return pageResponse;
    }

    @Override
    public Boolean create(UserAppPermissionVo userAppPermissionVo, UserBo userBo) {
        UserAppPermission userAppPermission = new UserAppPermission();
        BeanUtil.copyProperties(userAppPermissionVo, userAppPermission);
        if (StringUtils.isBlank(userAppPermission.getPermission())) {
            userAppPermission.setPermission(PermissionEnum.SELECT.getPermission());
        }
        return save(userAppPermission);
    }

    @Override
    public Boolean del(UserAppPermissionVo userAppPermissionVo, UserBo userBo) {
        LambdaQueryWrapper<UserAppPermission> lambdaQueryWrapper = new LambdaQueryWrapper<UserAppPermission>()
                .eq(UserAppPermission::getUserId, userAppPermissionVo.getUserId())
                .eq(UserAppPermission::getAppId, userAppPermissionVo.getAppId())
                .eq(StringUtils.isNotBlank(userAppPermissionVo.getPermission()), UserAppPermission::getPermission, userAppPermissionVo.getPermission());
        return remove(lambdaQueryWrapper);
    }

    @Override
    public Boolean add(UserAppPermission userAppPermission) {
        return save(userAppPermission);
    }

    private void getChildren(String appId, UserAppPermissionDto userAppPermissionDto, List<String> checked, List<UserAppPermission> userAppPermissionList) {
        Set<String> userPermissionSet = userAppPermissionList.stream().map(UserAppPermission::getPermission).collect(Collectors.toSet());
        Map<String, PermissionEnum> enumMap = PermissionEnum.getEnumMap();
        Set<String> permissionSet = enumMap.keySet();
        List<UserAppPermissionDto> children = new ArrayList<>();
        for (String permission : permissionSet) {
            UserAppPermissionDto appPermissionDto = new UserAppPermissionDto();
            appPermissionDto.setAppId(appId);
            appPermissionDto.setId(appId + TokenUtils.PERMISSION_SPACE_MARK + permission);
            appPermissionDto.setLabel(permission);
            children.add(appPermissionDto);
            if (userPermissionSet.contains(permission)) {
                checked.add(appId + TokenUtils.PERMISSION_SPACE_MARK + permission);
            }
        }
        userAppPermissionDto.setChildren(children);
    }

}
