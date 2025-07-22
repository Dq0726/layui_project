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
import com.zkyx.domain.mysql.zkmes.ZkmesWarehousemgmtWarehouse;
import com.zkyx.service.mysql.zkmes.IZkmesWarehousemgmtWarehouseService;

/**
 * 仓库管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/warehousemgmt/warehouse")
public class ZkmesWarehousemgmtWarehouseController extends BaseController
{
    @Autowired
    private IZkmesWarehousemgmtWarehouseService warehouseService;

    /**
     * 获取仓库管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesWarehousemgmtWarehouse entity)
    {
        startPage();
        List<ZkmesWarehousemgmtWarehouse> list = warehouseService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取仓库管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehouse:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesWarehousemgmtWarehouse entity)
    { 
        List<ZkmesWarehousemgmtWarehouse> list = warehouseService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据仓库管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehouse:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(warehouseService.selectById(Id));
    }

    /**
     * 新增仓库管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehouse:add')")
    @Log(title = "仓库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesWarehousemgmtWarehouse entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(warehouseService.insert(entity));
    }

    /**
     * 修改仓库管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehouse:edit')")
    @Log(title = "仓库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesWarehousemgmtWarehouse entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(warehouseService.update(entity));
    }

    /**
     * 删除仓库管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:warehouse:remove')")
    @Log(title = "仓库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(warehouseService.deleteByIds(Ids));
    }
}
