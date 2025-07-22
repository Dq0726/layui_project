package com.zkyx.web.controller.zkmes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkyx.common.annotation.Log;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.domain.mysql.zkmes.ZkmesSalesmgmtSalesOut;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutboundorderService;
import com.zkyx.service.mysql.zkmes.IZkmesSalesmgmtSalesOutService;

/**
 * 销售出库操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/salesmgmt/salesout")
public class ZkmesSalesmgmtSalesOutController extends BaseController {

	@Autowired
	private IZkmesSalesmgmtSalesOutService salesoutService;
  
	@Autowired
	private IZkmesInvmgmtOutboundorderService outboundorderService;
	
	/**
	 * 获取销售出库列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesout:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesSalesmgmtSalesOut entity) {
		startPage();

		List<ZkmesSalesmgmtSalesOut> list = salesoutService.selectList(entity);
		return getDataTable(list);
	}

	
	/**
	 * 删除出库单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:salesmgmt:salesout:remove')")
	@Log(title = "出库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {

		return toAjax(outboundorderService.deleteByIds(Ids));
	}
	
	 
 
}
