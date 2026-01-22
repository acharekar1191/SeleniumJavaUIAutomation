pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Select browser')
        choice(name: 'ENV', choices: ['qa', 'uat', 'prod'], description: 'Select environment')
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Regression Automation Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh """
                       mvn clean install \
                       -Dbrowser=${params.BROWSER} \
                       -Denv=${params.ENV}
                    """
                }
            }
        }

        stage('Publish Allure Results') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'allure-results']]
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/target/*.log', allowEmptyArchive: true
        }
        failure {
            echo '❌ Regression tests failed'
        }
        success {
            echo '✅ Regression tests passed'
        }
    }
}
