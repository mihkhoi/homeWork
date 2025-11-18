import 'package:flutter/material.dart';

import '../models/reading_settings.dart';
import '../services/db_helper.dart';

class ReadingSettingsProvider extends ChangeNotifier {
  ReadingSettings _settings = ReadingSettings.defaultSettings;
  bool _initialized = false;

  ReadingSettings get settings => _settings;
  bool get isDarkMode => _settings.isDarkMode;
  double get fontSize => _settings.fontSize;
  bool get initialized => _initialized;

  Future<void> load() async {
    _settings = await DbHelper.instance.loadSettings();
    _initialized = true;
    notifyListeners();
  }

  Future<void> toggleDarkMode() async {
    _settings = _settings.copyWith(isDarkMode: !_settings.isDarkMode);
    notifyListeners();
    await DbHelper.instance.saveSettings(_settings);
  }

  Future<void> setFontSize(double size) async {
    _settings = _settings.copyWith(fontSize: size);
    notifyListeners();
    await DbHelper.instance.saveSettings(_settings);
  }
}
