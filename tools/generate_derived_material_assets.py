from __future__ import annotations

import argparse
import json
import re
import shutil
import zipfile
from pathlib import Path
from typing import Iterable

from PIL import Image


MOD_ID = "redstone_chemical_elements"
VARIANTS_PER_GROUP = 16
MANIFEST_ENTRY = f"assets/{MOD_ID}/derived/manifest.json"
DERIVED_TEXTURE_PREFIX = f"assets/{MOD_ID}/textures/items/derived/"
DERIVED_MODEL_PREFIX = f"assets/{MOD_ID}/models/item/derived/"
SINGULARITY_TEXTURE_PREFIX = f"assets/{MOD_ID}/textures/items/singularity/"
SINGULARITY_MODEL_PREFIX = f"assets/{MOD_ID}/models/item/singularity/"
LANG_PREFIX = f"assets/{MOD_ID}/lang/"
ANIMATION_META = '{\n  "animation": {\n    "frametime": 2\n  }\n}\n'
STONE_TEXTURE_RELATIVE_CANDIDATES = (
    Path("build") / "rfg" / "minecraft-src" / "resources" / "assets" / "minecraft" / "textures" / "blocks" / "stone.png",
    Path("build") / "resources" / "patchedMc" / "assets" / "minecraft" / "textures" / "blocks" / "stone.png",
)


def clamp_channel(value: float) -> int:
    return max(0, min(255, round(value)))


def luminance(rgb: tuple[int, int, int]) -> float:
    r, g, b = rgb
    return 0.2126 * r + 0.7152 * g + 0.0722 * b


def blend(a: tuple[int, int, int], b: tuple[int, int, int], ratio: float) -> tuple[int, int, int]:
    return tuple(clamp_channel(a[index] * (1.0 - ratio) + b[index] * ratio) for index in range(3))


def average_rgb(pixels: Iterable[tuple[int, int, int]]) -> tuple[int, int, int]:
    pixels = list(pixels)
    if not pixels:
        return (255, 255, 255)

    total = [0, 0, 0]
    for red, green, blue in pixels:
        total[0] += red
        total[1] += green
        total[2] += blue
    return tuple(round(channel / len(pixels)) for channel in total)


def get_opaque_pixels(image: Image.Image) -> list[tuple[int, int, int]]:
    pixels: list[tuple[int, int, int]] = []
    for y in range(image.height):
        for x in range(image.width):
            red, green, blue, alpha = image.getpixel((x, y))
            if alpha > 0:
                pixels.append((red, green, blue))
    return pixels


def sample_average_color(image: Image.Image, skip_outer_border: bool) -> tuple[int, int, int]:
    pixels: list[tuple[int, int, int]] = []
    for y in range(image.height):
        for x in range(image.width):
            if skip_outer_border and (x == 0 or y == 0 or x == image.width - 1 or y == image.height - 1):
                continue
            red, green, blue, alpha = image.getpixel((x, y))
            if alpha >= 32:
                pixels.append((red, green, blue))

    if not pixels and skip_outer_border:
        return sample_average_color(image, False)
    if not pixels:
        return (255, 255, 255)
    return average_rgb(pixels)


def extract_reference_levels(reference: Image.Image) -> list[tuple[int, int, int]]:
    colors: set[tuple[int, int, int]] = set()
    for y in range(reference.height):
        for x in range(reference.width):
            red, green, blue, alpha = reference.getpixel((x, y))
            if alpha > 0:
                colors.add((red, green, blue))
    return sorted(colors, key=luminance)


def build_palette(source: Image.Image) -> list[tuple[int, int, int]]:
    opaque = get_opaque_pixels(source)
    if not opaque:
        return build_palette_from_color((160, 160, 160))

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

    mid_luma = luminance(palette[2])
    if mid_luma < 48:
        palette = [blend(color, (255, 255, 255), 0.10) for color in palette]
    elif mid_luma > 220:
        palette = [blend(color, (0, 0, 0), 0.10) for color in palette]

    return palette


def build_palette_from_color(color: tuple[int, int, int]) -> list[tuple[int, int, int]]:
    palette = [
        blend(color, (0, 0, 0), 0.42),
        blend(color, (0, 0, 0), 0.24),
        color,
        blend(color, (255, 255, 255), 0.18),
        blend(color, (255, 255, 255), 0.34),
    ]

    mid_luma = luminance(palette[2])
    if mid_luma < 48:
        palette = [blend(entry, (255, 255, 255), 0.10) for entry in palette]
    elif mid_luma > 220:
        palette = [blend(entry, (0, 0, 0), 0.10) for entry in palette]

    return palette


