package com.josephuszhou

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.util.Properties
import javax.inject.Inject

abstract class AndroidSigningExtension @Inject constructor(objects: ObjectFactory) {
    val localPropertiesFileName: Property<String> =
        objects.property(String::class.java).convention("local.properties")

    val propertiesFileName: Property<String> =
        objects.property(String::class.java).convention("key.properties")

    val localPropertyPrefix: Property<String> =
        objects.property(String::class.java).convention("")

    val releaseConfigName: Property<String> =
        objects.property(String::class.java).convention("release")

    val signDebugWithRelease: Property<Boolean> =
        objects.property(Boolean::class.java).convention(true)

    val fallbackToDebugSigning: Property<Boolean> =
        objects.property(Boolean::class.java).convention(true)
}

private data class SigningCredentials(
    val storeFilePath: String,
    val storePassword: String,
    val keyAlias: String,
    val keyPassword: String,
)

class AndroidSigningPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create(
                "androidSigning",
                AndroidSigningExtension::class.java,
            )

        project.pluginManager.withPlugin("com.android.application") {
            configureSigning(project, extension)
        }
    }

    private fun configureSigning(
        project: Project,
        extension: AndroidSigningExtension,
    ) {
        val android = project.extensions.getByType(ApplicationExtension::class.java)
        val releaseConfigName = extension.releaseConfigName.get()
        val credentials = loadSigningCredentials(project, extension)

        val releaseSigningConfig =
            credentials?.let {
                val storeFile = project.file(it.storeFilePath)
                if (!storeFile.exists()) {
                    throw GradleException(
                        "Android signing storeFile does not exist: ${storeFile.absolutePath}",
                    )
                }

                android.signingConfigs.maybeCreate(releaseConfigName).apply {
                    this.storeFile = storeFile
                    storePassword = it.storePassword
                    keyAlias = it.keyAlias
                    keyPassword = it.keyPassword
                }
            }

        android.buildTypes.named("debug").configure {
            signingConfig =
                if (extension.signDebugWithRelease.get() && releaseSigningConfig != null) {
                    releaseSigningConfig
                } else {
                    android.signingConfigs.getByName("debug")
                }
        }

        android.buildTypes.named("release").configure {
            when {
                releaseSigningConfig != null -> signingConfig = releaseSigningConfig
                extension.fallbackToDebugSigning.get() ->
                    signingConfig = android.signingConfigs.getByName("debug")
            }
        }
    }

    private fun loadSigningCredentials(
        project: Project,
        extension: AndroidSigningExtension,
    ): SigningCredentials? {
        val localPropertiesFile = project.rootProject.file(extension.localPropertiesFileName.get())
        val localProperties = Properties().apply {
            if (localPropertiesFile.isFile) {
                localPropertiesFile.inputStream().use(::load)
            }
        }
        val propertiesFile = project.rootProject.file(extension.propertiesFileName.get())
        val keyProperties = Properties().apply {
            if (propertiesFile.isFile) {
                propertiesFile.inputStream().use(::load)
            }
        }

        val values =
            SIGNING_KEYS.associateWith { key ->
                resolveValue(
                    localProperties = localProperties,
                    keyProperties = keyProperties,
                    localPropertyPrefix = extension.localPropertyPrefix.get(),
                    key = key,
                )
            }

        val presentValues = values.filterValues { !it.isNullOrBlank() }
        if (presentValues.isEmpty()) {
            return null
        }

        val missingKeys = values.filterValues { it.isNullOrBlank() }.keys
        if (missingKeys.isNotEmpty()) {
            throw GradleException(
                buildString {
                    append("Android signing configuration is incomplete for ${project.path}. ")
                    append("Missing keys: ${missingKeys.joinToString()}. ")
                    append(
                        "Supported sources are local.properties entries like " +
                            "\"${exampleLocalPropertyKey(extension.localPropertyPrefix.get(), "storeFile")}\" in " +
                            "${localPropertiesFile.absolutePath}, ",
                    )
                    append("or ${propertiesFile.absolutePath}.")
                },
            )
        }

        return SigningCredentials(
            storeFilePath = values.getValue("storeFile").orEmpty(),
            storePassword = values.getValue("storePassword").orEmpty(),
            keyAlias = values.getValue("keyAlias").orEmpty(),
            keyPassword = values.getValue("keyPassword").orEmpty(),
        )
    }

    private fun resolveValue(
        localProperties: Properties,
        keyProperties: Properties,
        localPropertyPrefix: String,
        key: String,
    ): String? {
        val localPropertyName = exampleLocalPropertyKey(localPropertyPrefix, key)

        return localProperties.getProperty(localPropertyName)?.trim()?.takeIf { it.isNotEmpty() }
            ?: keyProperties.getProperty(key)?.trim()?.takeIf { it.isNotEmpty() }
    }

    private fun exampleLocalPropertyKey(
        localPropertyPrefix: String,
        key: String,
    ): String =
        if (localPropertyPrefix.isBlank()) {
            key
        } else {
            "$localPropertyPrefix.$key"
        }

    private companion object {
        val SIGNING_KEYS = listOf("storeFile", "storePassword", "keyAlias", "keyPassword")
    }
}
