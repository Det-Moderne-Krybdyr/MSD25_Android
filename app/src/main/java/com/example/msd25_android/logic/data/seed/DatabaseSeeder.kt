package com.example.msd25_android.logic.data.seed

import android.util.Log
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.user.User
import kotlinx.datetime.Clock
import org.mindrot.jbcrypt.BCrypt

private val users = listOf(
    User(
        name = "Peter Nielsen",
        email = "petni23@student.sdu.dk",
        phoneNumber = "20481124",
        password = BCrypt.hashpw("1234", BCrypt.gensalt()),
        birthdate = Clock.System.now()
    ),
    User(
        name = "Julius Konge",
        email = "jukon23@student.sdu.dk",
        phoneNumber = "72930484",
        password = BCrypt.hashpw("1234", BCrypt.gensalt()),
        birthdate = Clock.System.now()
    ),
    User(
        name = "Bastian Gregersen",
        email = "bagre23@student.sdu.dk",
        phoneNumber = "62983037",
        password = BCrypt.hashpw("1234", BCrypt.gensalt()),
        birthdate = Clock.System.now()
    ),
    User(
        name = "Nicolai Skoda",
        email = "niben23@student.sdu.dk",
        phoneNumber = "01827401",
        password = BCrypt.hashpw("1234", BCrypt.gensalt()),
        birthdate = Clock.System.now()
    ),
    User(
        name = "Mille Jakobsen",
        email = "mijak22@student.sdu.dk",
        phoneNumber = "47302739",
        password = BCrypt.hashpw("1234", BCrypt.gensalt()),
        birthdate = Clock.System.now()
    ),
    User(
        name = "Marie Juhl",
        email = "majuh23@student.sdu.dk",
        phoneNumber = "51851206",
        password = BCrypt.hashpw("1234", BCrypt.gensalt()),
        birthdate = Clock.System.now()
    ),
    )

fun seed(db: AppDatabase) {
    users.forEach { user -> db.userDao().insertUser(user) }

    val users = db.userDao().getAllUsers()
    Log.w("seeder" , "Seeded users")

}