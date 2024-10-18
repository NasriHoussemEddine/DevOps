pipeline {
    agent any

    tools {
        maven "Maven"
    }

    environment {
        //DOCKER_HUB_CREDENTIALS = credentials('dockerhub')
        //GITHUB_CREDENTIALS = credentials('github-connection')
          DOCKER_HUB_TOKEN = credentials('docker-hub-key')
          GITHUB_TOKEN = credentials('github-token')
    }

    stages {
        stage('Build') {
            steps {
                // Get code from the specific GitHub repository with credentials and branch
                git branch: 'main', url: "https://${GITHUB_TOKEN}@github.com/NasriHoussemEddine/DevOps.git"
                sh "mvn clean package"
            }
        }
        stage('Deploy to Nexus') {
                    steps {
                        sh 'mvn deploy'
                    }
                }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t houssemnasri/houssemnasri1:1.0.0 .'
            }
        }

        stage('Docker Login and Push') {
            steps {
                sh 'echo $DOCKER_HUB_TOKEN | docker login -u houssemnasri --password-stdin'
                sh 'docker push houssemnasri/houssemnasri1:1.0.0'
            }
        }

        stage('Pull Docker Image') {
            steps {
                sh 'docker pull houssemnasri/houssemnasri1:1.0.0'
            }
        }

        stage('Cleanup') {
                    steps {
                        sh 'docker rm -f db || true'
                        sh 'docker rm -f DevopsProjetcontainer || true'
                    }
                }

       stage('Run Containers and Deploy Application') {
           steps {
               script {
                   // Step 1: Run the MySQL Database Container
                   echo 'Starting MySQL container...'
                   sh '''
                       docker run -d --name db \
                       -e MYSQL_ROOT_PASSWORD=0000 \
                       -e MYSQL_DATABASE=tpachato \
                       -p 3306:3306 \
                       mysql:5.7
                   '''

                   // Wait for MySQL to initialize
                   echo 'Waiting for MySQL to be ready...'
                   sleep(5) // Adjust sleep time based on your DB initialization time

                   // Step 2: Run the Spring Boot Application Container
                   echo 'Starting Spring Boot application container...'
                   sh '''
                       docker run -d --name DevopsProjetcontainer \
                      -p 8089:8089 \
                       houssemnasri/houssemnasri1:1.0.0
                   '''

                   // Confirm the containers are running
                   sh 'docker ps'

               }
           }
       }
    }
}