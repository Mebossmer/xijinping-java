plugins {
    application
    java
}

group = "org.xijinping.bot"
version = 1.2

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.21")
    implementation("com.google.code.gson:gson:2.13.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.xijinping.bot.Bot"
}
