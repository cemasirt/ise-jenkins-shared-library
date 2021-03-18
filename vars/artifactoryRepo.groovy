/*
This script is for hardening an AWS AMI to download the installer binaries from Artifactory Repository
*/

def download(Map downloadParams) {
    def downloadSpec = """{
        "files": [
                {
                    "pattern": "${downloadParams.repository}/${downloadParams.remotePath}",
                    "target": "${downloadParams.localPath}"
                }
            ]
        }"""

    echo "${downloadSpec}"

    echo "Artifactory Download: ${downloadParams.repository}/${downloadParams.remotePath} -> ${downloadParams.localPath}"

    def server = Artifactory.server("GSA_ARTIFACTORY_SERVER")
    def buildInfo = server.download spec: downloadSpec
    return buildInfo
}