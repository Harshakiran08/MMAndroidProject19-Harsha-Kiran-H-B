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
public final class AnimalProfileDao_Impl implements AnimalProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AnimalProfile> __insertionAdapterOfAnimalProfile;

  public AnimalProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAnimalProfile = new EntityInsertionAdapter<AnimalProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `animal_profiles` (`id`,`userId`,`name`,`animalType`,`breed`,`weight`,`currentMilkProduction`,`notes`,`lastUpdated`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AnimalProfile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getAnimalType());
        statement.bindString(5, entity.getBreed());
        statement.bindDouble(6, entity.getWeight());
        statement.bindDouble(7, entity.getCurrentMilkProduction());
        statement.bindString(8, entity.getNotes());
        statement.bindLong(9, entity.getLastUpdated());
      }
    };
  }

  @Override
  public Object upsertProfile(final AnimalProfile profile,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAnimalProfile.insertAndReturnId(profile);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AnimalProfile>> getProfilesForUser(final int userId) {
    final String _sql = "SELECT * FROM animal_profiles WHERE userId = ? ORDER BY lastUpdated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"animal_profiles"}, new Callable<List<AnimalProfile>>() {
      @Override
      @NonNull
      public List<AnimalProfile> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAnimalType = CursorUtil.getColumnIndexOrThrow(_cursor, "animalType");
          final int _cursorIndexOfBreed = CursorUtil.getColumnIndexOrThrow(_cursor, "breed");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfCurrentMilkProduction = CursorUtil.getColumnIndexOrThrow(_cursor, "currentMilkProduction");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<AnimalProfile> _result = new ArrayList<AnimalProfile>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AnimalProfile _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAnimalType;
            _tmpAnimalType = _cursor.getString(_cursorIndexOfAnimalType);
            final String _tmpBreed;
            _tmpBreed = _cursor.getString(_cursorIndexOfBreed);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final double _tmpCurrentMilkProduction;
            _tmpCurrentMilkProduction = _cursor.getDouble(_cursorIndexOfCurrentMilkProduction);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new AnimalProfile(_tmpId,_tmpUserId,_tmpName,_tmpAnimalType,_tmpBreed,_tmpWeight,_tmpCurrentMilkProduction,_tmpNotes,_tmpLastUpdated);
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
