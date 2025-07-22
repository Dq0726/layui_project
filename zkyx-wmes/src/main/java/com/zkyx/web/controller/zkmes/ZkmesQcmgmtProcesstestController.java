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
import com.zkyx.domain.mysql.zkmes.ZkmesQcmgmtProcesstest;
import com.zkyx.service.mysql.zkmes.IZkmesQcmgmtProcesstestService;

/**
 * 工序检验 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/qcmgmt/processtest")
public class ZkmesQcmgmtProcesstestController extends BaseController
{
 
    
    @Autowired
    private IZkmesQcmgmtProcesstestService processtestService;
  
    /**
     * 获取工序检验列表
     */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:processtest:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesQcmgmtProcesstest entity)
    {
        startPage();
        List<ZkmesQcmgmtProcesstest> list = processtestService.selectList(entity);
        return getDataTable(list);
    } 
  
     /**
      * 根据工序检验编号获取详细信息
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:processtest:query')")
     @GetMapping(value = "/{Id}")
     public AjaxResult getInfo(@PathVariable String Id)
     {

    	 return success(processtestService.selectById(Id));
     }

     /**
      * 新增工序检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:processtest:add')")
     @Log(title = "工序检验", businessType = BusinessType.INSERT)
     @PostMapping
     public AjaxResult add(@Validated @RequestBody ZkmesQcmgmtProcesstest entity)
     {
     	entity.setCreateBy(getUsername());
         return toAjax(processtestService.insert(entity));
     }

     /**
      * 修改工序检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:processtest:edit')")
     @Log(title = "工序检验", businessType = BusinessType.UPDATE)
     @PutMapping
     public AjaxResult edit(@Validated @RequestBody ZkmesQcmgmtProcesstest entity)
     {
     	entity.setUpdateBy(getUsername());
         return toAjax(processtestService.update(entity));
     }

     /**
      * 删除工序检验
      */
     @PreAuthorize("@ss.hasPermi('zkmes:qcmgmt:processtest:remove')")
     @Log(title = "工序检验", businessType = BusinessType.DELETE)
     @DeleteMapping("/{Ids}")
     public AjaxResult remove(@PathVariable String[] Ids)
     { 
         return toAjax(processtestService.deleteByIds(Ids));
     }
      
}
