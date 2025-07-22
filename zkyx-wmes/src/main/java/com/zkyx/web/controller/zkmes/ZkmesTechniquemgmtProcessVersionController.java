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
import com.zkyx.domain.mysql.zkmes.ZkmesTechniquemgmtProcessVersion;
import com.zkyx.service.mysql.zkmes.IZkmesTechniquemgmtProcessVersionService;

/**
 * 工序版本管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/techniquemgmt/process/version")
public class ZkmesTechniquemgmtProcessVersionController extends BaseController
{
    @Autowired
    private IZkmesTechniquemgmtProcessVersionService versionService;

    /**
     * 获取工序版本管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesTechniquemgmtProcessVersion entity)
    {
        startPage();
        List<ZkmesTechniquemgmtProcessVersion> list = versionService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取工序版本管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesTechniquemgmtProcessVersion entity)
    { 
        List<ZkmesTechniquemgmtProcessVersion> list = versionService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据工序版本管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(versionService.selectById(Id));
    }

    /**
     * 新增工序版本管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:add')")
    @Log(title = "工序版本管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesTechniquemgmtProcessVersion entity)
    {
    	entity.setVersionId(entity.getProcessId() + "-" + entity.getVersion());
    	entity.setCreateBy(getUsername());
        return toAjax(versionService.insert(entity));
    }

    /**
     * 修改工序版本管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:edit')")
    @Log(title = "工序版本管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesTechniquemgmtProcessVersion entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(versionService.update(entity));
    }

    /**
     * 删除工序版本管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:remove')")
    @Log(title = "工序版本管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(versionService.deleteByIds(Ids));
    }
}
