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
import com.zkyx.domain.mysql.zkmes.ZkmesEnergymgmtGateway;
import com.zkyx.service.mysql.zkmes.IZkmesEnergymgmtGatewayService;

/**
 * 能耗网关操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/energymgmt/gateway")
public class ZkmesEnergymgmtGatewayController extends BaseController
{
    @Autowired
    private IZkmesEnergymgmtGatewayService energymgmtGatewayService;

    /**
     * 获取能耗网关列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:gateway:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesEnergymgmtGateway entity)
    { 
	   List<ZkmesEnergymgmtGateway> entitys = energymgmtGatewayService.selectList(entity);
       
       return success(entitys); 
    }
    
    /**
     * 获取能耗网关列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:gateway:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesEnergymgmtGateway entity)
    {
        startPage();
        List<ZkmesEnergymgmtGateway> list = energymgmtGatewayService.selectList(entity);
        return getDataTable(list);
    }

    /**
     * 根据能耗网关编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:gateway:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(energymgmtGatewayService.selectById(Id));
    }

    /**
     * 新增能耗网关
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:gateway:add')")
    @Log(title = "能耗网关", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesEnergymgmtGateway entity)
    {
    	entity.setCreateBy(getUsername());
        return toAjax(energymgmtGatewayService.insert(entity));
    }

    /**
     * 修改能耗网关
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:gateway:edit')")
    @Log(title = "能耗网关", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesEnergymgmtGateway entity)
    {
    	entity.setUpdateBy(getUsername());
        return toAjax(energymgmtGatewayService.update(entity));
    }

    /**
     * 删除能耗网关
     */
    @PreAuthorize("@ss.hasPermi('zkmes:energymgmt:gateway:remove')")
    @Log(title = "能耗网关", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Id}")
    public AjaxResult remove(@PathVariable String Id)
    {
    	
    	if (energymgmtGatewayService.hasChildByGatewayId(Id))
        {
            return warn("存在通道节点,不允许删除");
        }
    	
    	return toAjax(energymgmtGatewayService.deleteById(Id)); 
    }
}
