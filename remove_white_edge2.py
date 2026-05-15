#!/usr/bin/env python3
"""
Remove white fringe/halo from PNG icons - aggressive pass.
More aggressive thresholds to catch remaining white edge pixels.
"""

from PIL import Image
import numpy as np
import os
import glob

DRAWABLE_DIR = "app/src/main/res/drawable"

def remove_white_fringe(img_path):
    """Remove white fringe from a PNG image with transparency - aggressive."""
    img = Image.open(img_path).convert("RGBA")
    data = np.array(img, dtype=np.float64)

    r, g, b, a = data[:,:,0], data[:,:,1], data[:,:,2], data[:,:,3]

    # Find semi-transparent pixels (alpha between 1 and 254)
    semi_mask = (a > 0) & (a < 255)

    if not np.any(semi_mask):
        return False

    alpha_norm = a / 255.0
    where_semi = np.where(semi_mask)
    changed = False

    for idx in range(len(where_semi[0])):
        y, x = where_semi[0][idx], where_semi[1][idx]
        alpha_val = alpha_norm[y, x]
        brightness = (r[y,x] + g[y,x] + b[y,x]) / 3.0

        if alpha_val < 0.02:
            data[y, x] = [0, 0, 0, 0]
            changed = True
            continue

        # More aggressive thresholds
        if brightness > 150 and alpha_val < 0.6:
            data[y, x] = [0, 0, 0, 0]
            changed = True
        elif brightness > 120 and alpha_val < 0.4:
            data[y, x] = [0, 0, 0, 0]
            changed = True
        elif alpha_val < 0.25 and brightness > 100:
            data[y, x] = [0, 0, 0, 0]
            changed = True
        elif alpha_val < 0.35:
            # Any very low alpha pixel near edge - remove
            data[y, x] = [0, 0, 0, 0]
            changed = True

    if changed:
        result = Image.fromarray(data.astype(np.uint8), "RGBA")
        result.save(img_path, "PNG")
    return changed

def main():
    png_files = glob.glob(os.path.join(DRAWABLE_DIR, "*.png"))
    skip = {"ic_launcher"}

    processed = 0
    skipped = 0

    for png_path in sorted(png_files):
        basename = os.path.basename(png_path)
        name = os.path.splitext(basename)[0]

        if any(s in name for s in skip):
            skipped += 1
            continue

        try:
            changed = remove_white_fringe(png_path)
            if changed:
                print(f"  ✓ {basename}")
                processed += 1
            else:
                print(f"  - {basename}")
                skipped += 1
        except Exception as e:
            print(f"  ✗ {basename}: {e}")
            skipped += 1

    print(f"\nDone! Processed: {processed}, Skipped: {skipped}")

if __name__ == "__main__":
    main()

