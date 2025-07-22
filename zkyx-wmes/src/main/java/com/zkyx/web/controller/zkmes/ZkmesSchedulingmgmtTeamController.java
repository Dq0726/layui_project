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
import com.zkyx.domain.mysql.zkmes.ZkmesSchedulingmgmtTeam;
import com.zkyx.service.mysql.zkmes.IZkmesSchedulingmgmtTeamService;

/**
 * 班组操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/schedulingmgmt/team")
public class ZkmesSchedulingmgmtTeamController extends BaseController
{
    @Autowired
    private IZkmesSchedulingmgmtTeamService teamService;

    /**
     * 获取班组列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:team:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesSchedulingmgmtTeam entity)
    {
        startPage();
        List<ZkmesSchedulingmgmtTeam> list = teamService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取班组列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:team:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesSchedulingmgmtTeam entity)
    { 
        List<ZkmesSchedulingmgmtTeam> list = teamService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据班组编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:team:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(teamService.selectById(Id));
    }

    /**
     * 新增班组
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:team:add')")
    @Log(title = "班组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesSchedulingmgmtTeam entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(teamService.insert(entity));
    }

    /**
     * 修改班组
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:team:edit')")
    @Log(title = "班组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesSchedulingmgmtTeam entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(teamService.update(entity));
    }

    /**
     * 删除班组
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:team:remove')")
    @Log(title = "班组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(teamService.deleteByIds(Ids));
    }
}
