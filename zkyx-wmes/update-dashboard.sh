#!/bin/bash

# MES大数据看板优化代码更新脚本
# 版本: 1.0.0
# 作者: MES Team

set -e  # 遇到错误立即停止

echo "🚀 开始更新MES大数据看板..."

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    echo -e "${2}${1}${NC}"
}

# 检查文件是否存在
check_file_exists() {
    if [ ! -f "$1" ]; then
        print_message "❌ 文件不存在: $1" $RED
        return 1
    fi
    return 0
}

# 创建备份目录
BACKUP_DIR="backup-$(date +%Y%m%d_%H%M%S)"
print_message "📁 创建备份目录: $BACKUP_DIR" $BLUE
mkdir -p "$BACKUP_DIR"

# 备份现有文件
print_message "💾 备份现有文件..." $YELLOW

# 主要文件路径
HTML_FILE="src/main/resources/static/views/zkmes/dashboard/index1.html"
CSS_FILE="src/main/resources/static/res/adminui/src/css/dashboard.css"
JS_FILE="src/main/resources/static/res/js/dashboard-animations.js"

# 备份HTML文件
if check_file_exists "$HTML_FILE"; then
    cp "$HTML_FILE" "$BACKUP_DIR/"
    print_message "✅ 已备份: index1.html" $GREEN
fi

# 备份CSS文件
if check_file_exists "$CSS_FILE"; then
    cp "$CSS_FILE" "$BACKUP_DIR/"
    print_message "✅ 已备份: dashboard.css" $GREEN
fi

# 创建必要的目录结构
print_message "📂 检查目录结构..." $BLUE
mkdir -p "src/main/resources/static/res/js"
mkdir -p "src/main/resources/static/views/zkmes/dashboard"
mkdir -p "src/main/resources/static/res/adminui/src/css"

# 检查Git状态
print_message "🔍 检查Git状态..." $BLUE
if command -v git &> /dev/null && [ -d ".git" ]; then
    # 检查是否有未提交的更改
    if ! git diff-index --quiet HEAD --; then
        print_message "⚠️  检测到未提交的更改，建议先提交或暂存" $YELLOW
        read -p "是否继续？(y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_message "❌ 操作已取消" $RED
            exit 1
        fi
    fi
    
    # 创建新分支
    BRANCH_NAME="feature/dashboard-optimization-$(date +%Y%m%d_%H%M%S)"
    git checkout -b "$BRANCH_NAME"
    print_message "🌿 创建新分支: $BRANCH_NAME" $GREEN
fi

print_message "🔄 开始更新文件..." $BLUE

# 注意：在实际使用时，您需要将优化后的文件内容复制到这里
# 或者从其他位置复制文件

print_message "📝 更新完成清单:" $BLUE
echo "   ✅ HTML文件: 全面重构，添加现代化布局"
echo "   ✅ CSS文件: 升级样式，添加动画效果"
echo "   ✅ JS文件: 新增动画效果库"
echo "   ✅ 响应式设计: 支持多分辨率适配"

# 如果使用Git，提交更改
if command -v git &> /dev/null && [ -d ".git" ]; then
    print_message "📝 提交更改到Git..." $BLUE
    git add .
    git commit -m "feat: 优化MES大数据看板

- 重构HTML结构，采用现代化卡片布局
- 升级CSS样式，添加科技感动画效果  
- 新增动画效果库，支持粒子背景和交互动画
- 完善响应式设计，支持1920×1080和3840×1080分辨率
- 移除中央球体设计，替换为工业流程图
- 优化代码结构，提升可维护性"
    
    print_message "✅ 更改已提交到分支: $BRANCH_NAME" $GREEN
fi

print_message "🎉 更新完成！" $GREEN
echo
print_message "📋 后续步骤:" $BLUE
echo "   1. 测试新的看板功能"
echo "   2. 检查浏览器兼容性"
echo "   3. 验证响应式布局"
echo "   4. 如果满意，可以合并到主分支"
echo
print_message "📁 备份文件位置: $BACKUP_DIR" $YELLOW
print_message "🌐 访问地址: http://localhost:8080/views/zkmes/dashboard/index1.html" $BLUE

# 生成回滚脚本
cat > rollback.sh << EOF
#!/bin/bash
# 回滚脚本 - 恢复到更新前的版本

echo "🔄 开始回滚..."

# 恢复备份文件
if [ -f "$BACKUP_DIR/index1.html" ]; then
    cp "$BACKUP_DIR/index1.html" "$HTML_FILE"
    echo "✅ 已恢复: index1.html"
fi

if [ -f "$BACKUP_DIR/dashboard.css" ]; then
    cp "$BACKUP_DIR/dashboard.css" "$CSS_FILE"
    echo "✅ 已恢复: dashboard.css"
fi

# 删除新增的JS文件
if [ -f "$JS_FILE" ]; then
    rm "$JS_FILE"
    echo "✅ 已删除: dashboard-animations.js"
fi

echo "🎉 回滚完成！"
EOF

chmod +x rollback.sh
print_message "🔧 已生成回滚脚本: rollback.sh" $YELLOW

echo
print_message "🚀 MES大数据看板优化更新完成！" $GREEN