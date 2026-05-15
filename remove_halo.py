#!/usr/bin/env python3
"""
Remove white halo/fringe from PNG icons.
Iteratively removes any white-ish pixel that is adjacent to a transparent pixel.
Runs multiple passes until no more pixels are removed.
"""

import os
import glob
from PIL import Image

DRAWABLE_DIR = "app/src/main/res/drawable"

# Only process sushi piece images (not UI icons like back, settings, etc.)
SUSHI_PIECES = [
    "nigiri.png", "nigiri2.png", "nigiri3.png", "nigiri4.png",
    "maki.png", "maki2.png",
    "sashimi.png",
    "uramaki.png", "uramaki2.png",
    "temaki.png",
    "gunkan.png", "gunkan2.png",
    "onigiri.png",
    "gyoza.png",
    "edamame.png",
    "takoyaki.png",
    "shrimp.png",
    "mochis.png",
    "bowl.png", "bowl2.png", "bowl3.png",
    "all.png",
    "logo.png",
    "salmon.png",
    "rice.png",
    "soja.png",
    "wasabi.png",
    "kcal.png",
]

THRESHOLD = 180  # More aggressive: R,G,B all >= this = "white-ish"
MAX_PASSES = 5   # Multiple passes to erode the halo

def has_transparent_neighbor(pixels, x, y, w, h):
    """Check if any of the 8 neighbors is fully transparent."""
    for dx in (-1, 0, 1):
        for dy in (-1, 0, 1):
            if dx == 0 and dy == 0:
                continue
            nx, ny = x + dx, y + dy
            if 0 <= nx < w and 0 <= ny < h:
                if pixels[nx, ny][3] == 0:
                    return True
    return False

def is_whiteish(pixel, threshold):
    r, g, b, a = pixel
    return a > 0 and r >= threshold and g >= threshold and b >= threshold

def remove_halo(img_path):
    img = Image.open(img_path).convert("RGBA")
    pixels = img.load()
    w, h = img.size
    total_changed = 0

    for pass_num in range(MAX_PASSES):
        to_clear = []
        for y in range(h):
            for x in range(w):
                if is_whiteish(pixels[x, y], THRESHOLD):
                    if has_transparent_neighbor(pixels, x, y, w, h):
                        to_clear.append((x, y))

        if not to_clear:
            break

        for (x, y) in to_clear:
            pixels[x, y] = (0, 0, 0, 0)
        total_changed += len(to_clear)
        print(f"    Pass {pass_num + 1}: {len(to_clear)} pixels")

    if total_changed > 0:
        img.save(img_path)
        print(f"  ✅ {os.path.basename(img_path)}: {total_changed} total pixels removed")
    else:
        print(f"  ⏭️  {os.path.basename(img_path)}: clean")

    return total_changed

def main():
    total = 0
    print(f"Processing sushi piece PNGs with halo removal (threshold={THRESHOLD}, passes={MAX_PASSES})...\n")
    for name in sorted(SUSHI_PIECES):
        path = os.path.join(DRAWABLE_DIR, name)
        if os.path.exists(path):
            total += remove_halo(path)
        else:
            print(f"  ⚠️  {name}: not found")
    print(f"\n✅ Done! {total} total pixels removed across all files.")

if __name__ == "__main__":
    main()

