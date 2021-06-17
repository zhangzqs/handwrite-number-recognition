plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")


    //implementation("org.openpnp:opencv:3.4.2-2")
    // https://mvnrepository.com/artifact/org.processing/core
    implementation("org.processing:core:3.3.7")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

