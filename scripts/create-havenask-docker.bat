@echo off
:: Havenask 容器创建脚本（Windows 无乱码版）

:: 切换到 UTF-8 编码
chcp 65001 >nul

setlocal enabledelayedexpansion

echo ===============================
echo  🐋 Havenask 容器创建脚本
echo ===============================

:: 询问容器名称
set /p CONTAINER_NAME=请输入容器名称（CONTAINER_NAME）:

if "%CONTAINER_NAME%"=="" (
  echo ❌ 容器名称不能为空！
  pause
  exit /b 1
)

echo ➡️ 正在下载 create_container.sh ...
powershell -Command "Invoke-WebRequest -Uri https://github.com/alibaba/havenask/releases/download/v1.2.0/create_container.sh -OutFile create_container.sh"

echo ➡️ 正在拉取镜像 registry.cn-hangzhou.aliyuncs.com/havenask/ha3_runtime:latest ...
docker pull registry.cn-hangzhou.aliyuncs.com/havenask/ha3_runtime:latest

echo ➡️ 正在创建容器：%CONTAINER_NAME% ...
wsl bash create_container.sh %CONTAINER_NAME% registry.cn-hangzhou.aliyuncs.com/havenask/ha3_runtime:latest

echo ✅ 容器创建完成：%CONTAINER_NAME%
pause
