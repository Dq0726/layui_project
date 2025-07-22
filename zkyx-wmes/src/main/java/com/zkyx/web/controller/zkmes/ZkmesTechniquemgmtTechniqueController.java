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
import com.zkyx.domain.mysql.zkmes.ZkmesTechniquemgmtTechnique;
import com.zkyx.service.mysql.zkmes.IZkmesTechniquemgmtTechniqueService;

/**
 * 工艺管理操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/techniquemgmt/technique")
public class ZkmesTechniquemgmtTechniqueController extends BaseController
{
    @Autowired
    private IZkmesTechniquemgmtTechniqueService techniqueService;

    /**
     * 获取工艺管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesTechniquemgmtTechnique entity)
    {
        startPage();
        List<ZkmesTechniquemgmtTechnique> list = techniqueService.selectList(entity);
        return getDataTable(list);
    }
    
    
    /**
    * 获取工艺管理列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesTechniquemgmtTechnique entity)
    { 
        List<ZkmesTechniquemgmtTechnique> list = techniqueService.selectList(entity);
        return success(list);
    }
    
    

    /**
     * 根据工艺管理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(techniqueService.selectById(Id));
    }

    /**
     * 新增工艺管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:add')")
    @Log(title = "工艺管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesTechniquemgmtTechnique entity)
    {
    	//String ulid = UlidCreator.getMonotonicUlid().toString();
    	
    	//entity.setWorkshopId(ulid);
    	entity.setCreateBy(getUsername());
        return toAjax(techniqueService.insert(entity));
    }

    /**
     * 修改工艺管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:edit')")
    @Log(title = "工艺管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesTechniquemgmtTechnique entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(techniqueService.update(entity));
    }

    /**
     * 删除工艺管理
     */
    @PreAuthorize("@ss.hasPermi('zkmes:techniquemgmt:technique:remove')")
    @Log(title = "工艺管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(techniqueService.deleteByIds(Ids));
    }
}
