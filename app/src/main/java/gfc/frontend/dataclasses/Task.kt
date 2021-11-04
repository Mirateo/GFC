package gfc.frontend.dataclasses


data class Task(val id: Long = 0, val ownerId: Long = 0, val name: String = "", val description: String = "", val points: Long = 0) {

}
