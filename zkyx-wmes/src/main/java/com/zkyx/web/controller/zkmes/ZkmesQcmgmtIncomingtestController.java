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
import com.zkyx.domain.mysql.zkmes.ZkmesQcmgmtIncomingtest;
import com.zkyx.domain.mysql.zkmes.ZkmesQcmgmtIncomingtestTests;
import com.zkyx.service.mysql.zkmes.IZkmesQcmgmtIncomingtestService;
import com.zkyx.service.mysql.zkmes.IZkmesQcmgmtIncomingtestTestsService;

/**
 * 来料检验 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/qcmgmt/incomingtest")
public class ZkmesQcmgmtIncomingtestController extends BaseController
{
 
    
    @Autowired
    private IZkmesQcmgmtIncomingtestService incomingtestService;
    
    @Autowired
    private IZkmesQcmgmtIncomingtestTestsService incomingtestTestsService;
 
    
    /**
     * 获取来料检验列表
     */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesQcmgmtIncomingtest entity)
    {
        startPage();
        List<ZkmesQcmgmtIncomingtest> list = incomingtestService.selectList(entity);
        return getDataTable(list);
    } 
  
     /**
      * 根据来料检验编号获取详细信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:query')")
     @GetMapping(value = "/{Id}")
     public AjaxResult getInfo(@PathVariable String Id)
     {

    	 return success(incomingtestService.selectById(Id));
     }

     /**
      * 新增来料检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:add')")
     @Log(title = "来料检验", businessType = BusinessType.INSERT)
     @PostMapping
     public AjaxResult add(@Validated @RequestBody ZkmesQcmgmtIncomingtest entity)
     {
     	entity.setCreateBy(getUsername());
         return toAjax(incomingtestService.insert(entity));
     }

     /**
      * 修改来料检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:edit')")
     @Log(title = "来料检验", businessType = BusinessType.UPDATE)
     @PutMapping
     public AjaxResult edit(@Validated @RequestBody ZkmesQcmgmtIncomingtest entity)
     {
     	entity.setUpdateBy(getUsername());
         return toAjax(incomingtestService.update(entity));
     }

     /**
      * 删除来料检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:remove')")
     @Log(title = "来料检验", businessType = BusinessType.DELETE)
     @DeleteMapping("/{Ids}")
     public AjaxResult remove(@PathVariable String[] Ids)
     { 
         return toAjax(incomingtestService.deleteByIds(Ids));
     }
     
     
     
     
     
   //来料检验单 //////////////////////////////////////////////////////  
     
      
     
     /**
      * 获取来料检验单列表
      */
      @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:tests:list')")
     @GetMapping("/tests/list")
     public TableDataInfo testsList(ZkmesQcmgmtIncomingtestTests entity)
     {
         startPage();
         List<ZkmesQcmgmtIncomingtestTests> list = incomingtestTestsService.selectList(entity);
         return getDataTable(list);
     } 
   
     
  	/**
  	 * 获取采购单列表
  	 */
  	@PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:tests:list')")
  	@GetMapping("/tests/list1")
  	public TableDataInfo list1(ZkmesQcmgmtIncomingtestTests entity) {
  		List<ZkmesQcmgmtIncomingtestTests> list = incomingtestTestsService.selectList(entity);
  		return getDataTable(list);
  	}
      
      
    /**
     * 修改来料检验单
     */
    @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:incomingtest:tests:edit')")
    @Log(title = "来料检验单", businessType = BusinessType.UPDATE)
    @PostMapping("/tests")
    public AjaxResult editTests(@Validated @RequestBody ZkmesQcmgmtIncomingtestTests entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(incomingtestTestsService.insert(entity));
    }
      
      
}
