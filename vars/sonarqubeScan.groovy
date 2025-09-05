def call (body) {

    def settings = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()

    container('sonar-scanner-cli') {
        sh '''
        echo "JOB_NAME= ${JOB_NAME}"
        echo "SONAR_HOST_URL= ${SONAR_HOST_URL}"

         sonar-scanner -X \
         -Dsonar.host.url=${SONAR_HOST_URL} \
         -Dsonar.token=${SONAR_TOKEN} \
         -Dsonar.projectKey=${JOB_NAME%/*}-$GIT_BRANCH \
         -Dsonar.sources=. \
         -Dsonar.exclusions=venv/**,tests/**,.pytest_cache/**
         -Dqualitygate.wait=true
        '''
    }

}
