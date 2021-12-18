package gfc.frontend.dataclasses

data class TaskDTO(val ownerId: Long, val name: String, val description: String, val points: Long, val repeatable: Boolean)
