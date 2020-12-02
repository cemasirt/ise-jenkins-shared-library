ISE Jenkins Shared Library
======

.. contents:
    :depth: 1
    :local:


hardenDockerImage
------

This library allows you to harden a Docker Image to meet GSA security policy.

Parameters reference:

- @param ``artifactoryURL`` The URL of your Docker Image artifactory service provider URL, example https://example.com/artifactory/1
- @param ``artifactoryRegistry`` The registry name, in most of case it is your repository name, example 'gp-ociso-image'
- @param ``artifactoryRegistryCredID`` The Jenkins credential ID for artifactory authentication, example 'fcs_ise_artifactory'
- @param ``pushImageName`` The image name we will Push to our repository
- @param ``pushImageTag`` The image tag we will apply to the image we Push to our repository
- @param ``twistlockURL`` The GSA twistedlockURL, example: https://twistlock.dummy.example.gsa.gov:12345/
- @param ``twistlockCredID`` The Jenkins credential ID for twistlock API call
- @param ``anchoreURL`` The GSA anchoreURL, example: http://111.111.111.111:12345/
- @param ``anchoreCredID`` The Jenkins credential ID for anchore API call

Source Code: `hardenDockerImage.groovy <./vars/hardenDockerImage.groovy>`_
