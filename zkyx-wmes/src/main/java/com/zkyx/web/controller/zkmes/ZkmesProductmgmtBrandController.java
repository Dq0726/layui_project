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
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtBrand;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtBrandService;

/**
 * 产品品牌操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productmgmt/brand")
public class ZkmesProductmgmtBrandController extends BaseController
{
    @Autowired
    private IZkmesProductmgmtBrandService brandService;

    
    /**
     * 获取产品品牌列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:brand:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesProductmgmtBrand entity)
    { 
	   List<ZkmesProductmgmtBrand> entitys = brandService.selectList(entity);
       
       return success(entitys); 
    }
    
    
    /**
     * 获取产品品牌列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:brand:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesProductmgmtBrand entity)
    {
        startPage();
        List<ZkmesProductmgmtBrand> list = brandService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据产品品牌编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:brand:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(brandService.selectById(Id));
    }

    /**
     * 新增产品品牌
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:brand:add')")
    @Log(title = "产品品牌", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesProductmgmtBrand entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(brandService.insert(entity));
    }

    /**
     * 修改产品品牌
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:brand:edit')")
    @Log(title = "产品品牌", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesProductmgmtBrand entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(brandService.update(entity));
    }

    /**
     * 删除产品品牌
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:brand:remove')")
    @Log(title = "产品品牌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(brandService.deleteByIds(Ids));
    }
}
