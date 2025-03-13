package com.github.nullsafe.watchwise.core.di.module

//TODO Should be implemented?
/*
@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
@InstallIn(SingletonComponent::class)
interface WorkManagerModule {

    @Binds
    @IntoMap
    @WorkerKey(UpdateMoviesWorker::class)
    fun bindUpdateMoviesWorker(factory: UpdateMoviesWorker.Factory): WorkerFactory

    fun workerFactory(): AppWorkerFactory
}*/
