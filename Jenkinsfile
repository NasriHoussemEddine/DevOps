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
                    // Récupérer la version Maven depuis le fichier pom.xml
                    VERSION = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    echo "Project version is ${VERSION}"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh "mvn deploy -DrepositoryId=nexus-releases-houcem"
            }
        }

        stage('Build Docker Image') {
            steps {
                // Utiliser la version Maven comme tag de l'image Docker
                sh "docker build -t houssemnasri/houssemnasri1:${VERSION} ."
            }
        }

        stage('Docker Login and Push to Docker Hub') {
            steps {
                sh 'echo $DOCKER_HUB_TOKEN | docker login -u houssemnasri --password-stdin'
                sh "docker push houssemnasri/houssemnasri1:${VERSION}"
            }
        }

        stage('Docker Login and Push to Nexus') {
            steps {
                sh 'echo $DOCKER_HUB_TOKEN | docker login -u houssemnasri --password-stdin ${NEXUS_DOCKER_REPO}'
                sh "docker tag houssemnasri/houssemnasri1:${VERSION} ${NEXUS_DOCKER_REPO}/houssemnasri/houssemnasri1:${VERSION}"
                sh "docker push ${NEXUS_DOCKER_REPO}/houssemnasri/houssemnasri1:${VERSION}"
            }
        }

        stage('Pull Docker Image') {
            steps {
                sh "docker pull houssemnasri/houssemnasri1:${VERSION}"
            }
        }
  }
}
