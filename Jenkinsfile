pipeline {

    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    parameters {

        choice(
            name: 'ENV',
            choices: ['qa', 'uat', 'prod'],
            description: 'Select Environment'
        )

        choice(
            name: 'BROWSER',
            choices: ['chrome', 'edge', 'firefox'],
            description: 'Select Browser'
        )
    }

    stages {

        stage('Checkout Source Code') {
            steps {

                git branch: 'main',
                    url: 'https://github.com/acharekar1191/SeleniumJavaUIAutomation.git'

                echo 'Source code checkout completed'
            }
        }

        stage('Execute Regression Suite') {

            steps {

                echo "Environment : ${params.ENV}"
                echo "Browser     : ${params.BROWSER}"

                catchError(
                    buildResult: 'UNSTABLE',
                    stageResult: 'FAILURE'
                ) {

                    bat """
                        mvn clean test ^
                        -Denv=${params.ENV} ^
                        -Dbrowser=${params.BROWSER}
                    """
                }
            }
        }
    }

    post {

        always {

            echo 'Publishing TestNG Results'

            junit(
                testResults: 'target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            echo 'Publishing Allure Report'

            allure([
                includeProperties: false,
                jdk: '',
                results: [[path: 'allure-results']]
            ])

            echo 'Archiving Logs'

            archiveArtifacts(
                artifacts: 'logs/**/*.*',
                allowEmptyArchive: true
            )

            echo 'Archiving Screenshots'

            archiveArtifacts(
                artifacts: 'screenshots/**/*.*',
                allowEmptyArchive: true
            )
        }

        success {
            echo '✅ All regression tests passed'
        }

        unstable {
            echo '⚠️ Some test cases failed. Please review Allure report.'
        }

        failure {
            echo '❌ Pipeline execution failed'
        }
    }
}