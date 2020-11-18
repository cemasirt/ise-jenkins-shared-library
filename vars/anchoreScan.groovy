/*
@param artifactoryRegistry example 'gp-ociso-image'
@param pushImageName The image name we will Push to our repository
@param pushImageTag The image tag we will apply to the image we Push to our repository
@param anchoreURL GSA anchoreURL, example: http://111.111.111.111:12345/
@param anchoreCredID anchoreCredID credential ID, will be used for anchore API call
*/
def call(
    String artifactoryRegistry,
    String pushImageName,
    String pushImageTag,
    String anchoreURL,
    String anchoreCredID
) {
    script {
        def image = "${artifactoryRegistry}/${pushImageName}:${pushImageTag}"
        //Anchore Scan
        withCredentials([string(credentialsId: "${anchoreCredID}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            echo "--- Anchore Scan ---"    
        }
    }
}
