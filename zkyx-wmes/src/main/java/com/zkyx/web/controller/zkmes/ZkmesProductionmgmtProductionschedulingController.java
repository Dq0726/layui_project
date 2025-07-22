package com.zkyx.web.controller.zkmes;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.zkyx.domain.mysql.zkmes.ZkmesProductionmgmtProductionplan;
import com.zkyx.domain.mysql.zkmes.ZkmesProductionmgmtProductiontask;
import com.zkyx.service.mysql.zkmes.IZkmesProductionmgmtProductionplanService;
import com.zkyx.service.mysql.zkmes.IZkmesProductionmgmtProductiontaskService;

/**
 * 生产排产 操作处理
 * 
 * @author zkyx
 */
@RestController
@RequestMapping("/zkmes/productionmgmt/productionscheduling")
public class ZkmesProductionmgmtProductionschedulingController extends BaseController
{
 
    @Autowired
    private IZkmesProductionmgmtProductionplanService planService;
    
    @Autowired
    private IZkmesProductionmgmtProductiontaskService taskService;

 
    class Productionscheduling
    {
    	private String id; 
    	private String pid; 
    	
    	private String productionplanId; 
    	private String productId; 
    	private String productName; 
		private String qty;  
		private String taskId;
    	private String type;
    	private String workstationId;
    	
    	private String processId; 
    	private String processName; 
    	
    	private String techniqueId;
    	
    	private String begintime;
    	private String endtime;
     
	  
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getWorkstationId() {
			return workstationId;
		}
		public void setWorkstationId(String workstationId) {
			this.workstationId = workstationId;
		}
		public String getPid() {
			return pid;
		}
		public void setPid(String pid) {
			this.pid = pid;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		} 
		public String getBegintime() {
			return begintime;
		}
		public void setBegintime(String begintime) {
			this.begintime = begintime;
		}
		public String getEndtime() {
			return endtime;
		}
		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}
		public String getTechniqueId() {
			return techniqueId;
		}
		public void setTechniqueId(String techniqueId) {
			this.techniqueId = techniqueId;
		}
		public String getProcessId() {
			return processId;
		}
		public void setProcessId(String processId) {
			this.processId = processId;
		}
		public String getProcessName() {
			return processName;
		}
		public void setProcessName(String processName) {
			this.processName = processName;
		} 
		public String getQty() {
			return qty;
		}
		public void setQty(String qty) {
			this.qty = qty;
		}
		public String getProductionplanId() {
			return productionplanId;
		}
		public void setProductionplanId(String productionplanId) {
			this.productionplanId = productionplanId;
		}
    	
    }
    /**
     * 获取生产排产列表
     */
    @PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionscheduling:list')")
    @GetMapping("/list")
    public AjaxResult list(ZkmesProductionmgmtProductionplan entity)
    {
      //  startPage();
    	entity.setStatus("1");
          List<ZkmesProductionmgmtProductionplan> planList = planService.selectList(entity);
 
          List<Productionscheduling> entitys = new ArrayList<>();
          
          Iterator<ZkmesProductionmgmtProductionplan> it = planList.iterator(); 
          while(it.hasNext()) {
        	  ZkmesProductionmgmtProductionplan plan = it.next();
        	  
        	  Productionscheduling ps = new Productionscheduling();
        	  String ulid = UlidCreator.getMonotonicUlid().toString();
        	  ps.setId(ulid);
        	  ps.setType("0");
        	  ps.setQty(plan.getProductionQty());
        	  ps.setProductionplanId(plan.getProductionplanId()); 
        	  ps.setProductName(plan.getProductName()); 
        	  ps.setTechniqueId(plan.getTechniqueId());
        	  
        	  entitys.add(ps);
        	  
        
        	  
        	  ////////////////////////////////////////////////////////////////////////
        	  
        	  ZkmesProductionmgmtProductiontask taskEntity = new ZkmesProductionmgmtProductiontask();
        	  
        	  
        	  taskEntity.setProductionplanId(plan.getProductionplanId());
        	  List<ZkmesProductionmgmtProductiontask> taskList = taskService.selectList(taskEntity);
        	  
        	  Iterator<ZkmesProductionmgmtProductiontask> taskIt = taskList.iterator(); 
        	  while(taskIt.hasNext()) {
        		  ZkmesProductionmgmtProductiontask task = taskIt.next();
        		  
        		  Productionscheduling ps1 = new Productionscheduling();
        		  String ulid1 = UlidCreator.getMonotonicUlid().toString();
        		  ps1.setId(ulid1);
        		  ps1.setPid(ulid);
        		  ps1.setType("1"); 
        		  ps1.setQty(task.getSchedulingQty());
        		  ps1.setTaskId(task.getProductiontaskId());
        	 	  ps1.setProductName(task.getProductName()); 
        		  ps1.setProcessName(task.getProcessName());
        		  
        		  ps1.setBegintime(task.getBegintime());
        		  ps1.setEndtime(task.getEndtime());
        		  
        		  
            	  entitys.add(ps1); 
        	  } 
          }
          
     
        
        return success(entitys); 
    } 
     
     /**
      * 根据生产排产编号获取详细订单
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionscheduling:query')")
     @GetMapping(value = "/{Id}")
     public AjaxResult getInfo(@PathVariable String Id)
     {

    	 return success(planService.selectById(Id));
     }

     /**
      * 新增生产排产
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionscheduling:add')")
     @Log(title = "生产排产", businessType = BusinessType.INSERT)
     @PostMapping
     public AjaxResult add(@Validated @RequestBody ZkmesProductionmgmtProductionplan entity)
     {
     	entity.setCreateBy(getUsername());
         return toAjax(planService.insert(entity));
     }

     /**
      * 修改生产排产型号
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionscheduling:edit')")
     @Log(title = "生产排产", businessType = BusinessType.UPDATE)
     @PutMapping
     public AjaxResult edit(@Validated @RequestBody ZkmesProductionmgmtProductionplan entity)
     {
     	entity.setUpdateBy(getUsername());
         return toAjax(planService.update(entity));
     }

     /**
      * 删除生产排产
      */
     @PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionscheduling:remove')")
     @Log(title = "生产排产", businessType = BusinessType.DELETE)
     @DeleteMapping("/{Ids}")
     public AjaxResult remove(@PathVariable String[] Ids)
     { 
         return toAjax(planService.deleteByIds(Ids));
     }
     
     
     
     
     
     //productiontask   /////////////////////////////////////////////////////////////////////////////
     
     /**
 	 * 获取生产任务列表
 	 */
 	@PreAuthorize("@ss.hasPermi('zkmes:productionmgmt:productionscheduling:productiontask:list')")
 	@GetMapping("/productiontask/list")
 	public TableDataInfo list(ZkmesProductionmgmtProductiontask entity) {
 		startPage(); 
 		List<ZkmesProductionmgmtProductiontask> list = taskService.selectList(entity);
 		return getDataTable(list);
 	}
     
     
}
