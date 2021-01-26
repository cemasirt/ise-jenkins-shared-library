/*
This pipeline script is for hardening an AWS AMI for EKS. It simply run packer build in CI environment. The packer build logic primarily is a ansible playbook running on remote EC2 localhost.

Assuming that the AMI hardening project having following code structure:

/ansible
/ansible/playbook.yml
/packer
/packer/packer-var-file.json
/packer/build_packer_var_file.py
/packer/packer.json

Example usage: https://github.com/GSA/odp-packer-amazon-linux2-eks
*/
def call () {
    pipeline {
        agent {
            // use a container with aws-cli, ansible, packer, python pre-installed
            // reference: https://www.jenkins.io/doc/book/pipeline/syntax/
            docker {
                image 'sanhe/cicd:aws-ultimate@sha256:7cdb672755b8e743e9f35d2719b0111f9750c92682baddaf2670f5ad065d7164'
                registryUrl 'https://registry.hub.docker.com'
                registryCredentialsId 'rjlupinek-dockerhub'
                args '-e JENKINS_HOME=$JENKINS_HOME -e AWS_DEFAULT_REGION="us-east-1"'
            }
        }
        stages {
            stage('verify dependencies') {
                steps {
                    script {
                        sh "which aws"
                        sh "which packer"
                        sh "which python"
                    }
                }
            }
            stage('run packer build') {
                steps {
                    script {
                        def runPackerShellScript = libraryResource 'hardenAMI/run-packer-build.sh'
                        writeFile(file:'run-packer-build.sh', text:runPackerShellScript)
                        sh "pwd"
                        sh "ls"
                        sh "aws sts get-caller-identity" // verify the build runtime has sufficent previledge in its IAM Role
                        sh "bash ./run-packer-build.sh"
                    }
                }
            }
        }
    }
}
