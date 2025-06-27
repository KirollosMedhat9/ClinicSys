#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Starting ClinicSys Services ===${NC}"
echo ""

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        echo -e "${RED}Port $1 is already in use. Please stop the service using this port first.${NC}"
        return 1
    fi
    return 0
}

# Function to wait for service to be ready
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    echo -e "${YELLOW}Waiting for $service_name to be ready...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✓ $service_name is ready!${NC}"
            return 0
        fi
        
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}✗ $service_name failed to start within expected time${NC}"
    return 1
}

# Check if required ports are available
echo -e "${YELLOW}Checking port availability...${NC}"
check_port 8761 || exit 1
check_port 8888 || exit 1
check_port 8080 || exit 1
check_port 8081 || exit 1
echo -e "${GREEN}✓ All ports are available${NC}"
echo ""

# Start Service Registry
echo -e "${YELLOW}1. Starting Service Registry (Eureka)...${NC}"
cd service-registry
mvn spring-boot:run > ../logs/service-registry.log 2>&1 &
REGISTRY_PID=$!
cd ..

# Wait for Service Registry
wait_for_service "http://localhost:8761/actuator/health" "Service Registry"
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start Service Registry${NC}"
    kill $REGISTRY_PID 2>/dev/null
    exit 1
fi

# Start Config Server
echo -e "${YELLOW}2. Starting Config Server...${NC}"
cd config-server
mvn spring-boot:run > ../logs/config-server.log 2>&1 &
CONFIG_PID=$!
cd ..

# Wait for Config Server
wait_for_service "http://localhost:8888/actuator/health" "Config Server"
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start Config Server${NC}"
    kill $REGISTRY_PID $CONFIG_PID 2>/dev/null
    exit 1
fi

# Start API Gateway
echo -e "${YELLOW}3. Starting API Gateway...${NC}"
cd api-gateway
mvn spring-boot:run > ../logs/api-gateway.log 2>&1 &
GATEWAY_PID=$!
cd ..

# Wait for API Gateway
wait_for_service "http://localhost:8080/actuator/health" "API Gateway"
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start API Gateway${NC}"
    kill $REGISTRY_PID $CONFIG_PID $GATEWAY_PID 2>/dev/null
    exit 1
fi

# Start Auth Service
echo -e "${YELLOW}4. Starting Auth Service...${NC}"
cd auth-service
mvn spring-boot:run > ../logs/auth-service.log 2>&1 &
AUTH_PID=$!
cd ..

# Wait for Auth Service
wait_for_service "http://localhost:8081/actuator/health" "Auth Service"
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start Auth Service${NC}"
    kill $REGISTRY_PID $CONFIG_PID $GATEWAY_PID $AUTH_PID 2>/dev/null
    exit 1
fi

echo ""
echo -e "${GREEN}=== All Services Started Successfully ===${NC}"
echo ""
echo -e "${YELLOW}Service URLs:${NC}"
echo "Service Registry: http://localhost:8761"
echo "Config Server:   http://localhost:8888"
echo "API Gateway:     http://localhost:8080"
echo "Auth Service:    http://localhost:8081"
echo ""
echo -e "${YELLOW}Process IDs:${NC}"
echo "Service Registry: $REGISTRY_PID"
echo "Config Server:   $CONFIG_PID"
echo "API Gateway:     $GATEWAY_PID"
echo "Auth Service:    $AUTH_PID"
echo ""
echo -e "${YELLOW}Log Files:${NC}"
echo "Service Registry: logs/service-registry.log"
echo "Config Server:   logs/config-server.log"
echo "API Gateway:     logs/api-gateway.log"
echo "Auth Service:    logs/auth-service.log"
echo ""
echo -e "${YELLOW}To stop all services, run:${NC}"
echo "kill $REGISTRY_PID $CONFIG_PID $GATEWAY_PID $AUTH_PID"
echo ""
echo -e "${GREEN}Ready for testing!${NC}" 