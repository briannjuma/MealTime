pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "MealTime"
include(":app")
include(":feature:addmeal")
include(":core")
include(":feature:favorites")
include(":feature:home")
include(":feature:settings")
include(":feature:search")
include(":compose-ui")
include(":core-database")
include(":core-network")
include(":feature:mealplanner")
