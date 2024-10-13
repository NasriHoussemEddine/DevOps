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

                // Build the project, without skipping tests
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

        stage('Run Container and Execute Tests') {
            steps {
                script {
                    sh 'docker run -d --name testDevopsProjet houssemnasri/houssemnasri1:1.0.0'

                    // Run tests inside the container and capture the output
                    def testOutput = sh(script: 'docker exec testDevopsProjet mvn test', returnStdout: true).trim()

                    // Print the test output
                    echo "Test Output:\n${testOutput}"

                    // Check the result of the test command
                    def testResult = sh(script: 'docker exec testDevopsProjet mvn test', returnStatus: true)

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
