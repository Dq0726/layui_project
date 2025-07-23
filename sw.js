// sw.js - PWA Todo应用的Service Worker
const CACHE_NAME = 'pwa-todo-v1.0.0';
const OFFLINE_URL = '/offline.html';

// 需要预缓存的资源
const PRECACHE_URLS = [
    '/',
    '/index.html',
    '/styles.css',
    '/app.js',
    '/manifest.json',
    '/offline.html',
    '/images/icon-192x192.png',
    '/images/icon-512x512.png'
];

// 运行时缓存的资源模式
const RUNTIME_CACHE_PATTERNS = [
    /\.(?:png|jpg|jpeg|svg|gif|webp)$/,
    /\.(?:js|css)$/,
    /^https:\/\/fonts\.googleapis\.com/,
    /^https:\/\/fonts\.gstatic\.com/
];

// 安装事件 - 预缓存资源
self.addEventListener('install', event => {
    console.log('[SW] 安装中...');
    
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                console.log('[SW] 预缓存资源');
                return cache.addAll(PRECACHE_URLS);
            })
            .then(() => {
                console.log('[SW] 安装完成，跳过等待');
                return self.skipWaiting();
            })
            .catch(error => {
                console.error('[SW] 安装失败:', error);
            })
    );
});

// 激活事件 - 清理旧缓存
self.addEventListener('activate', event => {
    console.log('[SW] 激活中...');
    
    event.waitUntil(
        Promise.all([
            // 清理旧版本缓存
            caches.keys().then(cacheNames => {
                return Promise.all(
                    cacheNames
                        .filter(cacheName => 
                            cacheName.startsWith('pwa-todo-') && 
                            cacheName !== CACHE_NAME
                        )
                        .map(cacheName => {
                            console.log('[SW] 删除旧缓存:', cacheName);
                            return caches.delete(cacheName);
                        })
                );
            }),
            // 立即控制所有客户端
            self.clients.claim()
        ]).then(() => {
            console.log('[SW] 激活完成');
        })
    );
});

// 拦截网络请求
self.addEventListener('fetch', event => {
    const { request } = event;
    const url = new URL(request.url);
    
    // 跳过非GET请求
    if (request.method !== 'GET') {
        return;
    }
    
    // 跳过chrome-extension和其他协议
    if (!url.protocol.startsWith('http')) {
        return;
    }
    
    // 处理导航请求（页面请求）
    if (request.mode === 'navigate') {
        event.respondWith(handleNavigationRequest(request));
        return;
    }
    
    // 处理其他资源请求
    event.respondWith(handleResourceRequest(request));
});

// 处理页面导航请求
async function handleNavigationRequest(request) {
    try {
        // 尝试网络请求
        const response = await fetch(request);
        
        // 成功则缓存并返回
        if (response.ok) {
            const cache = await caches.open(CACHE_NAME);
            cache.put(request, response.clone());
            return response;
        }
        
        throw new Error(`HTTP ${response.status}`);
    } catch (error) {
        console.log('[SW] 网络请求失败，返回缓存或离线页面:', error.message);
        
        // 先尝试返回缓存的页面
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            return cachedResponse;
        }
        
        // 最后返回离线页面
        const cache = await caches.open(CACHE_NAME);
        return cache.match(OFFLINE_URL);
    }
}

// 处理资源请求
async function handleResourceRequest(request) {
    const url = new URL(request.url);
    
    // 检查是否为需要运行时缓存的资源
    const shouldCache = RUNTIME_CACHE_PATTERNS.some(pattern => 
        pattern.test(url.pathname) || pattern.test(url.href)
    );
    
    if (shouldCache) {
        return handleCacheFirstStrategy(request);
    } else {
        return handleNetworkFirstStrategy(request);
    }
}

// 缓存优先策略（适用于静态资源）
async function handleCacheFirstStrategy(request) {
    try {
        // 先检查缓存
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            // 后台更新缓存
            updateCacheInBackground(request);
            return cachedResponse;
        }
        
        // 缓存中没有，发起网络请求
        const response = await fetch(request);
        
        if (response.ok) {
            const cache = await caches.open(CACHE_NAME);
            cache.put(request, response.clone());
        }
        
        return response;
    } catch (error) {
        console.log('[SW] 缓存优先策略失败:', error.message);
        
        // 网络失败，尝试返回缓存
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            return cachedResponse;
        }
        
        // 都失败了，返回错误响应
        return new Response('Network error', { 
            status: 408,
            statusText: 'Network timeout' 
        });
    }
}

// 网络优先策略（适用于动态内容）
async function handleNetworkFirstStrategy(request) {
    try {
        // 先尝试网络请求
        const response = await fetchWithTimeout(request, 3000);
        
        if (response.ok) {
            // 成功则更新缓存
            const cache = await caches.open(CACHE_NAME);
            cache.put(request, response.clone());
        }
        
        return response;
    } catch (error) {
        console.log('[SW] 网络优先策略失败，尝试缓存:', error.message);
        
        // 网络失败，返回缓存
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            return cachedResponse;
        }
        
        // 都失败了
        throw error;
    }
}

// 后台更新缓存
async function updateCacheInBackground(request) {
    try {
        const response = await fetch(request);
        if (response.ok) {
            const cache = await caches.open(CACHE_NAME);
            await cache.put(request, response);
            console.log('[SW] 后台更新缓存成功:', request.url);
        }
    } catch (error) {
        console.log('[SW] 后台更新缓存失败:', error.message);
    }
}