def interpolate_palette(palette: list[tuple[int, int, int]], ratio: float) -> tuple[int, int, int]:
    if ratio <= 0.0:
        return palette[0]
    if ratio >= 1.0:
        return palette[-1]

    scaled = ratio * (len(palette) - 1)
    index = int(scaled)
    next_index = min(index + 1, len(palette) - 1)
    local_ratio = scaled - index
    return blend(palette[index], palette[next_index], local_ratio)


def recolor_reference(reference: Image.Image, palette: list[tuple[int, int, int]]) -> Image.Image:
    output = Image.new("RGBA", reference.size, (0, 0, 0, 0))
    luminance_values: list[float] = []
    for y in range(reference.height):
        for x in range(reference.width):
            red, green, blue, alpha = reference.getpixel((x, y))
            if alpha > 0:
                luminance_values.append(luminance((red, green, blue)))

    if not luminance_values:
        return output

    minimum = min(luminance_values)
    maximum = max(luminance_values)
    spread = max(1.0, maximum - minimum)

    for y in range(reference.height):
        for x in range(reference.width):
            red, green, blue, alpha = reference.getpixel((x, y))
            if alpha == 0:
                continue
            tone = (luminance((red, green, blue)) - minimum) / spread
            mapped = interpolate_palette(palette, tone)
            output.putpixel((x, y), (*mapped, alpha))

    return output


def recolor_ore_reference(
    reference: Image.Image,
    palette: list[tuple[int, int, int]],
    stone_texture: Image.Image,
    threshold: int = 42,
) -> Image.Image:
    output = Image.new("RGBA", reference.size, (0, 0, 0, 0))
    ore_pixels: list[float] = []

    for y in range(reference.height):
        for x in range(reference.width):
            red, green, blue, alpha = reference.getpixel((x, y))
            if alpha == 0:
                continue
            stone_red, stone_green, stone_blue, _ = stone_texture.getpixel((x % stone_texture.width, y % stone_texture.height))
            distance = abs(red - stone_red) + abs(green - stone_green) + abs(blue - stone_blue)
            if distance > threshold:
                ore_pixels.append(luminance((red, green, blue)))

    if not ore_pixels:
        return recolor_reference(reference, palette)

    minimum = min(ore_pixels)
    maximum = max(ore_pixels)
    spread = max(1.0, maximum - minimum)

    for y in range(reference.height):
        for x in range(reference.width):
            red, green, blue, alpha = reference.getpixel((x, y))
            if alpha == 0:
                continue

            stone_red, stone_green, stone_blue, stone_alpha = stone_texture.getpixel((x % stone_texture.width, y % stone_texture.height))
            distance = abs(red - stone_red) + abs(green - stone_green) + abs(blue - stone_blue)
            if distance <= threshold:
                output.putpixel((x, y), (stone_red, stone_green, stone_blue, stone_alpha))
                continue

            tone = (luminance((red, green, blue)) - minimum) / spread
            mapped = interpolate_palette(palette, tone)
            output.putpixel((x, y), (*mapped, alpha))

    return output


def parse_hex_color(value: str) -> tuple[int, int, int]:
    normalized = value.strip().lstrip("#")
    if len(normalized) != 6:
        raise ValueError(f"invalid color: {value}")
    return tuple(int(normalized[index:index + 2], 16) for index in range(0, 6, 2))


def format_hex_color(value: tuple[int, int, int]) -> str:
    return "#" + "".join(f"{channel:02X}" for channel in value)


def color_distance(a: tuple[int, int, int], b: tuple[int, int, int]) -> int:
    return sum((a[index] - b[index]) ** 2 for index in range(3))


