plugins {
    `java-library`
    `maven-publish`
    pmd
    checkstyle
    id("fr.brouillard.oss.gradle.jgitver") version "0.10.0-rc03"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val appName = "atlas"
val codeArtifactUrl: String by project
group = "com.leegality"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri(codeArtifactUrl)
        credentials.username = "aws"
        credentials.password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
    }
}

gradle.startParameter.isContinueOnFailure = true

tasks.jar {
    enabled = true
}

// Define configuration for AspectJ agent
val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

dependencies {
    configurations.all {
        resolutionStrategy.force(libs.commons.codec.get())
    }

    api(platform(libs.test.allure.bom))
    // Platform/BOM dependencies
    api(platform(libs.aws.sdk))

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // API dependencies - these will be available to projects that depend on this library
    api(libs.apache.commons)
    api(libs.jackson.databind)
    api(libs.rest.assured)
    api(libs.jakarta.rs.api)
    api(libs.allure.testng)
    api(libs.allure.rest.assured)
    api(libs.testng)
    api(libs.secret.manager)
    api(libs.jackson.jsr310)

    // Selenium dependencies
    api(libs.selenium.java)
    api(libs.webdrivermanager)

    // Agent configuration
    agent(libs.aspectj.weaver)
}

pmd {
    toolVersion = "7.10.0"
    isConsoleOutput = true
    rulesMinimumPriority.set(3)
    ruleSetFiles = files("${project.rootDir}/config/pmd/pmd.xml")
    ruleSets = listOf()
}

checkstyle {
    configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")
}

tasks.test {
    configureAgent()
    configureDetailedTestLogging()
    useTestNG()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

private val TEST_EXCEPTION_FORMAT = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

fun Test.configureDetailedTestLogging() {
    testLogging {
        events("failed")
        showExceptions = true
        exceptionFormat = TEST_EXCEPTION_FORMAT
        showCauses = true
        showStackTraces = true
        showStandardStreams = false
    }
}

fun Test.configureAgent() {
    // Configure javaagent for test execution
    jvmArgs = listOf(
        "-javaagent:${agent.singleFile}"
    )
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveBaseName.set(appName)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
val javadocJar by tasks.registering(Jar::class, fun Jar.() {
    archiveBaseName.set(appName)
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Javadoc JAR"
    archiveClassifier.set("javadoc")
    from(sourceSets.main.get().allJava)
})

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            //project.shadow.component(this)
            artifactId = appName
            artifact(sourcesJar)
            artifact(javadocJar)

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("atlas")
                packaging = "jar"
                description.set("ATLAS - Automated Testing Library for API and Selenium")
            }
        }
    }
    repositories {
        maven {
            url = uri(codeArtifactUrl)
            credentials.username = "aws"
            credentials.password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
        }
    }
}