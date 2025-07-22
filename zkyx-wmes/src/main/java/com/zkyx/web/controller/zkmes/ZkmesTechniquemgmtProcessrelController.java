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
import com.zkyx.domain.mysql.zkmes.ZkmesTechniquemgmtProcessrel;
import com.zkyx.service.mysql.zkmes.IZkmesTechniquemgmtProcessrelService;

/**
 * 工序关系操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/techniquemgmt/processrel")
public class ZkmesTechniquemgmtProcessrelController extends BaseController
{
    @Autowired
    private IZkmesTechniquemgmtProcessrelService processrelService;

    /**
     * 获取工序关系列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:processrel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesTechniquemgmtProcessrel entity)
    {
        startPage();
        List<ZkmesTechniquemgmtProcessrel> list = processrelService.selectList(entity);
        return getDataTable(list);
    }
     
    /**
    * 获取工序关系列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:processrel:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesTechniquemgmtProcessrel entity)
    { 
        List<ZkmesTechniquemgmtProcessrel> list = processrelService.selectList(entity);
        return success(list);
    }
     
    /**
     * 根据工序关系编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:processrel:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(processrelService.selectById(Id));
    }

    /**
     * 新增工序关系
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:processrel:add')")
    @Log(title = "工序关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesTechniquemgmtProcessrel entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(processrelService.insert(entity));
    }

    /**
     * 修改工序关系
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:processrel:edit')")
    @Log(title = "工序关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesTechniquemgmtProcessrel entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(processrelService.update(entity));
    }

    /**
     * 删除工序关系
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:processrel:remove')")
    @Log(title = "工序关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(processrelService.deleteByIds(Ids));
    }
}
