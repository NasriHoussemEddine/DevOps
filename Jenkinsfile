pipeline {
    agent any

    tools {
        maven "Maven"
    }

    environment {
        VM_IP = '192.168.157.146'
        VM_PORT = '8081'
        DOCKER_HUB_TOKEN = credentials('docker-hub-key')
        GITHUB_TOKEN = credentials('github-token')
        SONAR_TOKEN = credentials('sonarqube2')
        URL_SONAR = 'http://192.168.157.146:9000'
        NEXUS_DOCKER_REPO = "http://192.168.157.146:8081/repository/backend-repo"
        VERSION = ""
        JAR_FILE = ""


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
                sh "mvn sonar:sonar -Dsonar.projectKey=devops_project -Dsonar.host.url=${URL_SONAR} -Dsonar.login=$SONAR_TOKEN"
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
                    //sh "echo ${NEXUS_PASSWORD} | docker login -u ${NEXUS_USERNAME} --password-stdin ${NEXUS_DOCKER_REPO}"
                    sh "mvn deploy -DaltDeploymentRepository=nexus::default::http://${VM_IP}:${VM_PORT}/repository/nexus-releases-houcem -Dusername=${NEXUS_USERNAME} -Dpassword=${NEXUS_PASSWORD}"
                    sh "docker tag houssemnasri/houssemnasri1:${VERSION} http://192.168.157.146:8081/repository/backend-repo/houssemnasri/houssemnasri1:${VERSION}"
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
       /*  stage('Start Docker Compose') {
                    steps {
                        // Exécutez Docker Compose à partir du répertoire du projet Jenkins
                        sh "docker-compose -f ./up -d"
                    }
                } */
    }
}
