package com.zkyx.web.controller.zkmes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.domain.mysql.zkmes.ZkmesInvmgmtInbatch;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtInbatchService;

/**
 * 库存批次操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/invmgmt/inbatch")
public class ZkmesInvmgmtInbatchController extends BaseController {


    @Autowired
    private IZkmesInvmgmtInbatchService inbatchService;

	/**
	 * 获取库存批次列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inbatch:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesInvmgmtInbatch entity) {
		startPage();
 
		List<ZkmesInvmgmtInbatch> list = inbatchService.selectList(entity);
		return getDataTable(list);
	}
	
	/**
	 * 获取 剩余数量大于0的库存批次列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inbatch:list')")
	@GetMapping("/list1")
	public TableDataInfo list1(ZkmesInvmgmtInbatch entity) {
		startPage();
 
		List<ZkmesInvmgmtInbatch> list = inbatchService.selectRemainningQtyGt0List(entity);
		return getDataTable(list);
	}

	/**
	 * 根据库存批次编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:invmgmt:inbatch:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {
		return success(inbatchService.selectById(Id));
	} 

	
 
}
