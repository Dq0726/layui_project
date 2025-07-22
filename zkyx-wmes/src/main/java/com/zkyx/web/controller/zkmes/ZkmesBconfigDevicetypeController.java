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
import com.zkyx.domain.mysql.zkmes.ZkmesBconfigDevicetype;
import com.zkyx.service.mysql.zkmes.IZkmesBconfigDevicetypeService;

/**
 * 设备类型操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/bconfig/devicetype")
public class ZkmesBconfigDevicetypeController extends BaseController
{
    @Autowired
    private IZkmesBconfigDevicetypeService devicetypeService;

    /**
     * 获取设备类型列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:devicetype:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesBconfigDevicetype entity)
    { 
	   List<ZkmesBconfigDevicetype> entitys = devicetypeService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取设备类型列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:devicetype:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesBconfigDevicetype entity)
    {
        startPage();
        List<ZkmesBconfigDevicetype> list = devicetypeService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据设备类型编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:devicetype:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(devicetypeService.selectById(Id));
    }

    /**
     * 新增设备类型
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:devicetype:add')")
    @Log(title = "设备类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesBconfigDevicetype entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(devicetypeService.insert(entity));
    }

    /**
     * 修改设备类型
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:devicetype:edit')")
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesBconfigDevicetype entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(devicetypeService.update(entity));
    }

    /**
     * 删除设备类型
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bconfig:devicetype:remove')")
    @Log(title = "设备类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(devicetypeService.deleteByIds(Ids));
    }
}
