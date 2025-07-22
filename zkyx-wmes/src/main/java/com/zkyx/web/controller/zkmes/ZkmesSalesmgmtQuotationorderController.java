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
import com.zkyx.domain.mysql.zkmes.ZkmesSalesmgmtQuotationorder;
import com.zkyx.domain.mysql.zkmes.ZkmesSalesmgmtSalesorder;
import com.zkyx.service.mysql.zkmes.IZkmesSalesmgmtQuotationorderService;
import com.zkyx.service.mysql.zkmes.IZkmesSalesmgmtSalesorderService;

/**
 * 报价单操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/salesmgmt/quotationorder")
public class ZkmesSalesmgmtQuotationorderController extends BaseController {

	@Autowired
	private IZkmesSalesmgmtQuotationorderService quotationorderService; 
	
	@Autowired
	private IZkmesSalesmgmtSalesorderService salesorderService;
	
//	@Autowired
//	private IZkmesInvmgmtInboundorderService inboundorderService;

	/**
	 * 获取报价单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesSalesmgmtQuotationorder entity) {
		startPage();

		List<ZkmesSalesmgmtQuotationorder> list = quotationorderService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 获取报价单列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:list')")
	@GetMapping("/list1")
	public AjaxResult list1(ZkmesSalesmgmtQuotationorder entity) {
		List<ZkmesSalesmgmtQuotationorder> list = quotationorderService.selectList(entity);
		return success(list);
	}

	/**
	 * 根据报价单编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(quotationorderService.selectById(Id));
	}

	/**
	 * 新增报价单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:add')")
	@Log(title = "报价单", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody ZkmesSalesmgmtQuotationorder entity) {

		// 创建一个Date对象，表示当前日期和时间
		Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		String formattedDate = dateFormat.format(now);
		//entity.setQuotationorderId("qo" + formattedDate);
		entity.setQuotationorderId(formattedDate);
		entity.setCreateBy(getUsername());
		return toAjax(quotationorderService.insert(entity));
	}

	/**
	 * 修改报价单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:edit')")
	@Log(title = "报价单", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesSalesmgmtQuotationorder entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(quotationorderService.update(entity));
	}

	/**
	 * 删除报价单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:remove')")
	@Log(title = "报价单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(quotationorderService.deleteByIds(Ids));
	}
 
	
	/**
	 * 销售报价单->销售订单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:quotationorder:salesorder')")
	@Log(title = "销售报价单->销售订单", businessType = BusinessType.INSERT)
	@PostMapping("/salesorder")
	public AjaxResult add(@Validated @RequestBody ZkmesSalesmgmtSalesorder entity) {
 
		entity.setCreateBy(getUsername());
		return toAjax(salesorderService.insert(entity));
	}
}
