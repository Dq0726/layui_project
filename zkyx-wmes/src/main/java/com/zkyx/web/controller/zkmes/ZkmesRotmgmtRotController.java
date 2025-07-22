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
import com.zkyx.domain.mysql.zkmes.ZkmesRotmgmtRot;
import com.zkyx.service.mysql.zkmes.IZkmesRotmgmtRotService;

/**
 * 税率操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/rotmgmt/rot")
public class ZkmesRotmgmtRotController extends BaseController
{
    @Autowired
    private IZkmesRotmgmtRotService rotService;

    /**
     * 获取税率列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:rotmgmt:rot:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesRotmgmtRot entity)
    { 
	   List<ZkmesRotmgmtRot> entitys = rotService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取税率列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:rotmgmt:rot:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesRotmgmtRot entity)
    {
        startPage();
        List<ZkmesRotmgmtRot> list = rotService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据税率编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:rotmgmt:rot:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(rotService.selectById(Id));
    }

    /**
     * 新增税率
     */
    @PreAuthorize("@ss.hasPermi('zkmes:rotmgmt:rot:add')")
    @Log(title = "税率", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesRotmgmtRot entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(rotService.insert(entity));
    }

    /**
     * 修改税率
     */
    @PreAuthorize("@ss.hasPermi('zkmes:rotmgmt:rot:edit')")
    @Log(title = "税率", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesRotmgmtRot entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(rotService.update(entity));
    }

    /**
     * 删除税率
     */
    @PreAuthorize("@ss.hasPermi('zkmes:rotmgmt:rot:remove')")
    @Log(title = "税率", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(rotService.deleteByIds(Ids));
    }
}
