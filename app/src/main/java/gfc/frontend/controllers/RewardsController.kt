package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.dataclasses.Reward
import gfc.frontend.requests.RewardDTO
import gfc.frontend.service.RewardsService
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

object RewardsController {
    lateinit var context: Context
    val url = "https://gamefication-for-children.herokuapp.com/rewards"
    lateinit var rewardsContainer: ArrayList<Reward>
    var timestamp: Date? = null

    fun init(context: Context) {
        this.context = context
        RewardsService.init(context)
        timestamp = null
    }

    fun tabChanged() {
        if(timestamp == null || timestamp?.time!!  < minuteAgo()) {
            refreshRewards()
        }
    }

    fun refreshRewards() {
        val temp :List<Reward>? = RewardsService.getData("${url}/")
        if( temp != null){
            rewardsContainer = ArrayList(temp)
        }
        timestamp = Date()
    }

    fun addReward(rewardDTO: RewardDTO): Long? {
        return RewardsService.add("${url}/add",rewardDTO)
    }

    fun deleteReward(rewardId: Any): Any? {
        return RewardsService.delete("${url}/delete/$rewardId")
    }

    fun editReward(reward: Reward): Long? {
        return RewardsService.edit("${url}/edit", reward)
    }

    fun select(rewardId: Long): Long? {
        val ret = RewardsService.select("$url/select/$rewardId")
        refreshRewards()
        return ret
    }

    fun unselect(rewardId: Long): Long? {
        val ret = RewardsService.select("$url/unselect/$rewardId")
        refreshRewards()
        return ret
    }

    fun accept(rewardId: Long): Long? {
        val ret = RewardsService.select("$url/accept/$rewardId")
        refreshRewards()
        return ret
    }

}
