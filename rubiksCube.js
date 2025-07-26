class RubiksCube {
    constructor() {
        this.scene = null;
        this.camera = null;
        this.renderer = null;
        this.controls = null;
        this.cubes = [];
        this.group = null;
        this.raycaster = new THREE.Raycaster();
        this.mouse = new THREE.Vector2();
        this.isAnimating = false;
        this.moveCount = 0;
        this.startTime = null;
        this.timerInterval = null;
        this.isGameActive = false;
        this.difficulty = 'easy';
        this.scrambleHistory = [];
        this.enableAnimations = true;
        
        this.init();
        this.animate();
    }
    
    init() {
        const canvas = document.getElementById('gameCanvas');
        
        // 创建场景
        this.scene = new THREE.Scene();
        this.scene.background = new THREE.Color(0x222222);
        
        // 创建相机
        this.camera = new THREE.PerspectiveCamera(
            75, 
            canvas.clientWidth / canvas.clientHeight, 
            0.1, 
            1000
        );
        this.camera.position.set(8, 8, 8);
        
        // 创建渲染器
        this.renderer = new THREE.WebGLRenderer({ 
            canvas: canvas, 
            antialias: true 
        });
        this.renderer.setSize(canvas.clientWidth, canvas.clientHeight);
        this.renderer.shadowMap.enabled = true;
        this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
        
        // 创建控制器
        this.controls = new THREE.OrbitControls(this.camera, this.renderer.domElement);
        this.controls.enableDamping = true;
        this.controls.dampingFactor = 0.05;
        this.controls.maxDistance = 20;
        this.controls.minDistance = 5;
        
        // 添加光源
        this.addLights();
        
        // 创建魔方
        this.createCube();
        
        // 添加事件监听
        this.addEventListeners();
        
        // 窗口大小调整
        window.addEventListener('resize', () => this.onWindowResize());
        
        console.log('魔方游戏初始化完成');
    }
    
    addLights() {
        // 环境光
        const ambientLight = new THREE.AmbientLight(0x404040, 0.6);
        this.scene.add(ambientLight);
        
        // 主光源
        const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
        directionalLight.position.set(10, 10, 5);
        directionalLight.castShadow = true;
        this.scene.add(directionalLight);
        
        // 补光
        const fillLight = new THREE.DirectionalLight(0xffffff, 0.3);
        fillLight.position.set(-5, -5, -5);
        this.scene.add(fillLight);
    }
    
    createCube() {
        this.group = new THREE.Group();
        this.cubes = [];
        
        const size = 0.95;
        const gap = 0.05;
        const colors = {
            white: 0xffffff,
            yellow: 0xffff00,
            orange: 0xff8800,
            red: 0xff0000,
            green: 0x00ff00,
            blue: 0x0000ff
        };
        
        for (let x = 0; x < 3; x++) {
            for (let y = 0; y < 3; y++) {
                for (let z = 0; z < 3; z++) {
                    const geometry = new THREE.BoxGeometry(size, size, size);
                    
                    // 为每个面创建不同的材质
                    const materials = [
                        new THREE.MeshLambertMaterial({ color: colors.red }),    // 右面
                        new THREE.MeshLambertMaterial({ color: colors.orange }), // 左面
                        new THREE.MeshLambertMaterial({ color: colors.green }),  // 上面
                        new THREE.MeshLambertMaterial({ color: colors.blue }),   // 下面
                        new THREE.MeshLambertMaterial({ color: colors.white }),  // 前面
                        new THREE.MeshLambertMaterial({ color: colors.yellow })  // 后面
                    ];
                    
                    const cube = new THREE.Mesh(geometry, materials);
                    
                    // 设置位置
                    cube.position.set(
                        (x - 1) * (size + gap),
                        (y - 1) * (size + gap),
                        (z - 1) * (size + gap)
                    );
                    
                    // 添加边框
                    const edges = new THREE.EdgesGeometry(geometry);
                    const line = new THREE.LineSegments(
                        edges, 
                        new THREE.LineBasicMaterial({ color: 0x000000, linewidth: 2 })
                    );
                    cube.add(line);
                    
                    cube.castShadow = true;
                    cube.receiveShadow = true;
                    
                    // 存储立方体的原始位置信息
                    cube.userData = { x, y, z, originalX: x, originalY: y, originalZ: z };
                    
                    this.cubes.push(cube);
                    this.group.add(cube);
                }
            }
        }
        
        this.scene.add(this.group);
        console.log('魔方创建完成，共', this.cubes.length, '个小立方体');
    }
    
    addEventListeners() {
        this.renderer.domElement.addEventListener('click', (event) => {
            if (this.isAnimating) return;
            
            this.mouse.x = (event.clientX / this.renderer.domElement.clientWidth) * 2 - 1;
            this.mouse.y = -(event.clientY / this.renderer.domElement.clientHeight) * 2 + 1;
            
            this.raycaster.setFromCamera(this.mouse, this.camera);
            const intersects = this.raycaster.intersectObjects(this.cubes);
            
            if (intersects.length > 0) {
                const cube = intersects[0].object;
                const face = intersects[0].face;
                this.rotateFace(cube, face);
            }
        });
    }
    
    rotateFace(cube, face) {
        if (!this.isGameActive) return;
        
        const normal = face.normal.clone();
        normal.transformDirection(cube.matrixWorld);
        
        // 确定旋转轴和方向
        let axis, direction;
        const threshold = 0.5;
        
        if (Math.abs(normal.x) > threshold) {
            axis = 'x';
            direction = normal.x > 0 ? 1 : -1;
        } else if (Math.abs(normal.y) > threshold) {
            axis = 'y';
            direction = normal.y > 0 ? 1 : -1;
        } else if (Math.abs(normal.z) > threshold) {
            axis = 'z';
            direction = normal.z > 0 ? 1 : -1;
        }
        
        if (axis) {
            this.performRotation(cube.userData, axis, direction);
        }
    }
    
    performRotation(cubeData, axis, direction) {
        if (this.isAnimating) return;
        
        this.isAnimating = true;
        this.moveCount++;
        this.updateMoveCount();
        
        // 找到需要旋转的立方体
        const cubesToRotate = this.cubes.filter(cube => {
            return cube.userData[axis] === cubeData[axis];
        });
        
        console.log(`旋转${axis}轴第${cubeData[axis]}层，共${cubesToRotate.length}个立方体`);
        
        // 创建旋转组
        const rotationGroup = new THREE.Group();
        this.scene.add(rotationGroup);
        
        // 将需要旋转的立方体添加到旋转组
        cubesToRotate.forEach(cube => {
            // 保存世界坐标
            const worldPosition = new THREE.Vector3();
            const worldQuaternion = new THREE.Quaternion();
            const worldScale = new THREE.Vector3();
            
            cube.getWorldPosition(worldPosition);
            cube.getWorldQuaternion(worldQuaternion);
            cube.getWorldScale(worldScale);
            
            this.group.remove(cube);
            rotationGroup.add(cube);
            
            cube.position.copy(worldPosition);
            cube.quaternion.copy(worldQuaternion);
            cube.scale.copy(worldScale);
        });
        
        // 执行旋转动画
        const targetRotation = direction * Math.PI / 2;
        const rotationAxis = new THREE.Vector3();
        rotationAxis[axis] = 1;
        
        if (this.enableAnimations) {
            this.animateRotation(rotationGroup, rotationAxis, targetRotation, () => {
                this.finishRotation(rotationGroup, cubesToRotate, axis, direction);
            });
        } else {
            rotationGroup.rotateOnAxis(rotationAxis, targetRotation);
            this.finishRotation(rotationGroup, cubesToRotate, axis, direction);
        }
    }
    
    animateRotation(group, axis, targetRotation, callback) {
        const duration = 300; // 毫秒
        const startTime = Date.now();
        
        const animate = () => {
            const elapsed = Date.now() - startTime;
            const progress = Math.min(elapsed / duration, 1);
            
            // 使用缓动函数
            const easeProgress = 1 - Math.pow(1 - progress, 3);
            const currentRotation = targetRotation * easeProgress;
            
            group.rotation.setFromVector3(axis.clone().multiplyScalar(currentRotation));
            
            if (progress < 1) {
                requestAnimationFrame(animate);
            } else {
                callback();
            }
        };
        
        animate();
    }
    
    finishRotation(rotationGroup, cubesToRotate, axis, direction) {
        // 将立方体移回主组
        cubesToRotate.forEach(cube => {
            const worldPosition = new THREE.Vector3();
            const worldQuaternion = new THREE.Quaternion();
            const worldScale = new THREE.Vector3();
            
            cube.getWorldPosition(worldPosition);
            cube.getWorldQuaternion(worldQuaternion);
            cube.getWorldScale(worldScale);
            
            rotationGroup.remove(cube);
            this.group.add(cube);
            
            cube.position.copy(worldPosition);
            cube.quaternion.copy(worldQuaternion);
            cube.scale.copy(worldScale);
            
            // 更新立方体的用户数据
            this.updateCubePosition(cube, axis, direction);
        });
        
        this.scene.remove(rotationGroup);
        this.isAnimating = false;
        
        // 检查是否完成
        if (this.checkSolved()) {
            this.onGameComplete();
        }
    }
    
    updateCubePosition(cube, axis, direction) {
        const { x, y, z } = cube.userData;
        let newX = x, newY = y, newZ = z;
        
        if (axis === 'x') {
            if (direction > 0) { // 顺时针
                newY = 2 - z;
                newZ = y;
            } else { // 逆时针
                newY = z;
                newZ = 2 - y;
            }
        } else if (axis === 'y') {
            if (direction > 0) { // 顺时针
                newX = z;
                newZ = 2 - x;
            } else { // 逆时针
                newX = 2 - z;
                newZ = x;
            }
        } else if (axis === 'z') {
            if (direction > 0) { // 顺时针
                newX = 2 - y;
                newY = x;
            } else { // 逆时针
                newX = y;
                newY = 2 - x;
            }
        }
        
        cube.userData.x = newX;
        cube.userData.y = newY;
        cube.userData.z = newZ;
    }
    
    checkSolved() {
        // 简化的检查：检查是否所有立方体都回到了原始位置
        return this.cubes.every(cube => {
            const { x, y, z, originalX, originalY, originalZ } = cube.userData;
            return x === originalX && y === originalY && z === originalZ;
        });
    }
    
    scrambleCube() {
        if (this.isAnimating) return;
        
        this.stopTimer();
        this.isGameActive = false;
        this.scrambleHistory = [];
        
        const moves = this.getDifficultyMoves();
        let moveIndex = 0;
        
        const executeMove = () => {
            if (moveIndex >= moves) {
                this.isGameActive = true;
                this.startTimer();
                this.updateStatus('游戏开始！尝试还原魔方');
                document.getElementById('solveBtn').disabled = false;
                return;
            }
            
            const randomCube = this.cubes[Math.floor(Math.random() * this.cubes.length)];
            const axes = ['x', 'y', 'z'];
            const axis = axes[Math.floor(Math.random() * axes.length)];
            const direction = Math.random() > 0.5 ? 1 : -1;
            
            // 记录移动用于自动还原
            this.scrambleHistory.push({ 
                cubeData: { ...randomCube.userData }, 
                axis, 
                direction: -direction 
            });
            
            this.performRotation(randomCube.userData, axis, direction);
            
            setTimeout(() => {
                moveIndex++;
                executeMove();
            }, this.enableAnimations ? 400 : 100);
        };
        
        this.updateStatus('正在打乱魔方...');
        executeMove();
    }
    
    getDifficultyMoves() {
        switch (this.difficulty) {
            case 'easy': return 5;
            case 'medium': return 10;
            case 'hard': return 15;
            default: return 8;
        }
    }
    
    solveCube() {
        if (this.isAnimating || this.scrambleHistory.length === 0) return;
        
        this.isGameActive = false;
        this.updateStatus('自动还原中...');
        
        const moves = [...this.scrambleHistory].reverse();
        let moveIndex = 0;
        
        const executeMove = () => {
            if (moveIndex >= moves.length) {
                this.onGameComplete();
                return;
            }
            
            const move = moves[moveIndex];
            this.performRotation(move.cubeData, move.axis, move.direction);
            
            setTimeout(() => {
                moveIndex++;
                executeMove();
            }, this.enableAnimations ? 400 : 100);
        };
        
        executeMove();
    }
    
    startNewGame() {
        this.resetCube();
        this.moveCount = 0;
        this.updateMoveCount();
        this.stopTimer();
        this.isGameActive = false;
        this.scrambleHistory = [];
        this.updateStatus('点击"打乱魔方"开始游戏');
        document.getElementById('solveBtn').disabled = true;
        document.getElementById('congratulations').style.display = 'none';
    }
    
    resetCube() {
        // 移除现有的魔方
        if (this.group) {
            this.scene.remove(this.group);
        }
        
        // 重新创建魔方
        this.createCube();
    }
    
    startTimer() {
        this.startTime = Date.now();
        this.timerInterval = setInterval(() => {
            this.updateTimer();
        }, 1000);
    }
    
    stopTimer() {
        if (this.timerInterval) {
            clearInterval(this.timerInterval);
            this.timerInterval = null;
        }
    }
    
    updateTimer() {
        if (!this.startTime) return;
        
        const elapsed = Math.floor((Date.now() - this.startTime) / 1000);
        const minutes = Math.floor(elapsed / 60);
        const seconds = elapsed % 60;
        
        document.getElementById('timer').textContent = 
            `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    }
    
    updateMoveCount() {
        document.getElementById('moveCount').textContent = this.moveCount;
    }
    
    updateStatus(message) {
        document.getElementById('status').textContent = message;
    }
    
    onGameComplete() {
        this.isGameActive = false;
        this.stopTimer();
        
        const finalTime = document.getElementById('timer').textContent;
        const finalMoves = this.moveCount;
        
        document.getElementById('finalTime').textContent = finalTime;
        document.getElementById('finalMoves').textContent = finalMoves;
        document.getElementById('congratulations').style.display = 'block';
        
        this.updateStatus('🎉 恭喜完成！');
    }
    
    resetView() {
        this.camera.position.set(8, 8, 8);
        this.controls.reset();
    }
    
    toggleAnimation() {
        this.enableAnimations = !this.enableAnimations;
        const button = event.target;
        button.textContent = this.enableAnimations ? '⚡ 切换动画' : '⚡ 动画已关闭';
    }
    
    setDifficulty(level) {
        this.difficulty = level;
        
        // 更新按钮状态
        document.querySelectorAll('.difficulty-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        document.getElementById(level + 'Btn').classList.add('active');
        
        this.updateStatus(`难度已设置为: ${level === 'easy' ? '简单' : level === 'medium' ? '中等' : '困难'}`);
    }
    
    onWindowResize() {
        const canvas = this.renderer.domElement;
        this.camera.aspect = canvas.clientWidth / canvas.clientHeight;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(canvas.clientWidth, canvas.clientHeight);
    }
    
    animate() {
        requestAnimationFrame(() => this.animate());
        
        this.controls.update();
        this.renderer.render(this.scene, this.camera);
    }
}

// 全局变量和函数
let rubiksCube;

// 初始化游戏
window.addEventListener('DOMContentLoaded', () => {
    try {
        rubiksCube = new RubiksCube();
        console.log('游戏初始化成功');
    } catch (error) {
        console.error('游戏初始化失败:', error);
        document.getElementById('status').textContent = '游戏初始化失败，请刷新页面重试';
    }
});

// 全局函数供HTML调用
function startNewGame() {
    if (rubiksCube) rubiksCube.startNewGame();
}

function scrambleCube() {
    if (rubiksCube) rubiksCube.scrambleCube();
}

function solveCube() {
    if (rubiksCube) rubiksCube.solveCube();
}

function resetView() {
    if (rubiksCube) rubiksCube.resetView();
}

function toggleAnimation() {
    if (rubiksCube) rubiksCube.toggleAnimation();
}

function setDifficulty(level) {
    if (rubiksCube) rubiksCube.setDifficulty(level);
}