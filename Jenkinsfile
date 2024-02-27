podTemplate(containers: [
    containerTemplate(
        name: 'gradle', image: 'gradle:6.3-jdk14', command: 'sleep', args: '30d'
    )
]) {
    node(POD_LABEL) {
        stage('Run pipeline against a gradle project') {
            container('gradle') {
                stage('Build a gradle project') {
                    git 'https://github.com/yballaj/Continuous-Delivery-with-Docker-and-Jenkins-Second-Edition.git'
                    sh '''
                    cd Chapter08/sample1
                    chmod +x gradlew
                    '''
                }

                stage("Conditional Tests") {
                    // Execute general tests for all branches
                    sh '''
                    pwd
                    cd Chapter08/sample1
                    ./gradlew test
                    ./gradlew checkstyleMain checkstyleTest
                    '''

                    // Conditionally execute coverage verification for the master branch
                    if (env.BRANCH_NAME == 'master') {
                        sh './gradlew jacocoTestCoverageVerification'
                    } else {
                        echo "Skipping 'jacocoTestCoverageVerification' for branch: ${env.BRANCH_NAME}"
                    }

                    // Publish Checkstyle report for all branches
                    publishHTML(target: [
                        reportDir: 'Chapter08/sample1/build/reports/checkstyle',
                        reportFiles: 'main.html', 
                        reportName: "Checkstyle Report"
                    ])
                }
            }
        }
    }
}
