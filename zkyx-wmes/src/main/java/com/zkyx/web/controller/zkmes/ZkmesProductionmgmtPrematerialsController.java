package com.zkyx.web.controller.zkmes;

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
import com.zkyx.domain.mysql.zkmes.ZkmesProductionmgmtPrematerials;
import com.zkyx.domain.mysql.zkmes.ZkmesProductionmgmtProductiontask;
import com.zkyx.service.mysql.zkmes.IZkmesInvmgmtOutboundorderService;
import com.zkyx.service.mysql.zkmes.IZkmesProductionmgmtPrematerialsService;

/**
 * 生产备料 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productionmgmt/prematerials")
public class ZkmesProductionmgmtPrematerialsController extends BaseController {

	@Autowired
	private IZkmesProductionmgmtPrematerialsService prematerialService;
  
	@Autowired
	private IZkmesInvmgmtOutboundorderService outboundorderService;
	
	/**
	 * 获取生产备料列表
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:prematerials:list')")
	@GetMapping("/list")
	public TableDataInfo list(ZkmesProductionmgmtPrematerials entity) {
		startPage(); 
		List<ZkmesProductionmgmtPrematerials> list = prematerialService.selectList(entity);
		return getDataTable(list);
	}

	/**
	 * 根据生产备料编号获取详细订单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:prematerials:query')")
	@GetMapping(value = "/{Id}")
	public AjaxResult getInfo(@PathVariable String Id) {

		return success(prematerialService.selectById(Id));
	}

 
	/**
	 * 修改生产备料型号
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:prematerials:edit')")
	@Log(title = "生产备料", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody ZkmesProductionmgmtPrematerials entity) {
		entity.setUpdateBy(getUsername());
		return toAjax(prematerialService.update(entity));
	}

	/**
	 * 删除生产备料
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:prematerials:remove')")
	@Log(title = "生产备料", businessType = BusinessType.DELETE)
	@DeleteMapping("/{Ids}")
	public AjaxResult remove(@PathVariable String[] Ids) {
		return toAjax(prematerialService.deleteByIds(Ids));
	}

	// /pmr ///////////////////////////////////////////////////////

	/**
	 * 生成生产领料单
	 */
	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:outboundorder')")
	@Log(title = "生成生产领料单", businessType = BusinessType.INSERT)
	@PostMapping("/outboundorder")
	public AjaxResult pmr(@Validated @RequestBody ZkmesProductionmgmtProductiontask entity) {
		ZkmesInvmgmtOutboundorder outboundorder = new ZkmesInvmgmtOutboundorder();

		String username = getUsername();

	/*	// 创建一个Date对象，表示当前日期和时间
		Date now = new Date();
		// 创建一个SimpleDateFormat对象，定义日期和时间的格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 使用format方法格式化日期和时间
		String formattedDate = dateFormat.format(now); 
		String pmrId = "pmr" + formattedDate;*/

		outboundorder.setOutboundorderId(entity.getProductiontaskId()); 
		outboundorder.setOotId("1");
		outboundorder.setStatus("1");
		outboundorder.setCreateBy(username);

		List<ZkmesInvmgmtOutboundorderProduct> outboundorderProductList = entity.getProductList();

		for (int i = 0; i < outboundorderProductList.size(); i++) {
			ZkmesInvmgmtOutboundorderProduct outboundorderProduct = outboundorderProductList.get(i);
			String ulid = UlidCreator.getMonotonicUlid().toString(); 
			outboundorderProduct.setOutboundorderproductId(ulid);
			outboundorderProduct.setOutboundorderId(entity.getProductiontaskId()); 
			outboundorderProduct.setCreateBy(username);

		}

		int result = outboundorderService.insertOutboundorderAndOutboundorderproduct(outboundorder, outboundorderProductList);

		return toAjax(result);
	}
}
