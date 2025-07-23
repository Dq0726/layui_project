# Service Worker 学习指南

## 目录
1. [什么是Service Worker](#什么是service-worker)
2. [Service Worker的生命周期](#service-worker的生命周期)
3. [注册Service Worker](#注册service-worker)
4. [缓存策略](#缓存策略)
5. [离线功能实现](#离线功能实现)
6. [推送通知](#推送通知)
7. [后台同步](#后台同步)
8. [完整实例项目](#完整实例项目)
9. [最佳实践](#最佳实践)
10. [常见问题与解决方案](#常见问题与解决方案)

## 什么是Service Worker

Service Worker是运行在浏览器后台的脚本，它充当Web应用程序、浏览器和网络之间的代理。它可以拦截网络请求、管理缓存、实现离线功能、推送通知等。

### 核心特性
- **独立线程运行**：不会阻塞主线程
- **事件驱动**：响应各种事件（install、activate、fetch等）
- **网络代理**：可以拦截和修改网络请求
- **持久化**：即使页面关闭也能在后台运行
- **HTTPS要求**：只能在HTTPS环境下工作（localhost除外）

### 应用场景
- 离线缓存
- 推送通知
- 后台数据同步
- 性能优化
- 网络请求拦截

## Service Worker的生命周期

Service Worker有三个主要状态：

```
下载 → 安装 → 激活 → 空闲 → 终止
```

### 生命周期事件

1. **install事件**：Service Worker首次安装时触发
2. **activate事件**：Service Worker激活时触发
3. **fetch事件**：拦截网络请求时触发
4. **message事件**：接收消息时触发

## 注册Service Worker

### 基础注册代码

```javascript
// main.js - 主页面脚本
if ('serviceWorker' in navigator) {
    window.addEventListener('load', async () => {
        try {
            const registration = await navigator.serviceWorker.register('/sw.js');
            console.log('Service Worker注册成功:', registration.scope);
            
            // 监听更新
            registration.addEventListener('updatefound', () => {
                const newWorker = registration.installing;
                newWorker.addEventListener('statechange', () => {
                    if (newWorker.state === 'installed') {
                        if (navigator.serviceWorker.controller) {
                            console.log('新版本可用，请刷新页面');
                        } else {
                            console.log('内容已缓存，可离线使用');
                        }
                    }
                });
            });
        } catch (error) {
            console.error('Service Worker注册失败:', error);
        }
    });
} else {
    console.log('浏览器不支持Service Worker');
}
```

### Service Worker基础结构

```javascript
// sw.js - Service Worker脚本
const CACHE_NAME = 'my-app-v1';
const URLS_TO_CACHE = [
    '/',
    '/index.html',
    '/styles.css',
    '/script.js',
    '/offline.html'
];

// 安装事件
self.addEventListener('install', event => {
    console.log('Service Worker: 安装中...');
    
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                console.log('Service Worker: 缓存文件');
                return cache.addAll(URLS_TO_CACHE);
            })
            .then(() => {
                // 强制激活新的Service Worker
                return self.skipWaiting();
            })
    );
});

// 激活事件
self.addEventListener('activate', event => {
    console.log('Service Worker: 激活中...');
    
    event.waitUntil(
        caches.keys().then(cacheNames => {
            return Promise.all(
                cacheNames.map(cacheName => {
                    if (cacheName !== CACHE_NAME) {
                        console.log('Service Worker: 删除旧缓存', cacheName);
                        return caches.delete(cacheName);
                    }
                })
            );
        }).then(() => {
            // 立即控制所有页面
            return self.clients.claim();
        })
    );
});

// 拦截网络请求
self.addEventListener('fetch', event => {
    event.respondWith(
        caches.match(event.request)
            .then(response => {
                // 如果缓存中有响应，返回缓存
                if (response) {
                    return response;
                }
                
                // 否则发起网络请求
                return fetch(event.request);
            })
            .catch(() => {
                // 网络请求失败，返回离线页面
                if (event.request.destination === 'document') {
                    return caches.match('/offline.html');
                }
            })
    );
});
```

## 缓存策略

### 1. Cache First（缓存优先）

```javascript
self.addEventListener('fetch', event => {
    event.respondWith(
        caches.match(event.request)
            .then(response => {
                return response || fetch(event.request);
            })
    );
});
```

### 2. Network First（网络优先）

```javascript
self.addEventListener('fetch', event => {
    event.respondWith(
        fetch(event.request)
            .then(response => {
                // 克隆响应，因为响应流只能使用一次
                const responseClone = response.clone();
                caches.open(CACHE_NAME)
                    .then(cache => {
                        cache.put(event.request, responseClone);
                    });
                return response;
            })
            .catch(() => {
                return caches.match(event.request);
            })
    );
});
```

### 3. Stale While Revalidate（后台更新）

```javascript
self.addEventListener('fetch', event => {
    event.respondWith(
        caches.match(event.request)
            .then(cachedResponse => {
                const fetchPromise = fetch(event.request)
                    .then(networkResponse => {
                        const responseClone = networkResponse.clone();
                        caches.open(CACHE_NAME)
                            .then(cache => {
                                cache.put(event.request, responseClone);
                            });
                        return networkResponse;
                    });
                
                return cachedResponse || fetchPromise;
            })
    );
});
```

### 4. 高级缓存策略实现

```javascript
// 缓存策略工具类
class CacheStrategy {
    static cacheFirst(request, cacheName) {
        return caches.open(cacheName)
            .then(cache => cache.match(request))
            .then(response => {
                if (response) {
                    return response;
                }
                return fetch(request)
                    .then(networkResponse => {
                        const responseClone = networkResponse.clone();
                        caches.open(cacheName)
                            .then(cache => cache.put(request, responseClone));
                        return networkResponse;
                    });
            });
    }
    
    static networkFirst(request, cacheName, timeout = 3000) {
        return Promise.race([
            fetch(request),
            new Promise((_, reject) => 
                setTimeout(() => reject(new Error('timeout')), timeout)
            )
        ])
        .then(response => {
            const responseClone = response.clone();
            caches.open(cacheName)
                .then(cache => cache.put(request, responseClone));
            return response;
        })
        .catch(() => {
            return caches.open(cacheName)
                .then(cache => cache.match(request));
        });
    }
}

// 使用不同策略处理不同类型的请求
self.addEventListener('fetch', event => {
    const { request } = event;
    const url = new URL(request.url);
    
    // 静态资源使用缓存优先策略
    if (request.destination === 'style' || 
        request.destination === 'script' || 
        request.destination === 'image') {
        event.respondWith(
            CacheStrategy.cacheFirst(request, 'static-cache')
        );
    }
    // API请求使用网络优先策略
    else if (url.pathname.startsWith('/api/')) {
        event.respondWith(
            CacheStrategy.networkFirst(request, 'api-cache')
        );
    }
    // 页面请求使用后台更新策略
    else if (request.destination === 'document') {
        event.respondWith(
            caches.match(request)
                .then(cachedResponse => {
                    const fetchPromise = fetch(request)
                        .then(networkResponse => {
                            const responseClone = networkResponse.clone();
                            caches.open('page-cache')
                                .then(cache => cache.put(request, responseClone));
                            return networkResponse;
                        });
                    
                    return cachedResponse || fetchPromise;
                })
        );
    }
});
```

## 离线功能实现

### 完整的离线功能示例

```javascript
// sw.js - 离线功能Service Worker
const CACHE_NAME = 'offline-app-v1';
const OFFLINE_URL = '/offline.html';

const URLS_TO_CACHE = [
    '/',
    '/index.html',
    '/styles.css',
    '/app.js',
    '/offline.html',
    '/images/logo.png'
];

// 安装时预缓存资源
self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => cache.addAll(URLS_TO_CACHE))
            .then(() => self.skipWaiting())
    );
});

// 激活时清理旧缓存
self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys()
            .then(cacheNames => {
                return Promise.all(
                    cacheNames
                        .filter(cacheName => cacheName !== CACHE_NAME)
                        .map(cacheName => caches.delete(cacheName))
                );
            })
            .then(() => self.clients.claim())
    );
});

// 处理离线请求
self.addEventListener('fetch', event => {
    // 只处理导航请求
    if (event.request.mode === 'navigate') {
        event.respondWith(
            fetch(event.request)
                .catch(() => {
                    return caches.open(CACHE_NAME)
                        .then(cache => cache.match(OFFLINE_URL));
                })
        );
        return;
    }
    
    // 处理其他请求
    event.respondWith(
        caches.match(event.request)
            .then(response => {
                if (response) {
                    return response;
                }
                
                return fetch(event.request)
                    .then(response => {
                        // 只缓存成功的GET请求
                        if (response.status === 200 && 
                            event.request.method === 'GET') {
                            const responseClone = response.clone();
                            caches.open(CACHE_NAME)
                                .then(cache => {
                                    cache.put(event.request, responseClone);
                                });
                        }
                        return response;
                    });
            })
    );
});

// 监听在线状态变化
self.addEventListener('message', event => {
    if (event.data && event.data.type === 'SKIP_WAITING') {
        self.skipWaiting();
    }
});
```

### 离线页面HTML

```html
<!-- offline.html -->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>离线模式</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            min-height: 100vh;
            margin: 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        
        .offline-container {
            background: rgba(255, 255, 255, 0.1);
            padding: 40px;
            border-radius: 15px;
            backdrop-filter: blur(10px);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }
        
        .offline-icon {
            font-size: 64px;
            margin-bottom: 20px;
        }
        
        h1 {
            margin: 0 0 20px 0;
            font-size: 2.5em;
        }
        
        p {
            font-size: 1.2em;
            margin-bottom: 30px;
            opacity: 0.9;
        }
        
        .retry-btn {
            background: #4CAF50;
            color: white;
            border: none;
            padding: 12px 24px;
            font-size: 1.1em;
            border-radius: 25px;
            cursor: pointer;
            transition: background 0.3s;
        }
        
        .retry-btn:hover {
            background: #45a049;
        }
        
        .status {
            margin-top: 20px;
            font-size: 0.9em;
            opacity: 0.8;
        }
    </style>
</head>
<body>
    <div class="offline-container">
        <div class="offline-icon">📡</div>
        <h1>您当前处于离线状态</h1>
        <p>请检查您的网络连接，然后重试</p>
        <button class="retry-btn" onclick="window.location.reload()">
            重新连接
        </button>
        <div class="status" id="status">
            正在检测网络状态...
        </div>
    </div>

    <script>
        // 检测网络状态
        function updateStatus() {
            const status = document.getElementById('status');
            if (navigator.onLine) {
                status.textContent = '网络已连接，点击重新连接按钮';
                status.style.color = '#4CAF50';
            } else {
                status.textContent = '网络未连接';
                status.style.color = '#f44336';
            }
        }
        
        // 监听网络状态变化
        window.addEventListener('online', updateStatus);
        window.addEventListener('offline', updateStatus);
        
        // 初始化状态
        updateStatus();
        
        // 定期检查网络状态
        setInterval(() => {
            fetch('/', { method: 'HEAD', mode: 'no-cors' })
                .then(() => {
                    if (!navigator.onLine) {
                        window.location.reload();
                    }
                })
                .catch(() => {});
        }, 5000);
    </script>
</body>
</html>
```

## 推送通知

### 服务器端推送通知

```javascript
// sw.js - 推送通知处理
self.addEventListener('push', event => {
    const options = {
        body: '您有新的消息',
        icon: '/images/icon-192x192.png',
        badge: '/images/badge-72x72.png',
        vibrate: [100, 50, 100],
        data: {
            dateOfArrival: Date.now(),
            primaryKey: 1
        },
        actions: [
            {
                action: 'explore',
                title: '查看详情',
                icon: '/images/checkmark.png'
            },
            {
                action: 'close',
                title: '关闭',
                icon: '/images/xmark.png'
            }
        ]
    };
    
    if (event.data) {
        const pushData = event.data.json();
        options.body = pushData.message;
        options.data = pushData;
    }
    
    event.waitUntil(
        self.registration.showNotification('我的应用', options)
    );
});

// 处理通知点击
self.addEventListener('notificationclick', event => {
    event.notification.close();
    
    if (event.action === 'explore') {
        // 打开应用页面
        event.waitUntil(
            clients.openWindow('/')
        );
    } else if (event.action === 'close') {
        // 关闭通知
        event.notification.close();
    } else {
        // 默认行为：打开应用
        event.waitUntil(
            clients.matchAll()
                .then(clientList => {
                    for (const client of clientList) {
                        if (client.url === '/' && 'focus' in client) {
                            return client.focus();
                        }
                    }
                    if (clients.openWindow) {
                        return clients.openWindow('/');
                    }
                })
        );
    }
});
```

### 客户端推送订阅

```javascript
// push-notifications.js - 推送通知客户端
class PushNotificationManager {
    constructor() {
        this.vapidPublicKey = 'your-vapid-public-key';
        this.serverEndpoint = '/api/push-subscription';
    }
    
    // 检查推送通知支持
    isSupported() {
        return 'serviceWorker' in navigator && 
               'PushManager' in window && 
               'Notification' in window;
    }
    
    // 请求通知权限
    async requestPermission() {
        const permission = await Notification.requestPermission();
        return permission === 'granted';
    }
    
    // 订阅推送通知
    async subscribe() {
        if (!this.isSupported()) {
            throw new Error('推送通知不被支持');
        }
        
        const hasPermission = await this.requestPermission();
        if (!hasPermission) {
            throw new Error('用户拒绝了通知权限');
        }
        
        const registration = await navigator.serviceWorker.ready;
        const subscription = await registration.pushManager.subscribe({
            userVisibleOnly: true,
            applicationServerKey: this.urlBase64ToUint8Array(this.vapidPublicKey)
        });
        
        // 发送订阅信息到服务器
        await this.sendSubscriptionToServer(subscription);
        return subscription;
    }
    
    // 取消订阅
    async unsubscribe() {
        const registration = await navigator.serviceWorker.ready;
        const subscription = await registration.pushManager.getSubscription();
        
        if (subscription) {
            await subscription.unsubscribe();
            await this.removeSubscriptionFromServer(subscription);
        }
    }
    
    // 发送订阅信息到服务器
    async sendSubscriptionToServer(subscription) {
        await fetch(this.serverEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(subscription)
        });
    }
    
    // 从服务器移除订阅
    async removeSubscriptionFromServer(subscription) {
        await fetch(this.serverEndpoint, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(subscription)
        });
    }
    
    // 转换VAPID密钥格式
    urlBase64ToUint8Array(base64String) {
        const padding = '='.repeat((4 - base64String.length % 4) % 4);
        const base64 = (base64String + padding)
            .replace(/-/g, '+')
            .replace(/_/g, '/');
        
        const rawData = window.atob(base64);
        const outputArray = new Uint8Array(rawData.length);
        
        for (let i = 0; i < rawData.length; ++i) {
            outputArray[i] = rawData.charCodeAt(i);
        }
        return outputArray;
    }
}

// 使用示例
const pushManager = new PushNotificationManager();

// 订阅按钮事件
document.getElementById('subscribe-btn').addEventListener('click', async () => {
    try {
        await pushManager.subscribe();
        console.log('推送通知订阅成功');
        updateUI(true);
    } catch (error) {
        console.error('订阅失败:', error);
    }
});

// 取消订阅按钮事件
document.getElementById('unsubscribe-btn').addEventListener('click', async () => {
    try {
        await pushManager.unsubscribe();
        console.log('取消订阅成功');
        updateUI(false);
    } catch (error) {
        console.error('取消订阅失败:', error);
    }
});

function updateUI(isSubscribed) {
    const subscribeBtn = document.getElementById('subscribe-btn');
    const unsubscribeBtn = document.getElementById('unsubscribe-btn');
    
    subscribeBtn.style.display = isSubscribed ? 'none' : 'block';
    unsubscribeBtn.style.display = isSubscribed ? 'block' : 'none';
}
```

## 后台同步

### Background Sync实现

```javascript
// sw.js - 后台同步
self.addEventListener('sync', event => {
    if (event.tag === 'background-sync') {
        event.waitUntil(doBackgroundSync());
    }
});

async function doBackgroundSync() {
    try {
        // 获取待同步的数据
        const pendingData = await getPendingData();
        
        for (const data of pendingData) {
            try {
                await syncDataToServer(data);
                await removePendingData(data.id);
            } catch (error) {
                console.error('同步失败:', error);
            }
        }
    } catch (error) {
        console.error('后台同步错误:', error);
    }
}

// 获取待同步数据
async function getPendingData() {
    const db = await openDB();
    return db.getAll('pending-sync');
}

// 同步数据到服务器
async function syncDataToServer(data) {
    const response = await fetch('/api/sync', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    if (!response.ok) {
        throw new Error(`同步失败: ${response.status}`);
    }
    
    return response.json();
}

// 移除已同步的数据
async function removePendingData(id) {
    const db = await openDB();
    await db.delete('pending-sync', id);
}

// 简单的IndexedDB操作
async function openDB() {
    return new Promise((resolve, reject) => {
        const request = indexedDB.open('SyncDB', 1);
        
        request.onerror = () => reject(request.error);
        request.onsuccess = () => resolve(request.result);
        
        request.onupgradeneeded = event => {
            const db = event.target.result;
            if (!db.objectStoreNames.contains('pending-sync')) {
                const store = db.createObjectStore('pending-sync', { keyPath: 'id', autoIncrement: true });
                store.createIndex('timestamp', 'timestamp');
            }
        };
    });
}
```

### 客户端后台同步触发

```javascript
// background-sync.js - 客户端后台同步管理
class BackgroundSyncManager {
    constructor() {
        this.dbName = 'SyncDB';
        this.storeName = 'pending-sync';
    }
    
    // 添加待同步数据
    async addPendingData(data) {
        const db = await this.openDB();
        const transaction = db.transaction([this.storeName], 'readwrite');
        const store = transaction.objectStore(this.storeName);
        
        const syncData = {
            ...data,
            timestamp: Date.now(),
            attempts: 0
        };
        
        await store.add(syncData);
        
        // 注册后台同步
        if ('serviceWorker' in navigator && 'sync' in window.ServiceWorkerRegistration.prototype) {
            const registration = await navigator.serviceWorker.ready;
            await registration.sync.register('background-sync');
        }
    }
    
    // 立即尝试同步
    async syncNow() {
        if (navigator.onLine) {
            const registration = await navigator.serviceWorker.ready;
            if (registration.sync) {
                await registration.sync.register('background-sync');
            }
        }
    }
    
    // 获取待同步数据数量
    async getPendingCount() {
        const db = await this.openDB();
        const transaction = db.transaction([this.storeName], 'readonly');
        const store = transaction.objectStore(this.storeName);
        return store.count();
    }
    
    async openDB() {
        return new Promise((resolve, reject) => {
            const request = indexedDB.open(this.dbName, 1);
            
            request.onerror = () => reject(request.error);
            request.onsuccess = () => resolve(request.result);
            
            request.onupgradeneeded = event => {
                const db = event.target.result;
                if (!db.objectStoreNames.contains(this.storeName)) {
                    const store = db.createObjectStore(this.storeName, { 
                        keyPath: 'id', 
                        autoIncrement: true 
                    });
                    store.createIndex('timestamp', 'timestamp');
                }
            };
        });
    }
}

// 使用示例
const syncManager = new BackgroundSyncManager();

// 提交表单时添加到后台同步队列
document.getElementById('data-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());
    
    try {
        if (navigator.onLine) {
            // 在线时直接提交
            await submitData(data);
            showMessage('数据提交成功', 'success');
        } else {
            // 离线时添加到同步队列
            await syncManager.addPendingData({
                type: 'form-submission',
                data: data
            });
            showMessage('数据已保存，将在网络恢复时同步', 'info');
        }
    } catch (error) {
        // 提交失败时也添加到同步队列
        await syncManager.addPendingData({
            type: 'form-submission',
            data: data
        });
        showMessage('数据已保存到本地，稍后重试', 'warning');
    }
});

async function submitData(data) {
    const response = await fetch('/api/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    if (!response.ok) {
        throw new Error(`提交失败: ${response.status}`);
    }
    
    return response.json();
}

function showMessage(message, type) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;
    document.body.appendChild(messageDiv);
    
    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}
```

## 完整实例项目

### 项目结构
```
my-pwa-app/
├── index.html
├── styles.css
├── app.js
├── sw.js
├── manifest.json
├── offline.html
└── images/
    ├── icon-192x192.png
    └── icon-512x512.png
```

### index.html

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PWA Todo应用</title>
    <link rel="stylesheet" href="styles.css">
    <link rel="manifest" href="manifest.json">
    <meta name="theme-color" content="#2196F3">
    <link rel="icon" type="image/png" sizes="192x192" href="images/icon-192x192.png">
</head>
<body>
    <div class="app-container">
        <header class="app-header">
            <h1>📝 PWA Todo</h1>
            <div class="status-indicators">
                <span id="online-status" class="status-indicator">🟢</span>
                <span id="sync-status" class="status-indicator">⏳</span>
            </div>
        </header>
        
        <main class="app-main">
            <form id="todo-form" class="todo-form">
                <input 
                    type="text" 
                    id="todo-input" 
                    placeholder="添加新任务..." 
                    required
                >
                <button type="submit">添加</button>
            </form>
            
            <div class="todo-list" id="todo-list">
                <!-- 动态生成的待办事项 -->
            </div>
            
            <div class="app-actions">
                <button id="install-btn" class="install-btn" style="display: none;">
                    📱 安装应用
                </button>
                <button id="notification-btn" class="notification-btn">
                    🔔 启用通知
                </button>
            </div>
        </main>
    </div>
    
    <div id="toast-container" class="toast-container"></div>
    
    <script src="app.js"></script>
</body>
</html>
```

### styles.css

```css
/* styles.css */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
    color: #333;
}

.app-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    min-height: 100vh;
}

.app-header {
    background: rgba(255, 255, 255, 0.95);
    padding: 20px;
    border-radius: 15px;
    margin-bottom: 20px;
    backdrop-filter: blur(10px);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.app-header h1 {
    color: #2196F3;
    font-size: 1.8em;
}

.status-indicators {
    display: flex;
    gap: 10px;
}

.status-indicator {
    font-size: 1.2em;
    cursor: help;
}

.app-main {
    background: rgba(255, 255, 255, 0.95);
    padding: 30px;
    border-radius: 15px;
    backdrop-filter: blur(10px);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.todo-form {
    display: flex;
    gap: 10px;
    margin-bottom: 30px;
}

.todo-form input {
    flex: 1;
    padding: 12px 16px;
    border: 2px solid #e0e0e0;
    border-radius: 25px;
    font-size: 1em;
    outline: none;
    transition: border-color 0.3s;
}

.todo-form input:focus {
    border-color: #2196F3;
}

.todo-form button {
    background: #2196F3;
    color: white;
    border: none;
    padding: 12px 24px;
    border-radius: 25px;
    cursor: pointer;
    font-size: 1em;
    transition: background 0.3s;
}

.todo-form button:hover {
    background: #1976D2;
}

.todo-item {
    display: flex;
    align-items: center;
    padding: 15px;
    margin-bottom: 10px;
    background: #f8f9fa;
    border-radius: 10px;
    border-left: 4px solid #2196F3;
    transition: transform 0.2s, box-shadow 0.2s;
}

.todo-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.todo-item.completed {
    opacity: 0.7;
    border-left-color: #4CAF50;
}

.todo-item.completed .todo-text {
    text-decoration: line-through;
}

.todo-checkbox {
    margin-right: 15px;
    transform: scale(1.2);
}

.todo-text {
    flex: 1;
    font-size: 1.1em;
}

.todo-delete {
    background: #f44336;
    color: white;
    border: none;
    padding: 8px 12px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background 0.3s;
}

.todo-delete:hover {
    background: #d32f2f;
}

.app-actions {
    margin-top: 30px;
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
}

.install-btn, .notification-btn {
    background: #4CAF50;
    color: white;
    border: none;
    padding: 12px 20px;
    border-radius: 25px;
    cursor: pointer;
    font-size: 1em;
    transition: background 0.3s;
}

.install-btn:hover, .notification-btn:hover {
    background: #45a049;
}

.notification-btn {
    background: #FF9800;
}

.notification-btn:hover {
    background: #F57C00;
}

.toast-container {
    position: fixed;
    top: 20px;
    right: 20px;
    z-index: 1000;
}

.toast {
    background: #333;
    color: white;
    padding: 12px 20px;
    border-radius: 5px;
    margin-bottom: 10px;
    animation: slideIn 0.3s ease;
}

.toast.success {
    background: #4CAF50;
}

.toast.error {
    background: #f44336;
}

.toast.warning {
    background: #FF9800;
}

.toast.info {
    background: #2196F3;
}

@keyframes slideIn {
    from {
        transform: translateX(100%);
        opacity: 0;
    }
    to {
        transform: translateX(0);
        opacity: 1;
    }
}

@media (max-width: 480px) {
    .app-container {
        padding: 10px;
    }
    
    .app-header {
        padding: 15px;
    }
    
    .app-main {
        padding: 20px;
    }
    
    .todo-form {
        flex-direction: column;
    }
    
    .todo-form button {
        margin-top: 10px;
    }
}
```

### app.js

```javascript
// app.js - 主应用逻辑
class TodoApp {
    constructor() {
        this.todos = JSON.parse(localStorage.getItem('todos')) || [];
        this.deferredPrompt = null;
        this.init();
    }
    
    async init() {
        this.bindEvents();
        this.render();
        this.updateOnlineStatus();
        await this.registerServiceWorker();
        this.setupInstallPrompt();
        this.setupNotifications();
        
        // 监听网络状态变化
        window.addEventListener('online', () => this.updateOnlineStatus());
        window.addEventListener('offline', () => this.updateOnlineStatus());
    }
    
    bindEvents() {
        // 表单提交
        document.getElementById('todo-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.addTodo();
        });
        
        // 安装按钮
        document.getElementById('install-btn').addEventListener('click', () => {
            this.installApp();
        });
        
        // 通知按钮
        document.getElementById('notification-btn').addEventListener('click', () => {
            this.enableNotifications();
        });
    }
    
    addTodo() {
        const input = document.getElementById('todo-input');
        const text = input.value.trim();
        
        if (text) {
            const todo = {
                id: Date.now(),
                text: text,
                completed: false,
                createdAt: new Date().toISOString()
            };
            
            this.todos.push(todo);
            this.saveTodos();
            this.render();
            input.value = '';
            
            this.showToast('任务添加成功', 'success');
        }
    }
    
    toggleTodo(id) {
        const todo = this.todos.find(t => t.id === id);
        if (todo) {
            todo.completed = !todo.completed;
            this.saveTodos();
            this.render();
            
            if (todo.completed) {
                this.showToast('任务已完成', 'success');
            }
        }
    }
    
    deleteTodo(id) {
        this.todos = this.todos.filter(t => t.id !== id);
        this.saveTodos();
        this.render();
        this.showToast('任务已删除', 'info');
    }
    
    saveTodos() {
        localStorage.setItem('todos', JSON.stringify(this.todos));
    }
    
    render() {
        const todoList = document.getElementById('todo-list');
        
        if (this.todos.length === 0) {
            todoList.innerHTML = '<p style="text-align: center; color: #999; padding: 40px;">暂无任务，添加一个开始吧！</p>';
            return;
        }
        
        todoList.innerHTML = this.todos.map(todo => `
            <div class="todo-item ${todo.completed ? 'completed' : ''}">
                <input 
                    type="checkbox" 
                    class="todo-checkbox" 
                    ${todo.completed ? 'checked' : ''}
                    onchange="app.toggleTodo(${todo.id})"
                >
                <span class="todo-text">${this.escapeHtml(todo.text)}</span>
                <button class="todo-delete" onclick="app.deleteTodo(${todo.id})">
                    删除
                </button>
            </div>
        `).join('');
    }
    
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    async registerServiceWorker() {
        if ('serviceWorker' in navigator) {
            try {
                const registration = await navigator.serviceWorker.register('/sw.js');
                console.log('Service Worker注册成功:', registration.scope);
                
                // 监听Service Worker更新
                registration.addEventListener('updatefound', () => {
                    const newWorker = registration.installing;
                    newWorker.addEventListener('statechange', () => {
                        if (newWorker.state === 'installed') {
                            if (navigator.serviceWorker.controller) {
                                this.showToast('应用有新版本，请刷新页面', 'info');
                            }
                        }
                    });
                });
            } catch (error) {
                console.error('Service Worker注册失败:', error);
            }
        }
    }
    
    setupInstallPrompt() {
        window.addEventListener('beforeinstallprompt', (e) => {
            e.preventDefault();
            this.deferredPrompt = e;
            document.getElementById('install-btn').style.display = 'block';
        });
    }
    
    async installApp() {
        if (this.deferredPrompt) {
            this.deferredPrompt.prompt();
            const { outcome } = await this.deferredPrompt.userChoice;
            
            if (outcome === 'accepted') {
                this.showToast('应用安装成功', 'success');
            }
            
            this.deferredPrompt = null;
            document.getElementById('install-btn').style.display = 'none';
        }
    }
    
    async setupNotifications() {
        if ('Notification' in window) {
            const permission = Notification.permission;
            const btn = document.getElementById('notification-btn');
            
            if (permission === 'granted') {
                btn.textContent = '🔔 通知已启用';
                btn.disabled = true;
            } else if (permission === 'denied') {
                btn.textContent = '🔕 通知被禁用';
                btn.disabled = true;
            }
        }
    }
    
    async enableNotifications() {
        if ('Notification' in window) {
            const permission = await Notification.requestPermission();
            const btn = document.getElementById('notification-btn');
            
            if (permission === 'granted') {
                btn.textContent = '🔔 通知已启用';
                btn.disabled = true;
                this.showToast('通知权限已授予', 'success');
                
                // 发送测试通知
                new Notification('PWA Todo', {
                    body: '通知功能已启用！',
                    icon: '/images/icon-192x192.png'
                });
            } else {
                this.showToast('通知权限被拒绝', 'error');
            }
        }
    }
    
    updateOnlineStatus() {
        const indicator = document.getElementById('online-status');
        const syncIndicator = document.getElementById('sync-status');
        
        if (navigator.onLine) {
            indicator.textContent = '🟢';
            indicator.title = '在线';
            syncIndicator.textContent = '✅';
            syncIndicator.title = '已同步';
        } else {
            indicator.textContent = '🔴';
            indicator.title = '离线';
            syncIndicator.textContent = '⏳';
            syncIndicator.title = '等待同步';
        }
    }
    
    showToast(message, type = 'info') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.textContent = message;
        
        container.appendChild(toast);
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
}

// 初始化应用
const app = new TodoApp();
```

### manifest.json

```json
{
    "name": "PWA Todo应用",
    "short_name": "PWA Todo",
    "description": "一个功能完整的PWA待办事项应用",
    "start_url": "/",
    "display": "standalone",
    "background_color": "#667eea",
    "theme_color": "#2196F3",
    "orientation": "portrait-primary",
    "icons": [
        {
            "src": "images/icon-192x192.png",
            "sizes": "192x192",
            "type": "image/png",
            "purpose": "any maskable"
        },
        {
            "src": "images/icon-512x512.png",
            "sizes": "512x512",
            "type": "image/png",
            "purpose": "any maskable"
        }
    ],
    "categories": ["productivity", "utilities"],
    "lang": "zh-CN",
    "dir": "ltr"
}
```

## 最佳实践

### 1. 缓存策略选择

```javascript
// 根据资源类型选择合适的缓存策略
const getCacheStrategy = (request) => {
    const url = new URL(request.url);
    
    // 静态资源：缓存优先
    if (request.destination === 'style' || 
        request.destination === 'script' || 
        request.destination === 'image') {
        return 'cacheFirst';
    }
    
    // API请求：网络优先
    if (url.pathname.startsWith('/api/')) {
        return 'networkFirst';
    }
    
    // HTML页面：后台更新
    if (request.destination === 'document') {
        return 'staleWhileRevalidate';
    }
    
    return 'networkFirst';
};
```

### 2. 缓存版本管理

```javascript
// 使用版本号管理缓存
const CACHE_VERSION = 'v1.2.0';
const CACHE_NAME = `my-app-${CACHE_VERSION}`;

// 清理旧版本缓存
self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(cacheNames => {
            return Promise.all(
                cacheNames
                    .filter(cacheName => 
                        cacheName.startsWith('my-app-') && 
                        cacheName !== CACHE_NAME
                    )
                    .map(cacheName => caches.delete(cacheName))
            );
        })
    );
});
```

### 3. 错误处理和重试机制

```javascript
// 带重试机制的网络请求
async function fetchWithRetry(request, maxRetries = 3) {
    for (let i = 0; i <= maxRetries; i++) {
        try {
            const response = await fetch(request);
            if (response.ok) {
                return response;
            }
            throw new Error(`HTTP ${response.status}`);
        } catch (error) {
            if (i === maxRetries) {
                throw error;
            }
            // 指数退避
            await new Promise(resolve => 
                setTimeout(resolve, Math.pow(2, i) * 1000)
            );
        }
    }
}
```

### 4. 性能优化

```javascript
// 预缓存关键资源
const PRECACHE_URLS = [
    '/',
    '/styles.css',
    '/app.js',
    '/critical-image.jpg'
];

// 运行时缓存非关键资源
const RUNTIME_CACHE_URLS = [
    '/api/data',
    '/images/',
    '/fonts/'
];

// 限制缓存大小
async function limitCacheSize(cacheName, maxItems) {
    const cache = await caches.open(cacheName);
    const keys = await cache.keys();
    
    if (keys.length > maxItems) {
        const keysToDelete = keys.slice(0, keys.length - maxItems);
        await Promise.all(
            keysToDelete.map(key => cache.delete(key))
        );
    }
}
```

## 常见问题与解决方案

### 1. Service Worker不更新

**问题**: Service Worker缓存了自身，导致更新不生效

**解决方案**:
```javascript
// 在Service Worker中跳过等待
self.addEventListener('install', event => {
    event.waitUntil(
        // ... 缓存操作
        self.skipWaiting() // 强制激活新版本
    );
});

// 在客户端检测更新
navigator.serviceWorker.addEventListener('controllerchange', () => {
    window.location.reload();
});
```

### 2. 缓存过期问题

**问题**: 缓存的资源过期或损坏

**解决方案**:
```javascript
// 添加缓存验证
async function getCachedResponse(request) {
    const cache = await caches.open(CACHE_NAME);
    const response = await cache.match(request);
    
    if (response) {
        const cachedDate = new Date(response.headers.get('date'));
        const now = new Date();
        const maxAge = 24 * 60 * 60 * 1000; // 24小时
        
        if (now - cachedDate > maxAge) {
            await cache.delete(request);
            return null;
        }
    }
    
    return response;
}
```

### 3. 内存泄漏问题

**问题**: Service Worker占用过多内存

**解决方案**:
```javascript
// 定期清理缓存
self.addEventListener('message', event => {
    if (event.data === 'CLEANUP_CACHE') {
        cleanupCache();
    }
});

async function cleanupCache() {
    const cacheNames = await caches.keys();
    const promises = cacheNames.map(async cacheName => {
        const cache = await caches.open(cacheName);
        const requests = await cache.keys();
        
        // 删除过期的缓存项
        const deletePromises = requests
            .filter(request => shouldDeleteCache(request))
            .map(request => cache.delete(request));
            
        return Promise.all(deletePromises);
    });
    
    await Promise.all(promises);
}
```

### 4. 调试技巧

```javascript
// 添加详细日志
const DEBUG = true;

function log(...args) {
    if (DEBUG) {
        console.log('[SW]', ...args);
    }
}

// 监控Service Worker状态
navigator.serviceWorker.addEventListener('statechange', event => {
    log('Service Worker状态变化:', event.target.state);
});

// 性能监控
self.addEventListener('fetch', event => {
    const start = performance.now();
    
    event.respondWith(
        handleRequest(event.request).then(response => {
            const end = performance.now();
            log(`请求处理耗时: ${end - start}ms`, event.request.url);
            return response;
        })
    );
});
```

---

## 总结

Service Worker是现代Web应用的重要技术，它为Web应用提供了类似原生应用的功能。通过本指南的学习，您应该能够：

1. 理解Service Worker的工作原理和生命周期
2. 实现各种缓存策略
3. 创建离线功能
4. 实现推送通知和后台同步
5. 构建完整的PWA应用

记住，Service Worker的强大功能需要谨慎使用，始终考虑用户体验和性能影响。在生产环境中部署前，请充分测试各种场景，包括网络状况不佳的情况。

继续学习和实践，您将能够创建出色的Progressive Web Applications！