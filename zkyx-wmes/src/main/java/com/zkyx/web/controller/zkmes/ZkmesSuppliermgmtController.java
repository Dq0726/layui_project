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
import com.zkyx.domain.mysql.zkmes.ZkmesSuppliermgmt;
import com.zkyx.service.mysql.zkmes.IZkmesSuppliermgmtService;

/**
 * 供应商管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/suppliermgmt")
public class ZkmesSuppliermgmtController extends BaseController
{
    @Autowired
    private IZkmesSuppliermgmtService suppliermgmtService;

    /**
     * 获取供应商管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:suppliermgmt:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesSuppliermgmt entity)
    {
        startPage();
        List<ZkmesSuppliermgmt> list = suppliermgmtService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 获取供应商管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:suppliermgmt:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesSuppliermgmt entity)
    { 
        List<ZkmesSuppliermgmt> list = suppliermgmtService.selectList(entity);
        return success(list);
    }

    
    
    /**
     * 根据供应商管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:suppliermgmt:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(suppliermgmtService.selectById(Id));
    }

    /**
     * 新增供应商管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:suppliermgmt:add')")
    @Log(title = "供应商管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesSuppliermgmt entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(suppliermgmtService.insert(entity));
    }

    /**
     * 修改供应商管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:suppliermgmt:edit')")
    @Log(title = "供应商管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesSuppliermgmt entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(suppliermgmtService.update(entity));
    }

    /**
     * 删除供应商管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:suppliermgmt:remove')")
    @Log(title = "供应商管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(suppliermgmtService.deleteByIds(Ids));
    }
}
