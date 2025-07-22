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
import com.zkyx.domain.mysql.zkmes.ZkmesWarehousemgmtPlace;
import com.zkyx.service.mysql.zkmes.IZkmesWarehousemgmtPlaceService;

/**
 * 库位管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/warehousemgmt/place")
public class ZkmesWarehousemgmtPlaceController extends BaseController
{
    @Autowired
    private IZkmesWarehousemgmtPlaceService placeService;

    /**
     * 获取库位管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:place:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesWarehousemgmtPlace entity)
    {
        startPage();
        List<ZkmesWarehousemgmtPlace> list = placeService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据库位管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:place:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(placeService.selectById(Id));
    }

    /**
     * 新增库位管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:place:add')")
    @Log(title = "库位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesWarehousemgmtPlace entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(placeService.insert(entity));
    }

    /**
     * 修改库位管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:place:edit')")
    @Log(title = "库位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesWarehousemgmtPlace entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(placeService.update(entity));
    }

    /**
     * 删除库位管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:warehousemgmt:place:remove')")
    @Log(title = "库位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(placeService.deleteByIds(Ids));
    }
}
