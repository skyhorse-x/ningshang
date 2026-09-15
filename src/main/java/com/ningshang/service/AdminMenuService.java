package com.ningshang.service;
import com.ningshang.entity.AdminMenu;
import com.ningshang.repository.AdminMenuRepository;
import com.ningshang.repository.AdminGroupMenuRepository;
import com.ningshang.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminMenuService {
    @Autowired private AdminMenuRepository menuRepository;
    @Autowired private AdminGroupMenuRepository groupMenuRepository;
    public List<AdminMenu> tree() { return buildTree(listAll()); }
    private List<AdminMenu> buildTree(List<AdminMenu> all) {
        all.sort(Comparator.comparing(AdminMenu::getSortOrder, Comparator.nullsLast(Integer::compareTo)).thenComparing(AdminMenu::getId));
        Map<Long,AdminMenu> roots = new LinkedHashMap<>();
        for (AdminMenu m : all) {
            m.setChildren(new ArrayList<>());
            if (Long.valueOf(0).equals(m.getParentId())) roots.put(m.getId(),m);
        }
        for (AdminMenu m : all) {
            AdminMenu parent = roots.get(m.getParentId());
            if (parent != null) parent.getChildren().add(m);
        }
        // 管理端必须能看到空分组及独立菜单，才能添加子项。
        return new ArrayList<>(roots.values());
    }
    public List<AdminMenu> treeByMenuIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<AdminMenu> all = listAll();
        Set<Long> visible = new HashSet<>(ids);
        for (AdminMenu m : all) if (ids.contains(m.getId())) visible.add(m.getParentId());
        List<AdminMenu> tree = buildTree(all.stream().filter(m -> visible.contains(m.getId()) && Integer.valueOf(1).equals(m.getStatus())).collect(Collectors.toList()));
        return tree.stream().filter(m -> !m.getChildren().isEmpty() || (ids.contains(m.getId()) && m.getPath() != null && !m.getPath().isEmpty())).collect(Collectors.toList());
    }
    public List<AdminMenu> listAll() { return menuRepository.findAllByOrderByParentIdAscSortOrderAsc(); }
    @Transactional
    public AdminMenu save(AdminMenu menu) {
        if (menu.getId() != null) menu.setCreatedAt(findById(menu.getId()).getCreatedAt());
        if (menu.getParentId() == null) throw new BusinessException(400, "父菜单不能为空");
        if (menu.getParentId() != 0) {
            AdminMenu parent = findById(menu.getParentId());
            if (parent.getParentId() != 0 || parent.getId().equals(menu.getId())
                || (menu.getId() != null && menuRepository.countByParentId(menu.getId()) > 0)) throw new BusinessException(400, "只支持两级菜单，不能将分组设为子菜单");
        }
        return menuRepository.save(menu);
    }
    @Transactional
    public void delete(Long id) { batchDelete(Collections.singletonList(id)); }
    @Transactional
    public void batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.contains(null)) throw new BusinessException(400, "请选择要删除的菜单");
        Set<Long> remove = new LinkedHashSet<>(ids);
        for (Long id : ids) {
            findById(id);
            for (AdminMenu child : menuRepository.findByParentIdOrderBySortOrderAsc(id)) remove.add(child.getId());
        }
        groupMenuRepository.deleteByMenuIdIn(new ArrayList<>(remove));
        menuRepository.deleteAllById(remove);
    }
    public AdminMenu findById(Long id) { return menuRepository.findById(id).orElseThrow(() -> new BusinessException(404, "菜单不存在")); }
}
