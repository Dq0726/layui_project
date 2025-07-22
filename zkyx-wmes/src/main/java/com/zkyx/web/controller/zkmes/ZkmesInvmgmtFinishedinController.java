package com.zkyx.web.controller.zkmes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtInboundorder;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtInboundorderProduct;
import com.zkyx.framework.web.service.InoutInboundService;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtInboundorderProductService;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtInboundorderService;

/**
 * 成品入库操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/invmgmt/finishedin")
public class ZkmesInvmgmtFinishedinController extends BaseController {

	@Autowired
	private IZkmesInvmgmtInboundorderService inboundorderService;

	@Autowired
	private IZkmesInvmgmtInboundorderProductService inboundorderProductService;

	@Autowired
	private InoutInboundService inoutInboundService;
	
	
	

	/**
	 * 获取成品入库列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesInvmgmtInboundorder entity) {
		startPage();

		entity.setIotId("4");

		List<ZkmesInvmgmtInboundorder> list = inboundorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 根据成品入库编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(inboundorderService.selectById(Id));
	}

	/**
	 * 新增成品入库
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:add')")
	@Log(title = "成品入库", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add() {
		// String ulid = UlidCreator.getMonotonicUlid().toString();
		// 创建一个Date对象，表示当前日期和时间
		Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		String formattedDate = dateFormat.format(now);

		ZkmesInvmgmtInboundorder entity = new ZkmesInvmgmtInboundorder();

		entity.setInboundorderId("io" + formattedDate);
		entity.setIotId("4");

		entity.setCreateBy(getUsername());
		return toAjax(inboundorderService.insert(entity));
	}

	/**
	 * 修改成品入库
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:edit')")
	@Log(title = "成品入库", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtInboundorder entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(inboundorderService.update(entity));
	}

	/**
	 * 删除成品入库
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:remove')")
	@Log(title = "成品入库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(inboundorderService.deleteByIds(Ids));
	}

	// /zkmes/invmgmt/finishedin/product
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取成品入库单产品列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:product:list')")
	@GetMapping("/product/list")
	public TableDataInfo list(ZkmesInvmgmtInboundorderProduct entity) { 
		List<ZkmesInvmgmtInboundorderProduct> list = inboundorderProductService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 新增成品入库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:product:add')")
	@Log(title = "成品入库单产品", businessType = BusinessType.INSERT)
	@PostMapping("/product")
	public AjaxResult addInvmgmt(@Validated @RequestBody ZkmesInvmgmtInboundorderProduct entity) {
		String ulid = UlidCreator.getMonotonicUlid().toString();
		entity.setInboundorderproductId(ulid);
		entity.setCreateBy(getUsername());
		return toAjax(inboundorderProductService.insert(entity));
	}

	/**
	 * 修改成品入库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:product:edit')")
	@Log(title = "成品入库单产品", businessType = BusinessType.UPDATE)
	@PutMapping("/product")
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtInboundorderProduct entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(inboundorderProductService.update(entity));
	}

	/**
	 * 成品入库单产品同步入库
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:finishedin:product:sync')")
	@Log(title = "成品入库单产品同步入库", businessType = BusinessType.UPDATE)
	@PostMapping("/product/sync")
	public AjaxResult sync(@Validated @RequestBody HashMap<String, String> map) {
		 
		//
		String inboundorder = map.get("inboundorder");
		String inboundorderproductId = map.get("inboundorderproductId");
		String productId = map.get("productId");
		String warehouseId = map.get("warehouseId");  
		String szInboundQty = map.get("inboundQty");

		String updateBy = getUsername();

		float inboundQty = Float.parseFloat(szInboundQty);

		 
		int result = inoutInboundService.updateInboundorderproductStatusAndProductnum(inboundorder, inboundorderproductId, productId, warehouseId, inboundQty, "1", updateBy);
		
	    if(result == 1) {
			return success("同步成功");
		}else if(result == 2) {
			return success("已同步");
		}

		return error();
	}

	 

}
