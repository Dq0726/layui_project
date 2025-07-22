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
import com.zkyx.domain.mysql.zkmes.ZkmesWorkshopmgmtWorkshop;
import com.zkyx.service.mysql.zkmes.IZkmesWorkshopmgmtWorkshopService;

/**
 * 车间管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/workshopmgmt/workshop")
public class ZkmesWorkshopmgmtWorkshopController extends BaseController
{
    @Autowired
    private IZkmesWorkshopmgmtWorkshopService workshopService;

    /**
     * 获取车间管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workshop:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesWorkshopmgmtWorkshop entity)
    {
        startPage();
        List<ZkmesWorkshopmgmtWorkshop> list = workshopService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取车间管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workshop:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesWorkshopmgmtWorkshop entity)
    { 
        List<ZkmesWorkshopmgmtWorkshop> list = workshopService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据车间管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workshop:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(workshopService.selectById(Id));
    }

    /**
     * 新增车间管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workshop:add')")
    @Log(title = "车间管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesWorkshopmgmtWorkshop entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(workshopService.insert(entity));
    }

    /**
     * 修改车间管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workshop:edit')")
    @Log(title = "车间管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesWorkshopmgmtWorkshop entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(workshopService.update(entity));
    }

    /**
     * 删除车间管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:workshopmgmt:workshop:remove')")
    @Log(title = "车间管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(workshopService.deleteByIds(Ids));
    }
}
