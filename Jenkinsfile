node('master') {
    git url: 'ssh://git@git.plank.life:5443/juna/zone-androidapp.git'

    stage 'Checkout and Setup'
    checkout scm

    stage 'Build'
    sh 'chmod +x ./gradlew' // DO NOT REMOVE this line, needed for ./gradlew tasks to work.
    sh "./gradlew clean build"
    echo '********************************************************************************'

    stage 'Running UI/Unit cases'

    stage 'Code Quality Analysis'

    stage 'Running Integration Test cases'

    stage 'Uploading artifact to Nexus'

    /*-------------END OF DEV AUTOMATION PROCESS */

    stage 'Ready for QA build?[Manual]'
    input 'Do you want to start QA build?'

    stage 'Download from Nexus and Tagging'
    // TODO: Download from Nexus and add git Tagging

    stage 'Uploading new target to Nexus'
    // TODO: Re-upload to nexus with the tagged version

    stage 'Deploying to Play store alpha testing'
    // TODO: Deploy to QA environment(Playstore)
    //send mail on successful upload to alpha play store

    stage 'Verify QA setup checklist[Manual]'
    input 'Have you finished your setup checklist and are ready to test?'

    /*-------- QA EXECUTES TESTS-------------*/

    stage 'Quality Gate[Manual]'
    input 'Have all the tests passed?'

    stage 'Resetting QA Datasets'
    // TODO: Reset Datasets used by QA

    stage 'Ready for Staging?[Manual]'
    input 'Do you want to start the staging process?'

    stage 'Deploying to staging environment'
    // TODO: Deploy to staging environment

    stage 'Running performance tests'
    // TODO: Run performance tests

    stage 'Quality Gate[Manual]'
    input 'Have all the performance tests passed?'

    stage 'Ready for release?[Manual]'
    input 'Do you want to release this build to the appstore?'

    stage 'Deploying to production environment'
    // TODO: Deploy to Google play store

    stage 'Start running Smoke tests'
    input 'Do you want to start running smoke tests?'
}