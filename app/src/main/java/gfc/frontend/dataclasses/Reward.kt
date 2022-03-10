package gfc.frontend.dataclasses

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Reward(@Required var rewardId: Long,

                  @Required var title: String,

                  @Required var description: String? = null,

                  @Required var reporter: UserInfo,

                  @Required var owner: UserInfo,

                  @Required var chosen: Boolean,

                  @Required var points: Long = 0L)
