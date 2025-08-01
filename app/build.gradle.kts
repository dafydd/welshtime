group = "net.dafydd"
version = findProperty("publishVersion")?.toString() ?: "unspecified"

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    `maven-publish`
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
//    val localRepo = System.getenv("LOCAL_MAVEN_REPO")
//        ?: throw GradleException("Environment variable LOCAL_MAVEN_REPO not set")
//    maven {
//        url = uri("file://$localRepo")
//    }
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.mockito)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "net.dafydd.bootstrap.App"
}
val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
    testImplementation(libs.mockito)
    mockitoAgent(libs.mockito) { isTransitive = false }
}
tasks {
    test {
        jvmArgs("-javaagent:${mockitoAgent.asPath}")
    }
}
tasks.named<Test>("test") {
    useJUnitPlatform()
}

//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            artifactId = "welshtime"
//            from(components["java"])
//        }
//    }
//    repositories {
//        maven {
//            val localRepo = System.getenv("LOCAL_MAVEN_REPO")
//                ?: throw GradleException("Set LOCAL_MAVEN_REPO environment variable")
//            url = uri("file://$localRepo")
//        }
//    }
//}