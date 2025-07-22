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
import com.zkyx.domain.mysql.zkmes.ZkmesBconfigUom;
import com.zkyx.service.mysql.zkmes.IZkmesBconfigUomService;

/**
 * 计量单位操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/bconfig/uom")
public class ZkmesBconfigUomController extends BaseController
{
    @Autowired
    private IZkmesBconfigUomService uomService;

    /**
     * 获取计量单位列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:uom:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesBconfigUom entity)
    { 
	   List<ZkmesBconfigUom> entitys = uomService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取计量单位列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:uom:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesBconfigUom entity)
    {
        startPage();
        List<ZkmesBconfigUom> list = uomService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据计量单位编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:uom:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(uomService.selectById(Id));
    }

    /**
     * 新增计量单位
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:uom:add')")
    @Log(title = "计量单位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesBconfigUom entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(uomService.insert(entity));
    }

    /**
     * 修改计量单位
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:uom:edit')")
    @Log(title = "计量单位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesBconfigUom entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(uomService.update(entity));
    }

    /**
     * 删除计量单位
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:uom:remove')")
    @Log(title = "计量单位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(uomService.deleteByIds(Ids));
    }
}
