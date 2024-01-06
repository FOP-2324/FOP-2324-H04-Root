dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "H04-Root"
