package com.example.msd25_android.logic

import android.app.Application
import com.example.msd25_android.UserAuthState
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.session.Session
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.flow.first
import org.mindrot.jbcrypt.BCrypt
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SessionManager(application: Application,
                     private val setUserAuthState: (UserAuthState) -> Unit,
                     private val userRepository: UserRepository
) {
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val sessionDao = AppDatabase.getDatabase(application).sessionDao()

    @OptIn(ExperimentalUuidApi::class)
    suspend fun login(phone: String, password: String): BackendResponse<Unit> {
        val user = userDao.getUserByPhone(phone)
        if (user == null) {
            return BackendResponse(false, "Invalid phone number")
        }
        if (!BCrypt.checkpw(password, user.password)) {
            return BackendResponse(false, "Invalid password")
        }
        // create session
        val uuid = Uuid.random()
        val token = BCrypt.hashpw(uuid.toString(), BCrypt.gensalt(5))
        val session = Session(user.id, token)

        sessionDao.insertSession(session)
        setUserAuthState(UserAuthState.AUTHENTICATED)
        userRepository.savePhoneNumber(user.phoneNumber)
        userRepository.saveUserToken(uuid.toString())
        return BackendResponse(true, "")
    }

    suspend fun signup(user: User): BackendResponse<Unit> {
        val encrypted = BCrypt.hashpw(user.password, BCrypt.gensalt(5))

        if (userDao.getUserByPhone(user.phoneNumber) != null) {
            return BackendResponse(false, "Phone number is already registered")
        }

        if (userDao.getUserByEmail(user.email) != null) {
            return BackendResponse(false, "Email is already registered")
        }

        userDao.insertUser(User(
            name = user.name,
            password = encrypted,
            id = 0,
            email = user.email,
            phoneNumber = user.phoneNumber,
            birthdate = user.birthdate,
        ))
        val newUser = userDao.getUserByPhone(user.phoneNumber)
        if (newUser == null) {
            return BackendResponse(false, "Something went wrong!")
        }

        return login(user.phoneNumber, user.password)
    }

    suspend fun logout() {
        setUserAuthState(UserAuthState.UNAUTHENTICATED)

        val phone = userRepository.currentPhoneNumber.first()
        val token = userRepository.currentToken.first()

        if (phone == null || token == null) return

        val userWithSessions = userDao.getUserWithSessions(phone)
        if (userWithSessions == null) return

        for (session in userWithSessions.sessions) {
            if (BCrypt.checkpw(token, session.token)) {
                sessionDao.deleteSession(session)
                return
            }
        }
    }

    suspend fun restoreToken() {
        val phone = userRepository.currentPhoneNumber.first()
        val token = userRepository.currentToken.first()
        if (phone == null || token == null) {
            setUserAuthState(UserAuthState.UNAUTHENTICATED)
            return
        }
        val userWithSessions = userDao.getUserWithSessions(phone)
        if (userWithSessions == null) {
            setUserAuthState(UserAuthState.UNAUTHENTICATED)
            return
        }
        for (session in userWithSessions.sessions) {
            if (BCrypt.checkpw(token, session.token)) {
                setUserAuthState(UserAuthState.AUTHENTICATED)
                return
            }
        }

        setUserAuthState(UserAuthState.UNAUTHENTICATED)
        return
    }




}