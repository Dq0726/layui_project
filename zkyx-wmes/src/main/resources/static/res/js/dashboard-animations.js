/**
 * MES智能制造大数据看板 - 动画效果增强库
 * 提供丰富的动画效果和交互体验
 * @version 1.0.0
 * @author MES Team
 */

(function(window) {
    'use strict';
    
    // 动画效果管理器
    const DashboardAnimations = {
        // 初始化所有动画效果
        init: function() {
            this.initParticleSystem();
            this.initDataFlowEffects();
            this.initNumberCounters();
            this.initProgressBars();
            this.initWaveEffects();
            this.initGlowEffects();
            this.initFloatingElements();
            this.initInteractiveEffects();
            this.startAnimationLoop();
        },
        
        // 粒子系统
        initParticleSystem: function() {
            const particlesContainer = document.getElementById('particles-bg');
            if (!particlesContainer) return;
            
            const particleCount = window.innerWidth > 3840 ? 80 : 50;
            const particles = [];
            
            // 创建粒子
            for (let i = 0; i < particleCount; i++) {
                const particle = document.createElement('div');
                particle.className = 'particle';
                particle.style.left = Math.random() * 100 + '%';
                particle.style.top = Math.random() * 100 + '%';
                particle.style.animationDelay = Math.random() * 6 + 's';
                particle.style.animationDuration = (Math.random() * 4 + 4) + 's';
                
                // 随机粒子大小和颜色
                const size = Math.random() * 3 + 1;
                const colors = ['#00bfff', '#00ff80', '#ff6b6b', '#ffd93d'];
                const color = colors[Math.floor(Math.random() * colors.length)];
                
                particle.style.width = size + 'px';
                particle.style.height = size + 'px';
                particle.style.background = color;
                particle.style.boxShadow = `0 0 ${size * 3}px ${color}`;
                
                particlesContainer.appendChild(particle);
                particles.push(particle);
            }
            
            // 鼠标交互效果
            particlesContainer.addEventListener('mousemove', function(e) {
                const rect = particlesContainer.getBoundingClientRect();
                const x = (e.clientX - rect.left) / rect.width * 100;
                const y = (e.clientY - rect.top) / rect.height * 100;
                
                particles.forEach((particle, index) => {
                    const particleX = parseFloat(particle.style.left);
                    const particleY = parseFloat(particle.style.top);
                    const distance = Math.sqrt(Math.pow(x - particleX, 2) + Math.pow(y - particleY, 2));
                    
                    if (distance < 15) {
                        const angle = Math.atan2(particleY - y, particleX - x);
                        const force = (15 - distance) / 15;
                        const moveX = Math.cos(angle) * force * 5;
                        const moveY = Math.sin(angle) * force * 5;
                        
                        particle.style.transform = `translate(${moveX}px, ${moveY}px) scale(${1 + force})`;
                        particle.style.opacity = Math.min(1, 0.6 + force * 0.4);
                    } else {
                        particle.style.transform = '';
                        particle.style.opacity = '';
                    }
                });
            });
        },
        
        // 数据流动效果
        initDataFlowEffects: function() {
            const centralArea = document.querySelector('.central-area');
            if (!centralArea) return;
            
            setInterval(() => {
                this.createDataFlow(centralArea);
            }, 800);
            
            // 创建数据流光束
            setInterval(() => {
                this.createDataStream();
            }, 2000);
        },
        
        // 创建数据流粒子
        createDataFlow: function(container) {
            const flow = document.createElement('div');
            flow.className = 'data-flow';
            flow.style.left = Math.random() * 80 + 10 + '%';
            flow.style.top = Math.random() * 80 + 10 + '%';
            
            // 随机颜色和大小
            const colors = ['#00ff80', '#00bfff', '#ff6b6b', '#ffd93d'];
            const color = colors[Math.floor(Math.random() * colors.length)];
            const size = Math.random() * 4 + 3;
            
            flow.style.background = `radial-gradient(circle, ${color}, transparent)`;
            flow.style.width = size + 'px';
            flow.style.height = size + 'px';
            flow.style.boxShadow = `0 0 ${size * 3}px ${color}`;
            
            container.appendChild(flow);
            
            setTimeout(() => {
                if (flow.parentNode) {
                    flow.parentNode.removeChild(flow);
                }
            }, 4000);
        },
        
        // 创建数据流光束
        createDataStream: function() {
            const streamCount = Math.random() * 3 + 2;
            for (let i = 0; i < streamCount; i++) {
                const stream = document.createElement('div');
                stream.className = 'data-stream';
                stream.style.left = Math.random() * 100 + '%';
                stream.style.animationDelay = Math.random() * 2 + 's';
                stream.style.animationDuration = (Math.random() * 2 + 2) + 's';
                
                // 随机方向
                if (Math.random() > 0.5) {
                    stream.style.transform = 'rotate(45deg)';
                }
                
                document.body.appendChild(stream);
                
                setTimeout(() => {
                    if (stream.parentNode) {
                        stream.parentNode.removeChild(stream);
                    }
                }, 3000);
            }
        },
        
        // 数字计数器动画
        initNumberCounters: function() {
            const counters = document.querySelectorAll('.digital-number');
            counters.forEach(counter => {
                this.animateCounter(counter);
            });
        },
        
        // 数字滚动动画
        animateCounter: function(element, targetValue, duration = 2000) {
            if (!element) return;
            
            const startValue = parseInt(element.textContent.replace(/,/g, '')) || 0;
            const target = targetValue !== undefined ? targetValue : startValue;
            const increment = (target - startValue) / (duration / 16);
            let currentValue = startValue;
            
            const timer = setInterval(() => {
                currentValue += increment;
                if ((increment > 0 && currentValue >= target) || 
                    (increment < 0 && currentValue <= target)) {
                    currentValue = target;
                    clearInterval(timer);
                }
                
                // 添加千分位分隔符
                const displayValue = Math.floor(currentValue).toLocaleString();
                element.textContent = displayValue;
                
                // 添加闪烁效果
                if (Math.abs(currentValue - target) < Math.abs(increment * 5)) {
                    element.style.textShadow = '0 0 20px currentColor';
                    setTimeout(() => {
                        element.style.textShadow = '';
                    }, 100);
                }
            }, 16);
        },
        
        // 进度条动画
        initProgressBars: function() {
            const progressElements = document.querySelectorAll('.node-value');
            progressElements.forEach(element => {
                this.animateProgress(element);
            });
        },
        
        // 进度动画
        animateProgress: function(element) {
            if (!element) return;
            
            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const targetValue = parseFloat(entry.target.textContent);
                        this.animateProgressValue(entry.target, targetValue);
                    }
                });
            });
            
            observer.observe(element);
        },
        
        // 进度值动画
        animateProgressValue: function(element, targetValue) {
            let currentValue = 0;
            const increment = targetValue / 60; // 60帧动画
            
            const animate = () => {
                currentValue += increment;
                if (currentValue >= targetValue) {
                    currentValue = targetValue;
                } else {
                    requestAnimationFrame(animate);
                }
                
                element.textContent = currentValue.toFixed(1) + '%';
                
                // 根据数值调整颜色
                if (currentValue >= 95) {
                    element.style.color = '#00ff80';
                } else if (currentValue >= 80) {
                    element.style.color = '#ffd93d';
                } else {
                    element.style.color = '#ff6b6b';
                }
            };
            
            animate();
        },
        
        // 波浪效果
        initWaveEffects: function() {
            const waveElements = document.querySelectorAll('.data-card, .stat-card');
            waveElements.forEach(element => {
                element.addEventListener('mouseenter', () => {
                    this.createWaveEffect(element);
                });
            });
        },
        
        // 创建波浪效果
        createWaveEffect: function(element) {
            const wave = document.createElement('div');
            wave.style.position = 'absolute';
            wave.style.borderRadius = '50%';
            wave.style.background = 'radial-gradient(circle, rgba(0, 191, 255, 0.3), transparent)';
            wave.style.width = '20px';
            wave.style.height = '20px';
            wave.style.left = '50%';
            wave.style.top = '50%';
            wave.style.transform = 'translate(-50%, -50%)';
            wave.style.pointerEvents = 'none';
            wave.style.zIndex = '1';
            
            element.style.position = 'relative';
            element.appendChild(wave);
            
            // 波浪扩散动画
            wave.animate([
                { transform: 'translate(-50%, -50%) scale(0)', opacity: 1 },
                { transform: 'translate(-50%, -50%) scale(10)', opacity: 0 }
            ], {
                duration: 600,
                easing: 'ease-out'
            }).onfinish = () => {
                wave.remove();
            };
        },
        
        // 发光效果
        initGlowEffects: function() {
            const glowElements = document.querySelectorAll('.process-node, .card-icon');
            glowElements.forEach(element => {
                this.addGlowEffect(element);
            });
        },
        
        // 添加发光效果
        addGlowEffect: function(element) {
            const originalBoxShadow = getComputedStyle(element).boxShadow;
            
            element.addEventListener('mouseenter', () => {
                element.style.boxShadow = `${originalBoxShadow}, 0 0 30px rgba(0, 191, 255, 0.6)`;
            });
            
            element.addEventListener('mouseleave', () => {
                element.style.boxShadow = originalBoxShadow;
            });
        },
        
        // 浮动元素
        initFloatingElements: function() {
            const floatingElements = document.querySelectorAll('.process-node');
            floatingElements.forEach((element, index) => {
                this.addFloatingAnimation(element, index);
            });
        },
        
        // 添加浮动动画
        addFloatingAnimation: function(element, index) {
            const baseDelay = index * 0.5;
            const amplitude = 10 + Math.random() * 10;
            const frequency = 0.02 + Math.random() * 0.01;
            
            let startTime = Date.now();
            
            const animate = () => {
                const elapsed = (Date.now() - startTime) / 1000;
                const y = Math.sin(elapsed * frequency + baseDelay) * amplitude;
                const rotation = Math.sin(elapsed * frequency * 0.5 + baseDelay) * 2;
                
                element.style.transform = `translateY(${y}px) rotate(${rotation}deg)`;
                
                requestAnimationFrame(animate);
            };
            
            animate();
        },
        
        // 交互效果
        initInteractiveEffects: function() {
            // 卡片倾斜效果
            const cards = document.querySelectorAll('.data-card, .stat-card');
            cards.forEach(card => {
                this.addTiltEffect(card);
            });
            
            // 按钮点击效果
            const buttons = document.querySelectorAll('.nav-item');
            buttons.forEach(button => {
                this.addClickEffect(button);
            });
        },
        
        // 添加倾斜效果
        addTiltEffect: function(element) {
            element.addEventListener('mousemove', (e) => {
                const rect = element.getBoundingClientRect();
                const x = e.clientX - rect.left;
                const y = e.clientY - rect.top;
                
                const centerX = rect.width / 2;
                const centerY = rect.height / 2;
                
                const rotateX = (y - centerY) / centerY * -10;
                const rotateY = (x - centerX) / centerX * 10;
                
                element.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) translateZ(10px)`;
            });
            
            element.addEventListener('mouseleave', () => {
                element.style.transform = '';
            });
        },
        
        // 添加点击效果
        addClickEffect: function(element) {
            element.addEventListener('click', (e) => {
                const ripple = document.createElement('div');
                const rect = element.getBoundingClientRect();
                const size = Math.max(rect.width, rect.height);
                const x = e.clientX - rect.left - size / 2;
                const y = e.clientY - rect.top - size / 2;
                
                ripple.style.width = ripple.style.height = size + 'px';
                ripple.style.left = x + 'px';
                ripple.style.top = y + 'px';
                ripple.style.position = 'absolute';
                ripple.style.borderRadius = '50%';
                ripple.style.background = 'rgba(0, 191, 255, 0.4)';
                ripple.style.pointerEvents = 'none';
                ripple.style.transform = 'scale(0)';
                
                element.style.position = 'relative';
                element.appendChild(ripple);
                
                ripple.animate([
                    { transform: 'scale(0)', opacity: 1 },
                    { transform: 'scale(1)', opacity: 0 }
                ], {
                    duration: 600,
                    easing: 'ease-out'
                }).onfinish = () => {
                    ripple.remove();
                };
            });
        },
        
        // 动画循环
        startAnimationLoop: function() {
            let lastTime = 0;
            
            const animate = (currentTime) => {
                const deltaTime = currentTime - lastTime;
                lastTime = currentTime;
                
                // 更新粒子位置
                this.updateParticles(deltaTime);
                
                // 更新数据流
                this.updateDataFlows(deltaTime);
                
                // 更新发光效果
                this.updateGlowEffects(currentTime);
                
                requestAnimationFrame(animate);
            };
            
            requestAnimationFrame(animate);
        },
        
        // 更新粒子
        updateParticles: function(deltaTime) {
            const particles = document.querySelectorAll('.particle');
            particles.forEach(particle => {
                if (Math.random() < 0.001) { // 随机闪烁
                    particle.style.opacity = Math.random() * 0.5 + 0.5;
                    setTimeout(() => {
                        particle.style.opacity = '';
                    }, 100);
                }
            });
        },
        
        // 更新数据流
        updateDataFlows: function(deltaTime) {
            const flows = document.querySelectorAll('.data-flow');
            flows.forEach(flow => {
                // 添加随机轨迹变化
                if (Math.random() < 0.1) {
                    const currentTransform = flow.style.transform || '';
                    const randomOffset = (Math.random() - 0.5) * 10;
                    flow.style.transform = currentTransform + ` translateX(${randomOffset}px)`;
                }
            });
        },
        
        // 更新发光效果
        updateGlowEffects: function(currentTime) {
            const glowElements = document.querySelectorAll('.process-node, .logo');
            glowElements.forEach((element, index) => {
                const intensity = Math.sin(currentTime * 0.002 + index) * 0.3 + 0.7;
                const currentBoxShadow = getComputedStyle(element).boxShadow;
                
                if (currentBoxShadow !== 'none') {
                    element.style.filter = `brightness(${intensity})`;
                }
            });
        },
        
        // 响应式调整
        handleResize: function() {
            const width = window.innerWidth;
            const container = document.querySelector('.dashboard-container');
            
            if (width >= 3840) {
                // 超宽屏特殊效果
                this.enableUltraWideEffects();
            } else {
                this.disableUltraWideEffects();
            }
        },
        
        // 启用超宽屏效果
        enableUltraWideEffects: function() {
            // 添加更多粒子
            const particlesContainer = document.getElementById('particles-bg');
            if (particlesContainer && particlesContainer.children.length < 80) {
                for (let i = 0; i < 30; i++) {
                    const particle = document.createElement('div');
                    particle.className = 'particle ultra-wide-particle';
                    particle.style.left = Math.random() * 100 + '%';
                    particle.style.top = Math.random() * 100 + '%';
                    particle.style.animationDelay = Math.random() * 6 + 's';
                    particle.style.animationDuration = (Math.random() * 4 + 4) + 's';
                    particlesContainer.appendChild(particle);
                }
            }
            
            // 增强数据流效果
            const centralArea = document.querySelector('.central-area');
            if (centralArea) {
                centralArea.classList.add('ultra-wide-enhanced');
            }
        },
        
        // 禁用超宽屏效果
        disableUltraWideEffects: function() {
            const ultraWideParticles = document.querySelectorAll('.ultra-wide-particle');
            ultraWideParticles.forEach(particle => {
                particle.remove();
            });
            
            const centralArea = document.querySelector('.central-area');
            if (centralArea) {
                centralArea.classList.remove('ultra-wide-enhanced');
            }
        },
        
        // 性能优化
        optimizePerformance: function() {
            // 减少低性能设备上的动画
            const isLowPerformance = navigator.hardwareConcurrency < 4 || 
                                    window.devicePixelRatio < 2;
            
            if (isLowPerformance) {
                document.body.classList.add('low-performance');
                
                // 减少粒子数量
                const particles = document.querySelectorAll('.particle');
                particles.forEach((particle, index) => {
                    if (index % 2 === 0) {
                        particle.remove();
                    }
                });
                
                // 简化动画
                const complexAnimations = document.querySelectorAll('[style*="animation"]');
                complexAnimations.forEach(element => {
                    element.style.animationDuration = '0.5s';
                });
            }
        },
        
        // 主题切换动画
        switchTheme: function(theme) {
            const body = document.body;
            body.classList.add('theme-switching');
            
            setTimeout(() => {
                body.className = body.className.replace(/theme-\w+/g, '');
                body.classList.add(`theme-${theme}`);
                
                setTimeout(() => {
                    body.classList.remove('theme-switching');
                }, 300);
            }, 150);
        },
        
        // 销毁动画
        destroy: function() {
            // 清理事件监听器
            const elements = document.querySelectorAll('.particle, .data-flow, .data-stream');
            elements.forEach(element => {
                element.remove();
            });
            
            // 清理样式
            const styledElements = document.querySelectorAll('[style*="transform"], [style*="animation"]');
            styledElements.forEach(element => {
                element.style.transform = '';
                element.style.animation = '';
            });
        }
    };
    
    // 导出到全局
    window.DashboardAnimations = DashboardAnimations;
    
    // 自动初始化
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            DashboardAnimations.init();
            DashboardAnimations.optimizePerformance();
        });
    } else {
        DashboardAnimations.init();
        DashboardAnimations.optimizePerformance();
    }
    
    // 监听窗口大小变化
    window.addEventListener('resize', () => {
        DashboardAnimations.handleResize();
    });
    
    // 页面卸载时清理
    window.addEventListener('beforeunload', () => {
        DashboardAnimations.destroy();
    });
    
})(window);