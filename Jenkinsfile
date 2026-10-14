pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        // Explicitly guarantee JDK 21 on Windows native Jenkins agent
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.12.1'
        PATH = "${JAVA_HOME}\\bin;${env.PATH}"
    }

    stages {
        stage('Checkout SCM') {
            steps {
                echo '=== Stage 1: Checkout Source Code ==='
                checkout scm
            }
        }

        stage('Environment Verification') {
            steps {
                echo '=== Stage 2: Verifying Toolchain & Environment ==='
                script {
                    if (isUnix()) {
                        sh 'java -version'
                        sh 'javac -version'
                        sh 'git --version'
                    } else {
                        bat 'java -version'
                        bat 'javac -version'
                        bat 'git --version'
                    }
                }
            }
        }

        stage('Compile') {
            steps {
                echo '=== Stage 3: Compiling Application Sources ==='
                script {
                    if (isUnix()) {
                        sh 'chmod +x ./mvnw'
                        sh './mvnw clean compile'
                    } else {
                        bat '.\\mvnw.cmd clean compile'
                    }
                }
            }
        }

        stage('Automated Tests') {
            steps {
                echo '=== Stage 4: Running Unit, Integration & Privacy Tests ==='
                script {
                    if (isUnix()) {
                        sh './mvnw test'
                    } else {
                        bat '.\\mvnw.cmd test'
                    }
                }
            }
        }

        stage('Package Artifact') {
            steps {
                echo '=== Stage 5: Packaging Spring Boot Executable JAR ==='
                script {
                    if (isUnix()) {
                        sh './mvnw package -DskipTests'
                    } else {
                        bat '.\\mvnw.cmd package -DskipTests'
                    }
                }
            }
        }
    }

    post {
        always {
            echo '=== Pipeline Completed: Publishing Test Results ==='
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
        success {
            echo '=== Build Succeeded! Archiving Production JAR ==='
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false, fingerprint: true
        }
        failure {
            echo '=== Build Failed! Check Console Output for Root Cause ==='
        }
    }
}
