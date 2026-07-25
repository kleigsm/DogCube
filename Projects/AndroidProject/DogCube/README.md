# 🐶 DogCube — 狗狗方块

**DogCube** 是一款 Android 休闲益智游戏，基于经典俄罗斯方块玩法，将七种标准方块替换为七种狗狗品种主题形象。

## 截图

> 🚧 开发中，截图即将更新

## 技术栈

| 层级 | 技术 |
|------|------|
| 语言 | Kotlin 2.1 |
| UI | Jetpack Compose + Canvas |
| 架构 | MVVM + Hilt DI |
| 持久化 | Room |
| 音效 | SoundPool |
| 最低系统 | Android 8.0 (API 26) |

## 快速开始

1. 克隆仓库
   ```bash
   git clone https://github.com/<your-username>/DogCube.git
   ```
2. 用 Android Studio (Hedgehog 2024.1+) 打开项目
3. 等待 Gradle 同步完成
4. 选择设备或模拟器，点击 Run

## 分支管理

本项目采用简化 Gitflow 模型：

- `main` — 生产就绪代码
- `develop` — 开发主线
- `feature/*` — 功能分支
- `fix/*` — 修复分支

Commit 遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范。

## 开源协议

[MIT License](LICENSE)
