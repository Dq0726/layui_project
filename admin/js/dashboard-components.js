/**
 * Dashboard Components - 仪表板科技感组件库
 * 用于替代中心地图的各种可视化组件
 */

layui.define(['jquery'], function(exports) {
  const $ = layui.jquery;

  const components = {
    
    // 3D 数据立方体
    dataCube: {
      init(container, options = {}) {
        const defaultOptions = {
          title: '数据立方体',
          data: [
            { name: '生产效率', value: 85, color: '#00C6FF' },
            { name: '质量指标', value: 92, color: '#0072FF' },
            { name: '设备状态', value: 78, color: '#00FFB0' },
            { name: '能耗水平', value: 67, color: '#FFBF11' }
          ]
        };
        
        const opts = Object.assign(defaultOptions, options);
        const chart = echarts.init(container, 'cyber');
        
        const option = {
          backgroundColor: 'transparent',
          title: {
            text: opts.title,
            left: 'center',
            top: '5%',
            textStyle: {
              color: '#00C6FF',
              fontSize: 18
            }
          },
          grid3D: {
            boxWidth: 100,
            boxHeight: 100,
            boxDepth: 100,
            viewControl: {
              autoRotate: true,
              autoRotateSpeed: 3
            },
            light: {
              main: {
                color: '#00C6FF',
                intensity: 1.2
              },
              ambient: {
                intensity: 0.3
              }
            }
          },
          xAxis3D: { type: 'category', data: opts.data.map(d => d.name) },
          yAxis3D: { type: 'value', max: 100 },
          zAxis3D: { type: 'value', max: 100 },
          series: [{
            type: 'bar3D',
            data: opts.data.map((d, i) => [i, d.value, d.value]),
            itemStyle: {
              color: (params) => opts.data[params.dataIndex].color
            }
          }]
        };
        
        chart.setOption(option);
        return chart;
      }
    },

    // 雷达能量场
    radarField: {
      init(container, options = {}) {
        const defaultOptions = {
          title: '综合能力雷达',
          indicators: [
            { name: '生产能力', max: 100 },
            { name: '质量控制', max: 100 },
            { name: '设备效率', max: 100 },
            { name: '成本控制', max: 100 },
            { name: '交付准时', max: 100 },
            { name: '创新能力', max: 100 }
          ],
          data: [85, 92, 78, 88, 95, 76]
        };
        
        const opts = Object.assign(defaultOptions, options);
        const chart = echarts.init(container, 'cyber');
        
        const option = {
          backgroundColor: 'transparent',
          title: {
            text: opts.title,
            left: 'center',
            top: '5%',
            textStyle: {
              color: '#00C6FF',
              fontSize: 18
            }
          },
          radar: {
            indicator: opts.indicators,
            center: ['50%', '55%'],
            radius: '65%',
            axisLine: {
              lineStyle: { color: 'rgba(0, 198, 255, 0.3)' }
            },
            splitLine: {
              lineStyle: { color: 'rgba(0, 198, 255, 0.2)' }
            },
            splitArea: {
              areaStyle: {
                color: ['rgba(0, 198, 255, 0.05)', 'rgba(0, 114, 255, 0.05)']
              }
            }
          },
          series: [{
            type: 'radar',
            data: [{
              value: opts.data,
              areaStyle: {
                color: 'rgba(0, 198, 255, 0.2)'
              },
              lineStyle: {
                color: '#00C6FF',
                width: 2
              },
              symbol: 'circle',
              symbolSize: 8,
              itemStyle: {
                color: '#00C6FF'
              }
            }]
          }]
        };
        
        chart.setOption(option);
        this.addBreathingEffect(container);
        return chart;
      },
      
      addBreathingEffect(container) {
        $(container).addClass('breathe-glow');
      }
    },

    // 粒子星云图
    particleCloud: {
      init(container, options = {}) {
        const defaultOptions = {
          title: '数据星云',
          particleCount: 200
        };
        
        const opts = Object.assign(defaultOptions, options);
        const chart = echarts.init(container, 'cyber');
        
        // 生成粒子数据
        const data = [];
        for (let i = 0; i < opts.particleCount; i++) {
          data.push([
            Math.random() * 100,
            Math.random() * 100,
            Math.random() * 50 + 10
          ]);
        }
        
        const option = {
          backgroundColor: 'transparent',
          title: {
            text: opts.title,
            left: 'center',
            top: '5%',
            textStyle: {
              color: '#00C6FF',
              fontSize: 18
            }
          },
          xAxis: {
            type: 'value',
            show: false,
            min: 0,
            max: 100
          },
          yAxis: {
            type: 'value', 
            show: false,
            min: 0,
            max: 100
          },
          series: [{
            type: 'scatter',
            data: data,
            symbolSize: (data) => data[2] / 2,
            itemStyle: {
              color: (params) => {
                const colors = ['#00C6FF', '#0072FF', '#00FFB0', '#FFBF11'];
                return colors[params.dataIndex % colors.length];
              },
              opacity: 0.8
            },
            animationDuration: 3000,
            animationEasing: 'cubicInOut'
          }]
        };
        
        chart.setOption(option);
        this.startParticleAnimation(chart, data);
        return chart;
      },
      
      startParticleAnimation(chart, data) {
        setInterval(() => {
          const newData = data.map(point => [
            (point[0] + Math.random() * 4 - 2 + 100) % 100,
            (point[1] + Math.random() * 4 - 2 + 100) % 100,
            point[2]
          ]);
          
          chart.setOption({
            series: [{ data: newData }]
          });
        }, 2000);
      }
    },

    // 能量环形图
    energyRing: {
      init(container, options = {}) {
        const defaultOptions = {
          title: '能量监控',
          data: [
            { name: '核心动力', value: 85, color: '#00C6FF' },
            { name: '辅助系统', value: 72, color: '#0072FF' },
            { name: '备用能源', value: 45, color: '#00FFB0' }
          ]
        };
        
        const opts = Object.assign(defaultOptions, options);
        const chart = echarts.init(container, 'cyber');
        
        const option = {
          backgroundColor: 'transparent',
          title: {
            text: opts.title,
            left: 'center',
            top: '5%',
            textStyle: {
              color: '#00C6FF',
              fontSize: 18
            }
          },
          series: opts.data.map((item, index) => ({
            type: 'pie',
            radius: [`${30 + index * 15}%`, `${45 + index * 15}%`],
            center: ['50%', '55%'],
            data: [
              {
                value: item.value,
                name: item.name,
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                    { offset: 0, color: item.color },
                    { offset: 1, color: item.color + '40' }
                  ])
                }
              },
              {
                value: 100 - item.value,
                itemStyle: { color: 'transparent' },
                label: { show: false },
                emphasis: { itemStyle: { color: 'transparent' }}
              }
            ],
            label: {
              show: index === 0,
              position: 'center',
              formatter: `{b}\n{c}%`,
              fontSize: 14,
              color: '#FFFFFF'
            },
            animationType: 'scale',
            animationEasing: 'elasticOut'
          }))
        };
        
        chart.setOption(option);
        this.addRotationEffect(chart);
        return chart;
      },
      
      addRotationEffect(chart) {
        let angle = 0;
        setInterval(() => {
          angle += 1;
          chart.setOption({
            series: chart.getOption().series.map(s => ({
              ...s,
              startAngle: angle
            }))
          });
        }, 100);
      }
    },

    // 数据流网络图
    dataFlow: {
      init(container, options = {}) {
        const defaultOptions = {
          title: '数据流网络',
          nodes: [
            { name: '数据源', x: 50, y: 20, symbolSize: 30, category: 0 },
            { name: '处理中心', x: 50, y: 50, symbolSize: 40, category: 1 },
            { name: '存储系统', x: 20, y: 80, symbolSize: 25, category: 2 },
            { name: '分析引擎', x: 80, y: 80, symbolSize: 35, category: 2 },
            { name: '输出终端', x: 50, y: 90, symbolSize: 20, category: 3 }
          ]
        };
        
        const opts = Object.assign(defaultOptions, options);
        const chart = echarts.init(container, 'cyber');
        
        const links = [
          { source: 0, target: 1 },
          { source: 1, target: 2 },
          { source: 1, target: 3 },
          { source: 2, target: 4 },
          { source: 3, target: 4 }
        ];
        
        const option = {
          backgroundColor: 'transparent',
          title: {
            text: opts.title,
            left: 'center',
            top: '5%',
            textStyle: {
              color: '#00C6FF',
              fontSize: 18
            }
          },
          series: [{
            type: 'graph',
            layout: 'none',
            data: opts.nodes,
            links: links,
            categories: [
              { name: '源', itemStyle: { color: '#00C6FF' }},
              { name: '处理', itemStyle: { color: '#0072FF' }},
              { name: '存储', itemStyle: { color: '#00FFB0' }},
              { name: '输出', itemStyle: { color: '#FFBF11' }}
            ],
            roam: false,
            lineStyle: {
              color: '#00C6FF',
              curveness: 0.3,
              width: 2
            },
            emphasis: {
              focus: 'adjacency',
              lineStyle: { width: 4 }
            },
            animationDurationUpdate: 1500,
            animationEasingUpdate: 'quinticInOut'
          }]
        };
        
        chart.setOption(option);
        this.addFlowAnimation(container);
        return chart;
      },
      
      addFlowAnimation(container) {
        $(container).addClass('scan-line data-pulse');
      }
    }
  };

  // 自动初始化组件
  const autoInit = {
    init() {
      // 为不同看板选择不同的中心组件
      const pageTitle = document.title;
      const centerContainer = document.querySelector('.top_center .infor_cont_img, .center-component');
      
      if (!centerContainer) return;
      
      // 根据页面标题选择组件
      let componentType = 'radarField'; // 默认
      let componentOptions = {};
      
      switch(pageTitle) {
        case '生产管理':
          componentType = 'dataCube';
          componentOptions = {
            title: '生产数据立方体',
            data: [
              { name: '产量完成', value: 88, color: '#00C6FF' },
              { name: '质量达标', value: 95, color: '#0072FF' },
              { name: '设备运行', value: 82, color: '#00FFB0' },
              { name: '人员效率', value: 76, color: '#FFBF11' }
            ]
          };
          break;
          
        case '采购管理':
          componentType = 'energyRing';
          componentOptions = {
            title: '采购能效环',
            data: [
              { name: '供应商质量', value: 92, color: '#00C6FF' },
              { name: '成本控制', value: 78, color: '#0072FF' },
              { name: '交付及时', value: 85, color: '#00FFB0' }
            ]
          };
          break;
          
        case '销售管理':
          componentType = 'particleCloud';
          componentOptions = {
            title: '销售数据星云',
            particleCount: 150
          };
          break;
          
        case '质量管理':
          componentType = 'radarField';
          componentOptions = {
            title: '质量控制雷达',
            indicators: [
              { name: '进料检验', max: 100 },
              { name: '过程控制', max: 100 },
              { name: '成品检测', max: 100 },
              { name: '客户满意', max: 100 },
              { name: '持续改进', max: 100 }
            ],
            data: [95, 88, 92, 87, 79]
          };
          break;
          
        case '设备管理':
          componentType = 'dataFlow';
          componentOptions = {
            title: '设备数据流',
            nodes: [
              { name: '传感器', x: 30, y: 20, symbolSize: 25, category: 0 },
              { name: '控制中心', x: 50, y: 50, symbolSize: 40, category: 1 },
              { name: '生产设备', x: 70, y: 20, symbolSize: 35, category: 0 },
              { name: '监控系统', x: 20, y: 80, symbolSize: 30, category: 2 },
              { name: '维护系统', x: 80, y: 80, symbolSize: 30, category: 2 }
            ]
          };
          break;
          
        case '能耗管理':
          componentType = 'energyRing';
          componentOptions = {
            title: '能耗分布环',
            data: [
              { name: '生产用电', value: 65, color: '#00C6FF' },
              { name: '照明空调', value: 25, color: '#0072FF' },
              { name: '其他设备', value: 10, color: '#00FFB0' }
            ]
          };
          break;
      }
      
      // 创建组件容器
      const componentDiv = document.createElement('div');
      componentDiv.style.cssText = `
        position: absolute;
        top: 10%;
        left: 10%;
        width: 80%;
        height: 80%;
        z-index: 1;
      `;
      centerContainer.appendChild(componentDiv);
      
      // 初始化选定的组件
      const component = components[componentType];
      if (component) {
        component.init(componentDiv, componentOptions);
      }
    }
  };

  // 导出模块
  exports('dashboardComponents', {
    components,
    autoInit
  });
});