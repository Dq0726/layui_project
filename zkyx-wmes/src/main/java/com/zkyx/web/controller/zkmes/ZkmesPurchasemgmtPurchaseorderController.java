package com.zkyx.web.controller.zkmes;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.zkyx.domain.mysql.zkmes.ZkmesPurchasemgmtPurchaseorder;
import com.zkyx.service.mysql.zkmes.IZkmesPurchasemgmtPurchaseorderService;

import cn.hutool.json.JSONObject;

/**
 * 采购单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/purchasemgmt/purchaseorder")
public class ZkmesPurchasemgmtPurchaseorderController extends BaseController {

	@Autowired
	private IZkmesPurchasemgmtPurchaseorderService purchaseorderService; 

	/**
	 * 获取采购单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseorder:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesPurchasemgmtPurchaseorder entity) {
		startPage();

		List<ZkmesPurchasemgmtPurchaseorder> list = purchaseorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 获取采购单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseorder:list')")
	@GetMapping("/list1")
	public AjaxResult list1(ZkmesPurchasemgmtPurchaseorder entity) {
		List<ZkmesPurchasemgmtPurchaseorder> list = purchaseorderService.selectList(entity);
		return success(list);
	}

	/**
	 * 根据采购单编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseorder:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(purchaseorderService.selectById(Id));
	}

	/**
	 * 新增采购单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseorder:add')")
	@Log(title = "采购单", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add() {
	/*public AjaxResult add(@Validated @RequestBody ZkmesPurchasemgmtPurchaseorder entity) {*/
		ZkmesPurchasemgmtPurchaseorder entity = new ZkmesPurchasemgmtPurchaseorder();
		// 创建一个Date对象，表示当前日期和时间
		Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		String formattedDate = dateFormat.format(now);
		//entity.setPurchaseorderId("po" + formattedDate);
		entity.setPurchaseorderId(formattedDate);
		entity.setCreateBy(getUsername());
		
		int result = purchaseorderService.insert(entity);
		
		JSONObject object  = new JSONObject();
		object.set("purchaseorderId", formattedDate);
		object.set("result", result);
		
		return success(object);
	}

	/**
	 * 修改采购单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseorder:edit')")
	@Log(title = "采购单", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesPurchasemgmtPurchaseorder entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(purchaseorderService.update(entity));
	}
  
	
	/**
	 * 删除采购单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:purchasemgmt:purchaseorder:remove')")
	@Log(title = "采购单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) { 
		return toAjax(purchaseorderService.deleteByIds(Ids));
	} 
	
}
