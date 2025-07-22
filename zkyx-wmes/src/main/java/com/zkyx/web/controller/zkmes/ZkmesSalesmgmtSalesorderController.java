package com.zkyx.web.controller.zkmes;

import java.util.ArrayList;
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
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtOutboundorder;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtOutboundorderProduct;
import com.zkyx.domain.mysql.zkmes.ZkmesProductionmgmtProductiontask;
import com.zkyx.domain.mysql.zkmes.ZkmesSalesmgmtOrderproduct;
import com.zkyx.domain.mysql.zkmes.ZkmesSalesmgmtSalesorder;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutboundorderService;
import com.zkyx.service.mysql.zkmes.IZkmesSalesmgmtOrderproductService;
import com.zkyx.service.mysql.zkmes.IZkmesSalesmgmtSalesorderService;

/**
 * 销售单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/salesmgmt/salesorder")
public class ZkmesSalesmgmtSalesorderController extends BaseController {

	@Autowired
	private IZkmesSalesmgmtSalesorderService salesorderService;
	
	@Autowired
 	private IZkmesSalesmgmtOrderproductService orderproductService;
  
	@Autowired
	private IZkmesInvmgmtOutboundorderService outboundorderService;
	/**
	 * 获取销售单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesorder:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesSalesmgmtSalesorder entity) {
		startPage();

		List<ZkmesSalesmgmtSalesorder> list = salesorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 获取销售单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesorder:list')")
	@GetMapping("/list1")
	public AjaxResult list1(ZkmesSalesmgmtSalesorder entity) {
		List<ZkmesSalesmgmtSalesorder> list = salesorderService.selectList(entity);
		return success(list);
	}

	/**
	 * 根据销售单编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesorder:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(salesorderService.selectById(Id));
	}

	/**
	 * 新增销售单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesorder:add')")
	@Log(title = "销售单", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody ZkmesSalesmgmtSalesorder entity) {

		// 创建一个Date对象，表示当前日期和时间
		//Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		//String formattedDate = dateFormat.format(now);
		//entity.setSalesorderId("qo" + formattedDate);
		//entity.setSalesorderId(formattedDate);
		entity.setCreateBy(getUsername());
		return toAjax(salesorderService.insert(entity));
	}

	/**
	 * 修改销售单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesorder:edit')")
	@Log(title = "销售单", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@RequestBody ZkmesSalesmgmtSalesorder entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(salesorderService.update(entity));
	}

	/**
	 * 删除销售单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesorder:remove')")
	@Log(title = "销售单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(salesorderService.deleteByIds(Ids));
	}
 
	
	/**
	 * 生成销售出库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesorder:outboundorder')")
	@Log(title = "生成生产领料单", businessType = BusinessType.INSERT)
	@PostMapping("/outboundorder")
	public AjaxResult outboundorder(@Validated @RequestBody ZkmesSalesmgmtSalesorder entity) {
		ZkmesInvmgmtOutboundorder outboundorder = new ZkmesInvmgmtOutboundorder();

		String username = getUsername();
  
		outboundorder.setOutboundorderId(entity.getSalesorderId()); 
		outboundorder.setOotId("2");
		outboundorder.setStatus("1");
		outboundorder.setCreateBy(username);

		//获取销售订单产品记录 转 存入库产品记录
		
		ZkmesSalesmgmtOrderproduct salesmgmtOrderproduct = new ZkmesSalesmgmtOrderproduct();
		salesmgmtOrderproduct.setOrderId(entity.getSalesorderId());
		List<ZkmesSalesmgmtOrderproduct> salesmgmtOrderproductList =  orderproductService.selectList(salesmgmtOrderproduct);
		
		
		List<ZkmesInvmgmtOutboundorderProduct> outboundorderProductList = new ArrayList<>();

		for (int i = 0; i < salesmgmtOrderproductList.size(); i++) {
			 
			salesmgmtOrderproduct = salesmgmtOrderproductList.get(i);
			
			ZkmesInvmgmtOutboundorderProduct outboundorderProduct= new ZkmesInvmgmtOutboundorderProduct();
			String ulid = UlidCreator.getMonotonicUlid().toString();  
			outboundorderProduct.setOutboundorderproductId(ulid);
			
			outboundorderProduct.setOutboundorderId(entity.getSalesorderId()); 
			outboundorderProduct.setProductId(salesmgmtOrderproduct.getProductId());
			outboundorderProduct.setOutboundQty(salesmgmtOrderproduct.getQuotationQty());
	  
			outboundorderProduct.setCreateBy(username);
			 
			outboundorderProductList.add(outboundorderProduct);
			

		}

		int result = outboundorderService.insertOutboundorderAndOutboundorderproduct(outboundorder, outboundorderProductList);

		return toAjax(result);
	}
	
	
	
	
	
 
}
