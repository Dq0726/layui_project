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
import com.zkyx.domain.mysql.zkmes.ZkmesSchedulingmgmtSwway;
import com.zkyx.service.mysql.zkmes.IZkmesSchedulingmgmtSwwayService;

/**
 * 倒班方式操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/schedulingmgmt/swway")
public class ZkmesSchedulingmgmtSwwayController extends BaseController
{
    @Autowired
    private IZkmesSchedulingmgmtSwwayService swwayService;

    /**
     * 获取倒班方式列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:swway:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesSchedulingmgmtSwway entity)
    {
        startPage();
        List<ZkmesSchedulingmgmtSwway> list = swwayService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取倒班方式列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:swway:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesSchedulingmgmtSwway entity)
    { 
        List<ZkmesSchedulingmgmtSwway> list = swwayService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据倒班方式编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:swway:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(swwayService.selectById(Id));
    }

    /**
     * 新增倒班方式
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:swway:add')")
    @Log(title = "倒班方式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesSchedulingmgmtSwway entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(swwayService.insert(entity));
    }

    /**
     * 修改倒班方式
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:swway:edit')")
    @Log(title = "倒班方式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesSchedulingmgmtSwway entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(swwayService.update(entity));
    }

    /**
     * 删除倒班方式
     */
    @PreAuthorize("@ss.hasPermi('zkmes:schedulingmgmt:swway:remove')")
    @Log(title = "倒班方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(swwayService.deleteByIds(Ids));
    }
}
