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
import com.zkyx.domain.tdsql.szgy.SzgyBdataEem;
import com.zkyx.service.tdsql.szgy.ISzgyBdataEemService;

/**
 * 生态流量大数据操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/bdata/eem")
public class ZkmesBdataEemController extends BaseController
{
    @Autowired
    private ISzgyBdataEemService eemService;

    /**
     * 获取生态流量大数据列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bdata:eem:list')")
    @GetMapping("/list")
    public TableDataInfo list(SzgyBdataEem data)
    {
        startPage();
        List<SzgyBdataEem> list = eemService.selectList(data);
        return getDataTable(list);
    }

    /**
     * 根据生态流量大数据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bdata:eem:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(eemService.selectById(Id));
    }

    /**
     * 新增生态流量大数据
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bdata:eem:add')")
    @Log(title = "生态流量大数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SzgyBdataEem data)
    { 
        return toAjax(eemService.insert(data));
    }
    
    /**
     * 修改生态流量大数据
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bdata:eem:edit')")
    @Log(title = "生态流量大数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SzgyBdataEem data)
    {
    	 return toAjax(eemService.insert(data));
    }

    

    /**
     * 删除生态流量大数据
     */
    @PreAuthorize("@ss.hasPermi('zkmes:bdata:eem:remove')")
    @Log(title = "生态流量大数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(eemService.deleteByIds(Ids));
    }
}
