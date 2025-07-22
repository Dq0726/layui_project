package com.zkyx.web.controller.zkmes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkyx.common.annotation.Log;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtOutboundorder;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtOutboundorderProduct;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutboundorderProductService;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutboundorderService;

/**
 * 生产领料单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productionmgmt/pmr")
public class ZkmesProductionmgmtPmrController extends BaseController { 
	@Autowired
    private IZkmesInvmgmtOutboundorderService outboundorderService;
    
    @Autowired
	private IZkmesInvmgmtOutboundorderProductService outboundorderProductService;
    
	/**
	 * 获取生产领料单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:pmr:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesInvmgmtOutboundorder entity) {
		startPage();

		List<ZkmesInvmgmtOutboundorder> list = outboundorderService.selectList(entity);
		return getDataTable(list);
	}
 
    /**
    * 获取生产领料单列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:pmr:list')")
    @GetMapping("/list1")
    public AjaxResult list1(ZkmesInvmgmtOutboundorder entity)
    { 
    	List<ZkmesInvmgmtOutboundorder> list = outboundorderService.selectList(entity);
        return success(list);
    }
     
	/**
	 * 根据生产领料单编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:pmr:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(outboundorderService.selectById(Id));
	}
 
	/**
	 * 修改生产领料单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:pmr:edit')")
	@Log(title = "生产领料单", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtOutboundorder entity) {
		entity.setUpdateBy(getUsername());
	  
		return toAjax(outboundorderService.update(entity));
	}

	/**
	 * 删除生产领料单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:pmr:remove')")
	@Log(title = "生产领料单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(outboundorderService.deleteByIds(Ids));
	}

    // /product //////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取生产领料单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:pmr:product:list')")
	@GetMapping("/product/list")
	public TableDataInfo list(ZkmesInvmgmtOutboundorderProduct entity) { 
		List<ZkmesInvmgmtOutboundorderProduct> list = outboundorderProductService.selectList(entity);
		return getDataTable(list);
	} 
}
