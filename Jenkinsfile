pipeline {
    agent any 

    stages {
        stage('Prepare Environment') {
            agent {
                kubernetes {
                    // Define pod template using yaml or yamlFile
                    yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: jenkins-pipeline
spec:
  containers:
  - name: gradle
    image: gradle:6.3-jdk14
    command:
    - cat
    tty: true
    volumeMounts:
    - name: shared-storage
      mountPath: /mnt
    - name: kaniko-secret
      mountPath: /kaniko/.docker
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command:
    - cat
    tty: true
    volumeMounts:
    - name: shared-storage
      mountPath: /mnt
  volumes:
  - name: shared-storage
    persistentVolumeClaim:
      claimName: jenkins-pv-claim
  - name: kaniko-secret
    secret:
      secretName: dockercred
"""
                }
            }
            steps {
                script {
                    container('gradle') {
                        sh '''
                        git clone https://github.com/yballaj/Continuous-Delivery-with-Docker-and-Jenkins-Second-Edition.git
                        cd Continuous-Delivery-with-Docker-and-Jenkins-Second-Edition/Chapter08/sample1
                        chmod +x gradlew
                        '''
                    }
                }
            }
        }

        stage("Conditional Tests") {
            steps {
                script {
                    container('gradle') {
                        boolean testsPassed = true
                        try {
                            sh '''
                            cd Continuous-Delivery-with-Docker-and-Jenkins-Second-Edition/Chapter08/sample1
                            ./gradlew test --no-daemon
                            ./gradlew checkstyleMain --no-daemon
                            '''
                            if (env.BRANCH_NAME == 'main') {
                                sh 'cd Continuous-Delivery-with-Docker-and-Jenkins-Second-Edition/Chapter08/sample1 && ./gradlew jacocoTestCoverageVerification --no-daemon'
                            }
                        } catch (Exception e) {
                            testsPassed = false
                            echo "Tests failed!"
                        }

                        if (!testsPassed) {
                            error("Tests failed or code coverage not met, stopping the build!")
                        }
                    }
                }
            }
        }

        stage('Build and Push Container') {
            when {
                not {
                    branch 'playground'
                }
            }
            steps {
                script {
                    container('kaniko') {
                        // Determine image name based on branch
                        def imageTag = (env.BRANCH_NAME == 'main') ? '1.0' : '0.1'
                        def imageName = (env.BRANCH_NAME == 'main' || env.BRANCH_NAME.startsWith('release')) ? 'calculator' : 'calculator-feature'
                        sh """
                        echo 'FROM openjdk:8-jre' > Dockerfile
                        echo 'COPY ./build/libs/*.jar app.jar' >> Dockerfile
                        echo 'ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]' >> Dockerfile
                        /kaniko/executor --context . --dockerfile Dockerfile --destination yballaj/${imageName}:${imageTag}
                        """
                    }
                }
            }
        }
    }

    post {
        failure {
            echo 'The build failed.'
        }
    }
}
