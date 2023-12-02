package com.alexzh.coffeedrinks.api.plugins

import com.alexzh.coffeedrinks.api.di.appModule
import io.ktor.server.application.*
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun Application.configureDependencyInjection() {
    install(AppKoinPlugin) {
        modules(appModule)
    }
}

internal class AppKoinPlugin(internal val koinApplication: KoinApplication) {

    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline, KoinApplication, AppKoinPlugin> {
        override val key = AttributeKey<AppKoinPlugin>("AppKoinPlugin")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: KoinApplication.() -> Unit
        ): AppKoinPlugin {
            val koinApplication = startKoin(appDeclaration = configure)
            return AppKoinPlugin(koinApplication)
        }
    }
}