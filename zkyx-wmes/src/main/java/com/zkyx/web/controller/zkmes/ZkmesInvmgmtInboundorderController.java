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
 * 入库单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/invmgmt/inboundorder")
public class ZkmesInvmgmtInboundorderController extends BaseController {
	@Autowired
	private IZkmesInvmgmtInboundorderService inboundorderService;

	@Autowired
	private IZkmesInvmgmtInboundorderProductService inboundorderProductService;
   
	@Autowired
	private InoutInboundService inoutInboundService;

	/**
	 * 获取入库单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesInvmgmtInboundorder entity) {
		startPage();

		List<ZkmesInvmgmtInboundorder> list = inboundorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 根据入库单编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(inboundorderService.selectById(Id));
	}

	/**
	 * 新增入库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:add')")
	@Log(title = "入库单", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody ZkmesInvmgmtInboundorder entity) {

		
		// 创建一个Date对象，表示当前日期和时间
		Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		String formattedDate = dateFormat.format(now);

		String inboundorderId = "io" + formattedDate;
		
		
		// String ulid = UlidCreator.getMonotonicUlid().toString();
		// entity.setInvmgmtId(ulid);
		// entity.setInoutbound("0"); //出入库:0入库，1出库
		// entity.setInboundType("0"); //入库类型：0订单入库,1内部入库,2其他入库

		entity.setCreateBy(getUsername());
		return toAjax(inboundorderService.insert(entity));
	}

	/**
	 * 修改入库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:edit')")
	@Log(title = "入库单", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtInboundorder entity) {
		
		
		if (entity.getStatus().equals("3")) {
			entity.setReviewTime(new Date());
		}
		
		entity.setUpdateBy(getUsername());
		return toAjax(inboundorderService.update(entity));
	}

	/**
	 * 删除入库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:remove')")
	@Log(title = "入库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(inboundorderService.deleteByIds(Ids));
	}

	// /product
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取入库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:product:list')")
	@GetMapping("/product/list")
	public TableDataInfo list(ZkmesInvmgmtInboundorderProduct entity) {
		List<ZkmesInvmgmtInboundorderProduct> list = inboundorderProductService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 新增入库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:product:add')")
	@Log(title = "入库单产品", businessType = BusinessType.INSERT)
	@PostMapping("/product")
	public AjaxResult addInvmgmt(@Validated @RequestBody ZkmesInvmgmtInboundorderProduct entity) {
		String ulid = UlidCreator.getMonotonicUlid().toString();
		entity.setInboundorderproductId(ulid);
		entity.setCreateBy(getUsername());
		return toAjax(inboundorderProductService.insert(entity));
	}

	/**
	 * 修改入库单产品
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:product:edit')")
	@Log(title = "入库单产品", businessType = BusinessType.UPDATE)
	@PutMapping("/product")
	public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmtInboundorderProduct entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(inboundorderProductService.update(entity));
	}

	/**
	 * 入库单产品同步入库
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inboundorder:product:sync')")
	@Log(title = "入库单产品同步入库", businessType = BusinessType.UPDATE)
	@PostMapping("/product/sync")
	public AjaxResult sync(@Validated @RequestBody HashMap<String, String> map) {
		// 
		String inboundorderId = map.get("inboundorderId");
		String inboundorderproductId = map.get("inboundorderproductId");
		String productId = map.get("productId");
		String warehouseId = map.get("warehouseId");  
		String szInboundQty = map.get("inboundQty");

		String updateBy = getUsername();

		float inboundQty = Float.parseFloat(szInboundQty);

		int result = inoutInboundService.updateInboundorderproductStatusAndProductnum(inboundorderId, inboundorderproductId, productId, warehouseId, 
				inboundQty, "1", updateBy);

		if (result == 1) {
			return success("同步成功");
		} else if (result == 2) {
			return success("已同步");
		}

		return error();
	} 
}
