pipeline {
    agent any

    // =========================================================================
    // Pipeline Parameters (Customizable when triggering "Build with Parameters")
    // =========================================================================
    parameters {
        choice(
            name: 'DEPLOYMENT_MODE',
            choices: ['Local Docker Compose (Recommended)', 'Docker Hub Registry + SSH Remote'],
            description: 'Choose deployment mode: Local (Jenkins & Docker on same server) or Remote via Docker Hub'
        )
        string(
            name: 'SERVER_IP',
            defaultValue: 'localhost',
            description: 'Public IP or domain of the deployment server'
        )
        string(
            name: 'DOCKER_USERNAME',
            defaultValue: 'yourdockerhubusername',
            description: 'Docker Hub username (only required for Registry push mode)'
        )
        string(
            name: 'DOCKER_CREDENTIALS_ID',
            defaultValue: 'docker-hub-credentials',
            description: 'Jenkins Credentials ID for Docker Hub (only required for Registry push mode)'
        )
    }

    // =========================================================================
    // Environment Variables
    // =========================================================================
    environment {
        BACKEND_IMAGE  = "${params.DOCKER_USERNAME}/food-delivery-backend"
        FRONTEND_IMAGE = "${params.DOCKER_USERNAME}/food-delivery-frontend"
        IMAGE_TAG      = "${BUILD_NUMBER}"
        VITE_API_URL   = "http://${params.SERVER_IP}:8089/api/users"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {
        // ---------------------------------------------------------------------
        // Stage 1: Checkout Source Code
        // ---------------------------------------------------------------------
        stage('Checkout Source Code') {
            steps {
                echo "📦 Checking out repository branch: ${env.GIT_BRANCH ?: 'main'}"
                checkout scm
            }
        }

        // ---------------------------------------------------------------------
        // Stage 2: Build Backend (Spring Boot 3 with Java 17)
        // ---------------------------------------------------------------------
        stage('Build Backend') {
            steps {
                dir('food-delivery-backend') {
                    echo "☕ Packaging Spring Boot Backend with Maven Wrapper..."
                    script {
                        if (isUnix()) {
                            sh "chmod +x ./mvnw"
                            sh "./mvnw clean package -DskipTests -B"
                        } else {
                            bat ".\\mvnw.cmd clean package -DskipTests -B"
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------------------
        // Stage 3: Build Frontend (React / Vite)
        // ---------------------------------------------------------------------
        stage('Build Frontend') {
            steps {
                dir('food-delivery-frontend') {
                    echo "⚛️ Installing dependencies and building React production bundle..."
                    script {
                        if (isUnix()) {
                            sh "npm ci || npm install"
                            sh "npm run build"
                        } else {
                            bat "npm ci || npm install"
                            bat "npm run build"
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------------------
        // Stage 4: Deploy with Docker Compose (Local / Single-Server Deployment)
        // ---------------------------------------------------------------------
        stage('Deploy with Docker Compose') {
            when {
                expression { 
                    return params.DEPLOYMENT_MODE == 'Local Docker Compose (Recommended)' || 
                           params.DOCKER_USERNAME == 'yourdockerhubusername' 
                }
            }
            steps {
                script {
                    echo "🚀 Deploying PostgreSQL, Backend, and Frontend containers via Docker Compose..."
                    if (isUnix()) {
                        sh """
                            export VITE_API_BASE_URL="${env.VITE_API_URL}"
                            docker compose up -d --build --remove-orphans || docker-compose up -d --build --remove-orphans
                        """
                    } else {
                        bat """
                            set VITE_API_BASE_URL=${env.VITE_API_URL}
                            docker compose up -d --build --remove-orphans
                        """
                    }
                }
            }
        }

        // ---------------------------------------------------------------------
        // Stage 5: Build & Push to Docker Hub (Only for Registry Mode)
        // ---------------------------------------------------------------------
        stage('Push to Docker Hub') {
            when {
                expression { 
                    return params.DEPLOYMENT_MODE == 'Docker Hub Registry + SSH Remote' && 
                           params.DOCKER_USERNAME != 'yourdockerhubusername' 
                }
            }
            steps {
                script {
                    echo "🐳 Building & Pushing images to Docker Hub..."
                    withCredentials([usernamePassword(
                        credentialsId: params.DOCKER_CREDENTIALS_ID,
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                        
                        sh "docker build -t ${BACKEND_IMAGE}:${IMAGE_TAG} -t ${BACKEND_IMAGE}:latest ./food-delivery-backend"
                        sh "docker build --build-arg VITE_API_BASE_URL=${VITE_API_URL} -t ${FRONTEND_IMAGE}:${IMAGE_TAG} -t ${FRONTEND_IMAGE}:latest ./food-delivery-frontend"
                        
                        sh "docker push ${BACKEND_IMAGE}:${IMAGE_TAG}"
                        sh "docker push ${BACKEND_IMAGE}:latest"
                        sh "docker push ${FRONTEND_IMAGE}:${IMAGE_TAG}"
                        sh "docker push ${FRONTEND_IMAGE}:latest"
                        sh "docker logout"
                    }
                }
            }
        }

        // ---------------------------------------------------------------------
        // Stage 6: Verify Health of Running Containers
        // ---------------------------------------------------------------------
        stage('Verify Containers Health') {
            steps {
                script {
                    echo "🔍 Checking container status..."
                    if (isUnix()) {
                        sh "docker compose ps || docker-compose ps"
                    } else {
                        bat "docker compose ps"
                    }
                }
            }
        }
    }

    // =========================================================================
    // Post-Pipeline Actions & Cleanup
    // =========================================================================
    post {
        always {
            echo "🧹 Cleaning up obsolete dangling images..."
            script {
                if (isUnix()) {
                    sh "docker image prune -f || true"
                } else {
                    bat "docker image prune -f || ver >nul"
                }
            }
        }
        success {
            echo "🎉 ========================================================"
            echo "🎉 Pipeline Completed Successfully!"
            echo "👉 Frontend Application : http://${params.SERVER_IP}:3001"
            echo "👉 Backend REST API     : http://${params.SERVER_IP}:8089/api/users"
            echo "👉 PostgreSQL Database  : Port 3307 (DB: fooddelivery)"
            echo "🎉 ========================================================"
        }
        failure {
            echo "❌ Pipeline failed! Please check error log above."
        }
    }
}
