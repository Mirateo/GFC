package gfc.frontend.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.Reward
import gfc.frontend.requests.RewardDTO
import kotlinx.coroutines.runBlocking

object RewardsService : KtorService(){

    class SimpleRewardsServiceBinder(val servc: RewardsService): Binder() {
        fun getService(): RewardsService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleRewardsServiceBinder(this)
    }

    fun getData(url: String) = runBlocking<List<Reward>?>  {
        ktorRequest <List<Reward>?>("GET", url, null)

        super.response as List<Reward>?
    }

    fun add(url: String, rewardDTO: RewardDTO) = runBlocking<Long?>  {
        ktorRequest <Long?>("POST", url, rewardDTO)
        super.response as Long?
    }

    fun delete(url: String) = runBlocking<Long?>  {
        ktorRequest <Long?>("POST", url, null)
        super.response as Long?
    }

    fun edit(url: String, reward: Reward) = runBlocking<Long?> {
        ktorRequest<Long?>("POST", url, reward)
        response as Long?
    }

    fun select(url: String): Long? = runBlocking<Long?> {
        ktorRequest<Long?>("POST", url, null)
        response as Long?
    }

    fun unselect(url: String): Long? = runBlocking<Long?> {
        ktorRequest<Long?>("POST", url, null)
        response as Long?
    }

    fun accept(url: String): Long? = runBlocking<Long?> {
        ktorRequest<Long?>("POST", url, null)
        response as Long?
    }
}
