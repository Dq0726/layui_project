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
import com.zkyx.domain.mysql.zkmes.ZkmesWarehousemgmtWarehousearea;
import com.zkyx.service.mysql.zkmes.IZkmesWarehousemgmtWarehouseareaService;

/**
 * 库区管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/warehousemgmt/warehousearea")
public class ZkmesWarehousemgmtWarehouseareaController extends BaseController
{
    @Autowired
    private IZkmesWarehousemgmtWarehouseareaService warehouseareaService;

    /**
     * 获取库区管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehousearea:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesWarehousemgmtWarehousearea entity)
    {
        startPage();
        List<ZkmesWarehousemgmtWarehousearea> list = warehouseareaService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取库区管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehousearea:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesWarehousemgmtWarehousearea entity)
    { 
        List<ZkmesWarehousemgmtWarehousearea> list = warehouseareaService.selectList(entity);
        return success(list);
    }
    
    
    

    /**
     * 根据库区管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehousearea:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(warehouseareaService.selectById(Id));
    }

    /**
     * 新增库区管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehousearea:add')")
    @Log(title = "库区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesWarehousemgmtWarehousearea entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(warehouseareaService.insert(entity));
    }

    /**
     * 修改库区管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehousearea:edit')")
    @Log(title = "库区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesWarehousemgmtWarehousearea entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(warehouseareaService.update(entity));
    }

    /**
     * 删除库区管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehousearea:remove')")
    @Log(title = "库区管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(warehouseareaService.deleteByIds(Ids));
    }
}
