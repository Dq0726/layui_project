/**
 * Beautify.js - 科技感大屏美化与动画模块
 * 基于 GSAP + ECharts-GL + Layui
 */

layui.define(['jquery'], function(exports) {
  const $ = layui.jquery;
  
  // 模拟黄冈市 GeoJSON 数据
  const huanggangGeoJSON = {
    type: "FeatureCollection",
    features: [
      {
        type: "Feature",
        properties: { name: "黄州区", value: 850 },
        geometry: {
          type: "Polygon",
          coordinates: [[[114.8, 30.4], [115.0, 30.4], [115.0, 30.6], [114.8, 30.6], [114.8, 30.4]]]
        }
      },
      {
        type: "Feature", 
        properties: { name: "团风县", value: 720 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.0, 30.4], [115.2, 30.4], [115.2, 30.6], [115.0, 30.6], [115.0, 30.4]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "红安县", value: 680 },
        geometry: {
          type: "Polygon", 
          coordinates: [[[114.8, 30.6], [115.0, 30.6], [115.0, 30.8], [114.8, 30.8], [114.8, 30.6]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "罗田县", value: 590 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.0, 30.6], [115.2, 30.6], [115.2, 30.8], [115.0, 30.8], [115.0, 30.6]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "英山县", value: 520 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.2, 30.4], [115.4, 30.4], [115.4, 30.6], [115.2, 30.6], [115.2, 30.4]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "浠水县", value: 780 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.2, 30.6], [115.4, 30.6], [115.4, 30.8], [115.2, 30.8], [115.2, 30.6]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "蕲春县", value: 660 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.4, 30.4], [115.6, 30.4], [115.6, 30.6], [115.4, 30.6], [115.4, 30.4]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "黄梅县", value: 710 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.4, 30.6], [115.6, 30.6], [115.6, 30.8], [115.4, 30.8], [115.4, 30.6]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "武穴市", value: 820 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.6, 30.4], [115.8, 30.4], [115.8, 30.6], [115.6, 30.6], [115.6, 30.4]]]
        }
      },
      {
        type: "Feature",
        properties: { name: "麻城市", value: 890 },
        geometry: {
          type: "Polygon",
          coordinates: [[[115.6, 30.6], [115.8, 30.6], [115.8, 30.8], [115.6, 30.8], [115.6, 30.6]]]
        }
      }
    ]
  };

  // ECharts Cyber 主题配置
  const cyberTheme = {
    color: ['#00C6FF', '#0072FF', '#FFFFFF', 'rgba(0,198,255,0.5)'],
    backgroundColor: 'transparent',
    textStyle: {
      fontFamily: 'Microsoft YaHei',
      color: '#FFFFFF'
    },
    title: {
      textStyle: {
        color: '#00C6FF',
        fontWeight: 'bold'
      }
    },
    tooltip: {
      backgroundColor: 'rgba(5,5,15,0.9)',
      borderColor: '#00C6FF',
      textStyle: { color: '#FFFFFF' }
    },
    legend: {
      textStyle: { color: '#FFFFFF' }
    },
    grid: {
      borderColor: 'rgba(0,198,255,0.2)'
    },
    categoryAxis: {
      axisLine: { lineStyle: { color: 'rgba(0,198,255,0.5)' }},
      axisTick: { lineStyle: { color: 'rgba(0,198,255,0.5)' }},
      axisLabel: { textStyle: { color: '#FFFFFF' }},
      splitLine: { lineStyle: { color: 'rgba(0,198,255,0.1)' }}
    },
    valueAxis: {
      axisLine: { lineStyle: { color: 'rgba(0,198,255,0.5)' }},
      axisTick: { lineStyle: { color: 'rgba(0,198,255,0.5)' }},
      axisLabel: { textStyle: { color: '#FFFFFF' }},
      splitLine: { lineStyle: { color: 'rgba(0,198,255,0.1)' }}
    },
    series: [{
      itemStyle: {
        borderColor: '#00C6FF',
        borderWidth: 1
      }
    }]
  };

  // 美化模块主对象
  const beautify = {
    
    // 初始化
    init() {
      this.registerTheme();
      this.initPageAnimation();
      this.initCenterMap();
      this.setupDataUpdate();
      this.setupInteractions();
      this.setupResize();
      this.loadGSAP();
    },

    // 注册 ECharts 主题
    registerTheme() {
      if (window.echarts) {
        echarts.registerTheme('cyber', cyberTheme);
      }
    },

    // 加载 GSAP (如果需要)
    loadGSAP() {
      if (!window.gsap) {
        // 使用简化版动画，不依赖外部GSAP
        this.initSimpleAnimations();
      } else {
        // 配置 GSAP
        gsap.ticker.lagSmoothing(500, 33);
      }
    },

    // 页面入场动画
    initPageAnimation() {
      $('body').addClass('page-enter');
      
      // 卡片动画
      setTimeout(() => {
        $('.infor_sma, .infor_sma2, .nav_infor').each((index, element) => {
          setTimeout(() => {
            $(element).addClass('card-animate cyber-border cyber-hover');
          }, index * 80);
        });
      }, 500);
    },

    // 初始化中心 3D 地图
    initCenterMap() {
      const centerContainer = document.querySelector('.top_center .infor_cont_img');
      if (!centerContainer) return;

      // 清空原有内容，保留标签
      const labels = centerContainer.querySelectorAll('.cont_label');
      centerContainer.innerHTML = '';
      
      // 重新添加标签
      labels.forEach(label => centerContainer.appendChild(label));
      
      // 创建地图容器
      const mapContainer = document.createElement('div');
      mapContainer.id = 'centerSphere';
      mapContainer.style.cssText = `
        position: absolute;
        top: 10%;
        left: 10%;
        width: 80%;
        height: 80%;
        z-index: 1;
      `;
      centerContainer.appendChild(mapContainer);

      // 初始化 3D 地图
      this.init3DMap();
    },

    // 初始化 3D 地图
    init3DMap() {
      const mapChart = echarts.init(document.getElementById('centerSphere'), 'cyber');
      
      // 检查是否有echarts-gl
      if (!window.echarts.graphic3D) {
        console.warn('ECharts-GL 未加载，使用2D地图替代');
        this.init2DMap(mapChart);
        return mapChart;
      }
      
      // 注册地图
      echarts.registerMap('huanggang', huanggangGeoJSON);
      
      const option = {
        backgroundColor: 'transparent',
        geo3D: {
          map: 'huanggang',
          regionHeight: 3,
          shading: 'realistic',
          realisticMaterial: {
            baseColor: '#0B1E38',
            roughness: 0.12,
            metalness: 0.85
          },
          postEffect: {
            enable: true,
            bloom: {
              enable: true,
              intensity: 0.3
            }
          },
          light: {
            main: {
              color: '#00C6FF',
              intensity: 1.5,
              shadow: true
            },
            ambient: {
              color: '#0072FF',
              intensity: 0.3
            }
          },
          viewControl: {
            autoRotate: true,
            autoRotateSpeed: 2,
            distance: 80,
            alpha: 45,
            beta: 0,
            center: [0, 0, 0]
          },
          itemStyle: {
            color: '#003366',
            borderColor: '#00C6FF',
            borderWidth: 2
          },
          emphasis: {
            itemStyle: {
              color: '#00C6FF'
            }
          }
        },
        series: [{
          type: 'map3D',
          map: 'huanggang',
          data: huanggangGeoJSON.features.map(feature => ({
            name: feature.properties.name,
            value: feature.properties.value
          }))
        }]
      };

      mapChart.setOption(option);

      // 添加呼吸扫光效果
      this.addBreathingEffect();
      
      // 定时更新数据
      this.startMapDataUpdate(mapChart);

      return mapChart;
    },

    // 2D地图备用方案
    init2DMap(mapChart) {
      echarts.registerMap('huanggang', huanggangGeoJSON);
      
      const option = {
        backgroundColor: 'transparent',
        geo: {
          map: 'huanggang',
          roam: false,
          itemStyle: {
            areaColor: '#003366',
            borderColor: '#00C6FF',
            borderWidth: 2
          },
          emphasis: {
            itemStyle: {
              areaColor: '#00C6FF'
            }
          }
        },
        series: [{
          type: 'map',
          map: 'huanggang',
          data: huanggangGeoJSON.features.map(feature => ({
            name: feature.properties.name,
            value: feature.properties.value
          })),
          itemStyle: {
            areaColor: '#003366',
            borderColor: '#00C6FF',
            borderWidth: 2
          }
        }]
      };
      
      mapChart.setOption(option);
      this.addBreathingEffect();
      this.startMapDataUpdate(mapChart);
    },

    // 生成飞线数据
    generateFlyLines() {
      const lines = [];
      const centers = [
        [114.9, 30.5], [115.1, 30.5], [114.9, 30.7], [115.1, 30.7],
        [115.3, 30.5], [115.3, 30.7], [115.5, 30.5], [115.5, 30.7],
        [115.7, 30.5], [115.7, 30.7]
      ];

      for (let i = 0; i < 5; i++) {
        const from = centers[Math.floor(Math.random() * centers.length)];
        const to = centers[Math.floor(Math.random() * centers.length)];
        if (from !== to) {
          lines.push({
            coords: [from, to]
          });
        }
      }
      return lines;
    },

    // 添加呼吸效果
    addBreathingEffect() {
      const sphere = document.getElementById('centerSphere');
      if (sphere) {
        sphere.classList.add('breathe-glow');
      }
    },

    // 地图数据更新
    startMapDataUpdate(chart) {
      setInterval(() => {
        const newData = huanggangGeoJSON.features.map(feature => ({
          name: feature.properties.name,
          value: Math.floor(Math.random() * 500) + 500
        }));

        chart.setOption({
          series: [{
            data: newData
          }]
        });
      }, 8000);
    },

    // 数据更新动画
    setupDataUpdate() {
      // 数字翻牌动画
      setInterval(() => {
        $('.pro_num').each(function() {
          const $this = $(this);
          const newValue = Math.floor(Math.random() * 10);
          $this.addClass('number-flip');
          setTimeout(() => {
            $this.text(newValue);
            $this.removeClass('number-flip');
          }, 400);
        });
      }, 8000);

      // 图表数据脉冲
      $('.infor_cont').addClass('data-pulse');
    },

    // 交互效果
    setupInteractions() {
      // Hover 效果
      $('.cyber-hover').on('mouseenter', function() {
        $(this).addClass('link-highlight active');
      }).on('mouseleave', function() {
        $(this).removeClass('link-highlight active');
      });

      // 点击波纹效果
      $('.cyber-hover').on('click', function(e) {
        const $this = $(this);
        const rect = this.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        $this.addClass('ripple-effect');
        const ripple = $('<div>').css({
          position: 'absolute',
          borderRadius: '50%',
          background: 'rgba(0, 198, 255, 0.6)',
          transform: 'scale(0)',
          animation: 'ripple 0.4s linear',
          left: x - 10,
          top: y - 10,
          width: 20,
          height: 20,
          pointerEvents: 'none'
        });

        $this.append(ripple);
        setTimeout(() => ripple.remove(), 400);
      });

      // 卡片联动高亮
      $('.nav_infor').on('mouseenter', function() {
        const index = $(this).index();
        $(`.visual_top .top_infor:eq(${index % 3})`).addClass('link-highlight active');
      }).on('mouseleave', function() {
        $('.link-highlight').removeClass('active');
      });
    },

    // 响应式处理
    setupResize() {
      let resizeTimer;
      $(window).on('resize', () => {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(() => {
          // 重新计算响应式变量
          this.updateResponsiveVars();
          
          // 重新调整图表大小
          $('.infor_cont').each(function() {
            const chartInstance = echarts.getInstanceByDom(this);
            if (chartInstance) {
              chartInstance.resize();
            }
          });

          // 重新调整地图
          const mapInstance = echarts.getInstanceByDom(document.getElementById('centerSphere'));
          if (mapInstance) {
            mapInstance.resize();
          }
        }, 300);
      });
    },

    // 更新响应式变量
    updateResponsiveVars() {
      const vw = window.innerWidth / 100;
      const vh = window.innerHeight / 100;
      
      document.documentElement.style.setProperty('--current-vw', vw + 'px');
      document.documentElement.style.setProperty('--current-vh', vh + 'px');
    },

    // 简单动画实现（无GSAP时）
    initSimpleAnimations() {
      // 数字计数动画
      window.animateNumber = function(element, target, duration = 800) {
        const start = parseInt(element.textContent) || 0;
        const increment = (target - start) / (duration / 16);
        let current = start;
        
        const timer = setInterval(() => {
          current += increment;
          if ((increment > 0 && current >= target) || (increment < 0 && current <= target)) {
            current = target;
            clearInterval(timer);
          }
          element.textContent = Math.floor(current);
        }, 16);
      };

      // 环形进度动画
      window.animateProgress = function(element, percent, duration = 1200) {
        element.style.transition = `stroke-dashoffset ${duration}ms ease-in-out`;
        element.style.strokeDashoffset = 283 - (283 * percent / 100);
      };
    }
  };

  // 导出模块
  exports('animate', beautify);
});