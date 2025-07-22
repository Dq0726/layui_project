# 中科轻云MES智能制造大数据看板

## 项目概述

这是一个基于Layui和ECharts构建的现代化MES（Manufacturing Execution System）智能制造大数据可视化看板，专为工业4.0环境设计，支持1920×1080和3840×1080分辨率的自适应显示。

## ✨ 核心特性

### 🎨 视觉设计
- **科技感界面**：采用Layui经典蓝色系配合霓虹色调，营造未来科技感
- **工业风格**：深灰色工业背景，卡片式布局，专业而现代
- **动态效果**：丰富的动画效果，包括粒子背景、数据流动、辉光边框等
- **响应式设计**：完美适配从1920×1080到3840×1080的各种分辨率

### 📊 功能模块

#### 1. 生产数据总览
- **实时统计卡片**：生产计划、销售订单金额、出库数量、设备在线率
- **数字滚动动画**：数据更新时的平滑过渡效果
- **状态指示器**：根据数据状态显示不同颜色（成功/警告/错误）

#### 2. 工业流程可视化（中央区域）
- **流程节点**：原料投入 → 生产加工 → 质量检测 → 包装出库
- **实时数据流**：动态粒子效果展示数据流向
- **交互式节点**：点击节点查看详细信息
- **效率监控**：实时显示各环节效率百分比

#### 3. 图表分析
- **产品生产数量**：柱状图展示各产品线生产情况
- **生产报工状态**：双仪表盘显示完成率和质量率
- **订单进度情况**：多线图对比报价、销售、出库订单趋势
- **来料不良分类**：堆叠柱状图分析质量问题
- **出货趋势分析**：面积图展示出货量变化趋势
- **设备运行状态**：水平条形图显示设备状态分布

#### 4. 实时告警系统
- **动态告警面板**：右侧固定位置显示实时告警信息
- **分级告警**：信息、警告、错误三级告警显示
- **滑入动画**：新告警项目平滑滑入效果
- **自动清理**：自动管理告警数量，保持界面整洁

### 🚀 动画效果

#### 粒子系统
- **动态粒子背景**：50-80个彩色粒子随机漂浮
- **鼠标交互**：粒子会被鼠标吸引或排斥
- **性能优化**：根据设备性能自动调整粒子数量

#### 数据流效果
- **流动粒子**：在工业流程图中显示数据流动
- **光束效果**：垂直光束模拟数据传输
- **随机轨迹**：粒子运动路径带有随机变化

#### 交互动画
- **卡片悬停**：3D倾斜效果和波浪扩散
- **按钮点击**：涟漪扩散效果
- **元素浮动**：工艺节点的自然浮动动画
- **发光效果**：鼠标悬停时的辉光增强

### 📱 响应式适配

#### 标准屏幕 (1920×1080)
- 三栏布局：左侧图表 + 中央流程图 + 右侧图表
- 紧凑的导航菜单和统计卡片
- 优化的字体大小和间距

#### 超宽屏 (2560×1080)
- 扩展的卡片间距和内容区域
- 增强的粒子效果
- 更大的图表显示区域

#### 超宽屏 (3840×1080)
- 网格布局：4列×3行的图表排列
- 侧边栏扩展：左右各400px的辅助数据区
- 更多粒子和增强的动画效果
- 扩大的工艺流程节点

## 🛠️ 技术架构

### 前端技术栈
- **Layui v2.8+**：UI框架和组件库
- **ECharts v5.4+**：数据可视化图表库
- **原生JavaScript**：动画效果和交互逻辑
- **CSS3**：样式和动画效果
- **HTML5**：语义化标记

### 核心文件结构
```
src/main/resources/static/
├── views/zkmes/dashboard/
│   └── index1.html                    # 主页面文件
├── res/
│   ├── adminui/src/css/
│   │   └── dashboard.css              # 主样式文件
│   └── js/
│       └── dashboard-animations.js    # 动画效果库
└── json/                              # 模拟数据文件
```

### 数据接口设计
- **RESTful API**：标准的REST接口设计
- **JSON格式**：统一的数据交换格式
- **实时更新**：WebSocket或轮询机制
- **数据缓存**：前端数据缓存优化

## 🎯 使用指南

### 快速开始
1. **环境准备**：确保项目运行在支持HTML5的现代浏览器中
2. **文件部署**：将所有文件部署到Web服务器
3. **数据配置**：配置后端数据接口地址
4. **访问页面**：通过浏览器访问dashboard页面

### 自定义配置

#### 修改主题色彩
```css
/* 在dashboard.css中修改主色调 */
:root {
    --primary-color: #00bfff;
    --secondary-color: #00ff80;
    --accent-color: #ffd93d;
    --danger-color: #ff6b6b;
}
```

