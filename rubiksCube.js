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
        this.animationSpeed = 0.1;
        this.difficulty = 'easy';
        this.scrambleHistory = [];
        this.enableAnimations = true;
        
        // 魔方状态 - 6个面，每面9个小方块
        this.cubeState = {
            front: Array(9).fill('white'),   // 前面 - 白色
            back: Array(9).fill('yellow'),   // 后面 - 黄色
            left: Array(9).fill('orange'),   // 左面 - 橙色
            right: Array(9).fill('red'),     // 右面 - 红色
            top: Array(9).fill('green'),     // 上面 - 绿色
            bottom: Array(9).fill('blue')    // 下面 - 蓝色
        };
        
        this.solvedState = JSON.parse(JSON.stringify(this.cubeState));
        
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
    }
    
    addLights() {
        // 环境光
        const ambientLight = new THREE.AmbientLight(0x404040, 0.6);
        this.scene.add(ambientLight);
        
        // 主光源
        const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
        directionalLight.position.set(10, 10, 5);
        directionalLight.castShadow = true;
        directionalLight.shadow.mapSize.width = 2048;
        directionalLight.shadow.mapSize.height = 2048;
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
            blue: 0x0000ff,
            black: 0x000000
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
                    cube.userData = { x, y, z };
                    
                    this.cubes.push(cube);
                    this.group.add(cube);
                }
            }
        }
        
        this.scene.add(this.group);
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
        
        // 创建旋转组
        const rotationGroup = new THREE.Group();
        this.scene.add(rotationGroup);
        
        // 将需要旋转的立方体添加到旋转组
        cubesToRotate.forEach(cube => {
            cube.parent.remove(cube);
            rotationGroup.add(cube);
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
        const startRotation = 0;
        const duration = 300; // 毫秒
        const startTime = Date.now();
        
        const animate = () => {
            const elapsed = Date.now() - startTime;
            const progress = Math.min(elapsed / duration, 1);
            
            // 使用缓动函数
            const easeProgress = 1 - Math.pow(1 - progress, 3);
            const currentRotation = startRotation + (targetRotation - startRotation) * easeProgress;
            
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
        
        // 更新魔方状态
        this.updateCubeState(axis, direction, cubesToRotate[0].userData[axis]);
        
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
        
        cube.userData = { x: newX, y: newY, z: newZ };
    }
    
    updateCubeState(axis, direction, layer) {
        // 这里应该更新内部状态表示
        // 为简化，我们通过检查立方体的实际颜色来判断是否完成
    }
    
    checkSolved() {
        // 检查每个面是否都是同一颜色
        const faces = ['right', 'left', 'top', 'bottom', 'front', 'back'];
        
        for (let faceIndex = 0; faceIndex < 6; faceIndex++) {
            const faceColors = [];
            
            // 获取这个面上所有立方体的颜色
            this.cubes.forEach(cube => {
                if (this.isOnFace(cube, faceIndex)) {
                    const material = cube.material[faceIndex];
                    faceColors.push(material.color.getHex());
                }
            });
            
            // 检查这个面是否所有颜色都相同
            if (faceColors.length > 0) {
                const firstColor = faceColors[0];
                if (!faceColors.every(color => color === firstColor)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    isOnFace(cube, faceIndex) {
        const { x, y, z } = cube.userData;
        
        switch (faceIndex) {
            case 0: return x === 2; // 右面
            case 1: return x === 0; // 左面
            case 2: return y === 2; // 上面
            case 3: return y === 0; // 下面
            case 4: return z === 2; // 前面
            case 5: return z === 0; // 后面
            default: return false;
        }
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
            this.scrambleHistory.push({ cube: randomCube.userData, axis, direction: -direction });
            
            this.performRotation(randomCube.userData, axis, direction);
            
            setTimeout(() => {
                moveIndex++;
                executeMove();
            }, this.enableAnimations ? 200 : 50);
        };
        
        this.updateStatus('正在打乱魔方...');
        executeMove();
    }
    
    getDifficultyMoves() {
        switch (this.difficulty) {
            case 'easy': return 10;
            case 'medium': return 20;
            case 'hard': return 30;
            default: return 15;
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
            this.performRotation(move.cube, move.axis, move.direction);
            
            setTimeout(() => {
                moveIndex++;
                executeMove();
            }, this.enableAnimations ? 200 : 50);
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
        // 重置所有立方体到初始位置和旋转
        this.cubes.forEach((cube, index) => {
            const x = Math.floor(index / 9);
            const y = Math.floor((index % 9) / 3);
            const z = index % 3;
            
            cube.position.set(
                (x - 1) * 1,
                (y - 1) * 1,
                (z - 1) * 1
            );
            
            cube.rotation.set(0, 0, 0);
            cube.userData = { x, y, z };
        });
        
        this.cubeState = JSON.parse(JSON.stringify(this.solvedState));
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
    rubiksCube = new RubiksCube();
});

// 全局函数供HTML调用
function startNewGame() {
    rubiksCube.startNewGame();
}

function scrambleCube() {
    rubiksCube.scrambleCube();
}

function solveCube() {
    rubiksCube.solveCube();
}

function resetView() {
    rubiksCube.resetView();
}

function toggleAnimation() {
    rubiksCube.toggleAnimation();
}

function setDifficulty(level) {
    rubiksCube.setDifficulty(level);
}