package gfc.frontend.requests
import kotlinx.serialization.Serializable


@Serializable
data class SigninRequest(val username: String, val password: String) {
    init {
        require(username.isNotBlank()) { "Najpierw wprowadź nazwę użytkownika" }
        require(username.length >= 3) { "Użytkownik niepoprawny" }
        require(username.length <= 20) { "Użytkownik niepoprawny" }
        require(password.isNotBlank()) { "Najpierw wprowadź hasło" }
        require(password.length >= 6) { "Użytkownik niepoprawny" }
        require(password.length <= 40) { "Użytkownik niepoprawny" }
    }
}