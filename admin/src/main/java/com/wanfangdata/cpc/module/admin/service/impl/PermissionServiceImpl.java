package com.wanfangdata.cpc.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfangdata.cpc.common.util.CoreConst;
import com.wanfangdata.cpc.common.util.UUIDUtil;
import com.wanfangdata.cpc.module.admin.mapper.PermissionMapper;
import com.wanfangdata.cpc.module.admin.model.Permission;
import com.wanfangdata.cpc.module.admin.service.PermissionService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private static final Pattern SLASH_PATTERN = Pattern.compile("/");
    private final PermissionMapper permissionMapper;

    @Override
    public Set<String> findPermsByUserId(String userId) {
        return permissionMapper.findPermsByUserId(userId);
    }

    @Override
    public List<Permission> selectAll(Integer status) {
        return permissionMapper.selectAllPerms(status);
    }

    @Override
    public List<Permission> selectAllMenuName(Integer status) {
        return permissionMapper.selectAllMenuName(status);
    }

    @Override
    public List<Permission> selectMenuByUserId(String userId) {
        return permissionMapper.selectMenuByUserId(userId);
    }

    @Override
    public List<Permission> selectMenuTreeByUserId(String userId) {
        return buildPermissionTree(permissionMapper.selectMenuByUserId(userId));
    }

    @Override
    public int insert(Permission permission) {
        Date date = new Date();
        permission.setPermissionId(UUIDUtil.getUniqueIdByUUId());
        permission.setStatus(CoreConst.STATUS_VALID);
        permission.setCreateTime(date);
        permission.setUpdateTime(date);
        return permissionMapper.insert(permission);
    }

    @Override
    public int updateStatus(String permissionId, Integer status) {
        return permissionMapper.updateStatusByPermissionId(permissionId, status);
    }

    @Override
    public Permission findByPermissionId(String permissionId) {
        return permissionMapper.selectByPermissionId(permissionId);
    }

    @Override
    public Permission findById(Integer id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public int updateByPermissionId(Permission permission) {
        return permissionMapper.updateByPermissionId(permission);
    }

    @Override
    public int selectSubPermsByPermissionId(String permissionId) {
        return permissionMapper.selectSubPermsByPermissionId(permissionId);
    }

    private static List<Permission> buildPermissionTree(List<Permission> permissionList) {
        Map<Integer, List<Permission>> parentIdToPermissionListMap = permissionList.stream().map(p -> {
            if (StringUtils.startsWith(p.getUrl(), "/")) {
                p.setUrl(SLASH_PATTERN.matcher(p.getUrl()).replaceFirst("#"));
            }
            return p;
        }).collect(Collectors.groupingBy(Permission::getParentId));
        List<Permission> rootLevelPermissionList = parentIdToPermissionListMap.getOrDefault(CoreConst.TOP_MENU_ID, Collections.emptyList());
        fetchChildren(rootLevelPermissionList, parentIdToPermissionListMap);
        return rootLevelPermissionList;
    }

    private static void fetchChildren(List<Permission> permissionList, Map<Integer, List<Permission>> parentIdToPermissionListMap) {
        if (CollectionUtils.isEmpty(permissionList)) {
            return;
        }
        for (Permission permission : permissionList) {
            List<Permission> childrenList = parentIdToPermissionListMap.get(permission.getId());
            fetchChildren(childrenList, parentIdToPermissionListMap);
            permission.setChildren(childrenList);
        }
    }
}
