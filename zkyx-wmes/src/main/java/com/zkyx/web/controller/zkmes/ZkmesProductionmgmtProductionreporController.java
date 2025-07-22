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
import com.zkyx.domain.mysql.zkmes.ZkmesProductionmgmtProductiontask;
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtBom;
import com.zkyx.service.mysql.zkmes.IZkmesProductionmgmtProductiontaskService;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtBomService;

/**
 * 生产报工 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productionmgmt/productionrepor")
public class ZkmesProductionmgmtProductionreporController extends BaseController {

	@Autowired
	private IZkmesProductionmgmtProductiontaskService taskService;

	@Autowired
	private IZkmesProductmgmtBomService bomService; 

	/**
	 * 获取生产报工列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionrepor:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesProductionmgmtProductiontask entity) {
		startPage();
		List<ZkmesProductionmgmtProductiontask> list = taskService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 根据生产报工编号获取详细订单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionrepor:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {

		return success(taskService.selectById(Id));
	}

	/**
	 * 新增生产报工
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionrepor:add')")
	@Log(title = "生产报工", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody ZkmesProductionmgmtProductiontask entity) {
		// 创建一个Date对象，表示当前日期和时间
		Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		String formattedDate = dateFormat.format(now);

		String productiontaskId = "task" + formattedDate;

		entity.setProductiontaskId(productiontaskId);

		entity.setCreateBy(getUsername());
		return toAjax(taskService.insert(entity));
	}

	/**
	 * 修改生产报工型号
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionrepor:edit')")
	@Log(title = "生产报工", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesProductionmgmtProductiontask entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(taskService.update(entity));
	}

	/**
	 * 删除生产报工
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionrepor:remove')")
	@Log(title = "生产报工", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {
		return toAjax(taskService.deleteByIds(Ids));
	}

	// /materialpreparation/list
	// ///////////////////////////////////////////////////////

	/**
	 * 获取产品bom列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionrepor:materialpreparation:list')")
	@GetMapping("/materialpreparation/list")
	public TableDataInfo materialpreparationlist(ZkmesProductmgmtBom entity) {
		List<ZkmesProductmgmtBom> list = bomService.selectList(entity);
		return getDataTable(list);
	}
 
}
