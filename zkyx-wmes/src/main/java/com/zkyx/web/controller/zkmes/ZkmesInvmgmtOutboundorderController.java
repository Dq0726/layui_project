package com.zkyx.web.controller.zkmes;

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
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtOutboundorder;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtOutboundorderProduct;
import com.zkyx.framework.web.service.InoutInboundService;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutboundorderProductService;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutboundorderService;

/**
 * 出库单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/invmgmt/outboundorder")
public class ZkmesInvmgmtOutboundorderController extends BaseController {
	@Autowired
	private IZkmesInvmgmtOutboundorderService outboundorderService;

	@Autowired
	private IZkmesInvmgmtOutboundorderProductService outboundorderProductService;
 
	@Autowired
	private InoutInboundService inoutInboundService;

	/**
	 * 获取出库单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesInvmgmtOutboundorder entity) {
		startPage();

		List<ZkmesInvmgmtOutboundorder> list = outboundorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 根据出库单编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(outboundorderService.selectById(Id));
	}

	/**
	 * 新增出库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:add')")
	@Log(title = "出库单", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody ZkmesInvmgmtOutboundorder entity) {

		// String ulid = UlidCreator.getMonotonicUlid().toString();
		// entity.setInvmgmtId(ulid);
		// entity.setInoutbound("0"); //出出库:0出库，1出库
		// entity.setOutboundType("0"); //出库类型：0订单出库,1内部出库,2其他出库

		entity.setCreateBy(getUsername());
		return toAjax(outboundorderService.insert(entity));
	}

	/**
	 * 修改出库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:edit')")
	@Log(title = "出库单", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtOutboundorder entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(outboundorderService.update(entity));
	}

	/**
	 * 删除出库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:remove')")
	@Log(title = "出库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(outboundorderService.deleteByIds(Ids));
	}

	// /product
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取出库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:product:list')")
	@GetMapping("/product/list")
	public TableDataInfo list(ZkmesInvmgmtOutboundorderProduct entity) {
		List<ZkmesInvmgmtOutboundorderProduct> list = outboundorderProductService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 新增出库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:product:add')")
	@Log(title = "出库单产品", businessType = BusinessType.INSERT)
	@PostMapping("/product")
	public AjaxResult addInvmgmt(@Validated @RequestBody ZkmesInvmgmtOutboundorderProduct entity) {
		String ulid = UlidCreator.getMonotonicUlid().toString();
		entity.setOutboundorderproductId(ulid);
		entity.setCreateBy(getUsername());
		return toAjax(outboundorderProductService.insert(entity));
	}

	/**
	 * 修改出库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:product:edit')")
	@Log(title = "出库单产品", businessType = BusinessType.UPDATE)
	@PutMapping("/product")
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtOutboundorderProduct entity) {
		
		
		entity.setUpdateBy(getUsername());
		
		//List<ZkmesInvmgmtOutbatch> outbatchList = entity.getOutbatchList();
		
		int result = inoutInboundService.updateOutboundorderproductStatusAndInbatch(entity);
		
		return toAjax(result);
		
		//return toAjax(outboundorderProductService.update(entity));
	}

	/**
	 * 出库单产品同步出库
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:outboundorder:product:sync')")
	@Log(title = "出库单产品同步出库", businessType = BusinessType.UPDATE)
	@PostMapping("/product/sync")
	public AjaxResult sync(@Validated @RequestBody HashMap<String, String> map) {
		//
		String outboundorderproductId = map.get("outboundorderproductId");
		String productId = map.get("productId");
		String warehouseId = map.get("warehouseId");
		String szOutboundQty = map.get("outboundQty");

		String updateBy = getUsername();

		float outboundQty = Float.parseFloat(szOutboundQty);

		int result = inoutInboundService.updateOutboundorderproductStatusAndProductnum(outboundorderproductId,
				productId, warehouseId, outboundQty, "1", updateBy);

		if (result == 1) {
			return success("同步成功");
		} else if (result == 2) {
			return success("已同步");
		} else if (result == 3) {
			return success("库存不足");
		} else if (result == 4) {
			return success("该产品未入库");
		}

		return error();
	}

 
}
