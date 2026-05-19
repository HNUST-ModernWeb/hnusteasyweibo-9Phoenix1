@echo off
chcp 65001 >nul
echo ==========================================
echo  GitHub 上传脚本
echo ==========================================
echo.

REM 检查是否安装了 git
where git >nul 2>nul
if %errorlevel% neq 0 (
    echo [错误] 未检测到 Git，请先安装 Git
    echo 下载地址: https://git-scm.com/download/win
    pause
    exit /b 1
)

REM 进入 front 目录
cd /d "c:\webshiyan\work2\front"

REM 检查是否已有 git 仓库
if not exist ".git" (
    echo [1/5