// 带超时的fetch请求
function fetchWithTimeout(request, timeout = 5000) {
    return Promise.race([
        fetch(request),
        new Promise((_, reject) => 
            setTimeout(() => reject(new Error('Network timeout')), timeout)
        )
    ]);
}

// 推送通知处理
self.addEventListener('push', event => {
    console.log('[SW] 收到推送消息');
    
    let notificationData = {
        title: 'PWA Todo',
        body: '您有新的待办事项提醒',
        icon: '/images/icon-192x192.png',
        badge: '/images/icon-192x192.png',
        vibrate: [100, 50, 100],
        data: {
            dateOfArrival: Date.now(),
            primaryKey: 1
        },
        actions: [
            {
                action: 'explore',
                title: '查看',
                icon: '/images/icon-192x192.png'
            },
            {
                action: 'close',
                title: '关闭',
                icon: '/images/icon-192x192.png'
            }
        ],
        requireInteraction: true,
        tag: 'todo-notification'
    };
    
    // 如果有推送数据，使用推送的内容
    if (event.data) {
        try {
            const pushData = event.data.json();
            notificationData = { ...notificationData, ...pushData };
        } catch (error) {
            console.error('[SW] 解析推送数据失败:', error);
        }
    }
    
    event.waitUntil(
        self.registration.showNotification(notificationData.title, notificationData)
    );
});

// 通知点击处理
self.addEventListener('notificationclick', event => {
    console.log('[SW] 通知被点击:', event.action);
    
    event.notification.close();
    
    const action = event.action;
    const notificationData = event.notification.data || {};
    
    if (action === 'explore') {
        // 打开应用
        event.waitUntil(openApp('/'));
    } else if (action === 'close') {
        // 只关闭通知，不做其他操作
        console.log('[SW] 通知已关闭');
    } else {
        // 默认行为：打开应用
        event.waitUntil(openApp('/'));
    }
});

// 打开应用
async function openApp(url = '/') {
    const clients = await self.clients.matchAll({
        type: 'window',
        includeUncontrolled: true
    });
    
    // 如果已有窗口打开，则聚焦到该窗口
    for (const client of clients) {
        if (client.url.includes(url) && 'focus' in client) {
            return client.focus();
        }
    }
    
    // 否则打开新窗口
    if (self.clients.openWindow) {
        return self.clients.openWindow(url);
    }
}

// 后台同步处理
self.addEventListener('sync', event => {
    console.log('[SW] 后台同步事件:', event.tag);
    
    if (event.tag === 'todo-sync') {
        event.waitUntil(syncTodoData());
    }
});

// 同步待办事项数据
async function syncTodoData() {
    try {
        console.log('[SW] 开始同步待办事项数据');
        
        // 这里可以实现具体的同步逻辑
        // 比如从IndexedDB获取待同步的数据，发送到服务器
        
        // 示例：通知所有客户端同步完成
        const clients = await self.clients.matchAll();
        clients.forEach(client => {
            client.postMessage({
                type: 'SYNC_COMPLETE',
                timestamp: Date.now()
            });
        });
        
        console.log('[SW] 数据同步完成');
    } catch (error) {
        console.error('[SW] 数据同步失败:', error);
    }
}

// 消息处理
self.addEventListener('message', event => {
    console.log('[SW] 收到消息:', event.data);
    
    const { type, data } = event.data || {};
    
    switch (type) {
        case 'SKIP_WAITING':
            self.skipWaiting();
            break;
            
        case 'GET_VERSION':
            event.ports[0].postMessage({
                type: 'VERSION',
                version: CACHE_NAME
            });
            break;
            
        case 'CLEANUP_CACHE':
            event.waitUntil(cleanupCache());
            break;
            
        case 'FORCE_UPDATE':
            event.waitUntil(forceUpdateCache());
            break;
            
        default:
            console.log('[SW] 未知消息类型:', type);
    }
});

// 清理缓存
async function cleanupCache() {
    try {
        console.log('[SW] 开始清理缓存');
        
        const cache = await caches.open(CACHE_NAME);
        const requests = await cache.keys();
        
        // 删除过期的缓存项（这里可以根据实际需求实现）
        const deletePromises = requests
            .filter(request => {
                // 示例：删除超过7天的缓存
                const url = new URL(request.url);
                return url.searchParams.has('timestamp') && 
                       Date.now() - parseInt(url.searchParams.get('timestamp')) > 7 * 24 * 60 * 60 * 1000;
            })
            .map(request => cache.delete(request));
            
        await Promise.all(deletePromises);
        console.log('[SW] 缓存清理完成');
    } catch (error) {
        console.error('[SW] 缓存清理失败:', error);
    }
}

// 强制更新缓存
async function forceUpdateCache() {
    try {
        console.log('[SW] 强制更新缓存');
        
        const cache = await caches.open(CACHE_NAME);
        
        // 重新缓存所有预缓存资源
        await Promise.all(
            PRECACHE_URLS.map(async url => {
                try {
                    const response = await fetch(url, { cache: 'no-cache' });
                    if (response.ok) {
                        await cache.put(url, response);
                    }
                } catch (error) {
                    console.error('[SW] 更新缓存失败:', url, error);
                }
            })
        );
        
        console.log('[SW] 缓存更新完成');
    } catch (error) {
        console.error('[SW] 强制更新缓存失败:', error);
    }
}

// 错误处理
self.addEventListener('error', event => {
    console.error('[SW] Service Worker错误:', event.error);
});

self.addEventListener('unhandledrejection', event => {
    console.error('[SW] 未处理的Promise拒绝:', event.reason);
});

console.log('[SW] Service Worker已加载');