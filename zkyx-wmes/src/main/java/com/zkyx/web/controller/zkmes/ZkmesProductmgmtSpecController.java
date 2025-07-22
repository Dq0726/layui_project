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
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtSpec;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtSpecService;

/**
 * 产品规格操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productmgmt/spec")
public class ZkmesProductmgmtSpecController extends BaseController
{
    @Autowired
    private IZkmesProductmgmtSpecService specService;

    
    /**
     * 获取产品规格列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:spec:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesProductmgmtSpec entity)
    { 
	   List<ZkmesProductmgmtSpec> entitys = specService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取产品规格列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:spec:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesProductmgmtSpec entity)
    {
        startPage();
        List<ZkmesProductmgmtSpec> list = specService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据产品规格编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:spec:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(specService.selectById(Id));
    }

    /**
     * 新增产品规格
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:spec:add')")
    @Log(title = "产品规格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesProductmgmtSpec entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(specService.insert(entity));
    }

    /**
     * 修改产品规格
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:spec:edit')")
    @Log(title = "产品规格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesProductmgmtSpec entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(specService.update(entity));
    }

    /**
     * 删除产品规格
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:spec:remove')")
    @Log(title = "产品规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(specService.deleteByIds(Ids));
    }
}
