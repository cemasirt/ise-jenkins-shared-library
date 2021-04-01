
def pull(Map params) {
    // Create an Artifactory server instance
    def server = Artifactory.server("GSA_ARTIFACTORY_SERVER")

    // Create an Artifactory Docker instance. The instance stores the Artifactory credentials and the Docker daemon host address.
    // If the docker daemon host is not specified, "/var/run/docker.sock" is used as a default value (the host argument should not be specified in this case)
    def rtDocker = Artifactory.docker server: server

    // Pull a docker image from Artifactory.
    echo "Artifactory Download: ${params.dockerRegistryUrl}/${params.imageName}:${params.imageTag} from ${params.sourceRepository}"
    def buildInfo = rtDocker.pull "${params.dockerRegistryUrl}/${params.imageName}:${params.imageTag}", "${params.sourceRepository}"

    return buildInfo
}
