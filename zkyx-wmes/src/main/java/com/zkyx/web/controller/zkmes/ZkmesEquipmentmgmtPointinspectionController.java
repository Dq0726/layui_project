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
import com.zkyx.domain.mysql.zkmes.ZkmesEquipmentmgmtPointinspection;
import com.zkyx.service.mysql.zkmes.IZkmesEquipmentmgmtPointinspectionService;

/**
 * 设备点检单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/equipmentmgmt/pointinspection")
public class ZkmesEquipmentmgmtPointinspectionController extends BaseController
{
    @Autowired
    private IZkmesEquipmentmgmtPointinspectionService pointinspectionService;

    /**
     * 获取设备点检单列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:pointinspection:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesEquipmentmgmtPointinspection entity)
    { 
	   List<ZkmesEquipmentmgmtPointinspection> entitys = pointinspectionService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取设备点检单列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:pointinspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesEquipmentmgmtPointinspection entity)
    {
        startPage();
        List<ZkmesEquipmentmgmtPointinspection> list = pointinspectionService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据设备点检单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:pointinspection:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(pointinspectionService.selectById(Id));
    }

    /**
     * 新增设备点检单
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:pointinspection:add')")
    @Log(title = "设备点检单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesEquipmentmgmtPointinspection entity)
    {
    	String ulid = UlidCreator.getMonotonicUlid().toString();
    	entity.setPointinspectionId(ulid);
    	
    	entity.setCreateBy(getUsername());
        return toAjax(pointinspectionService.insert(entity));
    }

    /**
     * 修改设备点检单
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:pointinspection:edit')")
    @Log(title = "设备点检单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesEquipmentmgmtPointinspection entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(pointinspectionService.update(entity));
    }

    /**
     * 删除设备点检单
     */
    @PreAuthorize("@ss.hasPermi('zkmes:equipmentmgmt:pointinspection:remove')")
    @Log(title = "设备点检单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(pointinspectionService.deleteByIds(Ids));
    }
}
