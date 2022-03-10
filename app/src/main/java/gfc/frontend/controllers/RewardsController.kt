package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.dataclasses.Reward
import gfc.frontend.requests.RewardDTO
import gfc.frontend.service.RewardsService
import kotlin.properties.Delegates

object RewardsController {
    lateinit var context: Context
    val url = "https://gamefication-for-children.herokuapp.com/rewards"
    var rewardsContainer by Delegates.observable(ArrayList<Reward>()) { _, _, _ ->
//        notifier.notifyDataSetChanged()
    }

    fun init(context: Context) {
        this.context = context
        RewardsService.init(context)
    }

    fun refreshRewards() {
        val temp :List<Reward>? = RewardsService.getData("${url}/")
        if( temp != null){
            rewardsContainer = ArrayList(temp)
        }
    }

    fun addReward(rewardDTO: RewardDTO): Long? {
        return RewardsService.add("${url}/add",rewardDTO)
    }

    fun deleteReward(rewardId: Any): Any? {
        return RewardsService.delete("${url}/delete/$rewardId")
    }
}
