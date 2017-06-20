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

    // sh "./gradlew sonarqube"

    stage 'Uploading artifact to Nexus'
    // sh "./gradlew upload"

    /*-------------END OF DEV AUTOMATION PROCESS */

    stage 'Ready for QA build?[Manual]'
    try {
        timeout(time: 24, unit: 'HOURS') {
            input 'Do you want to start QA build?'

        }
    } catch (err) {
        echo "******* THIS BUILD RECEIVED NO USER INPUT FOR THE LAST 24 HOURS, ABBORTING. *******"
    }


    stage 'Download from Nexus and Tagging'
    // TODO: Download from Nexus and add git Tagging

    stage 'Uploading new target to Nexus'
    // TODO: Re-upload to nexus with the tagged version

    stage 'Deploying to Play store alpha testing'
    // TODO: Deploy to QA environment(Playstore)
    //send mail on successful upload to alpha play store

    stage 'Verify QA setup checklist[Manual]'
    try {
        timeout(time: 24, unit: 'HOURS') {
            input 'Have you finished your setup checklist and are ready to test?'
            

        }
    } catch (err) {
        echo "******* THIS BUILD RECEIVED NO USER INPUT FOR THE LAST 24 HOURS, ABBORTING. *******"
    }


    /*-------- QA EXECUTES TESTS-------------*/

    stage 'Quality Gate[Manual]'
    try {
        timeout(time: 24, unit: 'HOURS') {
            input 'Have all the tests passed?'

        }
    } catch (err) {
        echo "******* THIS BUILD RECEIVED NO USER INPUT FOR THE LAST 24 HOURS, ABBORTING. *******"
    }

    stage 'Resetting QA Datasets'
    // TODO: Reset Datasets used by QA

    stage 'Ready for Staging?[Manual]'
    try {
        timeout(time: 24, unit: 'HOURS') {
            input 'Do you want to start the staging process?'

        }
    } catch (err) {
        echo "******* THIS BUILD RECEIVED NO USER INPUT FOR THE LAST 24 HOURS, ABBORTING. *******"
    }


    stage 'Deploying to staging environment'
    // TODO: Deploy to staging environment

    stage 'Running performance tests'
    // TODO: Run performance tests

    stage 'Quality Gate[Manual]'
    try {
        timeout(time: 24, unit: 'HOURS') {
            input 'Have all the performance tests passed?'

        }
    } catch (err) {
        echo "******* THIS BUILD RECEIVED NO USER INPUT FOR THE LAST 24 HOURS, ABBORTING. *******"
    }


    stage 'Ready for release?[Manual]'

    try {
        timeout(time: 24, unit: 'HOURS') {
            input 'Do you want to release this build to the appstore?'

        }
    } catch (err) {
        echo "******* THIS BUILD RECEIVED NO USER INPUT FOR THE LAST 24 HOURS, ABBORTING. *******"
    }


    stage 'Deploying to production environment'
    // TODO: Deploy to Google play store

    stage 'Start running Smoke tests'
    try {
        timeout(time: 24, unit: 'HOURS') {
            input 'Do you want to start running smoke tests?'

        }
    } catch (err) {
        echo "******* THIS BUILD RECEIVED NO USER INPUT FOR THE LAST 24 HOURS, ABBORTING. *******"
    }

}