package com.zkyx.web.controller.zkmes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkyx.common.annotation.Log;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtInboundorder;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtInboundorderProduct;
import com.zkyx.domain.mysql.zkmes.ZkmesQcmgmtIncomingtest;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtInboundorderProductService;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtInboundorderService;
import com.zkyx.service.mysql.zkmes.IZkmesQcmgmtIncomingtestService;

/**
 * 采购入库操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/purchasemgmt/purchasein")
public class ZkmesPurchasemgmtPurchaseinController extends BaseController {

	@Autowired
	private IZkmesInvmgmtInboundorderService inboundorderService;

	@Autowired
	private IZkmesInvmgmtInboundorderProductService inboundorderProductService;
	
    @Autowired
    private IZkmesQcmgmtIncomingtestService incomingtestService;

	/**
	 * 获取采购入库列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchasein:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesInvmgmtInboundorder entity) {
		startPage();

		entity.setIotId("1");
		
		List<ZkmesInvmgmtInboundorder> list = inboundorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 获取采购入库列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchasein:list')")
	@GetMapping("/list1")
	public AjaxResult list1(ZkmesInvmgmtInboundorder entity) {
		List<ZkmesInvmgmtInboundorder> list = inboundorderService.selectList(entity);
		return success(list);
	}

	/**
	 * 根据采购入库编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchasein:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(inboundorderService.selectById(Id));
	}

	/**
	 * 删除采购入库列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:remove')")
	@Log(title = "采购入库列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(inboundorderService.deleteByIds(Ids));
	}
	 

	//product  /////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取采购入库产品列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchasein:product:list')")
	@GetMapping("/product/list")
	public TableDataInfo productlist(ZkmesInvmgmtInboundorderProduct entity) {
		startPage();
 
		List<ZkmesInvmgmtInboundorderProduct> list = inboundorderProductService.selectList(entity);
		return getDataTable(list);
	}
  
	
	/**
	 * 获取采购入库产品列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchasein:product:list')")
	@GetMapping("/product/list1")
	public AjaxResult productlist1(ZkmesInvmgmtInboundorderProduct entity) {
	 
		List<ZkmesInvmgmtInboundorderProduct> list = inboundorderProductService.selectList(entity);
		return success(list);
	}
	
	
	//tests  /////////////////////////////////////////////////////////////////////////////////
	/**
	 * 生成来料检验单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchasein:tests')")
	@PostMapping("/tests/{Id}")
	public AjaxResult tests(@PathVariable String Id) {
		
		ZkmesQcmgmtIncomingtest entity = new ZkmesQcmgmtIncomingtest();
		entity.setIncomingtestId(Id); 
		
		return toAjax(incomingtestService.insert(entity));
		 
	}
		
		
}
