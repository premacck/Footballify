echo 'Starting Zone Android Application Pipeline'

node('docker') {

    git url: 'ssh://git@git.plank.life:5443/juna/zone-androidapp.git'

    stage 'Checkout and Setup'
    checkout scm

    def branchName = env.BRANCH_NAME
            if (branchName.startsWith('feature/')) {
                buildFeatureBranch()
            } else if (branchName.startsWith('develop')) {
                buildDevelopmentBranch()
            } else if (branchName.startsWith('release/')) {
                buildReleaseBranch()
            }else if (branchName.startsWith('bugfix/')) {
                buildBugfixBranch()
            } else if (branchName.startsWith('master')) {
                buildMasterBranch()
            } else if (branchName.startsWith('hotfix/')) {
                buildHotfixBranch()
            } else {
                error "Don't know what to do with this branch..."
            }
        echo  '********************************************************************************'
}

    def buildFeatureBranch(){
        build()
        executeTests()
        uploadToNexus()
    }

    def buildBugfixBranch(){
        build()
        executeTests()
        uploadToNexus()
    }

    def buildReleaseBranch(){
        buildRelease()
        executeTests()
        uploadToNexus()
        publishApkToAlphaTrackPlayStore()
    }

    def buildHotfixBranch(){
    //todo: must not perform the same tasks as others, change hotfix tasks
        buildRelease()
        executeTests()
        uploadToNexus()
        publishApkToAlphaTrackPlayStore()
    }

    def buildDevelopmentBranch(){
        //todo: must not perform the same tasks as others, change Development tasks
        build()
        executeTests()
        uploadToNexus()
    }

    def buildMasterBranch(){
        //todo: must not perform the same tasks as others, change Master branch tasks
        build()
        executeTests()
        uploadToNexus()
    }

    // Utility Methods invoked by the branch builds
    def build(){
         stage 'Clean and Build android app'
         sh 'chmod +x ./gradlew' // DO NOT REMOVE this line, needed for ./gradlew tasks to work.
         sh "./gradlew clean build"
         echo  '********************************************************************************'
    }

    def buildRelease(){
         stage 'Clean and build release apk'
         sh 'chmod +x ./gradlew' // DO NOT REMOVE this line, needed for ./gradlew tasks to work.
         sh "./gradlew clean :app:assembleRelease"
         echo  '********************************************************************************'
    }

    def executeTests(){
        stage 'Run all android app test cases'
        sh "./gradlew test"
        echo  '********************************************************************************'
    }


    def uploadToNexus(){
        stage 'Upload to Nexus Repository'
        echo  'Uploading APK to Nexus Repository'
        sh "./gradlew upload"
        echo  '********************************************************************************'
    }


    def publishApkToAlphaTrackPlayStore(){
        stage 'Upload Signed APK to Playstore'
        sh "./gradlew -Ptrack=alpha publishApkRelease"
        echo  '********************************************************************************'
    }

    def publishApkToBetaTrackPlayStore(){
        stage 'Upload Signed APK to Playstore'
        sh "./gradlew -Ptrack=beta publishApkRelease"
        echo  '********************************************************************************'
    }

    def publishApkToProductionTrackPlayStore(){
        stage 'Upload Signed APK to Playstore'
        sh "./gradlew -Ptrack=production publishApkRelease"
        echo  '********************************************************************************'
    }

    def runSonarQubeAnalysis(){
        stage 'Run SonarQube analysis on juna-zone-android-app'
        sh 'chmod +x ./gradlew' // DO NOT REMOVE this line, needed for ./gradlew tasks to work.
        sh "./gradlew sonarqube"
        echo  '********************************************************************************'
    }