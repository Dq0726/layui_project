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
import com.zkyx.domain.mysql.zkmes.ZkmesQcmgmtProducttest;
import com.zkyx.service.mysql.zkmes.IZkmesQcmgmtProducttestService;

/**
 * 成品检验 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/qcmgmt/producttest")
public class ZkmesQcmgmtProducttestController extends BaseController
{
 
    
    @Autowired
    private IZkmesQcmgmtProducttestService producttestService;
  
    /**
     * 获取成品检验列表
     */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:producttest:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesQcmgmtProducttest entity)
    {
        startPage();
        List<ZkmesQcmgmtProducttest> list = producttestService.selectList(entity);
        return getDataTable(list);
    } 
  
     /**
      * 根据成品检验编号获取详细信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:producttest:query')")
     @GetMapping(value = "/{Id}")
     public AjaxResult getInfo(@PathVariable String Id)
     {

    	 return success(producttestService.selectById(Id));
     }

     /**
      * 新增成品检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:producttest:add')")
     @Log(title = "成品检验", businessType = BusinessType.INSERT)
     @PostMapping
     public AjaxResult add(@Validated @RequestBody ZkmesQcmgmtProducttest entity)
     {
     	entity.setCreateBy(getUsername());
         return toAjax(producttestService.insert(entity));
     }

     /**
      * 修改成品检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:producttest:edit')")
     @Log(title = "成品检验", businessType = BusinessType.UPDATE)
     @PutMapping
     public AjaxResult edit(@Validated @RequestBody ZkmesQcmgmtProducttest entity)
     {
     	entity.setUpdateBy(getUsername());
         return toAjax(producttestService.update(entity));
     }

     /**
      * 删除成品检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:producttest:remove')")
     @Log(title = "成品检验", businessType = BusinessType.DELETE)
     @DeleteMapping("/{Ids}")
     public AjaxResult remove(@PathVariable String[] Ids)
     { 
         return toAjax(producttestService.deleteByIds(Ids));
     }
      
}