def boost_saturation(color: tuple[int, int, int], factor: float) -> tuple[int, int, int]:
    red, green, blue = [channel / 255.0 for channel in color]
    maximum = max(red, green, blue)
    minimum = min(red, green, blue)
    delta = maximum - minimum
    if delta == 0.0:
        return color

    average = (maximum + minimum) / 2.0
    saturation = delta / (1.0 - abs(2.0 * average - 1.0))
    saturation = max(0.0, min(1.0, saturation * factor))

    if maximum == red:
        hue = ((green - blue) / delta) % 6.0
    elif maximum == green:
        hue = ((blue - red) / delta) + 2.0
    else:
        hue = ((red - green) / delta) + 4.0
    hue /= 6.0

    chroma = (1.0 - abs(2.0 * average - 1.0)) * saturation
    hue_section = hue * 6.0
    secondary = chroma * (1.0 - abs(hue_section % 2.0 - 1.0))
    match = average - chroma / 2.0

    if 0.0 <= hue_section < 1.0:
        raw = (chroma, secondary, 0.0)
    elif 1.0 <= hue_section < 2.0:
        raw = (secondary, chroma, 0.0)
    elif 2.0 <= hue_section < 3.0:
        raw = (0.0, chroma, secondary)
    elif 3.0 <= hue_section < 4.0:
        raw = (0.0, secondary, chroma)
    elif 4.0 <= hue_section < 5.0:
        raw = (secondary, 0.0, chroma)
    else:
        raw = (chroma, 0.0, secondary)

    return tuple(clamp_channel((channel + match) * 255.0) for channel in raw)


def shade_for_fluid(color: tuple[int, int, int], style: str) -> tuple[int, int, int]:
    lowered = normalize_fluid_style(style)
    shaded = boost_saturation(color, 1.12 if lowered == "water" else 1.24)
    target_luma = 56.0 if lowered == "water" else 44.0
    current_luma = max(1.0, luminance(shaded))
    if current_luma > target_luma:
        ratio = min(0.82, max(0.0, 1.0 - target_luma / current_luma))
        shaded = blend(shaded, (0, 0, 0), ratio)
    shaded = blend(shaded, (0, 0, 0), 0.08 if lowered == "water" else 0.16)
    if luminance(shaded) < 26:
        shaded = blend(shaded, color, 0.10)
    return shaded


def normalize_fluid_style(style: str | None) -> str:
    lowered = (style or "").strip().lower()
    return "water" if lowered == "water" else "lava"


def title_case_slug(slug: str) -> str:
    return " ".join(part.capitalize() for part in slug.split("_") if part)


def material_display_name(material: dict, locale: str) -> str:
    if locale == "zh_cn":
        return str(material.get("chineseName", "")).strip() or str(material.get("slug", "")).strip()
    return str(material.get("englishName", "")).strip() or title_case_slug(str(material.get("slug", "")).strip())


def derived_item_display_name(material: dict, entry_name: str, locale: str) -> str:
    slug = str(material["slug"]).strip()
    base_name = material_display_name(material, locale)
    if entry_name == f"crushed_raw_{slug}":
        return f"粉碎的粗{base_name}" if locale == "zh_cn" else f"Crushed Raw {base_name}"
    if entry_name == f"raw_{slug}":
        return f"粗{base_name}" if locale == "zh_cn" else f"Raw {base_name}"

    suffix_map = {
        "_dust": ("粉末", "Dust"),
        "_gear": ("齿轮", "Gear"),
        "_ingot": ("锭", "Ingot"),
        "_nugget": ("粒", "Nugget"),
        "_plate": ("板", "Plate"),
        "_rod": ("杆", "Rod"),
        "_wire": ("线", "Wire"),
    }
    for suffix, (zh_suffix, en_suffix) in suffix_map.items():
        if entry_name == f"{slug}{suffix}":
            return f"{base_name}{zh_suffix}" if locale == "zh_cn" else f"{base_name} {en_suffix}"

    return str(material.get("chineseName" if locale == "zh_cn" else "englishName", "")).strip() or entry_name


def derived_block_display_name(material: dict, entry_name: str, locale: str) -> str:
    slug = str(material["slug"]).strip()
    base_name = material_display_name(material, locale)
    if entry_name == f"raw_{slug}_block":
        return f"粗{base_name}块" if locale == "zh_cn" else f"Raw {base_name} Block"
    if entry_name == f"{slug}_ore":
        return f"{base_name}矿石" if locale == "zh_cn" else f"{base_name} Ore"
    if entry_name == f"{slug}_block":
        return f"{base_name}块" if locale == "zh_cn" else f"{base_name} Block"
    return str(material.get("chineseName" if locale == "zh_cn" else "englishName", "")).strip() or entry_name


def fluid_display_name(material: dict, locale: str) -> str:
    base_name = material_display_name(material, locale)
    style = normalize_fluid_style(str(material.get("fluidStyle", "")))
    if locale == "zh_cn":
        return ("液态" if style == "water" else "熔融") + base_name
    return ("Liquid " if style == "water" else "Molten ") + base_name


def singularity_display_name(material: dict, locale: str) -> str:
    base_name = material_display_name(material, locale)
    return f"{base_name}奇点" if locale == "zh_cn" else f"{base_name} Singularity"


