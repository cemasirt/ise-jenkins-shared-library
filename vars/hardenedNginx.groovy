def call(
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
                    script {
                        sh """
                            echo Build Image
                        """
                    }                
                }
            }
        //     // In this stage we will want to run addition tests. If your Pipeline makes it to this stage.
        //     // the Build and Docker Build stages can safely be assumed to have completed successfully.    
        //     stage('Twist Scan') {
        //         steps {
        //             script {
        //                 sh """
        //                     echo the artifactoryRegistry is ${params.artifactoryRegistry}
        //                     echo the pushImageName is ${params.pushImageName}
        //                     echo the pushImageTag is ${params.pushImageTag}
        //                     echo the twistlockURL is ${params.twistlockURL}
        //                     echo the twistlockCredID = ${params.twistlockCredID}
        //                 """
        //             }
        //             twistScan "${params.artifactoryRegistry}", "${params.pushImageName}", "${params.pushImageTag}", "${params.twistlockURL}", "${params.twistlockCredID}"
        //         }
        //     }
        //     //In this stage we push the Container we built in stage 'Build Container' to the Docker repo.
        //     stage('Push Image'){
        //         when {
        //             anyOf {
        //                 branch 'master'
        //                 tag ''
        //             }
        //         }
        //         steps {
        //             pushDockerImage "${params.artifactoryURL}", "${params.artifactoryRegistry}", "${params.artifactoryRegistryCredID}", "${params.pushImageName}", "${params.pushImageTag}"
        //         }
        //     }
        //     //In this stage we will want to run addition tests. If your Pipeline makes it to this stage
        //     //the Build and Docker Build stages can safely be assumed to have completed successfully.
        //     stage('Anchore Scan') {
        //         when {
        //             anyOf {
        //                 branch 'master'
        //                 tag ''
        //             }
        //         }
        //         steps {
        //             twistScan "${params.artifactoryRegistry}", "${params.pushImageName}", "${params.pushImageTag}", "${params.anchoreURL}", "${params.anchoreCredID}"
        //         }
        //     }
        }
        // //After all Pipleline stages are complete
        // post {
        //     //We don't have a slack channel :)
        //     //Post failure messages to Slack channel defined in $slackChannel
        //     //failure {
        //     //    slackSend (channel: "${slackChannel}", color: '#DC143C', message: "Failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        //     // }
        //     //success {
        //         //Uncomment to enable sending messages to Slack
        //         //slackSend (channel: "${slackChannel}", color: '#008000', message: "Success: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        //     //}
        //     cleanup {
        //         //Cleanup the Docker image if it was created during this run.
        //         sh "docker image ls ${params.pushImageName}"
        //         sh "if [[ `docker images -q ${params.pushImageName}:${params.pushImageTag} 2> /dev/null` != '' ]];then docker image rm ${params.pushImageName}:${params.pushImageTag};fi"
        //         sh "docker image ls ${params.pushImageName}"
        //     }
        // }
    }
}
