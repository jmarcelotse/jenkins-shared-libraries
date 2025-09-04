def call (body) {

    def settings = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()

    container('python') {
        sh '''
            pip install -r requirements.txt
            bandit -r . -x '/.venv/','./tests/' || true
            black .
            pytest -v --disable-warnings
        '''
    }

}
