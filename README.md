# Service Worker 学习项目

这是一个完整的Service Worker学习项目，包含了详细的学习指南和可运行的PWA Todo应用示例。

## 项目内容

### 📚 学习文档
- `Service_Worker_学习指南.md` - 完整的Service Worker学习指南，包含所有核心概念和实例代码

### 🚀 完整示例项目
- `index.html` - PWA Todo应用主页面（需要根据学习指南中的代码创建）
- `styles.css` - 应用样式文件（需要根据学习指南中的代码创建）
- `app.js` - 主应用逻辑（需要根据学习指南中的代码创建）
- `sw.js` - Service Worker实现文件
- `manifest.json` - PWA应用清单（需要根据学习指南中的代码创建）
- `offline.html` - 离线页面（需要根据学习指南中的代码创建）

## 🛠️ 如何使用

### 1. 创建完整项目
根据`Service_Worker_学习指南.md`中"完整实例项目"章节的代码，创建以下文件：
- index.html
- styles.css  
- app.js
- manifest.json
- offline.html

### 2. 创建图标文件
在项目根目录创建`images`文件夹，并添加以下图标文件：
- `icon-192x192.png` (192x192像素的PWA图标)
- `icon-512x512.png` (512x512像素的PWA图标)

### 3. 启动本地服务器
由于Service Worker需要HTTPS环境（localhost除外），请使用本地服务器运行项目：

```bash
# 使用Python启动服务器
python -m http.server 8000

# 或使用Node.js的http-server
npx http-server -p 8000

# 或使用Live Server（VS Code扩展）
```

### 4. 访问应用
在浏览器中访问 `http://localhost:8000`

## 📖 学习路径

### 第一步：阅读基础概念
- 什么是Service Worker
- Service Worker的生命周期
- 注册Service Worker

### 第二步：实践缓存策略
- Cache First（缓存优先）
- Network First（网络优先）
- Stale While Revalidate（后台更新）

### 第三步：实现高级功能
- 离线功能
- 推送通知
- 后台同步

### 第四步：运行完整项目
- 创建PWA Todo应用
- 测试离线功能
- 体验推送通知

## 🔧 调试技巧

### 1. 使用Chrome DevTools
- 打开 `Application` 面板
- 查看 `Service Workers` 部分
- 监控 `Cache Storage`
- 测试 `Offline` 模式

### 2. 常用调试命令
```javascript
// 在控制台中查看Service Worker状态
navigator.serviceWorker.getRegistrations().then(registrations => {
    console.log('已注册的Service Workers:', registrations);
});

// 强制更新Service Worker
navigator.serviceWorker.getRegistrations().then(registrations => {
    registrations.forEach(registration => {
        registration.update();
    });
});

// 清除所有缓存
caches.keys().then(names => {
    names.forEach(name => {
        caches.delete(name);
    });
});
```

### 3. 测试离线功能
1. 打开Chrome DevTools
2. 切换到 `Network` 面板
3. 勾选 `Offline` 复选框
4. 刷新页面测试离线体验

## 📱 PWA功能测试

### 安装到桌面
1. 在支持的浏览器中打开应用
2. 点击地址栏中的"安装"图标
3. 或使用应用内的"安装应用"按钮

### 推送通知测试
1. 点击"启用通知"按钮
2. 授权通知权限
3. 应用会发送测试通知

### 离线功能测试
1. 正常使用应用添加待办事项
2. 断开网络连接
3. 继续使用应用（数据存储在本地）
4. 恢复网络连接后数据会同步

## 🎯 学习目标

通过这个项目，您将掌握：

✅ Service Worker的基本概念和工作原理  
✅ 各种缓存策略的实现和应用场景  
✅ 离线功能的完整实现  
✅ 推送通知的客户端和服务端处理  
✅ 后台同步的实现方式  
✅ PWA应用的完整开发流程  
✅ Service Worker的调试和优化技巧  

## 📝 注意事项

1. **HTTPS要求**：Service Worker只能在HTTPS环境下工作（localhost除外）
2. **浏览器兼容性**：确保目标浏览器支持Service Worker
3. **缓存管理**：注意及时清理过期缓存，避免占用过多存储空间
4. **更新策略**：合理设计Service Worker的更新机制
5. **错误处理**：添加完善的错误处理和降级方案

## 🔗 相关资源

- [MDN Service Worker API](https://developer.mozilla.org/zh-CN/docs/Web/API/Service_Worker_API)
- [Google PWA 指南](https://web.dev/progressive-web-apps/)
- [Service Worker Cookbook](https://serviceworke.rs/)
- [PWA Builder](https://www.pwabuilder.com/)

---

开始您的Service Worker学习之旅吧！🚀