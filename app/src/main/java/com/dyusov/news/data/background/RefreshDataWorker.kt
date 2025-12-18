package com.dyusov.news.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dyusov.news.domain.usecase.articles.UpdateSubscribedArticlesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateSubscribedArticlesUseCase: UpdateSubscribedArticlesUseCase,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParameters) {

    // this method will be running in worker
    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "Start")
        updateSubscribedArticlesUseCase()
        Log.d("RefreshDataWorker", "Finish")
        notificationHelper.showNewArticlesNotification(listOf()) // todo: temp
        return Result.success()
    }
}