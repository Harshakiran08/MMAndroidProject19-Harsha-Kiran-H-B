package com.pashuaahar.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HistoryDao_Impl implements HistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FeedHistory> __insertionAdapterOfFeedHistory;

  private final EntityDeletionOrUpdateAdapter<FeedHistory> __deletionAdapterOfFeedHistory;

  public HistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFeedHistory = new EntityInsertionAdapter<FeedHistory>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `feed_history` (`id`,`userId`,`animalProfileId`,`cowName`,`breed`,`animalType`,`goalMode`,`location`,`weight`,`milkYield`,`date`,`maizeKg`,`cottonseedKg`,`branKg`,`proteinPercent`,`energyValueMcal`,`fiberPercent`,`savingsRupees`,`totalCostRupees`,`marketCostRupees`,`dailySavings`,`monthlySavings`,`yearlySavings`,`favorite`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FeedHistory entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        if (entity.getAnimalProfileId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getAnimalProfileId());
        }
        statement.bindString(4, entity.getCowName());
        statement.bindString(5, entity.getBreed());
        statement.bindString(6, entity.getAnimalType());
        statement.bindString(7, entity.getGoalMode());
        statement.bindString(8, entity.getLocation());
        statement.bindDouble(9, entity.getWeight());
        statement.bindDouble(10, entity.getMilkYield());
        statement.bindLong(11, entity.getDate());
        statement.bindDouble(12, entity.getMaizeKg());
        statement.bindDouble(13, entity.getCottonseedKg());
        statement.bindDouble(14, entity.getBranKg());
        statement.bindDouble(15, entity.getProteinPercent());
        statement.bindDouble(16, entity.getEnergyValueMcal());
        statement.bindDouble(17, entity.getFiberPercent());
        statement.bindDouble(18, entity.getSavingsRupees());
        statement.bindDouble(19, entity.getTotalCostRupees());
        statement.bindDouble(20, entity.getMarketCostRupees());
        statement.bindDouble(21, entity.getDailySavings());
        statement.bindDouble(22, entity.getMonthlySavings());
        statement.bindDouble(23, entity.getYearlySavings());
        final int _tmp = entity.getFavorite() ? 1 : 0;
        statement.bindLong(24, _tmp);
      }
    };
    this.__deletionAdapterOfFeedHistory = new EntityDeletionOrUpdateAdapter<FeedHistory>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `feed_history` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FeedHistory entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insertHistory(final FeedHistory history,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFeedHistory.insert(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHistory(final FeedHistory history,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFeedHistory.handle(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FeedHistory>> getHistoryForUser(final int userId) {
    final String _sql = "SELECT * FROM feed_history WHERE userId = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"feed_history"}, new Callable<List<FeedHistory>>() {
      @Override
      @NonNull
      public List<FeedHistory> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAnimalProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "animalProfileId");
          final int _cursorIndexOfCowName = CursorUtil.getColumnIndexOrThrow(_cursor, "cowName");
          final int _cursorIndexOfBreed = CursorUtil.getColumnIndexOrThrow(_cursor, "breed");
          final int _cursorIndexOfAnimalType = CursorUtil.getColumnIndexOrThrow(_cursor, "animalType");
          final int _cursorIndexOfGoalMode = CursorUtil.getColumnIndexOrThrow(_cursor, "goalMode");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfMilkYield = CursorUtil.getColumnIndexOrThrow(_cursor, "milkYield");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMaizeKg = CursorUtil.getColumnIndexOrThrow(_cursor, "maizeKg");
          final int _cursorIndexOfCottonseedKg = CursorUtil.getColumnIndexOrThrow(_cursor, "cottonseedKg");
          final int _cursorIndexOfBranKg = CursorUtil.getColumnIndexOrThrow(_cursor, "branKg");
          final int _cursorIndexOfProteinPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPercent");
          final int _cursorIndexOfEnergyValueMcal = CursorUtil.getColumnIndexOrThrow(_cursor, "energyValueMcal");
          final int _cursorIndexOfFiberPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "fiberPercent");
          final int _cursorIndexOfSavingsRupees = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsRupees");
          final int _cursorIndexOfTotalCostRupees = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCostRupees");
          final int _cursorIndexOfMarketCostRupees = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCostRupees");
          final int _cursorIndexOfDailySavings = CursorUtil.getColumnIndexOrThrow(_cursor, "dailySavings");
          final int _cursorIndexOfMonthlySavings = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlySavings");
          final int _cursorIndexOfYearlySavings = CursorUtil.getColumnIndexOrThrow(_cursor, "yearlySavings");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<FeedHistory> _result = new ArrayList<FeedHistory>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FeedHistory _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final Integer _tmpAnimalProfileId;
            if (_cursor.isNull(_cursorIndexOfAnimalProfileId)) {
              _tmpAnimalProfileId = null;
            } else {
              _tmpAnimalProfileId = _cursor.getInt(_cursorIndexOfAnimalProfileId);
            }
            final String _tmpCowName;
            _tmpCowName = _cursor.getString(_cursorIndexOfCowName);
            final String _tmpBreed;
            _tmpBreed = _cursor.getString(_cursorIndexOfBreed);
            final String _tmpAnimalType;
            _tmpAnimalType = _cursor.getString(_cursorIndexOfAnimalType);
            final String _tmpGoalMode;
            _tmpGoalMode = _cursor.getString(_cursorIndexOfGoalMode);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final double _tmpMilkYield;
            _tmpMilkYield = _cursor.getDouble(_cursorIndexOfMilkYield);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final double _tmpMaizeKg;
            _tmpMaizeKg = _cursor.getDouble(_cursorIndexOfMaizeKg);
            final double _tmpCottonseedKg;
            _tmpCottonseedKg = _cursor.getDouble(_cursorIndexOfCottonseedKg);
            final double _tmpBranKg;
            _tmpBranKg = _cursor.getDouble(_cursorIndexOfBranKg);
            final double _tmpProteinPercent;
            _tmpProteinPercent = _cursor.getDouble(_cursorIndexOfProteinPercent);
            final double _tmpEnergyValueMcal;
            _tmpEnergyValueMcal = _cursor.getDouble(_cursorIndexOfEnergyValueMcal);
            final double _tmpFiberPercent;
            _tmpFiberPercent = _cursor.getDouble(_cursorIndexOfFiberPercent);
            final double _tmpSavingsRupees;
            _tmpSavingsRupees = _cursor.getDouble(_cursorIndexOfSavingsRupees);
            final double _tmpTotalCostRupees;
            _tmpTotalCostRupees = _cursor.getDouble(_cursorIndexOfTotalCostRupees);
            final double _tmpMarketCostRupees;
            _tmpMarketCostRupees = _cursor.getDouble(_cursorIndexOfMarketCostRupees);
            final double _tmpDailySavings;
            _tmpDailySavings = _cursor.getDouble(_cursorIndexOfDailySavings);
            final double _tmpMonthlySavings;
            _tmpMonthlySavings = _cursor.getDouble(_cursorIndexOfMonthlySavings);
            final double _tmpYearlySavings;
            _tmpYearlySavings = _cursor.getDouble(_cursorIndexOfYearlySavings);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new FeedHistory(_tmpId,_tmpUserId,_tmpAnimalProfileId,_tmpCowName,_tmpBreed,_tmpAnimalType,_tmpGoalMode,_tmpLocation,_tmpWeight,_tmpMilkYield,_tmpDate,_tmpMaizeKg,_tmpCottonseedKg,_tmpBranKg,_tmpProteinPercent,_tmpEnergyValueMcal,_tmpFiberPercent,_tmpSavingsRupees,_tmpTotalCostRupees,_tmpMarketCostRupees,_tmpDailySavings,_tmpMonthlySavings,_tmpYearlySavings,_tmpFavorite);
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
