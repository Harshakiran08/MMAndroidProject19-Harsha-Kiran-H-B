package com.pashuaahar.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FavoriteRecipeDao_Impl implements FavoriteRecipeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FavoriteRecipe> __insertionAdapterOfFavoriteRecipe;

  public FavoriteRecipeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFavoriteRecipe = new EntityInsertionAdapter<FavoriteRecipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `favorite_recipes` (`id`,`userId`,`title`,`summary`,`animalType`,`goalMode`,`savedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteRecipe entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getSummary());
        statement.bindString(5, entity.getAnimalType());
        statement.bindString(6, entity.getGoalMode());
        statement.bindLong(7, entity.getSavedAt());
      }
    };
  }

  @Override
  public Object insertFavorite(final FavoriteRecipe recipe,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFavoriteRecipe.insertAndReturnId(recipe);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FavoriteRecipe>> getFavoritesForUser(final int userId) {
    final String _sql = "SELECT * FROM favorite_recipes WHERE userId = ? ORDER BY savedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"favorite_recipes"}, new Callable<List<FavoriteRecipe>>() {
      @Override
      @NonNull
      public List<FavoriteRecipe> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfAnimalType = CursorUtil.getColumnIndexOrThrow(_cursor, "animalType");
          final int _cursorIndexOfGoalMode = CursorUtil.getColumnIndexOrThrow(_cursor, "goalMode");
          final int _cursorIndexOfSavedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "savedAt");
          final List<FavoriteRecipe> _result = new ArrayList<FavoriteRecipe>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FavoriteRecipe _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final String _tmpAnimalType;
            _tmpAnimalType = _cursor.getString(_cursorIndexOfAnimalType);
            final String _tmpGoalMode;
            _tmpGoalMode = _cursor.getString(_cursorIndexOfGoalMode);
            final long _tmpSavedAt;
            _tmpSavedAt = _cursor.getLong(_cursorIndexOfSavedAt);
            _item = new FavoriteRecipe(_tmpId,_tmpUserId,_tmpTitle,_tmpSummary,_tmpAnimalType,_tmpGoalMode,_tmpSavedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
