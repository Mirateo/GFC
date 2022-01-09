package gfc.frontend.requests
import android.util.Patterns.EMAIL_ADDRESS
import kotlinx.serialization.Serializable


@Serializable
data class SignupRequest(val username: String, val email: String, val role: String, val password: String) {
    init {
        require(username.isNotBlank()) { "Najpierw wprowadź nazwę użytkownika" }
        require(username.length >= 3) {"Nazwa użytkownika musi składać się z co najmniej 3 znaków"}
        require(username.length <= 20) {"Nazwa użytkownika może się składać maksymalnie z 20 znaków"}
        require(email.isNotBlank()) { "Najpierw wprowadź adres Email" }
        require(EMAIL_ADDRESS.matcher(email).matches()) { "Adres Email musi być prawidłowy" }
        require(role.isNotBlank()) { "Role is blank" }
        require(role == "PARENT" || role == "CHILD") { "Niepoprawna rola" }
        require(password.isNotBlank()) { "Najpierw wprowadź hasło" }
        require(password.length >= 6) {"Hasło musi składać się z co najmniej 6 znaków"}
        require(password.length <= 40) {"Hasło może się składać maksymalnie z 40 znaków"}
    }
}