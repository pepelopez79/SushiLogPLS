#!/usr/bin/env python3
"""
Add a consistent thin white outline/border around PNG icons.
This creates a uniform white stroke around the opaque content of each icon.
"""

from PIL import Image, ImageFilter
import numpy as np
import os
import glob

DRAWABLE_DIR = "app/src/main/res/drawable"

# Icons to skip (logos, flags, launcher icons)
SKIP_NAMES = {"ic_launcher", "logo", "logo2", "logo3", "english", "spanish", "french", "italian"}

BORDER_WIDTH = 3  # pixels of white border


def add_white_outline(img_path):
    """Add a white outline around the opaque content of a PNG."""
    img = Image.open(img_path).convert("RGBA")
    data = np.array(img)

    # Extract alpha channel
    alpha = data[:, :, 3]

    # Create a mask of "solid" pixels (alpha > 20)
    mask = (alpha > 20).astype(np.uint8) * 255
    mask_img = Image.fromarray(mask, mode="L")

    # Dilate the mask to create the outline area
    dilated = mask_img
    for _ in range(BORDER_WIDTH):
        dilated = dilated.filter(ImageFilter.MaxFilter(3))

    dilated_arr = np.array(dilated)

    # The outline is where dilated is solid but original mask is not
    outline_mask = (dilated_arr > 127) & (alpha <= 20)

    # Create new image: start with original
    result = data.copy()

    # Add white pixels in the outline area
    result[outline_mask, 0] = 255  # R
    result[outline_mask, 1] = 255  # G
    result[outline_mask, 2] = 255  # B
    result[outline_mask, 3] = 255  # A (fully opaque)

    out_img = Image.fromarray(result, "RGBA")
    out_img.save(img_path, "PNG")
    return True


def main():
    png_files = glob.glob(os.path.join(DRAWABLE_DIR, "*.png"))

    processed = 0
    skipped = 0

    for png_path in sorted(png_files):
        basename = os.path.basename(png_path)
        name = os.path.splitext(basename)[0]

        if name in SKIP_NAMES:
            print(f"  SKIP: {basename}")
            skipped += 1
            continue

        try:
            add_white_outline(png_path)
            print(f"  ✓ {basename}")
            processed += 1
        except Exception as e:
            print(f"  ✗ {basename}: {e}")
            skipped += 1

    print(f"\nDone! Processed: {processed}, Skipped: {skipped}")


if __name__ == "__main__":
    main()

