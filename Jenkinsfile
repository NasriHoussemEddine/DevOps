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
                git branch: 'main', url: 'https://github.com/NasriHoussemEddine/DevOps.git', credentialsId : 'github-connection'

                // Change to the tpAchatProject directory before running Maven
                dir('pAchatProject-DevOps') {
                    // Run Maven build and skip tests
                    sh "mvn clean package -DskipTests"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Navigate to the project directory
                dir('pAchatProject-DevOps') {
                    // Build the Docker image from the Dockerfile in the current directory
                    sh 'docker build -t houssemnasri/houssemnasri1:1.0.0 .'
                }
            }
        }

        stage('Docker Login and Push') {
            steps {
                // Log in to Docker Hub using the credentials stored in Jenkins
                sh 'echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin'

                // Push the Docker image to Docker Hub
                sh 'docker push houssemnasri/houssemnasri1:1.0.0'
            }
        }

        stage('Pull Docker Image') {
            steps {
                // Pull the Docker image from Docker Hub
                sh 'docker pull houssemnasri/houssemnasri1:1.0.0'
            }
        }

        stage('Run Container and Execute Tests') {
            steps {
                script {
                    // Run the Docker container in detached mode
                    sh 'docker run -d --name testDevopsProjet houssemnasri/houssemnasri1:1.0.0'

                    // Run tests inside the container and capture the output
                    // Replace 'nvm test' with the actual command to run your tests
                    def testOutput = sh(script: 'docker exec testDevopsProjet nvm test', returnStdout: true).trim()

                    // Print the test output
                    echo "Test Output:\n${testOutput}"

                    // Check the result of the test command
                    def testResult = sh(script: 'docker exec testDevopsProjet nvm test', returnStatus: true)

                    if (testResult != 0) {
                        error "Tests failed in the container."
                    } else {
                        echo "Tests passed successfully."
                    }
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    // Stop and remove the container after tests
                    sh 'docker stop testDevopsProjet'
                    sh 'docker rm testDevopsProjet'
                }
            }
        }
    }



}
