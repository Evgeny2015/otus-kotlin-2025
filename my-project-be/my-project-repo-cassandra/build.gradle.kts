plugins {
    id("build-jvm")
    id("kotlin-kapt")
    //alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(projects.myProjectCommon)
    implementation(projects.myProjectRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.jdk9)
    implementation(libs.uuid)
    implementation(libs.bundles.cassandra)
    kapt(libs.db.cassandra.kapt)

    testImplementation(projects.myProjectRepoTests)
    testImplementation(libs.testcontainers.cassandra)
    testImplementation(libs.logback)
}
