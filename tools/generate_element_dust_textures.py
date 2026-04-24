from __future__ import annotations

import argparse
from pathlib import Path
from typing import Iterable

from PIL import Image


def luminance(rgb: tuple[int, int, int]) -> float:
    r, g, b = rgb
    return 0.2126 * r + 0.7152 * g + 0.0722 * b


def clamp_channel(value: float) -> int:
    return max(0, min(255, round(value)))


def blend(a: tuple[int, int, int], b: tuple[int, int, int], ratio: float) -> tuple[int, int, int]:
    return tuple(clamp_channel(a[i] * (1.0 - ratio) + b[i] * ratio) for i in range(3))


def average_rgb(pixels: Iterable[tuple[int, int, int]]) -> tuple[int, int, int]:
    pixels = list(pixels)
    if not pixels:
        return (255, 255, 255)
    total = [0, 0, 0]
    for r, g, b in pixels:
        total[0] += r
        total[1] += g
        total[2] += b
    return tuple(round(channel / len(pixels)) for channel in total)


def get_opaque_pixels(image: Image.Image) -> list[tuple[int, int, int]]:
    pixels: list[tuple[int, int, int]] = []
    for y in range(image.height):
        for x in range(image.width):
            r, g, b, a = image.getpixel((x, y))
            if a > 0:
                pixels.append((r, g, b))
    return pixels


def resize_reference(reference: Image.Image, target_size: tuple[int, int]) -> Image.Image:
    if reference.size == target_size:
        return reference.copy()
    return reference.resize(target_size, Image.NEAREST)


def parse_size(value: str) -> tuple[int, int]:
    normalized = value.lower().replace(" ", "")
    if "x" not in normalized:
        raise argparse.ArgumentTypeError("size must look like WIDTHxHEIGHT, for example 16x16")
    width_text, height_text = normalized.split("x", 1)
    try:
        width = int(width_text)
        height = int(height_text)
    except ValueError as exc:
        raise argparse.ArgumentTypeError("size must look like WIDTHxHEIGHT, for example 16x16") from exc
    if width <= 0 or height <= 0:
        raise argparse.ArgumentTypeError("size must be positive")
    return (width, height)


def extract_reference_levels(reference: Image.Image) -> list[tuple[int, int, int]]:
    colors: set[tuple[int, int, int]] = set()
    for y in range(reference.height):
        for x in range(reference.width):
            r, g, b, a = reference.getpixel((x, y))
            if a > 0:
                colors.add((r, g, b))
    return sorted(colors, key=luminance)


def build_palette(source: Image.Image) -> list[tuple[int, int, int]]:
    opaque = get_opaque_pixels(source)
    if not opaque:
        return [
            (96, 96, 96),
            (128, 128, 128),
            (160, 160, 160),
            (196, 196, 196),
            (232, 232, 232),
        ]

    opaque.sort(key=luminance)
    count = len(opaque)

    def band(start: float, end: float) -> list[tuple[int, int, int]]:
        left = max(0, min(count - 1, int(count * start)))
        right = max(left + 1, min(count, int(count * end)))
        return opaque[left:right]

    darkest = average_rgb(band(0.00, 0.12))
    dark = average_rgb(band(0.10, 0.32))
    mid = average_rgb(band(0.28, 0.72))
    light = average_rgb(band(0.68, 0.92))
    lightest = average_rgb(band(0.88, 1.00))

    palette = [
        blend(darkest, (0, 0, 0), 0.10),
        blend(dark, darkest, 0.15),
        mid,
        blend(light, lightest, 0.30),
        blend(lightest, (255, 255, 255), 0.18),
    ]

    # Keep very dark elements readable and bright elements from washing out.
    mid_luma = luminance(palette[2])
    if mid_luma < 48:
        palette = [blend(color, (255, 255, 255), 0.10) for color in palette]
    elif mid_luma > 220:
        palette = [blend(color, (0, 0, 0), 0.10) for color in palette]

    return palette


def recolor_reference(reference: Image.Image, palette: list[tuple[int, int, int]]) -> Image.Image:
    output = Image.new("RGBA", reference.size, (0, 0, 0, 0))
    ref_levels = extract_reference_levels(reference)
    color_map = {
        ref_levels[min(index, len(ref_levels) - 1)]: palette[min(index, len(palette) - 1)]
        for index in range(len(ref_levels))
    }

    for y in range(reference.height):
        for x in range(reference.width):
            r, g, b, a = reference.getpixel((x, y))
            if a == 0:
                continue
            mapped = color_map[(r, g, b)]
            output.putpixel((x, y), (*mapped, a))

    return output


def iter_elements(items_root: Path) -> list[str]:
    elements: list[str] = []
    for child in sorted(items_root.iterdir()):
        if not child.is_dir():
            continue
        ingot = child / f"{child.name}_ingot.png"
        dust = child / f"{child.name}_dust.png"
        if ingot.exists() and dust.exists():
            elements.append(child.name)
    return elements


def generate_dust_texture(
    reference_path: Path,
    items_root: Path,
    element: str,
    output_root: Path | None,
    target_size: tuple[int, int] | None,
) -> Path:
    element_dir = items_root / element
    ingot_path = element_dir / f"{element}_ingot.png"
    dust_path = (output_root / element / f"{element}_dust.png") if output_root else (element_dir / f"{element}_dust.png")
    dust_path.parent.mkdir(parents=True, exist_ok=True)

    source = Image.open(ingot_path).convert("RGBA")
    reference_image = Image.open(reference_path).convert("RGBA")
    effective_target_size = target_size or reference_image.size
    reference = resize_reference(reference_image, effective_target_size)
    palette = build_palette(source)
    recolored = recolor_reference(reference, palette)
    recolored.save(dust_path)
    return dust_path


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Generate pile-style dust textures from a reference sprite.")
    parser.add_argument("--reference", type=Path, required=True, help="Path to the reference dust sprite.")
    parser.add_argument("--items-root", type=Path, required=True, help="Root textures/items directory.")
    parser.add_argument("--output-root", type=Path, help="Optional alternate output root for previews.")
    parser.add_argument("--elements", nargs="*", help="Specific element ids to regenerate.")
    parser.add_argument("--target-size", type=parse_size, help="Optional output size, for example 16x16.")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    elements = args.elements or iter_elements(args.items_root)
    generated: list[Path] = []
    for element in elements:
        generated.append(
            generate_dust_texture(
                args.reference,
                args.items_root,
                element,
                args.output_root,
                args.target_size,
            )
        )

    print(f"generated {len(generated)} dust textures")
    for path in generated[:20]:
        print(path.as_posix())
    if len(generated) > 20:
        print("...")


if __name__ == "__main__":
    main()
