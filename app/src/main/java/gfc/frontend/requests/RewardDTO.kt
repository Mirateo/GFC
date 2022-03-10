package gfc.frontend.requests

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class RewardDTO(@Required var title: String,
                     @Required var description: String?,
                     @Required var reporter: Long?,
                     @Required var owner: Long,
                     @Required var chosen: Boolean = false,
                     @Required var points: Long = 0L)