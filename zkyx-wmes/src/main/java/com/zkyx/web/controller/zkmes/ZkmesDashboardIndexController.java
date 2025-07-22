package com.zkyx.web.controller.zkmes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zkyx.common.core.controller.BaseController;
import com.zkyx.common.core.domain.AjaxResult;
import com.zkyx.domain.mysql.zkmes.ZkmesBconfigDevicetype;
import com.zkyx.service.mysql.zkmes.IZkmesBconfigDevicetypeService;

import cn.hutool.json.JSONObject;

/**
 * 首页看板操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/dashboard/index")
public class ZkmesDashboardIndexController extends BaseController
{
    @Autowired
    private IZkmesBconfigDevicetypeService devicetypeService;

    /**
     * 获取首页看板销售列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:dashboard:index:sales')")
    @GetMapping("/sales")
    public AjaxResult sales(ZkmesBconfigDevicetype entity)
    {  
    	//x:["2025-01-03", "2025-01-04", "2025-01-05", "2025-01-06", "2025-01-07", "2025-01-08", "2025-01-09"]
    	//y:[12, 6, 5, 3, 8, 13, 2]
    	
    	String[] x = {"2025-01-02", "2025-01-04", "2025-01-05", "2025-01-06", "2025-01-07", "2025-01-08", "2025-01-09"};
    	/*int[][] y = {{112, 16, 15, 13, 8, 13, 2}, {112, 16, 15, 13, 8, 13, 2}};*/
    	int[] y = {12, 16, 15, 13, 8, 13, 2};
    	JSONObject json = new JSONObject(); 
    	json.set("x", x);
    	json.set("y", y);
       
       return success(json); 
    }
    
    /**
     * 获取首页看板采购列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:dashboard:index:salesout')")
    @GetMapping("/salesout")
    public AjaxResult salesout(ZkmesBconfigDevicetype entity)
    {  
    	//x:["2025-01-03", "2025-01-04", "2025-01-05", "2025-01-06", "2025-01-07", "2025-01-08", "2025-01-09"]
    	//y:[12, 6, 5, 3, 8, 13, 2]
    	
    	String[] x = {"2025-01-02", "2025-01-04", "2025-01-05", "2025-01-06", "2025-01-07", "2025-01-08", "2025-01-09"};
    	/*int[][] y = {{112, 16, 15, 13, 8, 13, 2}, {112, 16, 15, 13, 8, 13, 2}};*/
    	int[] y = {12, 16, 15, 13, 8, 13, 2};
    	JSONObject json = new JSONObject(); 
    	json.set("x", x);
    	json.set("y", y);
       
       return success(json); 
    }
    /**
     * 获取首页看板能耗列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:dashboard:index:ess')")
    @GetMapping("/ess")
    public AjaxResult ess(String val)
    {  
    	logger.debug(val);
    	//x:["2025-01-03", "2025-01-04", "2025-01-05", "2025-01-06", "2025-01-07", "2025-01-08", "2025-01-09"]
    	//y:[12, 6, 5, 3, 8, 13, 2]
    	 
    	//目前只读取通道1的数据
    	if(val.equals("year")) {
    		String[] x = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}; 
        	int[] y = {12, 16, 15, 13, 8, 13, 22, 12, 16, 15, 13, 8};
         	JSONObject json = new JSONObject(); 
         	
        	json.set("x", x);
        	json.set("y", y);
    		return success(json); 
    	}else if(val.equals("month")) {
    		String[] x = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16"}; 
        	int[] y = {12, 16, 15, 13, 8, 13, 22, 12, 16, 15, 13, 8, 15, 13, 8, 10};
         	JSONObject json = new JSONObject(); 
         	
        	json.set("x", x);
        	json.set("y", y);
        	 
    		return success(json); 
    	}else if(val.equals("weekly")) {
    		String[] x = {"2025-01-02", "2025-01-03", "2025-01-04", "2025-01-05", "2025-01-06", "2025-01-07", "2025-01-08"}; 
        	int[] y = {12, 16, 15, 13, 8, 13, 22};
         	JSONObject json = new JSONObject(); 
         	
        	json.set("x", x);
        	json.set("y", y);
        	 
    		return success(json); 
    	} 
    	return error();
        
    }
   /* @PreAuthorize("@ss.hasPermi('zkmes:dashboard:index:sales')")
    @GetMapping("/sales")
    public TableDataInfo sales(ZkmesBconfigDevicetype entity)
    {
        startPage();
        List<ZkmesBconfigDevicetype> list = devicetypeService.selectList(entity);
        return getDataTable(list);
    }*/
 
}
