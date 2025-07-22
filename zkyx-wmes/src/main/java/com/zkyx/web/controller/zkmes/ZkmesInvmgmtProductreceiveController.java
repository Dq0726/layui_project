package com.zkyx.web.controller.zkmes;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkyx.common.annotation.Log;
import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.common.enums.BusinessType;
import com.zkyx.domain.mysql.zkmes.ZkmesProductmgmtProduct;
import com.zkyx.service.mysql.zkmes.IZkmesProductmgmtProductService;

/**
 * 生产领料操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/invmgmt/productreceive")
public class ZkmesInvmgmtProductreceiveController extends BaseController
{
  //  @Autowired
  //  private IZkmesInvmgmtService productreceiveService;
    
    @Autowired
    private IZkmesProductmgmtProductService productService;


    /**
     * 获取生产领料列表
     */
  /*  @PreAuthorize("@ss.hasPermi('zkmes:invmgmt:productreceive:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZkmesInvmgmt entity)
    {
        startPage();
         
  //      entity.setInvType("10"); //入库类型：0订单入库,1内部入库,10生产领料
        
        List<ZkmesInvmgmt> list = productreceiveService.selectList(entity);
        return getDataTable(list);
    }*/

    /**
     * 根据生产领料编号获取详细信息
     */
  /*  @PreAuthorize("@ss.hasPermi('zkmes:invmgmt:productreceive:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable String Id)
    {
        return success(productreceiveService.selectById(Id));
    }*/

    /**
     * 新增生产领料
     */
   /* @PreAuthorize("@ss.hasPermi('zkmes:invmgmt:productreceive:add')")
    @Log(title = "生产领料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ZkmesInvmgmt entity)
    {
        
        String ulid = UlidCreator.getMonotonicUlid().toString(); 
        entity.setInvmgmtId(ulid);
     //   entity.setInvType("10"); //入库类型：0订单入库,内部入库,2其他入库
        
    	entity.setCreateBy(getUsername());
        return toAjax(productreceiveService.insert(entity));
    }*/

    /**
     * 修改生产领料
     */
   /* @PreAuthorize("@ss.hasPermi('zkmes:invmgmt:productreceive:edit')")
    @Log(title = "生产领料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ZkmesInvmgmt entity)
    { 
    	entity.setUpdateBy(getUsername());
        return toAjax(productreceiveService.update(entity));
    }*/
    
    /**
     * 领料同步入库
     */
    @PreAuthorize("@ss.hasPermi('zkmes:invmgmt:productreceive:sync')")
    @Log(title = "生产领料同步入库", businessType = BusinessType.UPDATE)
    @PostMapping("/sync")
    public AjaxResult sync(@Validated @RequestBody HashMap<String, String>map)
    {
    	
    	String invmgmtId = map.get("invmgmtId"); 
    	String productId = map.get("productId");
    	String number = map.get("number");
    	
    	
    	ZkmesProductmgmtProduct productEntity = new ZkmesProductmgmtProduct();
    	 
    	productEntity.setProductId(productId);
    	//productEntity.setNumber(number); 
    	productEntity.setUpdateBy(getUsername());
    	
    	int result = productService.update(productEntity);
    	
    	/*if(result > 0) {
    		ZkmesInvmgmt entity = new ZkmesInvmgmt();
    		
    		entity.setInvmgmtId(invmgmtId);
    	//	entity.setInvStatus("1"); //入库状态: 0待入库,1已入库
    		
    		entity.setUpdateBy(getUsername()); 
    		result = productreceiveService.update(entity);
    	}*/
    	 
        return toAjax(result);
    }
    

    /**
     * 删除生产领料
     */
  /*  @PreAuthorize("@ss.hasPermi('zkmes:invmgmt:productreceive:remove')")
    @Log(title = "生产领料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{Ids}")
    public AjaxResult remove(@PathVariable String[] Ids)
    {
    	
        return toAjax(productreceiveService.deleteByIds(Ids));
    }*/
}
