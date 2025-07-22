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
import com.zkyx.domain.mysql.zkmes.ZkmesSchedulingmgmtRsway;
import com.zkyx.service.mysql.zkmes.IZkmesSchedulingmgmtRswayService;

/**
 * 轮班方式操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/schedulingmgmt/rsway")
public class ZkmesSchedulingmgmtRswayController extends BaseController
{
    @Autowired
    private IZkmesSchedulingmgmtRswayService rswayService;

    /**
     * 获取轮班方式列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:rsway:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesSchedulingmgmtRsway entity)
    {
        startPage();
        List<ZkmesSchedulingmgmtRsway> list = rswayService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取轮班方式列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:rsway:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesSchedulingmgmtRsway entity)
    { 
        List<ZkmesSchedulingmgmtRsway> list = rswayService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据轮班方式编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:rsway:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(rswayService.selectById(Id));
    }

    /**
     * 新增轮班方式
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:rsway:add')")
    @Log(title = "轮班方式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesSchedulingmgmtRsway entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(rswayService.insert(entity));
    }

    /**
     * 修改轮班方式
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:rsway:edit')")
    @Log(title = "轮班方式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesSchedulingmgmtRsway entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(rswayService.update(entity));
    }

    /**
     * 删除轮班方式
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:rsway:remove')")
    @Log(title = "轮班方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(rswayService.deleteByIds(Ids));
    }
}