def refresh_manifest_display_names(material: dict) -> None:
    for entry in material.get("items", []):
        entry_name = str(entry.get("name", "")).strip()
        if not entry_name:
            continue
        entry["englishName"] = derived_item_display_name(material, entry_name, "en_us")
        entry["chineseName"] = derived_item_display_name(material, entry_name, "zh_cn")

    for entry in material.get("blocks", []):
        entry_name = str(entry.get("name", "")).strip()
        if not entry_name:
            continue
        entry["englishName"] = derived_block_display_name(material, entry_name, "en_us")
        entry["chineseName"] = derived_block_display_name(material, entry_name, "zh_cn")


def build_lang_overrides(manifest: dict, locale: str) -> list[str]:
    lines: list[str] = []
    for material in manifest["materials"]:
        slug = str(material["slug"]).strip()
        if not slug:
            continue

        for entry in material["items"]:
            entry_name = str(entry["name"]).strip()
            lines.append(
                f"item.{MOD_ID}.derived.{slug}.{entry_name}.name="
                f"{derived_item_display_name(material, entry_name, locale)}"
            )

        for entry in material["blocks"]:
            entry_name = str(entry["name"]).strip()
            localized_name = derived_block_display_name(material, entry_name, locale)
            lines.append(f"tile.{MOD_ID}.derived.{slug}.{entry_name}.name={localized_name}")
            lines.append(f"{MOD_ID}.derived.{slug}.{entry_name}.name={localized_name}")

        lines.append(
            f"item.{MOD_ID}.singularity.{slug}_singularity.name={singularity_display_name(material, locale)}"
        )
        lines.append(f"tile.{MOD_ID}.derived.{slug}.{slug}_fluid.name={fluid_display_name(material, locale)}")

    return sorted(set(lines))


def merge_lang_overrides(path: Path, lines: list[str]) -> None:
    if not lines:
        return

    overrides: dict[str, str] = {}
    for line in lines:
        if "=" not in line:
            continue
        key, value = line.split("=", 1)
        overrides[key] = value

    output_lines: list[str] = []
    seen_override_keys: set[str] = set()
    if path.exists():
        for raw_line in path.read_text(encoding="utf-8").splitlines():
            if "=" not in raw_line:
                output_lines.append(raw_line)
                continue

            key, _ = raw_line.split("=", 1)
            if key in overrides:
                if key in seen_override_keys:
                    continue
                output_lines.append(f"{key}={overrides[key]}")
                seen_override_keys.add(key)
            else:
                output_lines.append(raw_line)

    missing_keys = [key for key in overrides if key not in seen_override_keys]
    if missing_keys and output_lines and output_lines[-1] != "":
        output_lines.append("")
    for key in missing_keys:
        output_lines.append(f"{key}={overrides[key]}")

    write_text(path, "\n".join(output_lines).rstrip("\n") + "\n")


def derive_palette_from_reference(reference: Image.Image, target_color: tuple[int, int, int]) -> list[tuple[int, int, int]]:
    source_palette = build_palette(reference)
    generic_palette = build_palette_from_color(target_color)
    source_mid = source_palette[2]
    derived: list[tuple[int, int, int]] = []

    for index, source_color in enumerate(source_palette):
        shifted = tuple(
            clamp_channel(target_color[channel] + (source_color[channel] - source_mid[channel]))
            for channel in range(3)
        )
        derived.append(blend(shifted, generic_palette[index], 0.35))

    derived[2] = blend(target_color, generic_palette[2], 0.15)
    return derived


def write_text(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8", newline="\n")


def clear_directory(path: Path) -> None:
    if path.exists():
        shutil.rmtree(path)
    path.mkdir(parents=True, exist_ok=True)


def copy_tree(source_root: Path, target_root: Path) -> None:
    if not source_root.exists():
        return

    for source in source_root.rglob("*"):
        if source.is_dir():
            continue
        target = target_root / source.relative_to(source_root)
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, target)


def extract_zip_prefix(archive: zipfile.ZipFile, prefix: str, target_root: Path) -> int:
    count = 0
    for name in archive.namelist():
        if not name.startswith(prefix) or name.endswith("/"):
            continue
        relative = Path(name[len(prefix):])
        target = target_root / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        with archive.open(name) as source_stream, target.open("wb") as target_stream:
            shutil.copyfileobj(source_stream, target_stream)
        count += 1
    return count


