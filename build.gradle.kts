plugins {
    `java-library`
    `maven-publish`
    pmd
    checkstyle
}

group = "com.leegality"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("${System.getenv("CODEARTIFACT_URL")}")
        credentials.username = "aws"
        credentials.password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
    }
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

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])

            pom {
                name.set("atlas")
                description.set("ATLAS - Automated Testing Library for API and Selenium")
                url.set("https://gitlab.leegality.com/automation-tests/atlas")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                scm {
                    connection.set("scm:git:git://gitlab.leegality.com/automation-tests/atlas.git")
                    developerConnection.set("scm:git:ssh://gitlab.leegality.com/automation-tests/atlas.git")
                    url.set("https://gitlab.leegality.com/automation-tests/atlas")
                }

                developers {
                    developer {
                        id.set("leegality")
                        name.set("Leegality")
                        email.set("QOPS@leegality.com")
                        organization.set("Leegality")
                        organizationUrl.set("https://www.leegality.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            url = uri("${System.getenv("CODEARTIFACT_URL")}")
            credentials.username = "aws"
            credentials.password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
        }
    }
}