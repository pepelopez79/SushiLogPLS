import os

files_and_replacements = {
    "app/src/main/java/pls/dev/sushilog/ui/components/PieceCounterItem.kt": [
        ("Modifier.size(24.dp)", "Modifier.size(36.dp)"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/components/CustomPieceCounterItem.kt": [
        ("Modifier.size(24.dp)", "Modifier.size(36.dp)"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/screens/HistoryScreen.kt": [
        ("modifier = Modifier.size(24.dp))", "modifier = Modifier.size(36.dp))"),
        ("modifier = Modifier.size(16.dp))", "modifier = Modifier.size(24.dp))"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/screens/StatsScreen.kt": [
        ("Modifier.size(48.dp))", "Modifier.size(64.dp))"),
        ("Modifier.size(20.dp))", "Modifier.size(28.dp))"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/screens/CustomPiecesScreen.kt": [
        ("id = piece.iconId), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(24.dp))", "id = piece.iconId), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(36.dp))"),
        ("id = icon), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(16.dp))", "id = icon), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(20.dp))"),
        ("id = emojiIcon), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(24.dp))", "id = emojiIcon), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(28.dp))"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/screens/AchievementsScreen.kt": [
        ("modifier = Modifier.size(32.dp)", "modifier = Modifier.size(40.dp)"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/screens/SplashScreen.kt": [
        ("Modifier.size(100.dp).padding(bottom = 16.dp)", "Modifier.size(140.dp).padding(bottom = 16.dp)"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/screens/SettingsScreen.kt": [
        ("modifier = Modifier.size(28.dp)", "modifier = Modifier.size(36.dp)"),
    ],
    "app/src/main/java/pls/dev/sushilog/ui/screens/HomeScreen.kt": [
        ("Modifier.size(48.dp).clip", "Modifier.size(56.dp).clip"),
        ("Modifier.size(40.dp).clip(CircleShape).background(iconColor", "Modifier.size(48.dp).clip(CircleShape).background(iconColor"),
    ],
}

for filepath, repls in files_and_replacements.items():
    with open(filepath, 'r') as f:
        content = f.read()
    for old, new in repls:
        if old in content:
            content = content.replace(old, new)
            print(f"OK: {filepath} -- replaced")
        else:
            print(f"SKIP: {filepath} -- '{old[:50]}...' not found")
    with open(filepath, 'w') as f:
        f.write(content)

