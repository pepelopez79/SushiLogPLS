import os
import re
def remove_comments_from_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    pattern = r'(".*?"|\'.*?\')|(/\*.*?\*/)|(//.*?$)'
    def replacer(match):
        if match.group(1): return match.group(1)
        else: return ''
    new_content = re.sub(pattern, replacer, content, flags=re.DOTALL | re.MULTILINE)
    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Cleaned {filepath}")
for root, _, files in os.walk('/Users/pepelopez/Desktop/GitHub/SushiLog/app/src/main/java'):
    for file in files:
        if file.endswith('.kt'):
            remove_comments_from_file(os.path.join(root, file))
