podTemplate(containers: [
    containerTemplate(
        name: 'gradle', image: 'gradle:6.3-jdk14', command: 'sleep', args: '30d'
    )
]) {
    node(POD_LABEL) {
        stage('Checkout code and prepare environment') {
            container('gradle') {
                stage('Build a gradle project') {
                    git 'https://github.com/yballaj/Continuous-Delivery-with-Docker-and-Jenkins-Second-Edition.git'
                    sh '''
                    cd Chapter08/sample1
                    chmod +x gradlew
                    '''
                }

                stage("Conditional Tests") {
                    boolean testsPassed = true
                    try {
                        // Execute general tests for all branches
                        sh '''
                        pwd
                        cd Chapter08/sample1
                        ./gradlew test
			./gradlew checkstyleMain checkstyleTest
                        '''
                    } catch (Exception e) {
                        testsPassed = false
                        echo "Tests failed!"
                    } finally {
                        // Generate JaCoCo report no matter if tests pass or fail
                        sh '''
                        cd Chapter08/sample1
                        ./gradlew jacocoTestReport
                        '''
                        if (testsPassed) {
                            echo "Tests passed!"
                        }
                    }

                    // Conditionally execute coverage verification for the main branch
                    if (env.BRANCH_NAME == 'main') {
                        sh 'cd Chapter08/sample1 && ./gradlew jacocoTestCoverageVerification'
                    } else {
                        echo "Skipping 'jacocoTestCoverageVerification' for branch: ${env.BRANCH_NAME}"
                    }
                     
                    // Publish JaCoCo Report for all branches
                    publishHTML(target: [
                        reportDir: 'Chapter08/sample1',
                        reportFiles: 'index.html',
                        reportName: "JaCoCo Report"
                    ])
                    
                    // Publish Checkstyle report for all branches
                    publishHTML(target: [
                        reportDir: 'Chapter08/sample1',
                        reportFiles: 'main.html', 
                        reportName: "Checkstyle Report"
                    ])
                }
            }
        }
    }
}
