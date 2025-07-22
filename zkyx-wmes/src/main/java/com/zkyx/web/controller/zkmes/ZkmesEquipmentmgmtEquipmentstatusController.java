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
import com.zkyx.domain.mysql.zkmes.ZkmesEquipmentmgmtEquipmentstatus;
import com.zkyx.service.mysql.zkmes.IZkmesEquipmentmgmtEquipmentstatusService;

/**
 * 设备状态操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/equipmentmgmt/equipmentstatus")
public class ZkmesEquipmentmgmtEquipmentstatusController extends BaseController
{
    @Autowired
    private IZkmesEquipmentmgmtEquipmentstatusService equipmentstatusService;

    /**
     * 获取设备状态列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentstatus:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesEquipmentmgmtEquipmentstatus entity)
    { 
	   List<ZkmesEquipmentmgmtEquipmentstatus> entitys = equipmentstatusService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取设备状态列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentstatus:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesEquipmentmgmtEquipmentstatus entity)
    {
        startPage();
        List<ZkmesEquipmentmgmtEquipmentstatus> list = equipmentstatusService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据设备状态编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentstatus:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(equipmentstatusService.selectById(Id));
    }

    /**
     * 新增设备状态
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentstatus:add')")
    @Log(title = "设备状态", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesEquipmentmgmtEquipmentstatus entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(equipmentstatusService.insert(entity));
    }

    /**
     * 修改设备状态
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentstatus:edit')")
    @Log(title = "设备状态", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesEquipmentmgmtEquipmentstatus entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(equipmentstatusService.update(entity));
    }

    /**
     * 删除设备状态
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:equipmentstatus:remove')")
    @Log(title = "设备状态", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(equipmentstatusService.deleteByIds(Ids));
    }
}
