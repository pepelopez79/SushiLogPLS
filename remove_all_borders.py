#!/usr/bin/env python3
"""
Remove ALL non-dark border pixels from ALL PNGs in drawable.
Erodes any pixel adjacent to transparency that is not dark (R,G,B < 100).
Runs until all icons have only dark outline as their border.
"""

import os
import glob
from PIL import Image

DRAWABLE_DIR = "app/src/main/res/drawable"
DARK_THRESHOLD = 100
MAX_PASSES = 100

def has_transparent_neighbor(pixels, x, y, w, h):
    for dx in (-1, 0, 1):
        for dy in (-1, 0, 1):
            if dx == 0 and dy == 0:
                continue
            nx, ny = x + dx, y + dy
            if 0 <= nx < w and 0 <= ny < h:
                if pixels[nx, ny][3] == 0:
                    return True
            else:
                return True
    return False

def is_dark(pixel):
    r, g, b, a = pixel
    return r < DARK_THRESHOLD and g < DARK_THRESHOLD and b < DARK_THRESHOLD

def remove_non_dark_border(img_path):
    img = Image.open(img_path).convert("RGBA")
    pixels = img.load()
    w, h = img.size
    total_changed = 0

    for pass_num in range(MAX_PASSES):
        to_clear = []
        for y in range(h):
            for x in range(w):
                p = pixels[x, y]
                if p[3] == 0:
                    continue
                if is_dark(p):
                    continue
                if has_transparent_neighbor(pixels, x, y, w, h):
                    to_clear.append((x, y))

        if not to_clear:
            break

        for (x, y) in to_clear:
            pixels[x, y] = (0, 0, 0, 0)
        total_changed += len(to_clear)

    if total_changed > 0:
        img.save(img_path)
        print(f"  ✅ {os.path.basename(img_path)}: {total_changed} pixels removed")
    else:
        print(f"  ⏭️  {os.path.basename(img_path)}: clean")

    return total_changed

def main():
    pngs = sorted(glob.glob(os.path.join(DRAWABLE_DIR, "*.png")))
    print(f"Processing ALL {len(pngs)} PNGs - eroding to dark edge...\n")
    total = 0
    for path in pngs:
        total += remove_non_dark_border(path)
    print(f"\n✅ Done! {total} total pixels removed across {len(pngs)} files.")

if __name__ == "__main__":
    main()


