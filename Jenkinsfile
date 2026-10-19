// Jenkins pipeline for the UI test suite ("pipeline as code": the CI setup lives in the repo, under version control).
pipeline {
    // Run on any available Jenkins machine (agent)
    agent any

    // Jenkins on a laptop can't receive GitHub webhooks, so it checks GitHub for new commits every ~5 minutes.
    // On a company Jenkins with a public URL, a GitHub webhook would trigger the build on every push instead.
    triggers {
        pollSCM('H/5 * * * *')
    }

    options {
        timeout(time: 30, unit: 'MINUTES')   // never let a stuck browser hang the build forever
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {
        stage('Run UI tests') {
            steps {
                script {
                    // headless: CI has no screen. test.failure.ignore: failed tests make the build UNSTABLE (yellow)
                    // via the junit step below, instead of a hard FAILURE (red) that hides the test report.
                    def mvnCommand = 'mvn -B clean test -Dheadless=true -Dmaven.test.failure.ignore=true'
                    if (isUnix()) {
                        sh mvnCommand
                    } else {
                        bat mvnCommand
                    }
                }
            }
        }
    }

    post {
        always {
            // Test results: pass/fail per test plus a trend chart across builds
            // (TEST-*.xml only: testng-results.xml in the same folder is TestNG's own format, not JUnit XML)
            junit testResults: 'target/surefire-reports/TEST-*.xml', allowEmptyResults: true
            // Failure screenshots from ScreenshotListener, downloadable from the build page
            archiveArtifacts artifacts: 'target/screenshots/*.png', allowEmptyArchive: true
        }
    }
}
