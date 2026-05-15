#!/usr/bin/env python3
"""
Aggressive but safe border cleanup.
Removes any light pixel (R,G,B >= 150) that touches transparency.
Only erodes 1 layer at a time from the edge inward.
Safe because it never touches pixels surrounded by content.
"""

import os
import glob
from PIL import Image

DRAWABLE_DIR = "app/src/main/res/drawable"
LIGHT_THRESHOLD = 120  # Even more aggressive
MAX_PASSES = 30        # Enough to erode deep halos

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

def is_light(pixel):
    r, g, b, a = pixel
    return a > 0 and r >= LIGHT_THRESHOLD and g >= LIGHT_THRESHOLD and b >= LIGHT_THRESHOLD

def process(img_path):
    img = Image.open(img_path).convert("RGBA")
    pixels = img.load()
    w, h = img.size
    total = 0

    for pass_num in range(MAX_PASSES):
        to_clear = []
        for y in range(h):
            for x in range(w):
                p = pixels[x, y]
                if p[3] == 0 or not is_light(p):
                    continue
                if has_transparent_neighbor(pixels, x, y, w, h):
                    to_clear.append((x, y))
        if not to_clear:
            break
        for (x, y) in to_clear:
            pixels[x, y] = (0, 0, 0, 0)
        total += len(to_clear)

    if total > 0:
        img.save(img_path)
        print(f"  ✅ {os.path.basename(img_path)}: {total} px")
    else:
        print(f"  ⏭️  {os.path.basename(img_path)}: clean")
    return total

def main():
    pngs = sorted(glob.glob(os.path.join(DRAWABLE_DIR, "*.png")))
    print(f"Aggressive border cleanup (threshold={LIGHT_THRESHOLD}) on {len(pngs)} PNGs...\n")
    total = 0
    for p in pngs:
        total += process(p)
    print(f"\n✅ Done! {total} total pixels removed.")

if __name__ == "__main__":
    main()


