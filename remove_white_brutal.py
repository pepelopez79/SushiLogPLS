#!/usr/bin/env python3
"""
Brutal white border removal - removes ALL semi-transparent pixels
and erodes any light-colored edge pixels until only solid dark content remains.
"""

from PIL import Image, ImageFilter
import numpy as np
import os
import glob

DRAWABLE_DIR = "app/src/main/res/drawable"
SKIP_NAMES = {"ic_launcher"}


def brutal_remove_white(img_path):
    """Aggressively remove all white/light fringe from PNG edges."""
    img = Image.open(img_path).convert("RGBA")
    data = np.array(img, dtype=np.float64)

    r, g, b, a = data[:, :, 0], data[:, :, 1], data[:, :, 2], data[:, :, 3]
    changed = False

    # Pass 1: Remove ALL semi-transparent pixels (alpha < 255)
    semi_mask = (a > 0) & (a < 255)
    if np.any(semi_mask):
        data[semi_mask, :] = [0, 0, 0, 0]
        changed = True

    # Pass 2: Remove fully opaque but very light pixels that are on the edge
    # (adjacent to transparent pixels)
    for iteration in range(5):  # Multiple erosion passes
        a = data[:, :, 3]
        r, g, b = data[:, :, 0], data[:, :, 1], data[:, :, 2]

        # Find opaque pixels
        opaque = a == 255
        # Find transparent pixels
        transparent = a == 0

        # Create padded transparent array for neighbor checking
        pad_trans = np.pad(transparent, 1, mode='constant', constant_values=True)

        # Check if any neighbor is transparent (edge pixels)
        has_trans_neighbor = (
            pad_trans[:-2, 1:-1] |  # top
            pad_trans[2:, 1:-1] |   # bottom
            pad_trans[1:-1, :-2] |  # left
            pad_trans[1:-1, 2:] |   # right
            pad_trans[:-2, :-2] |   # top-left
            pad_trans[:-2, 2:] |    # top-right
            pad_trans[2:, :-2] |    # bottom-left
            pad_trans[2:, 2:]       # bottom-right
        )

        edge_opaque = opaque & has_trans_neighbor
        brightness = (r + g + b) / 3.0

        # Remove edge pixels that are light (brightness > 200)
        # This catches white, near-white, and light gray fringe
        light_edge = edge_opaque & (brightness > 200)

        if not np.any(light_edge):
            break

        data[light_edge, :] = [0, 0, 0, 0]
        changed = True

    if changed:
        result = Image.fromarray(data.astype(np.uint8), "RGBA")
        result.save(img_path, "PNG")
    return changed


def main():
    png_files = glob.glob(os.path.join(DRAWABLE_DIR, "*.png"))

    processed = 0
    skipped = 0

    for png_path in sorted(png_files):
        basename = os.path.basename(png_path)
        name = os.path.splitext(basename)[0]

        if any(s in name for s in SKIP_NAMES):
            skipped += 1
            continue

        try:
            changed = brutal_remove_white(png_path)
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