def load_stone_texture(repo_root: Path) -> Image.Image:
    for relative in STONE_TEXTURE_RELATIVE_CANDIDATES:
        candidate = repo_root / relative
        if candidate.exists():
            return Image.open(candidate).convert("RGBA").resize((16, 16), Image.NEAREST)
    return Image.new("RGBA", (16, 16), (125, 125, 125, 255))


def read_zip_image(archive: zipfile.ZipFile, entry_name: str) -> Image.Image:
    with archive.open(entry_name) as handle:
        return Image.open(handle).convert("RGBA")


def resolve_texture_path(resource_root: Path, texture_type: str, material: str, name: str) -> Path | None:
    if texture_type == "item":
        candidates = (
            resource_root / "textures" / "item" / material / name,
            resource_root / "textures" / "items" / material / name,
        )
    else:
        candidates = (
            resource_root / "textures" / "block" / material / name,
            resource_root / "textures" / "blocks" / material / name,
        )
    for candidate in candidates:
        if candidate.exists():
            return candidate
    return None


def derive_source_name(entry_name: str, target_slug: str, source_slug: str) -> str:
    if target_slug == source_slug:
        return entry_name
    return entry_name.replace(target_slug, source_slug)


def load_source_template_catalog(resource_root: Path) -> dict[str, dict[str, tuple[int, int, int]]]:
    item_root = resource_root / "textures" / "item"
    catalog: dict[str, dict[str, tuple[int, int, int]]] = {}

    for child in sorted(item_root.iterdir()):
        if not child.is_dir():
            continue
        if child.name in {"derived", "singularity"}:
            continue

        ingot_path = child / f"{child.name}_ingot.png"
        if not ingot_path.exists():
            continue

        color = sample_average_color(Image.open(ingot_path).convert("RGBA"), True)
        catalog[child.name] = {"color": color}

    return catalog


def resolve_source_template_slug(
    material: dict,
    source_templates: dict[str, dict[str, tuple[int, int, int]]],
    resolved_templates: dict[str, str],
) -> str:
    template_slug = str(material.get("template", "")).strip().lower()
    slug = str(material["slug"]).strip().lower()

    for candidate in (template_slug, slug):
        if not candidate:
            continue
        if candidate in source_templates:
            return candidate
        if candidate in resolved_templates:
            return resolved_templates[candidate]

    target_color = parse_hex_color(str(material.get("color", "#FFFFFF")))
    return min(
        source_templates,
        key=lambda source_slug: color_distance(target_color, source_templates[source_slug]["color"]),
    )


def parse_string_array(java_path: Path, field_name: str) -> list[str]:
    values: list[str] = []
    in_field = False
    pattern = re.compile(r'"([^"]+)"')

    for raw_line in java_path.read_text(encoding="utf-8").splitlines():
        if f"public static final String[] {field_name}" in raw_line:
            in_field = True
            continue
        if not in_field:
            continue
        if raw_line.strip() == "};":
            break
        match = pattern.search(raw_line)
        if match:
            values.append(match.group(1))

    return values


def matches_group_category(block_name: str, category: str) -> bool:
    if category == "ore":
        return block_name.endswith("_ore")
    if category == "raw":
        return block_name.startswith("raw_") and block_name.endswith("_block")
    return block_name.endswith("_block") and not block_name.startswith("raw_")


def build_grouped_blockstates(resource_root: Path, manifest: dict) -> int:
    blockstate_root = resource_root / "blockstates"
    blockstate_root.mkdir(parents=True, exist_ok=True)

    for pattern in ("derived_storage_*.json", "derived_raw_*.json", "derived_ore_*.json"):
        for stale_path in blockstate_root.glob(pattern):
            stale_path.unlink()

    grouped_count = 0
    for category, registry_prefix in (("storage", "derived_storage"), ("raw", "derived_raw"), ("ore", "derived_ore")):
        pending: list[tuple[str, str]] = []
        group_index = 0

        for material in manifest["materials"]:
            matching_name = None
            for entry in material["blocks"]:
                name = entry["name"]
                if matches_group_category(name, category):
                    matching_name = name
                    break
            if matching_name is None:
                continue

            pending.append((material["slug"], matching_name))
            if len(pending) == VARIANTS_PER_GROUP:
                write_grouped_blockstate(blockstate_root, registry_prefix, group_index, pending)
                grouped_count += 1
                group_index += 1
                pending = []

        if pending:
            write_grouped_blockstate(blockstate_root, registry_prefix, group_index, pending)
            grouped_count += 1

    return grouped_count


