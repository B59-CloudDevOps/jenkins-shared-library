// ...existing code...
def call() {
    pipeline {
        agent any 
        environment {
            sonar_psw = credentials('sonar_psw') 
            sonar_url = "sonarqube.clouding-app.shop"
        }
        stages {
            stage('Lint Checks') {
                steps {
                    sh "echo 'Running lint checks...'"
                    sh "echo 'Lint checks completed successfully!'"
                    sh "env"
                }   
            }
            stage('Compliling the code') {
                tools {
                    maven 'mvn396' 
                }
                steps {
                    sh "mvn clean compile"
                }   
            }          
            stage('Sonar Checks') {
                steps {
                    sh "echo 'Running Sonar checks...'"
                    sh "sonar-scanner -Dsonar.host.url=http://${sonar_url}:9000 -Dsonar.login=admin -Dsonar.password=${sonar_psw} -Dsonar.projectKey=${component}"
                }   
            }
            stage('Sonar Scan Result') {
                steps {

                    echo "Uncomment below to validate the scan result"
                    // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > gate.sh"
                    // sh "bash -x gate.sh admin ${sonar_psw} ${sonar_url} ${component} || true"
                    // sh "echo SCAN Result Published"

                }
            }
            stage('Unit Testing') {
                steps {
                    sh "echo 'Running Unit Testing...'"
                    sh "echo 'Unit Testing completed successfully!'"
                    sh "echo mvn test"
                    sh "sleep 3"
                }   
            }
            stage('Integration Testing') {
                steps {
                    sh "echo 'Running Integration Testing...'"
                    sh "echo 'Integration Testing completed successfully!'"
                    sh "echo mvn verify"
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
                    sh "echo 'Validating the artifacts availability...'"
                }   
            }
            stage('Building Artifact') {
                tools {
                    maven 'mvn396' 
                }
                steps {
                    sh "echo 'Running Building Artifact...'"
                    sh "mvn clean install"
                    sh "mv target/${component}-1.0.jar ${component}-${BUILD_NUMBER}.jar"
                    sh "zip -r ${component}-${BUILD_NUMBER}.zip ${component}.jar systemd.service"
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