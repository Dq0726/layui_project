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
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtModel;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtModelService;

/**
 * 产品型号操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productmgmt/model")
public class ZkmesProductmgmtModelController extends BaseController
{
    @Autowired
    private IZkmesProductmgmtModelService modelService;

    
    /**
     * 获取产品型号列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:model:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesProductmgmtModel entity)
    { 
	   List<ZkmesProductmgmtModel> entitys = modelService.selectList(entity);
       
       return success(entitys); 
    }
    
    
    /**
     * 获取产品型号列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesProductmgmtModel entity)
    {
        startPage();
        List<ZkmesProductmgmtModel> list = modelService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据产品型号编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:model:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(modelService.selectById(Id));
    }

    /**
     * 新增产品型号
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:model:add')")
    @Log(title = "产品型号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesProductmgmtModel entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(modelService.insert(entity));
    }

    /**
     * 修改产品型号
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:model:edit')")
    @Log(title = "产品型号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesProductmgmtModel entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(modelService.update(entity));
    }

    /**
     * 删除产品型号
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:model:remove')")
    @Log(title = "产品型号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(modelService.deleteByIds(Ids));
    }
}
