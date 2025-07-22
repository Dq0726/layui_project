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
import com.zkyx.domain.mysql.zkmes.ZkmesProductionmgmtProductionplan;
import com.zkyx.service.mysql.zkmes.IZkmesProductionmgmtProductionplanService;

/**
 * 生产计划 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productionmgmt/productionplan")
public class ZkmesProductionmgmtProductionplanController extends BaseController {

	@Autowired
	private IZkmesProductionmgmtProductionplanService planService;
 
	/**
	 * 获取生产计划列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionplan:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesProductionmgmtProductionplan entity) {
		startPage();
		List<ZkmesProductionmgmtProductionplan> list = planService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 根据生产计划编号获取详细计划
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionplan:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {

		return success(planService.selectById(Id));
	}

	/**
	 * 新增生产计划
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionplan:add')")
	@Log(title = "生产计划", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody ZkmesProductionmgmtProductionplan entity) {
		// 创建一个Date对象，表示当前日期和时间
		Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		String formattedDate = dateFormat.format(now);

		String productionplanId = entity.getProductionplanId() + formattedDate;
		
		entity.setProductionplanId(productionplanId);
  
		entity.setCreateBy(getUsername());
		return toAjax(planService.insert(entity));
	}

	/**
	 * 修改生产计划型号
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionplan:edit')")
	@Log(title = "生产计划", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesProductionmgmtProductionplan entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(planService.update(entity));
	}

	/**
	 * 删除生产计划
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionplan:remove')")
	@Log(title = "生产计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {
		return toAjax(planService.deleteByIds(Ids));
	}

	//review  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 修改生产计划型号
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionplan:review:edit')")
	@Log(title = "生产计划审核", businessType = BusinessType.UPDATE)
	@PutMapping("/review")
	public AjaxResult review(@Validated @RequestBody ZkmesProductionmgmtProductionplan entity) {
 
		entity.setUpdateBy(getUsername());
		return toAjax(planService.update(entity));
	}
}
