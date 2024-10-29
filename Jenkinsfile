pipeline {
    agent any

    tools {
        maven "Maven"
    }

    environment {
        VM_IP = '192.168.157.146' // Set dynamically or via Jenkins configuration
        VM_PORT = '8081'          // Set dynamically or via Jenkins configuration
        DOCKER_HUB_TOKEN = credentials('docker-hub-key')
        GITHUB_TOKEN = credentials('github-token')
        SONAR_TOKEN = credentials('sonarqube2')
        url_sonar = 'http://192.168.157.146:9000'
        NEXUS_DOCKER_REPO = "http://${VM_IP}:${VM_PORT}/repository/docker-repo/"
        VERSION = "" // Variable for the version tag
        JAR_FILE = "" // Variable to hold the name of the latest jar file

        // Add Nexus credentials
        NEXUS_USERNAME = 'admin'
        NEXUS_PASSWORD = '0000'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: "https://${GITHUB_TOKEN}@github.com/NasriHoussemEddine/DevOps.git"
            }
        }

        stage('SonarQube') {
            steps {
                sh "mvn sonar:sonar -Dsonar.projectKey=devops_project -Dsonar.host.url=${url_sonar} -Dsonar.login=$SONAR_TOKEN"
            }
        }

        stage('Build') {
            steps {
                sh "mvn clean package"
                script {
                    // Get the project version from pom.xml
                    VERSION = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    echo "Project version is ${VERSION}"
                }
            }
        }

        stage('Get Latest JAR File') {
            steps {
                script {
                    // Get the latest JAR file in the target directory
                    JAR_FILE = sh(script: "ls -t target/*.jar | head -n 1", returnStdout: true).trim()
                    echo "Latest JAR file is ${JAR_FILE}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build the Docker image
                sh "docker build --build-arg JAR_FILE=${JAR_FILE} -t houssemnasri/houssemnasri1:${VERSION} ."
            }
        }

        stage('Deploy Docker Image to Nexus') {
            steps {
                script {
                    // Login to Nexus Docker repository using Nexus credentials
                    sh "echo ${NEXUS_PASSWORD} | docker login -u ${NEXUS_USERNAME} --password-stdin ${NEXUS_DOCKER_REPO}"

                    // Tagging the Docker image for Nexus
                    sh "docker tag houssemnasri/houssemnasri1:${VERSION} ${NEXUS_DOCKER_REPO}/houssemnasri/houssemnasri1:${VERSION}"

                    // Pushing the image to the Nexus repository
                    sh "docker push ${NEXUS_DOCKER_REPO}/houssemnasri/houssemnasri1:${VERSION}"
                }
            }
        }

        stage('Docker Login and Push to Docker Hub') {
            steps {
                script {
                    // Login to Docker Hub
                    sh 'echo $DOCKER_HUB_TOKEN | docker login -u houssemnasri --password-stdin'

                    // Tagging the Docker image for Docker Hub
                    sh "docker tag houssemnasri/houssemnasri1:${VERSION} houssemnasri/houssemnasri1:${VERSION}"

                    // Pushing the image to Docker Hub
                    sh "docker push houssemnasri/houssemnasri1:${VERSION}"
                }
            }
        }

        stage('Pull Docker Image') {
            steps {
                sh "docker pull houssemnasri/houssemnasri1:${VERSION}"
            }
        }
        stage('Start Docker Compose') {
                    steps {
                        // Exécutez Docker Compose à partir du répertoire du projet Jenkins
                        sh "docker-compose -f ./up -d"
                    }
                }
    }
}
