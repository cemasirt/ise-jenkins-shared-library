/*
@param artifactoryURL example 'https://example.com/artifactory/1'
@param artifactoryRegistry example 'gp-ociso-image'
@param artifactoryRegistryCredID example 'fcs_ise_artifactory'
@param pushImageName The image name we will Push to our repository
@param pushImageTag The image tag we will apply to the image we Push to our repository
@param twistlockURL GSA twistedlockURL, example: https://twistlock.dummy.example.gsa.gov:12345/
@param twistlockCredID Jenkins credential ID, will be used for twistlock API call
@param anchoreURL GSA anchoreURL, example: http://111.111.111.111:12345/
@param anchoreCredID anchoreCredID credential ID, will be used for anchore API call
*/
def call(
    String artifactoryURL,
    String artifactoryRegistry,
    String pushImageName,
    String pushImageTag,
    String dockerFile,
    String twistlockURL,
    String twistlockCredID,
    String anchoreURL,
    String anchoreCredID
) {
    pipeline {
        //This is where we request a build agent from Jenkins.
        agent {
            //Select an agent node with the labels Centos and small.
            node {
                label 'ubuntu && docker'
            }
        }
        stages {
        // In this stage we will build the Docker image using the Dockerfile defined in $dockerFile.
            stage('Build Image') {
                // Run a step conditionally. You can restrict by branches and tags.
                // Leaving the tag key empty will build on any tag. Optionally, you can use BLOB or REGEXP for tag names.
                steps { 
                    /*
                    Build the Docker image using the Docker file name and tag as defined in the associated variables.
                    
                    Note: We force remove any intermediate containers and do not use cache.
                        The reason for not using cache is to force Dockerfile steps that install the latest packages from an
                        external repository to always run even if that step has not changes from the perspective of Docker.
                        ie. If you change a packages minor version upstream in the Pipeline but your install steps for this package
                        do not specify minor version the cached image will see this as an already met requirement and not reinstall
                        if caching is enabled.
                    */
                    script {
                        echo "--- Building docker image ---"
                        dockerImage = docker.build("${artifactoryRegistry}/${pushImageName}:${pushImageTag}", "-f ${dockerFile} --no-cache --pull ./")
                    }            
                }
            }
            // In this stage we will want to run addition tests. If your Pipeline makes it to this stage.
            // the Build and Docker Build stages can safely be assumed to have completed successfully.    
            stage('Twist Scan') {
                steps {
                    script {
                        sh """
                            echo the artifactoryRegistry is ${artifactoryRegistry}
                            echo the pushImageName is ${pushImageName}
                            echo the pushImageTag is ${pushImageTag}
                            echo the twistlockURL is ${twistlockURL}
                            echo the twistlockCredID = ${twistlockCredID}
                        """
                        //Twist Lock Scan
                        withCredentials([usernamePassword(credentialsId:"${twistlockCredID}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                            echo "--- Twistlock Scan ---"
                            echo "getting twistlock CLI"
                            sh "curl -L -k -u ${USER}:${PASS} ${twistlockURL}/api/v1/util/twistcli > twistcli; chmod a+x twistcli"
                            sh "chmod +x ./twistcli"
                            sh "sleep 1"
                            sh "./twistcli -v"
                            sh """./twistcli images scan --ci --details --address ${twistlockURL} -u ${USER} -p '${PASS}' ${dockerImage} | sed -e 's/\\=\\=\\=\\=\\=DATA\\[.*//g' """
                            echo "Removing twistcli..."
                            sh "rm -f ./twistcli"
                        }
                    }
                }
            }
            //In this stage we push the Container we built in stage 'Build Container' to the Docker repo.
            stage('Push Image'){
                when {
                    anyOf {
                        branch 'master'
                        tag ''
                    }
                }
                steps {
                    script {
                        echo "--- Pushing docker image ---"
                        docker.withRegistry(
                            "${artifactoryURL}${artifactoryRegistry}", 
                            "${artifactoryRegistryCredID}"
                        ) {
                            dockerImage.push()
                        }     
                    }
                }
            }
            //In this stage we will want to run addition tests. If your Pipeline makes it to this stage
            //the Build and Docker Build stages can safely be assumed to have completed successfully.
            stage('Anchore Scan') {
                when {
                    anyOf {
                        branch 'master'
                        tag ''
                    }
                }
                steps {
                    script {
                        withCredentials([string(credentialsId: "${anchoreCredID}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                            echo "--- Anchore Scan ---"
                            echo "No Anchore Scan implemented yet"  
                        }
                    }
                }
            }
        }
        //After all Pipleline stages are complete
        post {
            cleanup {
                //Cleanup the Docker image if it was created during this run.
                sh "docker image ls ${pushImageName}"
                sh "if [[ `docker images -q ${pushImageName}:${pushImageTag} 2> /dev/null` != '' ]];then docker image rm ${pushImageName}:${pushImageTag};fi"
                sh "docker image ls ${pushImageName}"
            }
        }
    }
}
