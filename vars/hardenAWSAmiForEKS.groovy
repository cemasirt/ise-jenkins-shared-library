def call () {
    pipeline {
        agent {
            // use a container with aws-cli, ansible, packer, python pre-installed
            // reference: https://www.jenkins.io/doc/book/pipeline/syntax/
            docker { 
                image 'sanhe/cicd:aws-ultimate'
                args '-e JENKINS_HOME=$JENKINS_HOME'
            }
        }
        stages {
            stage('verify dependencies') {
                steps {
                    script {
                        sh "which aws"
                        sh "which ansible"
                        sh "which packer"
                        sh "which python"
                    }
                }
            }
            stage('run packer build') {
                steps {
                    script {
                        sh "pwd"
                        sh "aws sts get-caller-identity"
                        sh "echo JENKINS_HOME=${JENKINS_HOME}"
                        sh "bash ./packer/run-packer-build.sh"
                    }
                }
            }
        }
    }
}
