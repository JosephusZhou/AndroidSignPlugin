import org.gradle.plugin.compatibility.compatibility

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "2.1.1"
}

group = "com.josephuszhou"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withSourcesJar()
    withJavadocJar()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.5.1")
}

gradlePlugin {
    website.set("https://github.com/josephuszhou/AndroidSignPlugin")
    vcsUrl.set("https://github.com/josephuszhou/AndroidSignPlugin.git")

    plugins {
        create("androidSigning") {
            id = "com.josephuszhou.androidsigning"
            implementationClass = "com.josephuszhou.AndroidSigningPlugin"
            displayName = "Android Signing Plugin"
            description = "Configures Android application signing from local.properties or key.properties."
            tags.set(listOf("android", "signing", "keystore", "local-properties"))
            compatibility {
                features {
                    configurationCache = false
                }
            }
        }
    }
}

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            pom {
                name.set("AndroidSignPlugin")
                description.set("Reusable Gradle plugin for configuring Android application signing.")
                url.set("https://github.com/josephuszhou/AndroidSignPlugin")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/josephuszhou/AndroidSignPlugin.git")
                    developerConnection.set("scm:git:https://github.com/josephuszhou/AndroidSignPlugin.git")
                    url.set("https://github.com/josephuszhou/AndroidSignPlugin")
                }
            }
        }
    }
}
