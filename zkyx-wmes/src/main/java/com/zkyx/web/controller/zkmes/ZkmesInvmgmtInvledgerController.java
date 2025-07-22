package com.zkyx.web.controller.zkmes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.page.TableDataInfo;
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtProduct;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtProductService;

/**
 * 库存台账操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/invmgmt/invledger")
public class ZkmesInvmgmtInvledgerController extends BaseController {

	@Autowired
    private IZkmesProductmgmtProductService productService;
     
	/**
     * 获取产品信息列表
     */
     @PreAuthorize("@ss.hasPermi('zkmes:productmgmt:invledger:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesProductmgmtProduct entity)
    {
        startPage();
        List<ZkmesProductmgmtProduct> list = productService.selectList(entity);
        return getDataTable(list);
    } 
}
