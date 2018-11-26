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
        publishApkToInternalTestTrackPlayStore()
    }

    def buildHotfixBranch(){
    //todo: must not perform the same tasks as others, change hotfix tasks
        buildRelease()
        executeTests()
        uploadToNexus()
        publishApkToInternalTestTrackPlayStore()
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
	
		try {
			stage 'Clean and Build android app'
			sh 'chmod +x ./gradlew' // DO NOT REMOVE this line, needed for ./gradlew tasks to work.
			sh "./gradlew clean :app:assembleDebug"
			  echo  '********************************************************************************'
			//currentBuild.result = 'SUCCESS'
			updateJIRA('BuildPass')

		} catch (Exception err) {

			//currentBuild.result = 'FAILURE'
			updateJIRA('BuildFail')
		}	        
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

    def publishApkToInternalTestTrackPlayStore(){
        stage 'Upload Signed APK to Playstore Internal test track'
        sh "./gradlew -Ptrack=internal publishApkRelease"
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
	
	def updateJIRA(buildStatus) {

		// Get the JIRAticket from the changelog
		
		def publisher = LastChanges.getLastChangesPublisher "LAST_SUCCESSFUL_BUILD", "SIDE", "LINE", true, true, "", "", "", "", ""
		publisher.publishLastChanges()
		def changes = publisher.getLastChanges()
		def jiralist = []
		for (commit in changes.getCommits()) {
			def commitInfo = commit.getCommitInfo()
			def commitMessage = commitInfo.getCommitMessage()
			jiralist.add((commitMessage =~ /[A-Z]*-\d+/)[0])
					 
		}
		def jiratktlist = jiralist.unique()
    
		if (buildStatus == "BuildPass"){	
			stage('Notify JIRA to transition on build Success'){
				withEnv(['JIRA_SITE=JIRA']){			    
					for (i=0;i <jiratktlist.size();i++) {				
						def issue = jiraGetIssue idOrKey: jiratktlist[i]
						def statusName = issue.data.fields.status.name
						def issueType = issue.data.fields.issuetype.name
						
					    if (statusName == "BUILDING"){						
							jiraAddComment idOrKey: jiratktlist[i], comment: "Build Success: BUILD URL is env.BUILD_URL"										
							
							if (issueType == "Task"){
								def transitionInput =
								[
									transition: [
										id: '91'
									]
								]
							}
							else if (issueType == "Bug") {
								def transitionInput =
								[
									transition: [
										id: '61'
									]
								]
							}	
							jiraTransitionIssue idOrKey: jiratktlist[i], input: transitionInput		  
						}
					}
				}
			}	
		}else if (buildStatus == "BuildFail"){
			stage('Notify JIRA to transition on build Fail'){
				withEnv(['JIRA_SITE=JIRA']){
					for (i=0;i <jiratktlist.size();i++) {
						def issue = jiraGetIssue idOrKey: jiratktlist[i]
						def statusName = issue.data.fields.status.statusCategory.name.toString()
						def issueType = issue.data.fields.issuetype.name
						
					    if (statusName == "BUILDING"){	
							if (issueType == "Task"){
								def transitionInput =
								[
									transition: [
										id: '101'
									]
								]
							}
							else if (issueType == "Bug") {
								def transitionInput =
								[
									transition: [
										id: '171'
									]
								]
							}
							jiraTransitionIssue idOrKey: jiratktlist[i], input: transitionInput
						}
					}
				}
			}
		}
	
	}