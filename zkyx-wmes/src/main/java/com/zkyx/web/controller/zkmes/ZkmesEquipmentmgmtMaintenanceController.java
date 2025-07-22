package com.zkyx.web.controller.zkmes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.f4b6a3.ulid.UlidCreator;
import com.zkyx.common.annotation.Log;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.domain.mysql.zkmes.ZkmesEquipmentmgmtMaintenance;
import com.zkyx.service.mysql.zkmes.IZkmesEquipmentmgmtMaintenanceService;

/**
 * 设备点检单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/equipmentmgmt/maintenance")
public class ZkmesEquipmentmgmtMaintenanceController extends BaseController
{
    @Autowired
    private IZkmesEquipmentmgmtMaintenanceService maintenanceService;

    /**
     * 获取设备点检单列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:maintenance:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesEquipmentmgmtMaintenance entity)
    { 
	   List<ZkmesEquipmentmgmtMaintenance> entitys = maintenanceService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取设备点检单列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:maintenance:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesEquipmentmgmtMaintenance entity)
    {
        startPage();
        List<ZkmesEquipmentmgmtMaintenance> list = maintenanceService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据设备点检单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:maintenance:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(maintenanceService.selectById(Id));
    }

    /**
     * 新增设备点检单
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:maintenance:add')")
    @Log(title = "设备点检单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesEquipmentmgmtMaintenance entity)
    {
    	String ulid = UlidCreator.getMonotonicUlid().toString();
    	entity.setMaintenanceId(ulid);
    	
    	entity.setCreateBy(getUsername());
        return toAjax(maintenanceService.insert(entity));
    }

    /**
     * 修改设备点检单
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:maintenance:edit')")
    @Log(title = "设备点检单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesEquipmentmgmtMaintenance entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(maintenanceService.update(entity));
    }

    /**
     * 删除设备点检单
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:maintenance:remove')")
    @Log(title = "设备点检单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(maintenanceService.deleteByIds(Ids));
    }
}
