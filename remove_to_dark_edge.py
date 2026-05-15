#!/usr/bin/env python3
"""
Remove ALL non-dark border pixels from sushi PNGs.
Since all icons have a dark/black outline, any pixel near transparency
that is NOT dark should be removed. Iterates until only dark border remains.
A pixel is "dark" if R,G,B are all < 100.
"""

import os
import glob
from PIL import Image

DRAWABLE_DIR = "app/src/main/res/drawable"

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

DARK_THRESHOLD = 100  # R,G,B all < this = "dark" (part of the outline)
MAX_PASSES = 20       # enough passes to erode everything non-dark

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
                # Edge of image counts as transparent
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
                    continue  # already transparent
                if is_dark(p):
                    continue  # dark pixel = outline, keep it
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
    print(f"Eroding non-dark border pixels until dark outline is reached...\n")
    for name in sorted(SUSHI_PIECES):
        path = os.path.join(DRAWABLE_DIR, name)
        if os.path.exists(path):
            total += remove_non_dark_border(path)
        else:
            print(f"  ⚠️  {name}: not found")
    print(f"\n✅ Done! {total} total pixels removed.")

if __name__ == "__main__":
    main()