#### 调整动画效果
```javascript
// 在dashboard-animations.js中修改动画参数
DashboardAnimations.init({
    particleCount: 50,          // 粒子数量
    animationSpeed: 1.0,        // 动画速度
    enableInteraction: true     // 是否启用交互
});
```

#### 配置数据更新频率
```javascript
// 设置数据刷新间隔（毫秒）
const DATA_REFRESH_INTERVAL = 8000;  // 8秒刷新一次
const ALERT_CHECK_INTERVAL = 10000;  // 10秒检查告警
```

### 性能优化建议

#### 浏览器兼容性
- **推荐浏览器**：Chrome 80+, Firefox 75+, Safari 13+, Edge 80+
- **最低要求**：支持CSS Grid和Flexbox的现代浏览器
- **硬件要求**：建议4GB以上内存，支持硬件加速

#### 性能调优
- **低性能模式**：系统自动检测设备性能并调整动画复杂度
- **内存管理**：定期清理DOM元素和事件监听器
- **渲染优化**：使用requestAnimationFrame优化动画性能

## 🔧 开发指南

### 添加新的图表
1. **HTML结构**：在相应位置添加图表容器
```html
<div class="data-card">
    <div class="card-header">
        <div class="card-icon">📊</div>
        <div class="card-title">新图表标题</div>
    </div>
    <div class="card-content">
        <div class="chart-container" id="new-chart"></div>
    </div>
</div>
```

2. **JavaScript实现**：添加图表初始化函数
```javascript
function initNewChart() {
    const chart = echarts.init(document.getElementById('new-chart'));
    const option = {
        // ECharts配置
    };
    chart.setOption(option);
    return chart;
}
```

### 添加新的动画效果
```javascript
// 在DashboardAnimations对象中添加新方法
addCustomAnimation: function(element, options) {
    // 自定义动画逻辑
}
```

### 扩展告警系统
```javascript
// 添加自定义告警类型
const customAlert = {
    type: 'custom',
    message: '自定义告警信息',
    time: new Date().toLocaleString(),
    icon: '🔔',
    color: '#ff9500'
};
```

## 📊 数据格式规范

### 统计数据格式
```json
{
    "productionPlan": 1066,
    "salesAmount": 142066,
    "outboundQuantity": 196,
    "equipmentOnlineRate": 100
}
```

### 图表数据格式
```json
{
    "productionData": [
        {"name": "主板", "value": 120},
        {"name": "显卡", "value": 200}
    ],
    "trendData": {
        "categories": ["8月", "9月", "10月"],
        "series": [
            {"name": "报价订单", "data": [502, 205, 332]},
            {"name": "销售订单", "data": [281, 398, 214]}
        ]
    }
}
```

### 告警数据格式
```json
{
    "alerts": [
        {
            "id": "alert_001",
            "type": "warning",
            "message": "设备A01温度异常",
            "timestamp": "2025-01-20T14:30:15Z",
            "level": 2
        }
    ]
}
```

## 🚀 部署说明

### 开发环境
1. 启动本地Web服务器
2. 确保所有静态资源路径正确
3. 配置模拟数据接口

### 生产环境
1. **Web服务器**：Nginx、Apache或其他HTTP服务器
2. **CDN加速**：建议使用CDN加速静态资源加载
3. **HTTPS**：生产环境建议启用HTTPS
4. **缓存策略**：配置适当的缓存头

### Docker部署
```dockerfile
FROM nginx:alpine
COPY src/main/resources/static /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## 🔍 故障排除

### 常见问题

#### 图表不显示
- 检查ECharts库是否正确加载
- 验证DOM元素是否存在
- 查看浏览器控制台错误信息

#### 动画效果卡顿
- 检查设备性能是否满足要求
- 尝试启用低性能模式
- 关闭不必要的浏览器标签页

#### 响应式布局异常
- 检查CSS媒体查询是否正确
- 验证浏览器窗口大小
- 清除浏览器缓存

### 调试技巧
```javascript
// 启用调试模式
window.DEBUG_MODE = true;

// 查看动画性能
console.time('animation');
// 动画代码
console.timeEnd('animation');
```

## 📈 更新日志

### v1.0.0 (2025-01-20)
- ✨ 全新的MES大数据看板界面
- 🎨 丰富的动画效果和交互体验
- 📱 完美的响应式设计
- 🔧 模块化的代码架构
- 📊 多样化的数据可视化图表
- ⚡ 性能优化和兼容性增强

## 🤝 贡献指南

欢迎提交Issue和Pull Request来改进这个项目！

### 开发流程
1. Fork项目
2. 创建功能分支
3. 提交更改
4. 创建Pull Request

## 📄 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 📞 联系我们

- 项目地址：[GitHub Repository]
- 技术支持：[support@zkyx.com]
- 官方网站：[https://www.zkyx.com]

---

**中科轻云MES智能制造大数据看板** - 让数据更直观，让制造更智能！