package gfc.frontend.dataclasses

import java.sql.Date

data class RepeatableTask(val id: Long = 0, val ownerId: Long = 0, val name: String = "", val description: String = "", val points: Long = 0, val doneToday: Boolean = false, val lastDone: Date)
