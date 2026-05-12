package com.pashuaahar.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile HistoryDao _historyDao;

  private volatile AnimalProfileDao _animalProfileDao;

  private volatile InventoryDao _inventoryDao;

  private volatile FavoriteRecipeDao _favoriteRecipeDao;

  private volatile MarketAlertDao _marketAlertDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `username` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `googleLinked` INTEGER NOT NULL, `syncEnabled` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `feed_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `animalProfileId` INTEGER, `cowName` TEXT NOT NULL, `breed` TEXT NOT NULL, `animalType` TEXT NOT NULL, `goalMode` TEXT NOT NULL, `location` TEXT NOT NULL, `weight` REAL NOT NULL, `milkYield` REAL NOT NULL, `date` INTEGER NOT NULL, `maizeKg` REAL NOT NULL, `cottonseedKg` REAL NOT NULL, `branKg` REAL NOT NULL, `proteinPercent` REAL NOT NULL, `energyValueMcal` REAL NOT NULL, `fiberPercent` REAL NOT NULL, `savingsRupees` REAL NOT NULL, `totalCostRupees` REAL NOT NULL, `marketCostRupees` REAL NOT NULL, `dailySavings` REAL NOT NULL, `monthlySavings` REAL NOT NULL, `yearlySavings` REAL NOT NULL, `favorite` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_feed_history_userId` ON `feed_history` (`userId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_feed_history_animalProfileId` ON `feed_history` (`animalProfileId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `animal_profiles` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT NOT NULL, `animalType` TEXT NOT NULL, `breed` TEXT NOT NULL, `weight` REAL NOT NULL, `currentMilkProduction` REAL NOT NULL, `notes` TEXT NOT NULL, `lastUpdated` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_animal_profiles_userId` ON `animal_profiles` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `inventory_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `ingredient` TEXT NOT NULL, `quantityKg` REAL NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_inventory_items_userId` ON `inventory_items` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_recipes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `title` TEXT NOT NULL, `summary` TEXT NOT NULL, `animalType` TEXT NOT NULL, `goalMode` TEXT NOT NULL, `savedAt` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_favorite_recipes_userId` ON `favorite_recipes` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `market_alerts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `ingredient` TEXT NOT NULL, `thresholdPrice` REAL NOT NULL, `enabled` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_market_alerts_userId` ON `market_alerts` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bd086bdeb271f137fc4072a48f9ebaf7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `feed_history`");
        db.execSQL("DROP TABLE IF EXISTS `animal_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `inventory_items`");
        db.execSQL("DROP TABLE IF EXISTS `favorite_recipes`");
        db.execSQL("DROP TABLE IF EXISTS `market_alerts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(5);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("googleLinked", new TableInfo.Column("googleLinked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("syncEnabled", new TableInfo.Column("syncEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.pashuaahar.data.db.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsFeedHistory = new HashMap<String, TableInfo.Column>(24);
        _columnsFeedHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("animalProfileId", new TableInfo.Column("animalProfileId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("cowName", new TableInfo.Column("cowName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("breed", new TableInfo.Column("breed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("animalType", new TableInfo.Column("animalType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("goalMode", new TableInfo.Column("goalMode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("milkYield", new TableInfo.Column("milkYield", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("maizeKg", new TableInfo.Column("maizeKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("cottonseedKg", new TableInfo.Column("cottonseedKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("branKg", new TableInfo.Column("branKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("proteinPercent", new TableInfo.Column("proteinPercent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("energyValueMcal", new TableInfo.Column("energyValueMcal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("fiberPercent", new TableInfo.Column("fiberPercent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("savingsRupees", new TableInfo.Column("savingsRupees", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("totalCostRupees", new TableInfo.Column("totalCostRupees", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("marketCostRupees", new TableInfo.Column("marketCostRupees", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("dailySavings", new TableInfo.Column("dailySavings", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("monthlySavings", new TableInfo.Column("monthlySavings", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("yearlySavings", new TableInfo.Column("yearlySavings", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedHistory.put("favorite", new TableInfo.Column("favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFeedHistory = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysFeedHistory.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesFeedHistory = new HashSet<TableInfo.Index>(2);
        _indicesFeedHistory.add(new TableInfo.Index("index_feed_history_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        _indicesFeedHistory.add(new TableInfo.Index("index_feed_history_animalProfileId", false, Arrays.asList("animalProfileId"), Arrays.asList("ASC")));
        final TableInfo _infoFeedHistory = new TableInfo("feed_history", _columnsFeedHistory, _foreignKeysFeedHistory, _indicesFeedHistory);
        final TableInfo _existingFeedHistory = TableInfo.read(db, "feed_history");
        if (!_infoFeedHistory.equals(_existingFeedHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "feed_history(com.pashuaahar.data.db.FeedHistory).\n"
                  + " Expected:\n" + _infoFeedHistory + "\n"
                  + " Found:\n" + _existingFeedHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsAnimalProfiles = new HashMap<String, TableInfo.Column>(9);
        _columnsAnimalProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("animalType", new TableInfo.Column("animalType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("breed", new TableInfo.Column("breed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("currentMilkProduction", new TableInfo.Column("currentMilkProduction", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimalProfiles.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAnimalProfiles = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAnimalProfiles.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAnimalProfiles = new HashSet<TableInfo.Index>(1);
        _indicesAnimalProfiles.add(new TableInfo.Index("index_animal_profiles_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoAnimalProfiles = new TableInfo("animal_profiles", _columnsAnimalProfiles, _foreignKeysAnimalProfiles, _indicesAnimalProfiles);
        final TableInfo _existingAnimalProfiles = TableInfo.read(db, "animal_profiles");
        if (!_infoAnimalProfiles.equals(_existingAnimalProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "animal_profiles(com.pashuaahar.data.db.AnimalProfile).\n"
                  + " Expected:\n" + _infoAnimalProfiles + "\n"
                  + " Found:\n" + _existingAnimalProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsInventoryItems = new HashMap<String, TableInfo.Column>(5);
        _columnsInventoryItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryItems.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryItems.put("ingredient", new TableInfo.Column("ingredient", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryItems.put("quantityKg", new TableInfo.Column("quantityKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryItems.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInventoryItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysInventoryItems.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesInventoryItems = new HashSet<TableInfo.Index>(1);
        _indicesInventoryItems.add(new TableInfo.Index("index_inventory_items_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoInventoryItems = new TableInfo("inventory_items", _columnsInventoryItems, _foreignKeysInventoryItems, _indicesInventoryItems);
        final TableInfo _existingInventoryItems = TableInfo.read(db, "inventory_items");
        if (!_infoInventoryItems.equals(_existingInventoryItems)) {
          return new RoomOpenHelper.ValidationResult(false, "inventory_items(com.pashuaahar.data.db.InventoryItem).\n"
                  + " Expected:\n" + _infoInventoryItems + "\n"
                  + " Found:\n" + _existingInventoryItems);
        }
        final HashMap<String, TableInfo.Column> _columnsFavoriteRecipes = new HashMap<String, TableInfo.Column>(7);
        _columnsFavoriteRecipes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteRecipes.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteRecipes.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteRecipes.put("summary", new TableInfo.Column("summary", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteRecipes.put("animalType", new TableInfo.Column("animalType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteRecipes.put("goalMode", new TableInfo.Column("goalMode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteRecipes.put("savedAt", new TableInfo.Column("savedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoriteRecipes = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysFavoriteRecipes.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesFavoriteRecipes = new HashSet<TableInfo.Index>(1);
        _indicesFavoriteRecipes.add(new TableInfo.Index("index_favorite_recipes_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoFavoriteRecipes = new TableInfo("favorite_recipes", _columnsFavoriteRecipes, _foreignKeysFavoriteRecipes, _indicesFavoriteRecipes);
        final TableInfo _existingFavoriteRecipes = TableInfo.read(db, "favorite_recipes");
        if (!_infoFavoriteRecipes.equals(_existingFavoriteRecipes)) {
          return new RoomOpenHelper.ValidationResult(false, "favorite_recipes(com.pashuaahar.data.db.FavoriteRecipe).\n"
                  + " Expected:\n" + _infoFavoriteRecipes + "\n"
                  + " Found:\n" + _existingFavoriteRecipes);
        }
        final HashMap<String, TableInfo.Column> _columnsMarketAlerts = new HashMap<String, TableInfo.Column>(5);
        _columnsMarketAlerts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMarketAlerts.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMarketAlerts.put("ingredient", new TableInfo.Column("ingredient", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMarketAlerts.put("thresholdPrice", new TableInfo.Column("thresholdPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMarketAlerts.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMarketAlerts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMarketAlerts.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMarketAlerts = new HashSet<TableInfo.Index>(1);
        _indicesMarketAlerts.add(new TableInfo.Index("index_market_alerts_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoMarketAlerts = new TableInfo("market_alerts", _columnsMarketAlerts, _foreignKeysMarketAlerts, _indicesMarketAlerts);
        final TableInfo _existingMarketAlerts = TableInfo.read(db, "market_alerts");
        if (!_infoMarketAlerts.equals(_existingMarketAlerts)) {
          return new RoomOpenHelper.ValidationResult(false, "market_alerts(com.pashuaahar.data.db.MarketAlert).\n"
                  + " Expected:\n" + _infoMarketAlerts + "\n"
                  + " Found:\n" + _existingMarketAlerts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bd086bdeb271f137fc4072a48f9ebaf7", "1038f7f4d9be08246a25eb99584082ef");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","feed_history","animal_profiles","inventory_items","favorite_recipes","market_alerts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `feed_history`");
      _db.execSQL("DELETE FROM `animal_profiles`");
      _db.execSQL("DELETE FROM `inventory_items`");
      _db.execSQL("DELETE FROM `favorite_recipes`");
      _db.execSQL("DELETE FROM `market_alerts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HistoryDao.class, HistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AnimalProfileDao.class, AnimalProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(InventoryDao.class, InventoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FavoriteRecipeDao.class, FavoriteRecipeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MarketAlertDao.class, MarketAlertDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public HistoryDao historyDao() {
    if (_historyDao != null) {
      return _historyDao;
    } else {
      synchronized(this) {
        if(_historyDao == null) {
          _historyDao = new HistoryDao_Impl(this);
        }
        return _historyDao;
      }
    }
  }

  @Override
  public AnimalProfileDao animalProfileDao() {
    if (_animalProfileDao != null) {
      return _animalProfileDao;
    } else {
      synchronized(this) {
        if(_animalProfileDao == null) {
          _animalProfileDao = new AnimalProfileDao_Impl(this);
        }
        return _animalProfileDao;
      }
    }
  }

  @Override
  public InventoryDao inventoryDao() {
    if (_inventoryDao != null) {
      return _inventoryDao;
    } else {
      synchronized(this) {
        if(_inventoryDao == null) {
          _inventoryDao = new InventoryDao_Impl(this);
        }
        return _inventoryDao;
      }
    }
  }

  @Override
  public FavoriteRecipeDao favoriteRecipeDao() {
    if (_favoriteRecipeDao != null) {
      return _favoriteRecipeDao;
    } else {
      synchronized(this) {
        if(_favoriteRecipeDao == null) {
          _favoriteRecipeDao = new FavoriteRecipeDao_Impl(this);
        }
        return _favoriteRecipeDao;
      }
    }
  }

  @Override
  public MarketAlertDao marketAlertDao() {
    if (_marketAlertDao != null) {
      return _marketAlertDao;
    } else {
      synchronized(this) {
        if(_marketAlertDao == null) {
          _marketAlertDao = new MarketAlertDao_Impl(this);
        }
        return _marketAlertDao;
      }
    }
  }
}
