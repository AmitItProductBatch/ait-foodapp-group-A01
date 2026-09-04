#!/usr/bin/env bash
# ==============================================================================
# FoodDelivery Deployment Script
# Automates Docker Compose deployment and verification
# ==============================================================================

set -eo pipefail

# Text formatting
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${CYAN}======================================================${NC}"
echo -e "${CYAN}🚀 Starting FoodDelivery Stack Deployment...${NC}"
echo -e "${CYAN}======================================================${NC}"

# 1. Check if .env file exists; if not, copy from .env.example
if [ ! -f .env ]; then
    if [ -f .env.example ]; then
        echo -e "${YELLOW}⚠️  .env file not found. Creating .env from .env.example...${NC}"
        cp .env.example .env
        echo -e "${GREEN}✅ .env created successfully.${NC}"
    else
        echo -e "${RED}❌ Error: Neither .env nor .env.example file found!${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✅ .env file found.${NC}"
fi

# 2. Determine Docker Compose command
if docker compose version > /dev/null 2>&1; then
    DOCKER_COMPOSE_CMD="docker compose"
elif command -v docker-compose > /dev/null 2>&1; then
    DOCKER_COMPOSE_CMD="docker-compose"
else
    echo -e "${RED}❌ Error: Docker Compose is not installed or not in PATH!${NC}"
    exit 1
fi
echo -e "${BLUE}ℹ️  Using Docker Compose command: ${DOCKER_COMPOSE_CMD}${NC}"

# 3. Verify Docker daemon is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Error: Docker daemon is not running! Please start Docker first.${NC}"
    exit 1
fi

# 4. Pull, Build, and Launch Containers
echo -e "${YELLOW}📦 Building and launching containers in detached mode...${NC}"
${DOCKER_COMPOSE_CMD} up -d --build --remove-orphans

# 5. Display Status of Running Services
echo -e "\n${CYAN}🔍 Container Health & Status Overview:${NC}"
${DOCKER_COMPOSE_CMD} ps

# 6. Output Access Endpoints
echo -e "\n${GREEN}======================================================${NC}"
echo -e "${GREEN}🎉 FoodDelivery Stack Deployed Successfully!${NC}"
echo -e "${GREEN}======================================================${NC}"
echo -e "👉 ${CYAN}Frontend UI       :${NC} http://localhost:3001"
echo -e "👉 ${CYAN}Backend REST API  :${NC} http://localhost:8089/api/users"
echo -e "👉 ${CYAN}PostgreSQL DB     :${NC} localhost:3307 (DB: fooddelivery)"
echo -e "${GREEN}======================================================${NC}"
