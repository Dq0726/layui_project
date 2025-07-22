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
import com.zkyx.domain.mysql.zkmes.ZkmesTechniquemgmtTechniqueVersion;
import com.zkyx.service.mysql.zkmes.IZkmesTechniquemgmtTechniqueVersionService;

/**
 * 工艺版本操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/techniquemgmt/technique/version")
public class ZkmesTechniquemgmtTechniqueVersionController extends BaseController
{
    @Autowired
    private IZkmesTechniquemgmtTechniqueVersionService techniqueVersionService;

    /**
     * 获取工艺版本列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesTechniquemgmtTechniqueVersion entity)
    {
        startPage();
        List<ZkmesTechniquemgmtTechniqueVersion> list = techniqueVersionService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取工艺版本列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesTechniquemgmtTechniqueVersion entity)
    { 
        List<ZkmesTechniquemgmtTechniqueVersion> list = techniqueVersionService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据工艺版本编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(techniqueVersionService.selectById(Id));
    }

    /**
     * 新增工艺版本
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:add')")
    @Log(title = "工艺版本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesTechniquemgmtTechniqueVersion entity)
    {
    	entity.setVersionId(entity.getTechniqueId() + "-" + entity.getVersion());
    	entity.setCreateBy(getUsername());
        return toAjax(techniqueVersionService.insert(entity));
    }

    /**
     * 修改工艺版本
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:edit')")
    @Log(title = "工艺版本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesTechniquemgmtTechniqueVersion entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(techniqueVersionService.update(entity));
    }

    /**
     * 删除工艺版本
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:remove')")
    @Log(title = "工艺版本", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(techniqueVersionService.deleteByIds(Ids));
    }
}
