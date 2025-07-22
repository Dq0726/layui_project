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
import com.zkyx.domain.mysql.zkmes.ZkmesSalesmgmtOrderproduct;
import com.zkyx.service.mysql.zkmes.IZkmesSalesmgmtOrderproductService;

/**
 * 报价单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/salesmgmt/orderproduct")
public class ZkmesSalesmgmtOrderproductController extends BaseController {

	 
 	@Autowired
 	private IZkmesSalesmgmtOrderproductService orderproductService;
	
//	@Autowired
//	private IZkmesInvmgmtInboundorderService inboundorderService;
 
	/**
	 * 获取报价单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:orderproduct:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesSalesmgmtOrderproduct entity) {
		startPage();
		List<ZkmesSalesmgmtOrderproduct> list = orderproductService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 获取报价单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:orderproduct:list')")
	@GetMapping("/list1")
	public AjaxResult list1(ZkmesSalesmgmtOrderproduct entity) {  
		List<ZkmesSalesmgmtOrderproduct> list = orderproductService.selectList(entity);
		return success(list);
	}
	
	/**
	 * 新增报价单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:orderproduct:add')")
	@Log(title = "报价单产品", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult addQuotationmgmt(@Validated @RequestBody ZkmesSalesmgmtOrderproduct entity) {
		String ulid = UlidCreator.getMonotonicUlid().toString();
		entity.setOrderproductId(ulid);
		entity.setCreateBy(getUsername());
		return toAjax(orderproductService.insert(entity));
	}

	/**
	 * 修改报价单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:orderproduct:edit')")
	@Log(title = "报价单产品", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesSalesmgmtOrderproduct entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(orderproductService.update(entity));
	}

	/**
	 * 删除报价单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:orderproduct:remove')")
	@Log(title = "报价单产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult productRemove(@PathVariable String[] Ids) {
		return toAjax(orderproductService.deleteByIds(Ids));
	}

	
 
}
