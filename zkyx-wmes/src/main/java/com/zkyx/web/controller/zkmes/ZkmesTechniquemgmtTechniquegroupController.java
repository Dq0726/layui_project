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
import com.zkyx.domain.mysql.zkmes.ZkmesTechniquemgmtTechniquegroup;
import com.zkyx.service.mysql.zkmes.IZkmesTechniquemgmtTechniquegroupService;

/**
 * 工艺组管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/techniquemgmt/techniquegroup")
public class ZkmesTechniquemgmtTechniquegroupController extends BaseController
{
    @Autowired
    private IZkmesTechniquemgmtTechniquegroupService techniquegroupService;

    /**
     * 获取工艺组管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:techniquegroup:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesTechniquemgmtTechniquegroup entity)
    {
        startPage();
        List<ZkmesTechniquemgmtTechniquegroup> list = techniquegroupService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取工艺组管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:techniquegroup:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesTechniquemgmtTechniquegroup entity)
    { 
        List<ZkmesTechniquemgmtTechniquegroup> list = techniquegroupService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据工艺组管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:techniquegroup:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(techniquegroupService.selectById(Id));
    }

    /**
     * 新增工艺组管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:techniquegroup:add')")
    @Log(title = "工艺组管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesTechniquemgmtTechniquegroup entity)
    {
    	String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	entity.setTechniquegroupId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(techniquegroupService.insert(entity));
    }

    /**
     * 修改工艺组管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:techniquegroup:edit')")
    @Log(title = "工艺组管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesTechniquemgmtTechniquegroup entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(techniquegroupService.update(entity));
    }

    /**
     * 删除工艺组管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:techniquegroup:remove')")
    @Log(title = "工艺组管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(techniquegroupService.deleteByIds(Ids));
    }
}
