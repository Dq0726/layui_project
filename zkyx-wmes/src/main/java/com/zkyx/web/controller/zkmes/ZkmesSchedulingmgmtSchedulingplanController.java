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
import com.zkyx.domain.mysql.zkmes.ZkmesSchedulingmgmtSchedulingplan;
import com.zkyx.service.mysql.zkmes.IZkmesSchedulingmgmtSchedulingplanService;

/**
 * 排班计划操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/schedulingmgmt/schedulingplan")
public class ZkmesSchedulingmgmtSchedulingplanController extends BaseController
{
    @Autowired
    private IZkmesSchedulingmgmtSchedulingplanService schedulingplanService;

    /**
     * 获取排班计划列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:schedulingplan:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesSchedulingmgmtSchedulingplan entity)
    {
        startPage();
        List<ZkmesSchedulingmgmtSchedulingplan> list = schedulingplanService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取排班计划列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:schedulingplan:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesSchedulingmgmtSchedulingplan entity)
    { 
        List<ZkmesSchedulingmgmtSchedulingplan> list = schedulingplanService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据排班计划编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:schedulingplan:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(schedulingplanService.selectById(Id));
    }

    /**
     * 新增排班计划
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:schedulingplan:add')")
    @Log(title = "排班计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesSchedulingmgmtSchedulingplan entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(schedulingplanService.insert(entity));
    }

    /**
     * 修改排班计划
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:schedulingplan:edit')")
    @Log(title = "排班计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesSchedulingmgmtSchedulingplan entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(schedulingplanService.update(entity));
    }

    /**
     * 删除排班计划
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:schedulingplan:remove')")
    @Log(title = "排班计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(schedulingplanService.deleteByIds(Ids));
    }
}
