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
import com.zkyx.domain.mysql.zkmes.ZkmesEnergymgmtChannel;
import com.zkyx.service.mysql.zkmes.IZkmesEnergymgmtChannelService;

/**
 * 能耗通道操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/energymgmt/channel")
public class ZkmesEnergymgmtChannelController extends BaseController
{
    @Autowired
    private IZkmesEnergymgmtChannelService energymgmtChannelService;

    /**
     * 获取能耗通道列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:channel:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesEnergymgmtChannel entity)
    { 
	   List<ZkmesEnergymgmtChannel> entitys = energymgmtChannelService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取能耗通道列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:channel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesEnergymgmtChannel entity)
    {
        startPage();
        List<ZkmesEnergymgmtChannel> list = energymgmtChannelService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据能耗通道编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:channel:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(energymgmtChannelService.selectById(Id));
    }

    /**
     * 新增能耗通道
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:channel:add')")
    @Log(title = "能耗通道", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesEnergymgmtChannel entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(energymgmtChannelService.insert(entity));
    }

    /**
     * 修改能耗通道
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:channel:edit')")
    @Log(title = "能耗通道", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesEnergymgmtChannel entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(energymgmtChannelService.update(entity));
    }

    /**
     * 删除能耗通道
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:channel:remove')")
    @Log(title = "能耗通道", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(energymgmtChannelService.deleteByIds(Ids));
    }
}
