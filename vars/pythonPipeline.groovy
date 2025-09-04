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
        steps {
          pythonUnitTest{}
        }
        when {
          anyOf {
            branch 'develop'
            branch pattern: 'release-v*'
            branch pattern: 'feature-*'
            branch pattern: 'bugfix-*'
            branch pattern: 'hotfix-*'
            tag pattern: 'v*'
          }
        }
      }
    }
  }
}