import re
def fix(f):
    with open(f, 'r') as file:
        c = file.read()
    c = c.replace('piece.emoji', 'piece.iconId')
    c = c.replace('it.emoji', 'it.iconId')
    c = c.replace('emoji =', 'iconId =')
    c = c.replace('Text(getPieceIconId', 'Icon(painter = androidx.compose.ui.res.painterResource(id = getPieceIconId')
    with open(f, 'w') as file:
        file.write(c)
files = [
    'app/src/main/java/pls/dev/sushilog/ui/screens/HistoryScreen.kt',
    'app/src/main/java/pls/dev/sushilog/ui/screens/StatsScreen.kt',
    'app/src/main/java/pls/dev/sushilog/ui/components/PieceCounterItem.kt',
    'app/src/main/java/pls/dev/sushilog/ui/components/CustomPieceCounterItem.kt',
    'app/src/main/java/pls/dev/sushilog/ui/screens/CounterScreen.kt',
    'app/src/main/java/pls/dev/sushilog/ui/screens/CustomPiecesScreen.kt'
]
for f in files: fix(f)
