import os
import re
def clean(f):
    try:
        with open(f, 'r') as file:
            c = file.read()
            c2 = re.sub(r'(".*?"|\'.*?\')|(/\*.*?\*/)|(//.*?$)', lambda m: m.group(1) if m.group(1) else '', c, flags=re.DOTALL|re.MULTILINE)
            # just write back if changed
            if c != c2:
                with open(f, 'w') as out:
                    out.write(c2)
                print(f"Cleaned {f}")
    except Exception as e:
        pass
for r, d, files in os.walk('.'):
    if '.git' in r or 'build' in r: continue
    for f in files:
        if f.endswith('.kt') or f.endswith('.kts'):
            clean(os.path.join(r, f))
