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

import com.github.f4b6a3.ulid.UlidCreator;
import com.zkyx.common.annotation.Log;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtOutbatch;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutbatchService;

/**
 * 出库批次操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/invmgmt/outbatch")
public class ZkmesInvmgmtOutbatchController extends BaseController {


    @Autowired
    private IZkmesInvmgmtOutbatchService outbatchService;

	/**
	 * 获取出库批次列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outbatch:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesInvmgmtOutbatch entity) {
		startPage();
 
		List<ZkmesInvmgmtOutbatch> list = outbatchService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 根据出库批次编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outbatch:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(outbatchService.selectById(Id));
	} 
	    
	/**
	 * 新增出库批次
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outbatch:add')")
	@Log(title = "出库批次", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody ZkmesInvmgmtOutbatch entity) { 
		 String ulid = UlidCreator.getMonotonicUlid().toString();
	     entity.setOutbatchId(ulid); 
		return toAjax(outbatchService.insert(entity));
	}
	
	/**
	 * 修改出库批次
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outbatch:edit')")
	@Log(title = "出库批次", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtOutbatch entity) { 
		
		return toAjax(outbatchService.update(entity));
	}
	
	/**
	 * 删除出库批次
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outbatch:remove')")
	@Log(title = "出库批次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) { 
		return toAjax(outbatchService.deleteByIds(Ids));
	}
	
	
	
	
	
	/**
	 * 出库批次同步
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:product:sync')")
	@Log(title = "出库批次", businessType = BusinessType.UPDATE)
	@PostMapping("/sync")
	public AjaxResult sync(@Validated @RequestBody ZkmesInvmgmtOutbatch entity) {
		// 
		 
		
		
		

	//	String updateBy = getUsername();

	//	float inboundQty = Float.parseFloat(szInboundQty);

	//	int result = inoutInboundService.updateInboundorderproductStatusAndProductnum(inboundorderId, inboundorderproductId, productId, warehouseId, 
	//			inboundQty, "1", updateBy);

//		if (result == 1) {
	//		return success("同步成功");
	//	} else if (result == 2) {
	//		return success("已同步");
	//	}

		return error();
	} 
	
	
	
	
	
	
	
}
