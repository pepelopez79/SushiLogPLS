#!/usr/bin/env python3
"""
Remove white fringe/halo from PNG icons with transparency.
For each pixel that is semi-transparent near the edge, removes the white color
contamination while preserving the alpha channel.
"""

from PIL import Image
import numpy as np
import os
import glob

DRAWABLE_DIR = "app/src/main/res/drawable"

def remove_white_fringe(img_path):
    """Remove white fringe from a PNG image with transparency."""
    img = Image.open(img_path).convert("RGBA")
    data = np.array(img, dtype=np.float64)

    r, g, b, a = data[:,:,0], data[:,:,1], data[:,:,2], data[:,:,3]

    # Find semi-transparent pixels (alpha between 1 and 254)
    semi_mask = (a > 0) & (a < 255)

    if not np.any(semi_mask):
        return False  # No semi-transparent pixels, skip

    # For semi-transparent pixels, undo premultiplied white blending
    # The idea: if the original was composited over white, the stored color is:
    #   stored = original * (alpha/255) + white * (1 - alpha/255)
    # We want to recover 'original':
    #   original = (stored - white * (1 - alpha/255)) / (alpha/255)
    # But simpler approach: just check if the pixel looks "whitish" and darken it

    alpha_norm = a / 255.0

    # For semi-transparent pixels that are whitish (high R, G, B relative to alpha),
    # try to decontaminate the white
    where_semi = np.where(semi_mask)

    for idx in range(len(where_semi[0])):
        y, x = where_semi[0][idx], where_semi[1][idx]
        alpha_val = alpha_norm[y, x]

        if alpha_val < 0.01:
            # Nearly fully transparent - just make it fully transparent
            data[y, x] = [0, 0, 0, 0]
            continue

        # Check if this pixel is "too bright" for its alpha level
        # indicating white fringe contamination
        brightness = (r[y,x] + g[y,x] + b[y,x]) / 3.0

        if brightness > 200 and alpha_val < 0.5:
            # Very bright + very transparent = white fringe, remove it
            data[y, x] = [0, 0, 0, 0]
        elif brightness > 180 and alpha_val < 0.3:
            # Bright + mostly transparent = white fringe
            data[y, x] = [0, 0, 0, 0]
        elif alpha_val < 0.15 and brightness > 150:
            # Nearly transparent + somewhat bright = fringe
            data[y, x] = [0, 0, 0, 0]

    result = Image.fromarray(data.astype(np.uint8), "RGBA")
    result.save(img_path, "PNG")
    return True

def main():
    png_files = glob.glob(os.path.join(DRAWABLE_DIR, "*.png"))

    # Skip launcher logos (they should keep their backgrounds)
    skip = {"ic_launcher"}

    processed = 0
    skipped = 0

    for png_path in sorted(png_files):
        basename = os.path.basename(png_path)
        name = os.path.splitext(basename)[0]

        if any(s in name for s in skip):
            print(f"  SKIP: {basename}")
            skipped += 1
            continue

        try:
            changed = remove_white_fringe(png_path)
            if changed:
                print(f"  ✓ Processed: {basename}")
                processed += 1
            else:
                print(f"  - No fringe: {basename}")
                skipped += 1
        except Exception as e:
            print(f"  ✗ Error {basename}: {e}")
            skipped += 1

    print(f"\nDone! Processed: {processed}, Skipped: {skipped}")

if __name__ == "__main__":
    main()

