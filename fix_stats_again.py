import re
def fix(f):
    with open(f, 'r') as file: c = file.read()
    # CustomPiecesScreen fixes
    c = c.replace('listOf(pls.dev.sushilog.R.drawable.nigiris, pls.dev.sushilog.R.drawable.sashimi, pls.dev.sushilog.R.drawable.maki, pls.dev.sushilog.R.drawable.onigiri, pls.dev.sushilog.R.drawable.uramaki, pls.dev.sushilog.R.drawable.gunkan, pls.dev.sushilog.R.drawable.temaki, pls.dev.sushilog.R.drawable.gyoza, pls.dev.sushilog.R.drawable.shrimp, pls.dev.sushilog.R.drawable.edamame, pls.dev.sushilog.R.drawable.takoyaki)', 'listOf<Int>(pls.dev.sushilog.R.drawable.nigiris, pls.dev.sushilog.R.drawable.sashimi, pls.dev.sushilog.R.drawable.maki, pls.dev.sushilog.R.drawable.onigiri, pls.dev.sushilog.R.drawable.uramaki, pls.dev.sushilog.R.drawable.gunkan, pls.dev.sushilog.R.drawable.temaki, pls.dev.sushilog.R.drawable.gyoza, pls.dev.sushilog.R.drawable.shrimp, pls.dev.sushilog.R.drawable.edamame, pls.dev.sushilog.R.drawable.takoyaki)')
    c = c.replace('fun IconPicker(imageOptions: List<String>,', 'fun IconPicker(imageOptions: List<Int>,')
    c = c.replace('painterResource(id = emoji as Int)', 'painterResource(id = emoji)')
    c = c.replace('val emojiOptions = remember { listOf<String>(', 'val emojiOptions = remember { listOf<Int>(')
    c = c.replace('var newEmoji by remember { mutableStateOf(', 'var newEmoji by remember { mutableStateOf<Int>(')
    c = c.replace('var newIconId by remember { mutableStateOf(', 'var newIconId by remember { mutableStateOf<Int>(')
    with open(f, 'w') as file: file.write(c)
fix('app/src/main/java/pls/dev/sushilog/ui/screens/CustomPiecesScreen.kt')
def fix_stats(f):
    with open(f, 'r') as file: c = file.read()
    c = c.replace('Icon(painter = androidx.compose.ui.res.painterResource(id = emoji), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(20.dp))', 'Icon(painter = androidx.compose.ui.res.painterResource(id = iconId), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(20.dp))')
    c = c.replace('Text(\"🍱\", fontSize = 20.sp)', 'Icon(painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.nigiris), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(20.dp))')
    c = c.replace('modifier = Modifier.size(20.dp), fontSize = 20.sp)', 'modifier = Modifier.size(20.dp))')
    with open(f, 'w') as file: file.write(c)
fix_stats('app/src/main/java/pls/dev/sushilog/ui/screens/StatsScreen.kt')
