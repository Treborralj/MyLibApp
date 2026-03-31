package com.example.mylib.data.repo
import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mylib.data.repo.Dao.BookDao
import com.example.mylib.data.repo.Dao.BookListCrossRefDao
import com.example.mylib.data.repo.Dao.BookListDao
import com.example.mylib.data.repo.Dao.FollowingDao
import com.example.mylib.data.repo.Dao.PostDao
import com.example.mylib.data.repo.Dao.ReviewDao
import com.example.mylib.data.repo.Dao.UserDao
import org.checkerframework.checker.units.qual.Time
import org.checkerframework.common.aliasing.qual.Unique
import java.sql.Blob


@Database(
    entities = [
        User::class,
        Book::class,
        Review::class,
        Post::class,
        Following::class,
        BookList::class,
        BookListCrossRef::class
    ],
    version = 9
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun reviewDao(): ReviewDao
    abstract fun postDao(): PostDao

    abstract fun bookListDao(): BookListDao
    abstract fun bookListCrossRefDao(): BookListCrossRefDao
    abstract fun followingDao(): FollowingDao





    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

@Entity(
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val bio: String,
    val imagePath: String? = null
)
@Entity
data class Book(
    @PrimaryKey val id: Int,
    val name: String,
    val genre: String,
    val isbn: String,
    val writer: String,
    val score: Double
)

@Entity
data class Review(
    @PrimaryKey val id: Int,
    val bookId: Int,
    val username: String,
    val text: String?,
    val score: Double,
    val time: String?
)


@Entity
data class Post(
    @PrimaryKey val id: Int,
    val accountId: Int,
    val text: String?,
    val time: String?,
    val imagePath: String?
)

@Entity(
    indices = [
        Index(value = ["owner", "type"], unique = true)
    ]
)
data class BookList(
    @PrimaryKey(autoGenerate = true) val listId: Int,
    val owner: String,
    val type: String // "wishlist", "reading", "finished"
)



@Entity(
    primaryKeys = ["listId", "bookId"],
    foreignKeys = [
        ForeignKey(
            entity = BookList::class,
            parentColumns = ["listId"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BookListCrossRef(
    val listId: Int,
    val bookId: Int
)
@Entity(
    primaryKeys= ["followingUsername", "followedUsername"],
)
data class Following
    (
    val followingUsername: String,
    val followedUsername: String
)

