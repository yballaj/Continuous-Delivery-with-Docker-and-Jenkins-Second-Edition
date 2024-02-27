podTemplate(containers: [
    containerTemplate(
        name: 'gradle', image: 'gradle:6.3-jdk14', command: 'sleep', args: '30d'
    )
]) {
    node(POD_LABEL) {
        stage('Run pipeline against a gradle project') {
            container('gradle') {
                stage('Build a gradle project') {
                    git 'https://github.com/dlambrig/Continuous-Delivery-with-Docker-and-Jenkins-Second-Edition.git'
                    sh '''
                    cd Chapter08/sample1
                    chmod +x gradlew
                    '''
                }

                stage("Tests") {
                    try {
                        sh '''
                        pwd
                        cd Chapter08/sample1
                        ./gradlew test
                        ./gradlew checkstyleMain checkstyleTest
                        '''

                        // Conditional execution based on branch
                        if (env.BRANCH_NAME == 'master') {
                            // Execute coverage verification 
                            sh './gradlew jacocoTestCoverageVerification'
                           
                        }
                    } catch (Exception E) {
                        echo 'Failure detected'
                    }

                    // Publish Checkstyle report for all branches
                    publishHTML(target: [
                        reportDir: 'Chapter08/sample1',
                        reportFiles: 'main.html', 
                        reportName: "JaCoCo Checkstyle Report"
                    ])
                }
            }
        }
    }
}
