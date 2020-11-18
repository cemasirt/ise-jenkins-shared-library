/*
@param artifactoryRegistry example 'gp-ociso-image'
@param pushImageName The image name we will Push to our repository
@param pushImageTag The image tag we will apply to the image we Push to our repository
@param twistlockURL GSA twistedlockURL, example: https://twistlock.dummy.example.gsa.gov:12345/
@param twistlockCredID Jenkins credential ID, will be used for twistlock API call
*/
def call(
    String artifactoryRegistry,
    String pushImageName,
    String pushImageTag,
    String twistlockURL,
    String twistlockCredID
) {
    script {
        def image = "${artifactoryRegistry}/${pushImageName}:${pushImageTag}"
        //Twist Lock Scan
        withCredentials([usernamePassword(credentialsId:"${twistlockCredID}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            echo "--- Twistlock Scan ---"
            echo "getting twistlock CLI"
            sh "curl -L -k -u ${USER}:${PASS} ${twistlockURL}/api/v1/util/twistcli > twistcli; chmod a+x twistcli"
            sh "chmod +x ./twistcli"
            sh "sleep 1"
            sh "./twistcli -v"
            sh """./twistcli images scan --ci --details --address ${twistlockURL} -u ${USER} -p '${PASS}' ${image} | sed -e 's/\\=\\=\\=\\=\\=DATA\\[.*//g' """
            echo "Removing twistcli..."
            sh "rm -f ./twistcli"
        }
    }
}