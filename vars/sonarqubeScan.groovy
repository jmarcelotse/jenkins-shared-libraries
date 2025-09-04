def call (body) {

    def settings = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()

    container('sonar-scanner-cli') {
        sh '''
            PROJECT_NAME=$(echo ${JOB_NAME} | cut -d'/' -f1)
            BRANCH_NAME=$(echo ${GIT_BRANCH} | sed 's|origin/||')
            PROJECT_KEY="${PROJECT_NAME}-${BRANCH_NAME}"
            
            echo "Project Key: ${PROJECT_KEY}"
            
            sonar-scanner -X \
              -Dsonar.token=${SONAR_TOKEN} \
              -Dsonar.projectKey=${PROJECT_KEY}
        '''
    }

}
