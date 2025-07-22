/**
 * 初始化主题入口模块
 */

layui.config({
  base: 'res/', // 静态资源所在路径
}).extend({
  setter: 'config' // 将 config.js 扩展到 layui 模块
}).define(['setter'], function(exports) {
  var setter = layui.setter;

  // 将核心库扩展到 layui 模块
  layui.each({
    admin: 'admin',
    view: 'view',
    adminIndex: 'index'
  }, function(modName, fileName) {
    var libs = {};
    libs[modName] = '{/}'+ setter.paths.core +'modules/'+ fileName;
    layui.extend(libs);
  });

  // 指定业务模块基础目录
  layui.config({
    base: setter.paths.modules
  });

  // 将业务模块中的特殊模块扩展到 layui 模块
  layui.each(setter.extend, function(key, value) {
    var mods = {};
    mods[key] = '{/}' + layui.cache.base + value;
    layui.extend(mods);
  });

  
  layui.table.set({
    request: {
       pageName: 'pageNum' //页码的参数名称，默认：page
      ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
    },
    text: {
  	  none: '无数据'
    },  
    response: {
      statusName: "code", //规定数据状态的字段名称，默认：code
      statusCode: 200, //规定成功的状态码，默认：0
    },
    parseData: function (res) {
      // res 即为原始返回的数据 
      return {
        code: res.code, // 解析接口状态
        msg: res.msg, // 解析提示文本
        count: res.total, // 解析数据长度
        data: res.rows, // 解析数据列表
      };
    }
  });
  
//扩展任意外部模块
  layui.extend({
  	echarts: {
      src: 'res/js/echarts.min.js', // 模块路径
      api: 'echarts' // 接口名称
    }, 
    echartsliquidfill: {
      src: 'res/js/echartsliquidfill.js', // 模块路径
      api: 'echartsliquidfill' // 接口名称
    }, 
  });
  
  
  // 加载主题核心库入口模块
  layui.use('adminIndex', function() {
    layui.use('common'); // 加载公共业务模块，如不需要可剔除

    // 输出模块 / 模块加载完毕标志
    exports('index', layui.admin);
  });
});
