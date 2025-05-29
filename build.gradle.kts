import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Platform dependencies
    implementation(platform(libs.test.allure.bom))
    implementation(platform(libs.aws.sdk))

    // Core dependencies that are essential and should be bundled
    implementation(libs.apache.commons)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.jsr310)
    implementation(libs.secret.manager)
    implementation(libs.jakarta.rs.api)

    // Dependencies that should be provided by the consuming project
    compileOnly(libs.rest.assured)
    compileOnly(libs.allure.testng)
    compileOnly(libs.allure.rest.assured)
    compileOnly(libs.testng)
    compileOnly(libs.selenium.java)
    compileOnly(libs.webdrivermanager)

    // Test dependencies
    testImplementation(libs.rest.assured)
    testImplementation(libs.testng)
    testImplementation(libs.selenium.java)
    testImplementation(libs.webdrivermanager)
    testImplementation(libs.allure.testng)
    testImplementation(libs.allure.rest.assured)

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

tasks.withType<ShadowJar>() {
    archiveBaseName = appName
    archiveClassifier.set("")
    
    minimize {
        // Exclude these packages from minimization to prevent runtime issues
        exclude(dependency("com.fasterxml.jackson.*:.*"))
        exclude(dependency("org.apache.commons:commons-lang3"))
    }
    
    // Merge service files
    mergeServiceFiles()
    
    // Exclude unnecessary files
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    exclude("META-INF/*.MF")
    exclude("META-INF/LICENSE*")
    exclude("META-INF/NOTICE*")
    exclude("META-INF/DEPENDENCIES*")
    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")
    exclude("**/*.txt")
    exclude("**/*.properties")
    exclude("**/*.xml")
    exclude("**/*.html")
    exclude("**/*.md")
    exclude("**/module-info.class")
    exclude("**/package-info.class")
    
    // Relocate common libraries to avoid conflicts
    relocate("com.fasterxml.jackson", "com.leegality.shaded.jackson")
    relocate("org.apache.commons", "com.leegality.shaded.commons")
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.shadow.component(this)
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