import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

import '../models/reading_settings.dart';

class DbHelper {
  static final DbHelper instance = DbHelper._internal();
  DbHelper._internal();

  static const _dbName = 'ebook_reader.db';
  static const _dbVersion = 1;

  Database? _db;

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDb();
    return _db!;
  }

  Future<Database> _initDb() async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, _dbName);

    return await openDatabase(path, version: _dbVersion, onCreate: _onCreate);
  }

  Future<void> _onCreate(Database db, int version) async {
    await db.execute('''
      CREATE TABLE settings (
        id INTEGER PRIMARY KEY,
        isDarkMode INTEGER NOT NULL,
        fontSize REAL NOT NULL
      )
    ''');

    await db.execute('''
      CREATE TABLE progress (
        bookId INTEGER PRIMARY KEY,
        lastPage INTEGER NOT NULL
      )
    ''');

    // insert default settings
    await db.insert('settings', {'id': 1, 'isDarkMode': 0, 'fontSize': 18.0});
  }

  // ----- SETTINGS -----
  Future<ReadingSettings> loadSettings() async {
    final db = await database;
    final res = await db.query('settings', where: 'id = 1', limit: 1);
    if (res.isNotEmpty) {
      return ReadingSettings.fromMap(res.first);
    }
    return ReadingSettings.defaultSettings;
  }

  Future<void> saveSettings(ReadingSettings settings) async {
    final db = await database;
    await db.update('settings', settings.toMap(), where: 'id = 1');
  }

  // ----- PROGRESS -----
  Future<int> getLastPage(int bookId) async {
    final db = await database;
    final res = await db.query(
      'progress',
      where: 'bookId = ?',
      whereArgs: [bookId],
    );
    if (res.isNotEmpty) {
      return res.first['lastPage'] as int;
    }
    return 0;
  }

  Future<void> saveLastPage(int bookId, int lastPage) async {
    final db = await database;
    await db.insert('progress', {
      'bookId': bookId,
      'lastPage': lastPage,
    }, conflictAlgorithm: ConflictAlgorithm.replace);
  }
}
