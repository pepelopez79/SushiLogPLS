#!/usr/bin/env python3
"""
Remove white/near-white border pixels from all PNG icons.
Scans inward from all 4 edges and makes white-ish pixels fully transparent.
A pixel is considered "white-ish" if R,G,B are all >= 200 and alpha > 0.
"""

import os
import glob
from PIL import Image

DRAWABLE_DIR = "app/src/main/res/drawable"
THRESHOLD = 200  # R,G,B all >= this = "white-ish"
MAX_DEPTH = 15   # max pixels to scan inward from each edge

def is_whiteish(pixel):
    r, g, b, a = pixel
    return a > 0 and r >= THRESHOLD and g >= THRESHOLD and b >= THRESHOLD

def remove_white_borders(img_path):
    img = Image.open(img_path).convert("RGBA")
    pixels = img.load()
    w, h = img.size
    changed = 0

    # For each edge pixel, scan inward and make white-ish pixels transparent
    # Stop scanning inward when hitting a non-white pixel

    # Top edge - scan downward
    for x in range(w):
        for y in range(min(MAX_DEPTH, h)):
            if is_whiteish(pixels[x, y]):
                pixels[x, y] = (0, 0, 0, 0)
                changed += 1
            else:
                break

    # Bottom edge - scan upward
    for x in range(w):
        for y in range(h - 1, max(h - 1 - MAX_DEPTH, -1), -1):
            if is_whiteish(pixels[x, y]):
                pixels[x, y] = (0, 0, 0, 0)
                changed += 1
            else:
                break

    # Left edge - scan rightward
    for y in range(h):
        for x in range(min(MAX_DEPTH, w)):
            if is_whiteish(pixels[x, y]):
                pixels[x, y] = (0, 0, 0, 0)
                changed += 1
            else:
                break

    # Right edge - scan leftward
    for y in range(h):
        for x in range(w - 1, max(w - 1 - MAX_DEPTH, -1), -1):
            if is_whiteish(pixels[x, y]):
                pixels[x, y] = (0, 0, 0, 0)
                changed += 1
            else:
                break

    if changed > 0:
        img.save(img_path)
        print(f"  ✅ {os.path.basename(img_path)}: {changed} pixels removed")
    else:
        print(f"  ⏭️  {os.path.basename(img_path)}: clean")

def main():
    pngs = sorted(glob.glob(os.path.join(DRAWABLE_DIR, "*.png")))
    print(f"Processing {len(pngs)} PNG files...\n")
    for png in pngs:
        remove_white_borders(png)
    print("\n✅ Done!")

if __name__ == "__main__":
    main()

