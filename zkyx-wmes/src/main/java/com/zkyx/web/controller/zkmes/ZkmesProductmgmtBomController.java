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
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtBom;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtBomService;

/**
 * Bom信息 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productmgmt/bom")
public class ZkmesProductmgmtBomController extends BaseController
{ 
    
    @Autowired
    private IZkmesProductmgmtBomService bomService;
 
 
  
    /**
     * 获取Bom信息列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:bom:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesProductmgmtBom entity)
    {
        startPage();
        List<ZkmesProductmgmtBom> list = bomService.selectList(entity);
        return getDataTable(list);
    } 
  
     /**
      * 根据Bom信息编号获取详细信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:bom:query')")
     @GetMapping(value = "/{Id}")
     public AjaxResult getInfo(@PathVariable String Id)
     {

    	 return success(bomService.selectById(Id));
     }

     /**
      * 新增Bom信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:bom:add')")
     @Log(title = "Bom信息", businessType = BusinessType.INSERT)
     @PostMapping
     public AjaxResult add(@Validated @RequestBody ZkmesProductmgmtBom entity)
     {
    	  String ulid = UlidCreator.getMonotonicUlid().toString(); 
          entity.setBomId(ulid);
          entity.setCreateBy(getUsername());
          return toAjax(bomService.insert(entity));
     }

     /**
      * 修改Bom信息型号
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:bom:edit')")
     @Log(title = "Bom信息", businessType = BusinessType.UPDATE)
     @PutMapping
     public AjaxResult edit(@Validated @RequestBody ZkmesProductmgmtBom entity)
     {
     	entity.setUpdateBy(getUsername());
         return toAjax(bomService.update(entity));
     }

     /**
      * 删除Bom信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:bom:remove')")
     @Log(title = "Bom信息", businessType = BusinessType.DELETE)
     @DeleteMapping("/{Ids}")
     public AjaxResult remove(@PathVariable String[] Ids)
     { 
         return toAjax(bomService.deleteByIds(Ids));
     }
     
     
     
}
