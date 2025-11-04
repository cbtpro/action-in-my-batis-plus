#!/bin/bash
# Havenask 容器创建脚本（Linux/macOS）

# 提示用户输入容器名称
read -p "请输入容器名称（CONTAINER_NAME）: " CONTAINER_NAME

# 校验输入是否为空
if [ -z "$CONTAINER_NAME" ]; then
  echo "❌ 容器名称不能为空！"
  exit 1
fi

# 下载创建脚本
echo "➡️ 正在下载 create_container.sh ..."
curl -fsSL -O https://github.com/alibaba/havenask/releases/download/v1.2.0/create_container.sh

# 拉取镜像
echo "➡️ 正在拉取镜像 registry.cn-hangzhou.aliyuncs.com/havenask/ha3_runtime:latest ..."
docker pull registry.cn-hangzhou.aliyuncs.com/havenask/ha3_runtime:latest

# 创建容器
echo "➡️ 正在创建容器：$CONTAINER_NAME ..."
chmod +x create_container.sh
./create_container.sh "$CONTAINER_NAME" registry.cn-hangzhou.aliyuncs.com/havenask/ha3_runtime:latest

echo "✅ 容器创建完成：$CONTAINER_NAME"
