plugins {
    id("java")
    id("idea")
    id("com.google.protobuf") version "0.9.5"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.8"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.82.0"
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}

sourceSets {
    named("main") {
        java.srcDir(layout.buildDirectory.dir("generated/source"))
    }
}
dependencies {
    runtimeOnly("io.grpc:grpc-netty-shaded:1.82.0")
    implementation("io.grpc:grpc-protobuf:1.82.0")
    implementation("io.grpc:grpc-stub:1.82.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}