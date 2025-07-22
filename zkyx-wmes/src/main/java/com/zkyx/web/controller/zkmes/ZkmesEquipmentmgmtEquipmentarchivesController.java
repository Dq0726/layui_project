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

import com.zkyx.common.annotation.Log;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.domain.mysql.zkmes.ZkmesEquipmentmgmtEquipmentarchives;
import com.zkyx.service.mysql.zkmes.IZkmesEquipmentmgmtEquipmentarchivesService;

/**
 * 设备档案操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/equipmentmgmt/equipmentarchives")
public class ZkmesEquipmentmgmtEquipmentarchivesController extends BaseController
{
    @Autowired
    private IZkmesEquipmentmgmtEquipmentarchivesService equipmentarchivesService;

    /**
     * 获取设备档案列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentarchives:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesEquipmentmgmtEquipmentarchives entity)
    { 
	   List<ZkmesEquipmentmgmtEquipmentarchives> entitys = equipmentarchivesService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取设备档案列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentarchives:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesEquipmentmgmtEquipmentarchives entity)
    {
        startPage();
        List<ZkmesEquipmentmgmtEquipmentarchives> list = equipmentarchivesService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据设备档案编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentarchives:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(equipmentarchivesService.selectById(Id));
    }

    /**
     * 新增设备档案
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentarchives:add')")
    @Log(title = "设备档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesEquipmentmgmtEquipmentarchives entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(equipmentarchivesService.insert(entity));
    }

    /**
     * 修改设备档案
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentarchives:edit')")
    @Log(title = "设备档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesEquipmentmgmtEquipmentarchives entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(equipmentarchivesService.update(entity));
    }

    /**
     * 删除设备档案
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentarchives:remove')")
    @Log(title = "设备档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(equipmentarchivesService.deleteByIds(Ids));
    }
}
