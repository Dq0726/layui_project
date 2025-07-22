package com.zkyx.web.controller.zkmes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtInboundorder;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtInboundorderProduct;
import com.zkyx.domain.mysql.zkmes.ZkmesPurchasemgmtOrderproduct;
import com.zkyx.domain.mysql.zkmes.ZkmesPurchasemgmtPurchaseorder;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtInboundorderService;
import com.zkyx.service.mysql.zkmes.IZkmesPurchasemgmtOrderproductService;
import com.zkyx.service.mysql.zkmes.IZkmesPurchasemgmtPurchaseorderService;

/**
 * 采购单审批操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/purchasemgmt/purchaseapprove")
public class ZkmesPurchasemgmtPurchaseapproveController extends BaseController {

	@Autowired
	private IZkmesPurchasemgmtPurchaseorderService purchaseorderService; 
 
	@Autowired
	private IZkmesPurchasemgmtOrderproductService orderproductService;
	
	@Autowired
	private IZkmesInvmgmtInboundorderService inboundorderService;
	
	/**
	 * 获取采购单审批列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseapprove:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesPurchasemgmtPurchaseorder entity) {
		startPage();

		List<ZkmesPurchasemgmtPurchaseorder> list = purchaseorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 获取采购单审批列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseapprove:list')")
	@GetMapping("/list1")
	public AjaxResult list1(ZkmesPurchasemgmtPurchaseorder entity) {
		List<ZkmesPurchasemgmtPurchaseorder> list = purchaseorderService.selectList(entity);
		return success(list);
	}

	/**
	 * 根据采购单审批编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseapprove:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(purchaseorderService.selectById(Id));
	}
 
  
	/**
	 * 审批采购单审批
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseapprove:approve')")
	@Log(title = "采购单审批", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult approve(@Validated @RequestBody ZkmesPurchasemgmtPurchaseorder entity) {
		
		if(entity.getApproveStatus().equals("0")) {
			generatePurchasein(entity);
		}
		
		
		entity.setApproveBy(getUsername());
		return toAjax(purchaseorderService.approve(entity));
	}
	
	private int generatePurchasein(ZkmesPurchasemgmtPurchaseorder entity){
		String username = getUsername();
		
		//通过采购订单id 获取详细信息
		entity = purchaseorderService.selectById(entity.getPurchaseorderId());
		ZkmesInvmgmtInboundorder inboundorder = new ZkmesInvmgmtInboundorder();
		
		//复制审核同意的采购订单信息 到 入库单
		inboundorder.setInboundorderId(entity.getPurchaseorderId());  //同步采购订单号，采购订单号与入库单号一至 用于溯源
		inboundorder.setSupplierId(entity.getSupplierId());         //同步供应商
		inboundorder.setIotId("1");    //设置入库单类型id 0,未知 1采购订单生成，2半成品入库，3成品入库
		inboundorder.setStatus("1");   //设置入库单状态 0未知,1待确认仓库，2已确认仓库，待入库，3审核通过，已入库
		inboundorder.setCreateBy(username); //设置入库单生成人
		 
		//复制审核同意的采购订单产品信息 到 入库产品数据库
		//获取此订单下的产品列表 
		
		
		
		ZkmesPurchasemgmtOrderproduct op = new ZkmesPurchasemgmtOrderproduct();
		op.setOrderId(entity.getPurchaseorderId()); 
		List<ZkmesPurchasemgmtOrderproduct> purchasemgmtOrderproductList = orderproductService.selectList(op);
		 
		List<ZkmesInvmgmtInboundorderProduct> inboundorderProductList = new ArrayList<ZkmesInvmgmtInboundorderProduct>();
		 
		for (int i = 0; i < purchasemgmtOrderproductList.size(); i++) {
			ZkmesPurchasemgmtOrderproduct orderproduct = purchasemgmtOrderproductList.get(i);  
			
			ZkmesInvmgmtInboundorderProduct inboundOrderproduct = new ZkmesInvmgmtInboundorderProduct();
	 
			inboundOrderproduct.setInboundorderproductId(orderproduct.getOrderproductId());    //入库单产品id
			inboundOrderproduct.setInboundorderId(entity.getPurchaseorderId());  //入库单ID
			inboundOrderproduct.setProductId(orderproduct.getProductId());   //产品ID
			inboundOrderproduct.setInboundQty(orderproduct.getPurchaseQty()); //入库数量 
			inboundOrderproduct.setCreateBy(username);

			inboundorderProductList.add(inboundOrderproduct);
		}
		
		int result = inboundorderService.insertInboundorderAndInboundorderproduct(inboundorder, inboundorderProductList);
		
		return result;
	}
	
	
	// /purchasein /////////////////////////////////////////////////////////////////////////////////////////////
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseorder:purchasein:add')")
	@Log(title = "采购单产品", businessType = BusinessType.INSERT)
	@PostMapping("/purchasein")
	public AjaxResult addPurchasein(@Validated @RequestBody ZkmesPurchasemgmtPurchaseorder entity) {
		String username = getUsername();
		
		ZkmesInvmgmtInboundorder inboundorder = new ZkmesInvmgmtInboundorder();
  
		inboundorder.setInboundorderId(entity.getPurchaseorderId()); 
		inboundorder.setSupplierId(entity.getSupplierId());  
		inboundorder.setIotId("1");
		inboundorder.setStatus("1");
		inboundorder.setCreateBy(username);

		List<ZkmesInvmgmtInboundorderProduct> inboundorderProductList = entity.getProductList();

		for (int i = 0; i < inboundorderProductList.size(); i++) {
			ZkmesInvmgmtInboundorderProduct inboundorderProduct = inboundorderProductList.get(i);
			String ulid = UlidCreator.getMonotonicUlid().toString(); 
			inboundorderProduct.setInboundorderproductId(ulid);
			inboundorderProduct.setInboundorderId(entity.getPurchaseorderId()); 
			inboundorderProduct.setCreateBy(username);

		}

		int result = inboundorderService.insertInboundorderAndInboundorderproduct(inboundorder, inboundorderProductList);
		

		return toAjax(result);
		
		
	}

	 
}
