pipeline {
    agent any

    tools {
        maven "Maven"
    }

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub')
        GITHUB_CREDENTIALS = credentials('github-connection')
    }

    stages {
        stage('Build') {
            steps {
                // Get code from the specific GitHub repository with credentials and branch
                git branch: 'main', url: 'https://github.com/NasriHoussemEddine/DevOps.git', credentialsId: 'github-connection'

                sh "mvn clean package"
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t houssemnasri/houssemnasri1:1.0.0 .'
            }
        }

        stage('Docker Login and Push') {
            steps {
                sh 'echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin'
                sh 'docker push houssemnasri/houssemnasri1:1.0.0'
            }
        }

        stage('Pull Docker Image') {
            steps {
                sh 'docker pull houssemnasri/houssemnasri1:1.0.0'
            }
        }

       stage('Run Containers and Deploy Application') {
           steps {
               script {
                   // Step 1: Run the MySQL Database Container
                   echo 'Starting MySQL container...'
                   sh '''
                       docker run -d --name db \
                       -e MYSQL_ROOT_PASSWORD= \
                       -e MYSQL_DATABASE=tpachato \
                       -p 3306:3306 \
                       mysql:5.7
                   '''

                   // Wait for MySQL to initialize
                   echo 'Waiting for MySQL to be ready...'
                   sleep(30) // Adjust sleep time based on your DB initialization time

                   // Step 2: Run the Spring Boot Application Container
                   echo 'Starting Spring Boot application container...'
                   sh '''
                       docker run -d --name DevopsProjetcontainer \
                      -p 8089:8089 \
                       houssemnasri/houssemnasri1:1.0.0
                   '''

                   // Confirm the containers are running
                   sh 'docker ps'

                   // You can optionally add any health checks or validation logic to confirm both services are running
               }
           }
       }
    }
}