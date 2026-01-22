pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    parameters {
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
