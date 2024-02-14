// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
//    id("com.diffplug.ktlint") version "6.19.0" apply false
}

allprojects {
    tasks.register("copyGitHooks", Copy::class) {
        description = "Copies the git hooks from /git-hooks to the .git folder."
        group = "git hooks"
        from("$rootDir/scripts/pre-commit")
        into("$rootDir/.git/hooks/")
    }

    tasks.register("installGitHooks", Exec::class) {
        description = "Installs the pre-commit git hooks from /git-hooks."
        group = "git hooks"
        workingDir = rootDir
        commandLine("chmod", "-R", "+x", ".git/hooks/")
        dependsOn("copyGitHooks")
        doLast {
            logger.info("Git hook installed successfully.")
        }
    }
    afterEvaluate {
        tasks.findByName("preBuild")?.let {
            it.dependsOn("installGitHooks")
        }
    }
}

