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
import com.zkyx.domain.mysql.zkmes.ZkmesWorkshopmgmtWorkstation;
import com.zkyx.service.mysql.zkmes.IZkmesWorkshopmgmtWorkstationService;

/**
 * 车间管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/workshopmgmt/workstation")
public class ZkmesWorkshopmgmtWorkstationController extends BaseController
{
    @Autowired
    private IZkmesWorkshopmgmtWorkstationService workstationService;

    /**
     * 获取车间管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workstation:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesWorkshopmgmtWorkstation entity)
    {
        startPage();
        List<ZkmesWorkshopmgmtWorkstation> list = workstationService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取车间管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workstation:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesWorkshopmgmtWorkstation entity)
    { 
        List<ZkmesWorkshopmgmtWorkstation> list = workstationService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据车间管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workstation:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(workstationService.selectById(Id));
    }

    /**
     * 新增车间管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workstation:add')")
    @Log(title = "车间管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesWorkshopmgmtWorkstation entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(workstationService.insert(entity));
    }

    /**
     * 修改车间管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workstation:edit')")
    @Log(title = "车间管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesWorkshopmgmtWorkstation entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(workstationService.update(entity));
    }

    /**
     * 删除车间管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workstation:remove')")
    @Log(title = "车间管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(workstationService.deleteByIds(Ids));
    }
}
