/*
@param artifactoryURL example 'https://example.com/artifactory/1'
@param artifactoryRegistry example 'gp-ociso-image'
@param artifactoryRegistryCredID example 'fcs_ise_artifactory'
@param pushImageName The image name we will Push to our repository
@param pushImageTag The image tag we will apply to the image we Push to our repository
*/
def call(
    String artifactoryURL,
    String artifactoryRegistry,
    String artifactoryRegistryCredID,
    String pushImageName,
    String pushImageTag
) {
    script {
        echo "--- Pushing docker image ---"
        dockerImage = docker.image("${artifactoryRegistry}/${pushImageName}:${pushImageTag}")
        docker.withRegistry(
            "${artifactoryURL}${artifactoryRegistry}", 
            "${artifactoryRegistryCredID}"
        ) {
            dockerImage.push()
        }                    
    }
}