/*
@param artifactoryRegistry example 'gp-ociso-image'
@param pushImageName The image name we will Push to our repository
@param pushImageTag The image tag we will apply to the image we Push to our repository
@param dockerFile The Docker file we will use to build the image we will Push to our repository
*/
def call(
    String artifactoryRegistry,
    String pushImageName,
    String pushImageTag,
    String dockerFile
) {
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
        docker.build("${artifactoryRegistry}/${pushImageName}:${pushImageTag}", "-f ${dockerFile} --no-cache --pull ./")
    }
}