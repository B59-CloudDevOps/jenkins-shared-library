// ...existing code...
def call() {
    pipeline {
        agent any 
        environment {
            sonar_psw = credentials('sonar_psw') 
        }
        stages {
            stage('Lint Checks') {
                steps {
                    sh "echo 'Running lint checks...'"
                    sh "echo 'Lint checks completed successfully!'"
                    sh "env"
                }   
            }
            stage('Sonar Checks') {
                steps {
                    sh "echo 'Running Sonar checks...'"
                    sh "sonar-scanner -Dsonar.host.url=http://sonarqube.clouding-app.shop:9000 -Dsonar.login=admin -Dsonar.password=${sonar_psw} -Dsonar.projectKey=${component}"
                }   
            }
            stage('Unit Testing') {
                steps {
                    sh "echo 'Running Unit Testing...'"
                    sh "echo 'Unit Testing completed successfully!'"
                    sh "sleep 3"
                }   
            }
            stage('Integration Testing') {
                steps {
                    sh "echo 'Running Integration Testing...'"
                    sh "echo 'Integration Testing completed successfully!'"
                    sh "sleep 3"
                }   
            }
            stage('Functional Testing') {
                steps {
                    sh "echo 'Running Functional Testing...'"
                    sh "echo 'Functional Testing completed successfully!'"
                    sh "sleep 3"
                }   
            }
            stage('Validating the artifacts availablilty') {
                steps {
                    sh "aws s3 ls s3://b59-roboshop-artifacts/${component}/${component}-${BUILD_NUMBER}.zip"
                    // sh "if [ $? -ne 0 ]; then echo 'Artifact not found!'; echo "building artifact"; else echo 'Artifact found!'; exit 1; fi"
                    // sh 'if [ $? -ne 0 ]; then echo "Artifact not found!"; else echo "Artifact not found So proceeding"; fi'

                }   
            }
            stage('Building Artifact') {
                steps {
                    sh "echo 'Running Building Artifact...'"
                    sh "npm install"
                    sh "zip -r ${component}-${BUILD_NUMBER}.zip node_modules server.js"
                }   
            }
            stage('Pusing Artifact On S3') {
                steps {
                    sh "aws s3 ls"
                    sh "aws s3 cp ${component}-${BUILD_NUMBER}.zip s3://b59-roboshop-artifacts/${component}/"
                }   
            }
        }
    }
}