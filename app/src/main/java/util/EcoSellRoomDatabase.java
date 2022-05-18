package util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.GroupDao;
import data.PostDao;
import data.ProductDao;
import data.UserDao;
import model.Group;
import model.Post;
import model.Product;
import model.User;

@Database(entities = {User.class, Group.class, Product.class, Post.class}, version = 6, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class EcoSellRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract GroupDao groupDao();
    public abstract ProductDao productDao();
    public abstract PostDao postDao();
    public static final int NUM_OF_THREADS = 4;

    private static volatile EcoSellRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    public static EcoSellRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (EcoSellRoomDatabase.class) {
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EcoSellRoomDatabase.class, "EcoSellDB")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    // Away from UI Thread
                    databaseWriteExecutor.execute(() -> {
                        UserDao userDao = INSTANCE.userDao();
                        GroupDao groupDao = INSTANCE.groupDao();
                        ProductDao productDao = INSTANCE.productDao();
                        PostDao postDao = INSTANCE.postDao();
                        //userDao.deleteAll();
                    });
                }
            };
}
