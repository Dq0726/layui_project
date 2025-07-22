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
import com.zkyx.common.constant.UserConstants;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.common.utils.StringUtils;
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtClassify;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtClassifyService;

/**
 * 产品分类操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productmgmt/classify")
public class ZkmesProductmgmtClassifyController extends BaseController
{
    @Autowired
    private IZkmesProductmgmtClassifyService classifyService;

    /**
     * 获取产品分类列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:classify:list')")
    @GetMapping("/list")
    public AjaxResult list(ZkmesProductmgmtClassify entity)
    { 
	   List<ZkmesProductmgmtClassify> entitys = classifyService.selectClassifyList(entity);
       
       return success(entitys); 
    }

    /**
     * 根据产品分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:classify:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable Long Id)
    {
        return success(classifyService.selectClassifyById(Id));
    }

    /**
     * 新增根产品分类
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:classify:addroot')")
    @Log(title = "产品分类", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addroot")
    public AjaxResult addroot(@Validated @RequestBody ZkmesProductmgmtClassify entity)
    {
        if (!classifyService.checkClassifyNameUnique(entity))
        {
            return error("新增根产品分类'" + entity.getClassifyName() + "'失败，根产品分类名称已存在");
        }
        entity.setCreateBy(getUsername());
        return toAjax(classifyService.insertRootClassify(entity));
    }
    
    /**
     * 新增产品分类
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:classify:add')")
    @Log(title = "产品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesProductmgmtClassify entity)
    {
    	 if (!classifyService.checkClassifyNameUnique(entity))
         {
             return error("新增产品分类'" + entity.getClassifyName() + "'失败，产品分类名称已存在");
         }
    	 entity.setCreateBy(getUsername());
         return toAjax(classifyService.insertClassify(entity));
    }

    /**
     * 修改产品分类
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:classify:edit')")
    @Log(title = "产品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesProductmgmtClassify entity)
    {
    	 Long classifyId = entity.getClassifyId();
    	 classifyService.checkClassifyDataScope(classifyId);
         if (!classifyService.checkClassifyNameUnique(entity))
         {
             return error("修改产品分类'" + entity.getClassifyName() + "'失败，产品分类名称已存在");
         }
         else if (entity.getParentId().equals(classifyId))
         {
             return error("修改产品分类'" + entity.getClassifyName() + "'失败，上级产品分类不能是自己");
         }
         else if (StringUtils.equals(UserConstants.DEPT_DISABLE, entity.getStatus()) && classifyService.selectNormalChildrenClassifyById(classifyId) > 0)
         {
             return error("该产品分类包含未停用的子产品分类！");
         }
         entity.setUpdateBy(getUsername());
         return toAjax(classifyService.updateClassify(entity));
    }
    
    
    
    /**
     * 删除分类
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:classify:remove')")
    @Log(title = "产品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{classifyId}")
    public AjaxResult remove(@PathVariable Long classifyId)
    {
        if (classifyService.hasChildByClassifyId(classifyId))
        {
            return warn("存在下级分类,不允许删除");
        }
        if (classifyService.checkClassifyExistProduct(classifyId))
        {
            return warn("分类存在产品,不允许删除");
        }
        classifyService.checkClassifyDataScope(classifyId);
        return toAjax(classifyService.deleteClassifyById(classifyId));
    }
    

  
}