def write_grouped_blockstate(
    blockstate_root: Path,
    registry_prefix: str,
    group_index: int,
    variants: list[tuple[str, str]],
) -> None:
    payload_variants: dict[str, dict[str, str]] = {}
    fallback_slug, fallback_block_name = variants[-1]

    for meta in range(VARIANTS_PER_GROUP):
        slug, block_name = variants[meta] if meta < len(variants) else (fallback_slug, fallback_block_name)
        payload_variants[f"variant={meta}"] = {
            "model": f"{MOD_ID}:derived/{slug}/{block_name}",
        }

    write_text(
        blockstate_root / f"{registry_prefix}_{group_index}.json",
        json.dumps({"variants": payload_variants}, ensure_ascii=False, indent=2) + "\n",
    )


def generate_models_from_manifest(resource_root: Path, manifest: dict, singularity_ids: list[str]) -> tuple[int, int, int, int, int]:
    derived_item_model_root = resource_root / "models" / "item" / "derived"
    derived_block_model_root = resource_root / "models" / "block" / "derived"
    derived_blockstate_root = resource_root / "blockstates" / "derived"
    singularity_model_root = resource_root / "models" / "item" / "singularity"
    clear_directory(derived_item_model_root)
    clear_directory(derived_block_model_root)
    clear_directory(derived_blockstate_root)
    clear_directory(singularity_model_root)

    derived_item_model_count = 0
    derived_block_model_count = 0
    derived_blockstate_count = 0
    for material in manifest["materials"]:
        slug = material["slug"]
        for entry in material["items"]:
            payload = {
                "parent": "item/generated",
                "textures": {
                    "layer0": f"{MOD_ID}:items/derived/{slug}/{entry['name']}",
                },
            }
            write_text(
                derived_item_model_root / slug / f"{entry['name']}.json",
                json.dumps(payload, ensure_ascii=False, indent=2) + "\n",
            )
            derived_item_model_count += 1

        for entry in material["blocks"]:
            block_payload = {
                "parent": "block/cube_all",
                "textures": {
                    "all": f"{MOD_ID}:blocks/derived/{slug}/{entry['name']}",
                },
            }
            write_text(
                derived_block_model_root / slug / f"{entry['name']}.json",
                json.dumps(block_payload, ensure_ascii=False, indent=2) + "\n",
            )
            derived_block_model_count += 1

            payload = {
                "parent": f"{MOD_ID}:block/derived/{slug}/{entry['name']}",
            }
            write_text(
                derived_item_model_root / slug / f"{entry['name']}.json",
                json.dumps(payload, ensure_ascii=False, indent=2) + "\n",
            )
            derived_item_model_count += 1

            blockstate_payload = {
                "variants": {
                    "normal": {
                        "model": f"{MOD_ID}:derived/{slug}/{entry['name']}",
                    }
                }
            }
            write_text(
                derived_blockstate_root / slug / f"{entry['name']}.json",
                json.dumps(blockstate_payload, ensure_ascii=False, indent=2) + "\n",
            )
            derived_blockstate_count += 1

    grouped_blockstate_count = build_grouped_blockstates(resource_root, manifest)

    singularity_count = 0
    for singularity_id in singularity_ids:
        payload = {
            "parent": "item/generated",
            "textures": {
                "layer0": f"{MOD_ID}:items/singularity/{singularity_id}_singularity",
                "layer1": f"{MOD_ID}:items/singularity/{singularity_id}_singularity_overlay",
            },
        }
        write_text(
            singularity_model_root / f"{singularity_id}_singularity.json",
            json.dumps(payload, ensure_ascii=False, indent=2) + "\n",
        )
        singularity_count += 1

    return (
        derived_item_model_count,
        derived_block_model_count,
        derived_blockstate_count,
        grouped_blockstate_count,
        singularity_count,
    )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Rebuild derived item textures and singularity assets.")
    parser.add_argument("--jar", type=Path, required=True, help="Live mod jar used as the complete manifest/template source.")
    parser.add_argument("--resource-root", type=Path, required=True, help="Mod resource root, for example src/main/resources/assets/<modid>.")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    resource_root = args.resource_root.resolve()
    repo_root = resource_root.parents[4]
    textures_item_root = resource_root / "textures" / "item"
    textures_items_root = resource_root / "textures" / "items"
    textures_block_root = resource_root / "textures" / "block"
    textures_blocks_root = resource_root / "textures" / "blocks"
    derived_item_root = textures_item_root / "derived"
    derived_items_root = textures_items_root / "derived"
    derived_block_root = textures_block_root / "derived"
    derived_blocks_root = textures_blocks_root / "derived"
    singularity_item_root = textures_item_root / "singularity"
    singularity_items_root = textures_items_root / "singularity"
    lang_root = resource_root / "lang"
    manifest_output = resource_root / "derived" / "manifest.json"
    source_templates = load_source_template_catalog(resource_root)
    resolved_templates: dict[str, str] = {}
    stone_texture = load_stone_texture(repo_root)
    clear_directory(derived_item_root)
    clear_directory(derived_items_root)
    clear_directory(derived_block_root)
    clear_directory(derived_blocks_root)
    clear_directory(singularity_item_root)
    clear_directory(singularity_items_root)

    with zipfile.ZipFile(args.jar) as archive:
        with archive.open(MANIFEST_ENTRY) as manifest_stream:
            manifest = json.load(manifest_stream)

        singularity_ids = sorted(
            {
                Path(name).name.removesuffix("_singularity.json")
                for name in archive.namelist()
                if name.startswith(SINGULARITY_MODEL_PREFIX) and name.endswith("_singularity.json")
            }
        )

        derived_item_generated = 0
        derived_block_generated = 0
        singularity_generated = 0
        generated_ingots: dict[str, Path] = {}

        for material in manifest["materials"]:
            slug = material["slug"]
            refresh_manifest_display_names(material)
            source_template_slug = resolve_source_template_slug(material, source_templates, resolved_templates)
            resolved_templates[slug] = source_template_slug
            material_dir_item = derived_item_root / slug
            material_dir_items = derived_items_root / slug
            material_dir_block = derived_block_root / slug
            material_dir_blocks = derived_blocks_root / slug
            material_dir_item.mkdir(parents=True, exist_ok=True)
            material_dir_items.mkdir(parents=True, exist_ok=True)
            material_dir_block.mkdir(parents=True, exist_ok=True)
            material_dir_blocks.mkdir(parents=True, exist_ok=True)

            base_color = parse_hex_color(material.get("color", "#FFFFFF"))

            for entry in material["items"]:
                entry_name = entry["name"]
                source_name = derive_source_name(entry_name, slug, source_template_slug)
                reference_path = resolve_texture_path(resource_root, "item", source_template_slug, f"{source_name}.png")
                if reference_path is None:
                    raise FileNotFoundError(f"Missing source item template for {slug}: {source_template_slug}/{source_name}.png")
                reference = Image.open(reference_path).convert("RGBA")
                palette = derive_palette_from_reference(reference, base_color)

                recolored = recolor_reference(reference, palette)
                output_item = material_dir_item / f"{entry_name}.png"
                output_items = material_dir_items / f"{entry_name}.png"
                recolored.save(output_item)
                recolored.save(output_items)
                derived_item_generated += 1

                if entry_name == f"{slug}_ingot":
                    generated_ingots[slug] = output_item

            for entry in material["blocks"]:
                entry_name = entry["name"]
                output_block = material_dir_block / f"{entry_name}.png"
                output_blocks = material_dir_blocks / f"{entry_name}.png"
                if entry_name.endswith("_ore"):
                    source_name = derive_source_name(entry_name, slug, source_template_slug)
                    reference_path = resolve_texture_path(resource_root, "block", source_template_slug, f"{source_name}.png")
                    if reference_path is not None:
                        reference = Image.open(reference_path).convert("RGBA")
                        palette = derive_palette_from_reference(reference, base_color)
                        recolored = recolor_ore_reference(reference, palette, stone_texture)
                    else:
                        overlay_path = material_dir_item / f"raw_{slug}.png"
                        if not overlay_path.exists():
                            raise FileNotFoundError(f"Missing raw item overlay for ore generation: {overlay_path}")
                        overlay = Image.open(overlay_path).convert("RGBA").resize((16, 16), Image.NEAREST)
                        alpha = overlay.getchannel("A").point(lambda value: 224 if value > 0 else 0)
                        overlay.putalpha(alpha)
                        recolored = stone_texture.copy()
                        recolored.alpha_composite(overlay)
                else:
                    source_name = derive_source_name(entry_name, slug, source_template_slug)
                    reference_path = resolve_texture_path(resource_root, "block", source_template_slug, f"{source_name}.png")
                    if reference_path is None:
                        raise FileNotFoundError(f"Missing source block template for {slug}: {source_template_slug}/{source_name}.png")
                    reference = Image.open(reference_path).convert("RGBA")
                    palette = derive_palette_from_reference(reference, base_color)
                    recolored = recolor_reference(reference, palette)
                recolored.save(output_block)
                recolored.save(output_blocks)
                derived_block_generated += 1

            if slug not in generated_ingots:
                for entry in material["items"]:
                    candidate = material_dir_item / f"{entry['name']}.png"
                    if candidate.exists():
                        generated_ingots[slug] = candidate
                        break

            fluid_style = normalize_fluid_style(str(material.get("fluidStyle", "")))
            material["fluidStyle"] = fluid_style
            material["fluidColor"] = format_hex_color(shade_for_fluid(base_color, fluid_style))
            if fluid_style == "water":
                material["fluidTemperature"] = max(int(material.get("fluidTemperature", 0) or 0), 450)
                material["fluidViscosity"] = max(int(material.get("fluidViscosity", 0) or 0), 1150)
                material["fluidDensity"] = max(int(material.get("fluidDensity", 0) or 0), 1300)
                material["fluidLuminosity"] = max(int(material.get("fluidLuminosity", 0) or 0), 3)
            else:
                material["fluidTemperature"] = max(int(material.get("fluidTemperature", 0) or 0), 1250)
                material["fluidViscosity"] = max(int(material.get("fluidViscosity", 0) or 0), 1450)
                material["fluidDensity"] = max(int(material.get("fluidDensity", 0) or 0), 3000)
                minimum_luminosity = 5 if luminance(base_color) < 110 else 7
                material["fluidLuminosity"] = max(int(material.get("fluidLuminosity", 0) or 0), minimum_luminosity)

        write_text(manifest_output, json.dumps(manifest, ensure_ascii=False, indent=2) + "\n")

        iron_base = read_zip_image(archive, f"{SINGULARITY_TEXTURE_PREFIX}iron_singularity.png")
        iron_overlay = read_zip_image(archive, f"{SINGULARITY_TEXTURE_PREFIX}iron_singularity_overlay.png")

        for singularity_id in singularity_ids:
            source_path = generated_ingots.get(singularity_id)
            if source_path is None:
                source_path = resolve_texture_path(resource_root, "item", singularity_id, f"{singularity_id}_ingot.png")
            if source_path is None or not source_path.exists():
                continue

            palette = build_palette(Image.open(source_path).convert("RGBA"))
            recolored = recolor_reference(iron_base, palette)
            base_output_item = singularity_item_root / f"{singularity_id}_singularity.png"
            base_output_items = singularity_items_root / f"{singularity_id}_singularity.png"
            overlay_output_item = singularity_item_root / f"{singularity_id}_singularity_overlay.png"
            overlay_output_items = singularity_items_root / f"{singularity_id}_singularity_overlay.png"

            recolored.save(base_output_item)
            recolored.save(base_output_items)
            iron_overlay.save(overlay_output_item)
            iron_overlay.save(overlay_output_items)
            write_text(base_output_item.with_suffix(".png.mcmeta"), ANIMATION_META)
            write_text(base_output_items.with_suffix(".png.mcmeta"), ANIMATION_META)
            write_text(overlay_output_item.with_suffix(".png.mcmeta"), ANIMATION_META)
            write_text(overlay_output_items.with_suffix(".png.mcmeta"), ANIMATION_META)
            singularity_generated += 1

        copy_tree(derived_item_root, derived_items_root)
        copy_tree(derived_block_root, derived_blocks_root)
        copy_tree(singularity_item_root, singularity_items_root)

        (
            derived_item_model_count,
            derived_block_model_count,
            derived_blockstate_count,
            grouped_blockstate_count,
            singularity_model_count,
        ) = generate_models_from_manifest(resource_root, manifest, singularity_ids)

        if lang_root.exists():
            clear_directory(lang_root)
        else:
            lang_root.mkdir(parents=True, exist_ok=True)
        extract_zip_prefix(archive, LANG_PREFIX, lang_root)
        merge_lang_overrides(lang_root / "en_us.lang", build_lang_overrides(manifest, "en_us"))
        merge_lang_overrides(lang_root / "zh_cn.lang", build_lang_overrides(manifest, "zh_cn"))

    print(f"generated {derived_item_generated} derived item textures")
    print(f"generated {derived_block_generated} derived block textures")
    print(f"generated {singularity_generated} singularity texture sets")
    print(f"generated {derived_item_model_count} derived item models")
    print(f"generated {derived_block_model_count} derived block models")
    print(f"generated {derived_blockstate_count} derived blockstates")
    print(f"generated {grouped_blockstate_count} grouped derived blockstates")
    print(f"generated {singularity_model_count} singularity models")
    print(f"updated manifest {manifest_output}")


if __name__ == "__main__":
    main()
