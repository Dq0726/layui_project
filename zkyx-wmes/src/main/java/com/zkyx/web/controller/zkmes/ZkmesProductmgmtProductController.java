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
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtClassify;
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtProduct;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtClassifyService;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtProductService;

/**
 * 产品信息 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productmgmt/product")
public class ZkmesProductmgmtProductController extends BaseController
{
    @Autowired
    private IZkmesProductmgmtClassifyService classifyService;
    
    @Autowired
    private IZkmesProductmgmtProductService productService;
 
    /**
     * 获取产品分类树列表
     */ 
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:product:list')")
    @GetMapping("/classifyTree")
    public AjaxResult classifyTree(ZkmesProductmgmtClassify entity)
    {  
        return success(classifyService.selectClassifyTreeList(entity));
    }
  
    /**
     * 获取产品信息列表
     */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesProductmgmtProduct entity)
    {
        startPage(); 
        List<ZkmesProductmgmtProduct> list = productService.selectListGroupByProductId(entity);
        return getDataTable(list);
    } 
  
     /**
      * 根据产品信息编号获取详细信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:product:query')")
     @GetMapping(value = "/{Id}")
     public AjaxResult getInfo(@PathVariable String Id)
     {

    	 return success(productService.selectById(Id));
     }

     /**
      * 新增产品信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:product:add')")
     @Log(title = "产品信息", businessType = BusinessType.INSERT)
     @PostMapping
     public AjaxResult add(@Validated @RequestBody ZkmesProductmgmtProduct entity)
     {
     	entity.setCreateBy(getUsername());
         return toAjax(productService.insert(entity));
     }

     /**
      * 修改产品信息型号
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:product:edit')")
     @Log(title = "产品信息", businessType = BusinessType.UPDATE)
     @PutMapping
     public AjaxResult edit(@Validated @RequestBody ZkmesProductmgmtProduct entity)
     {
     	entity.setUpdateBy(getUsername());
         return toAjax(productService.update(entity));
     }

     /**
      * 删除产品信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:product:remove')")
     @Log(title = "产品信息", businessType = BusinessType.DELETE)
     @DeleteMapping("/{Ids}")
     public AjaxResult remove(@PathVariable String[] Ids)
     { 
         return toAjax(productService.deleteByIds(Ids));
     }
     
     
     
}
