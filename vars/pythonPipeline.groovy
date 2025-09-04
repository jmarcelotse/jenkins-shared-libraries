def call (body) {

  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()

  pipeline {
    agent {
      kubernetes {
        yamlFile 'jenkinsPod.yaml'
      }
    }
    stages {

      stage('Unit test') {
        when {
          anyOf {
            branch 'develop'
            branch pattern: 'release-v*'
            branch pattern: 'feature-*'
            branch pattern: 'bugfix-*'
            branch pattern: 'hotfix-*'
            tag    pattern: 'v*'
          }
        }
        steps {
          pythonUnitTest{}
        }
      }

      stage('SonarQube Scan') {
        when {
          anyOf {
            branch 'develop'
            branch pattern: 'release-v*'
            branch pattern: 'feature-*'
            branch pattern: 'bugfix-*'
            branch pattern: 'hotfix-*'
            tag    pattern: 'v*'
          }
        }
        environment {
          SONAR_HOST_URL = "http://sonarqube.localhost.com"
          SONAR_TOKEN    = credentials('sonar-scanner-cli')
        }
        steps {
          sonarqubeScan{}
        }
      }

    }
  }
}
