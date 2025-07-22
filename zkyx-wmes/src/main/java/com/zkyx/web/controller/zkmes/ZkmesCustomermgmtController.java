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
import com.zkyx.domain.mysql.zkmes.ZkmesCustomermgmt;
import com.zkyx.service.mysql.zkmes.IZkmesCustomermgmtService;

/**
 * 客户管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/customermgmt")
public class ZkmesCustomermgmtController extends BaseController
{
    @Autowired
    private IZkmesCustomermgmtService customermgmtService;

    /**
     * 获取客户管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:customermgmt:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesCustomermgmt entity)
    {
        startPage();
        List<ZkmesCustomermgmt> list = customermgmtService.selectList(entity);
        return getDataTable(list);
    }

    
    /**
     *  获取客户管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:customermgmt:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesCustomermgmt entity)
    { 
        List<ZkmesCustomermgmt> list = customermgmtService.selectList(entity);
        return success(list);
    }

    
    
    /**
     * 根据客户管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:customermgmt:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(customermgmtService.selectById(Id));
    }

    /**
     * 新增客户管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:customermgmt:add')")
    @Log(title = "客户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesCustomermgmt entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(customermgmtService.insert(entity));
    }

    /**
     * 修改客户管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:customermgmt:edit')")
    @Log(title = "客户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesCustomermgmt entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(customermgmtService.update(entity));
    }

    /**
     * 删除客户管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:customermgmt:remove')")
    @Log(title = "客户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(customermgmtService.deleteByIds(Ids));
    }
}
