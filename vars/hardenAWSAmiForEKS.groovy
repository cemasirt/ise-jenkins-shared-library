/*
This pipeline script is for hardening an AWS AMI for EKS. It simply run packer build in CI environment. The packer build logic primarily is a ansible playbook running on remote EC2 localhost.

Example usage: https://github.com/GSA/odp-packer-amazon-linux2-eks
*/
def call () {
    pipeline {
        agent {
            // use a container with aws-cli, ansible, packer, python pre-installed
            // reference: https://www.jenkins.io/doc/book/pipeline/syntax/
            docker { 
                image 'hashicorp/packer'
                // image 'sanhe/cicd:aws-ultimate@sha256:7cdb672755b8e743e9f35d2719b0111f9750c92682baddaf2670f5ad065d7164'
                registryUrl 'https://registry.hub.docker.com'
                registryCredentialsId 'rjlupinek-dockerhub'
                args '-e JENKINS_HOME=$JENKINS_HOME -e AWS_DEFAULT_REGION="us-east-1"'
            }
        }
        stages {
            stage('verify dependencies') {
                steps {
                    script {
                        // sh "which ansible"
                        sh "which packer"
                        sh "which aws"
                        sh "which python"
                        sh "which pip"
                        sh "pip list"
                    }
                }
            }
            // stage('run packer build') {
            //     steps {
            //         script {
            //             sh "pwd"
            //             sh "aws sts get-caller-identity" // verify the build runtime has sufficent previledge in its IAM Role
            //             sh "echo JENKINS_HOME=${JENKINS_HOME}"
            //             sh "bash ./packer/run-packer-build.sh"
            //         }
            //     }
            // }
        }
    }
}
