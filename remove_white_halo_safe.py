#!/usr/bin/env python3
"""
Conservative white halo removal for all PNGs.
Only removes pixels that are very close to white (R,G,B >= 220)
and are adjacent to transparency. This preserves light-colored
content while removing the white fringe/halo around icons.
"""

import os
import glob
from PIL import Image

DRAWABLE_DIR = "app/src/main/res/drawable"
WHITE_THRESHOLD = 220  # Only very white pixels (R,G,B all >= 220)
MAX_PASSES = 10

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

def is_white(pixel):
    r, g, b, a = pixel
    return a > 0 and r >= WHITE_THRESHOLD and g >= WHITE_THRESHOLD and b >= WHITE_THRESHOLD

def remove_white_halo(img_path):
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
                if not is_white(p):
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
    print(f"Conservative white halo removal (threshold={WHITE_THRESHOLD}) on {len(pngs)} PNGs...\n")
    total = 0
    for path in pngs:
        total += remove_white_halo(path)
    print(f"\n✅ Done! {total} total pixels removed.")

if __name__ == "__main__":
    main()

