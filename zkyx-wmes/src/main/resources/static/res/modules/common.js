/**
 * common
 */
 
layui.define(function(exports){
  var $ = layui.$
  ,layer = layui.layer
  ,laytpl = layui.laytpl
  ,setter = layui.setter
  ,view = layui.view
  ,admin = layui.admin
  
  //公共业务的逻辑处理可以写在此处，切换任何页面都会执行
  //……
  // 封装 layuiadmin 弹出层宽高方法，定义为getPopupSize()
    getPopupSize = function() {
      // 解构赋值获取窗口宽高
     const windowWidth = window.innerWidth, windowHeight = window.innerHeight
      

      // 获取 .layui-header 元素高度，若元素不存在则默认为 0
      const popupOffsetHeight = document.querySelector('.layui-header').offsetHeight || 0;
      // 计算弹出层高度
      const popupHeight = windowHeight - popupOffsetHeight;

      // 获取 .layui-side 元素宽度，若元素不存在则默认为 0
      const popupOffsetWidth = document.querySelector('.layui-side').offsetWidth || 0;
      // 计算弹出层宽度
      const popupWidth = windowWidth - popupOffsetWidth;

      // 利用对象字面量简洁语法返回结果
      return {
    	   popupWidthHeight: [popupWidth + "px", popupHeight + "px"],
           popupOffsetWidthHeight: [popupOffsetHeight + "px", popupOffsetWidth + "px"]
      };
  };
  
  
  //退出
  admin.events.logout = function(){
    //执行退出接口
    admin.req({
      url: 'logout'
      ,type: 'get'
      ,data: {}
      ,done: function(res){ //这里要说明一下：done 是只有 response 的 code 正常才会执行。而 succese 则是只要 http 为 200 就会执行
        
        //清空本地记录的 token，并跳转到登入页
        admin.exit();
      }
    });
  };

  
  //对外暴露的接口
  //将 getPopupSize 函数导出
  exports('common', {
	  getPopupSize: getPopupSize 
  });
});