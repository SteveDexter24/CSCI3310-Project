package util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.UserDao;
import model.User;

@Database(entities = {User.class /*Add more entities here*/}, version = 1, exportSchema = false)
public abstract class EcoSellRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public static final int NUM_OF_THREADS = 4;

    private static volatile EcoSellRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    public static EcoSellRoomDatabase getDatabase(final Context context){
        if (context == null){
            synchronized (EcoSellRoomDatabase.class) {
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EcoSellRoomDatabase.class, "EcoSellDB").build();
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
                    });
                }
            };
}
