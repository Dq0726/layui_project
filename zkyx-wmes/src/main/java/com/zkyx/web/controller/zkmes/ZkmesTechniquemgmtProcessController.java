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
import com.zkyx.domain.mysql.zkmes.ZkmesTechniquemgmtProcess;
import com.zkyx.service.mysql.zkmes.IZkmesTechniquemgmtProcessService;

/**
 * 工序管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/techniquemgmt/process")
public class ZkmesTechniquemgmtProcessController extends BaseController
{
    @Autowired
    private IZkmesTechniquemgmtProcessService processService;

    /**
     * 获取工序管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesTechniquemgmtProcess entity)
    {
        startPage();
        List<ZkmesTechniquemgmtProcess> list = processService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取工序管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesTechniquemgmtProcess entity)
    { 
        List<ZkmesTechniquemgmtProcess> list = processService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据工序管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(processService.selectById(Id));
    }

    /**
     * 新增工序管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:add')")
    @Log(title = "工序管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesTechniquemgmtProcess entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(processService.insert(entity));
    }

    /**
     * 修改工序管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:edit')")
    @Log(title = "工序管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesTechniquemgmtProcess entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(processService.update(entity));
    }

    /**
     * 删除工序管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:process:remove')")
    @Log(title = "工序管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(processService.deleteByIds(Ids));
    }
}
