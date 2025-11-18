class ReadingSettings {
  final bool isDarkMode;
  final double fontSize;

  ReadingSettings({required this.isDarkMode, required this.fontSize});

  ReadingSettings copyWith({bool? isDarkMode, double? fontSize}) {
    return ReadingSettings(
      isDarkMode: isDarkMode ?? this.isDarkMode,
      fontSize: fontSize ?? this.fontSize,
    );
  }

  Map<String, dynamic> toMap() => {
    'isDarkMode': isDarkMode ? 1 : 0,
    'fontSize': fontSize,
  };

  factory ReadingSettings.fromMap(Map<String, dynamic> map) {
    return ReadingSettings(
      isDarkMode: (map['isDarkMode'] as int) == 1,
      fontSize: map['fontSize'] as double,
    );
  }

  static ReadingSettings get defaultSettings =>
      ReadingSettings(isDarkMode: false, fontSize: 18);
}
