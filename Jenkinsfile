pipeline {
    agent any
    environment {
        // This can be nexus3 or nexus2
          NEXUS_VERSION = "nexus3"
          // This can be http or https
          NEXUS_PROTOCOL = "http"
          // Where your Nexus is running. In my case:
          NEXUS_URL = "192.168.1.50:8081"
          // Repository where we will upload the artifact
          NEXUS_REPOSITORY = "maven-snapshots"
          // Jenkins credential id to authenticate to Nexus OSS
          NEXUS_CREDENTIAL_ID = "nexus-credentials"
          /*
            Windows: set the ip address of docker host. In my case 192.168.1.50.
            to obtains this address : $ docker-machine ip
            Linux: set localhost to SONARQUBE_URL
          */
          SONARQUBE_URL = "http://192.168.1.50"
          SONARQUBE_PORT = "9000"
    }

    options {
        skipDefaultCheckout()
    }

    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            parallel {
                stage('Compile') {
                    steps {
                        sh './gradlew compileJava'
                    }
                }

                stage('CheckStyle') {
                    steps {
                        //sh 'mvn checkstyle:checkstyle'
                        sh './gradlew checkstyleMain'
                        step([$class: 'CheckStylePublisher',
                        //canRunOnFailed: true,
                        defaultEncoding: '',
                        healthy: '100',
                        pattern: '**/target/checkstyle-result.xml',
                        unHealthy: '90',
                        //useStableBuildAsReference: true
                        ])
                    }
                }
            }
        }
        stage('Unit Tests') {
            when {
                anyOf { branch 'master'; branch 'develop' }
            }
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/test/TEST-*.xml'
                }
            }
        }
        stage('Integration Tests') {
            when {
                anyOf { branch 'master'; branch 'develop' }
            }
            steps {
                sh './gradlew integrationTest build'
            }
            post {
                always {
                    junit '**/build/test-results/test/*IT.xml'
                }
            success {
                stash(name: 'artifact', includes: '**/build/libs/*.jar')
                stash(name: 'pom', includes: 'pom.xml')
                // to add artifacts in jenkins pipeline tab (UI)
                archiveArtifacts '**/build/libs/*.jar'
            }
        }
    }


    stage('Code Quality Analysis') {
        parallel {
            stage('PMD') {
                agent {
                    docker {
                        image 'maven:3.6.0-jdk-8-alpine'
                        //args '-v /root/.m2/repository:/root/.m2/repository'
                        args '-v /volume1/@docker/volumes/jenkins/_data/.m2/repository:/root/.m2/repository'
                        reuseNode true
                    }
                }
                steps {
                    sh ' mvn pmd:pmd'
                    // using pmd plugin
                    step([$class: 'PmdPublisher', pattern: '**/target/pmd.xml'])
                }
            }
            stage('Findbugs') {
                agent {
                    docker {
                        image 'maven:3.6.0-jdk-8-alpine'
                        //args '-v /root/.m2/repository:/root/.m2/repository'
                        args '-v /volume1/@docker/volumes/jenkins/_data/.m2/repository:/root/.m2/repository'
                        reuseNode true
                    }
                }
                steps {
                    sh ' mvn findbugs:findbugs'
                    // using findbugs plugin
                    findbugs pattern: '**/target/findbugsXml.xml'
                }
            }
            stage('JavaDoc') {
                agent {
                    docker {
                        image 'maven:3.6.0-jdk-8-alpine'
                        //args '-v /root/.m2/repository:/root/.m2/repository'
                        args '-v /volume1/@docker/volumes/jenkins/_data/.m2/repository:/root/.m2/repository'
                        reuseNode true
                    }
                }
                steps {
                    sh ' mvn javadoc:javadoc'
                    step([$class: 'JavadocArchiver', javadocDir: './target/site/apidocs', keepAll: 'true'])
                }
            }
            stage('SonarQube') {
                environment {
                    scannerHome = tool 'SonarQube Scanner'
                }
                agent {
                    docker {
                        image 'maven:3.6.0-jdk-8-alpine'
                        //args "-v /root/.m2/repository:/root/.m2/repository"
                        args '-v /volume1/@docker/volumes/jenkins/_data/.m2/repository:/root/.m2/repository' // Only for Docker on Synolgy
                        reuseNode true
                    }
                }
                steps {
                    //sh " mvn sonar:sonar -Dsonar.host.url=$SONARQUBE_URL:$SONARQUBE_PORT"
                    withSonarQubeEnv('sonarqube') {
                         sh "./gradlew jacocoTestReport"
                        //sh 'mvn clean verify -Psonar-test sonar:sonar'
                        //sh 'mvn clean verify sonar:sonar'
                    }
                    timeout(time: 15, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }
        post {
            always {
                // Using warning next gen plugin
                recordIssues aggregatingResults: true, tools: [javaDoc(), checkStyle(pattern: '**/target/checkstyle-result.xml'), findBugs(pattern: '**/target/findbugsXml.xml', useRankAsPriority: true), pmdParser(pattern: '**/target/pmd.xml')]
            }
        }
    }

    stage('Deploy Artifact To Nexus') {
        when {
            anyOf { branch 'master'; branch 'develop' }
        }
        steps {
            script {
                unstash 'pom'
                unstash 'artifact'
                // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
                pom = readMavenPom file: "pom.xml";
                // Find built artifact under target folder
                filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                // Print some info from the artifact found
                echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                // Extract the path from the File found
                artifactPath = filesByGlob[0].path;
                // Assign to a boolean response verifying If the artifact name exists
                artifactExists = fileExists artifactPath;
                if (artifactExists) {
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: pom.groupId,
                        version: pom.version,
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            // Artifact generated such as .jar, .ear and .war files.
                            [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging
                            ],
                            // Lets upload the pom.xml file for additional information for Transitive dependencies
                            [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                        ])
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
            }
        }
    }
}
