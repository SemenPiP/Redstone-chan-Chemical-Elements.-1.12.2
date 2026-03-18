from __future__ import annotations

import json
import re
import shutil
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


MOD_ID = "redstone_chemical_elements"
REPO_ROOT = Path(__file__).resolve().parent.parent
RESOURCE_ROOT = REPO_ROOT / "src" / "main" / "resources" / "assets" / MOD_ID
MINECRAFT_RESOURCE_ROOT = REPO_ROOT / "src" / "main" / "resources" / "assets" / "minecraft"
CATEGORY_PATH = REPO_ROOT / "src" / "main" / "java" / "com" / "chinaex123" / "redstone_chemical_elements" / "register" / "ElementCatalog.java"
ZH_JSON_PATH = REPO_ROOT / "zh_cn.json"
EN_LANG_PATH = RESOURCE_ROOT / "lang" / "en_us.lang"
ZH_LANG_PATH = RESOURCE_ROOT / "lang" / "zh_cn.lang"
UNICODE_PAGE_E1_PATH = MINECRAFT_RESOURCE_ROOT / "textures" / "font" / "unicode_page_e1.png"
MOD_UNICODE_PAGE_E1_PATH = RESOURCE_ROOT / "textures" / "font" / "unicode_page_e1.png"
GLYPH_SIZES_PATH = MINECRAFT_RESOURCE_ROOT / "font" / "glyph_sizes.bin"
ITEM_TEXTURE_SOURCE = RESOURCE_ROOT / "textures" / "item"
ITEM_TEXTURE_TARGET = RESOURCE_ROOT / "textures" / "items"
BLOCK_TEXTURE_SOURCE = RESOURCE_ROOT / "textures" / "block"
BLOCK_TEXTURE_TARGET = RESOURCE_ROOT / "textures" / "blocks"
ITEM_MODEL_ROOT = RESOURCE_ROOT / "models" / "item"
EXTERNAL_BLOCK_TEXTURE_SOURCE = Path(
    r"C:\Users\Seeds\Desktop\Redstone_chan-Chemical-Elements-1.21.1-neoforge\src\main\resources\assets\redstone_chemical_elements\textures\block"
)

STONE_TEXTURE_CANDIDATES = (
    REPO_ROOT / "build" / "rfg" / "minecraft-src" / "resources" / "assets" / "minecraft" / "textures" / "blocks" / "stone.png",
    REPO_ROOT / "build" / "resources" / "patchedMc" / "assets" / "minecraft" / "textures" / "blocks" / "stone.png",
)

GLYPH_SIZES_CANDIDATES = (
    REPO_ROOT / "build" / "rfg" / "minecraft-src" / "resources" / "assets" / "minecraft" / "font" / "glyph_sizes.bin",
    REPO_ROOT / "build" / "resources" / "patchedMc" / "assets" / "minecraft" / "font" / "glyph_sizes.bin",
)

FONT_FILE_CANDIDATES = (
    Path(r"C:\Windows\Fonts\NotoSansSC-VF.ttf"),
    Path(r"C:\Windows\Fonts\msyh.ttc"),
    Path(r"C:\Windows\Fonts\simsun.ttc"),
)

COMPATIBLE_SUPERHEAVY_ELEMENTS = {
    "bohrium": {"pua": 0xE100, "compat": (0x9485, 0x6CE2)},
    "darmstadtium": {"pua": 0xE101, "compat": (0x9485, 0x8FBE)},
    "dubnium": {"pua": 0xE102, "compat": (0x9485, 0x675C)},
    "flerovium": {"pua": 0xE103, "compat": (0x9485, 0x592B)},
    "hassium": {"pua": 0xE104, "compat": (0x9485, 0x9ED1)},
    "roentgenium": {"pua": 0xE105, "compat": (0x9485, 0x4ED1)},
    "seaborgium": {"pua": 0xE106, "compat": (0x9485, 0x559C)},
}


def write_text(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8", newline="\n")


def copy_tree(source_root: Path, target_root: Path) -> None:
    if not source_root.exists():
        return

    for source in source_root.rglob("*"):
        if source.is_dir():
            continue
        target = target_root / source.relative_to(source_root)
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, target)


def load_elements() -> list[str]:
    elements: list[str] = []
    in_elements = False

    for raw_line in CATEGORY_PATH.read_text(encoding="utf-8").splitlines():
        if "public static final String[] ELEMENTS" in raw_line:
            in_elements = True
            continue

        if not in_elements:
            continue

        if raw_line.strip() == "};":
            break

        match = re.search(r'"([^"]+)"', raw_line)
        if match:
            elements.append(match.group(1))

    return elements


def compatibility_name(element: str) -> str:
    config = COMPATIBLE_SUPERHEAVY_ELEMENTS.get(element)
    if config is None:
        return ""
    return "".join(chr(codepoint) for codepoint in config["compat"])


def compatibility_pua_char(element: str) -> str | None:
    config = COMPATIBLE_SUPERHEAVY_ELEMENTS.get(element)
    if config is None:
        return None
    return chr(config["pua"])


def resolve_font_path() -> Path:
    for candidate in FONT_FILE_CANDIDATES:
        if candidate.exists():
            return candidate
    raise FileNotFoundError("No compatible system font found for compact glyph generation.")


def load_glyph_sizes_template() -> bytearray:
    for candidate in GLYPH_SIZES_CANDIDATES:
        if candidate.exists():
            return bytearray(candidate.read_bytes())
    return bytearray(65536)


def render_compatibility_glyph(font_path: Path, left_codepoint: int, right_codepoint: int) -> Image.Image:
    left_font = ImageFont.truetype(str(font_path), 12)
    right_font = ImageFont.truetype(str(font_path), 13)
    tile = Image.new("L", (16, 16), 0)
    draw = ImageDraw.Draw(tile)
    draw.text((-1, 1), chr(left_codepoint), fill=255, font=left_font)
    draw.text((6, 1), chr(right_codepoint), fill=255, font=right_font)
    bbox = tile.getbbox()

    if bbox:
        cropped = tile.crop(bbox)
        if cropped.width > 15 or cropped.height > 15:
            scale = min(15 / cropped.width, 15 / cropped.height)
            cropped = cropped.resize(
                (max(1, round(cropped.width * scale)), max(1, round(cropped.height * scale))),
                Image.BICUBIC,
            )
    else:
        cropped = tile

    final_tile = Image.new("RGBA", (16, 16), (255, 255, 255, 0))
    offset_x = (16 - cropped.width) // 2
    offset_y = (16 - cropped.height) // 2

    for y in range(cropped.height):
        for x in range(cropped.width):
            alpha = cropped.getpixel((x, y))
            if alpha > 40:
                final_tile.putpixel((offset_x + x, offset_y + y), (255, 255, 255, 255))

    return final_tile


def rebuild_superheavy_font_assets() -> int:
    glyph_sizes = load_glyph_sizes_template()

    if not UNICODE_PAGE_E1_PATH.exists():
        font_path = resolve_font_path()
        page = Image.new("RGBA", (256, 256), (255, 255, 255, 0))

        for element, config in COMPATIBLE_SUPERHEAVY_ELEMENTS.items():
            glyph = render_compatibility_glyph(font_path, config["compat"][0], config["compat"][1])
            codepoint = config["pua"]
            cell_index = codepoint & 0xFF
            x = (cell_index % 16) * 16
            y = (cell_index // 16) * 16
            page.alpha_composite(glyph, (x, y))

        UNICODE_PAGE_E1_PATH.parent.mkdir(parents=True, exist_ok=True)
        page.save(UNICODE_PAGE_E1_PATH)

    for config in COMPATIBLE_SUPERHEAVY_ELEMENTS.values():
        codepoint = config["pua"]
        glyph_sizes[codepoint] = 0x0F

    MOD_UNICODE_PAGE_E1_PATH.parent.mkdir(parents=True, exist_ok=True)
    MOD_UNICODE_PAGE_E1_PATH.write_bytes(UNICODE_PAGE_E1_PATH.read_bytes())
    GLYPH_SIZES_PATH.parent.mkdir(parents=True, exist_ok=True)
    GLYPH_SIZES_PATH.write_bytes(glyph_sizes)
    return len(COMPATIBLE_SUPERHEAVY_ELEMENTS)


def english_element_name(element: str) -> str:
    return " ".join(part.capitalize() for part in element.split("_"))


def sync_en_lang_with_ores(elements: list[str]) -> int:
    lines = EN_LANG_PATH.read_text(encoding="utf-8").splitlines()
    existing_keys = {
        line.split("=", 1)[0]
        for line in lines
        if "=" in line
    }
    added = 0

    for element in elements:
        key = f"tile.{MOD_ID}.{element}.{element}_ore.name"
        if key in existing_keys:
            continue
        lines.append(f"{key}={english_element_name(element)} Ore")
        added += 1

    EN_LANG_PATH.write_text("\n".join(lines) + "\n", encoding="utf-8", newline="\n")
    return added


def build_item_models() -> int:
    copy_tree(ITEM_TEXTURE_SOURCE, ITEM_TEXTURE_TARGET)
    count = 0

    for texture in ITEM_TEXTURE_TARGET.rglob("*.png"):
        relative_path = texture.relative_to(ITEM_TEXTURE_TARGET).with_suffix("")
        payload = {
            "parent": "item/generated",
            "textures": {
                "layer0": f"{MOD_ID}:items/{relative_path.as_posix()}",
            },
        }
        model_path = ITEM_MODEL_ROOT / relative_path.with_suffix(".json")
        write_text(model_path, json.dumps(payload, ensure_ascii=False, indent=2) + "\n")
        count += 1

    return count


def load_stone_texture() -> Image.Image:
    for candidate in STONE_TEXTURE_CANDIDATES:
        if candidate.exists():
            return Image.open(candidate).convert("RGBA").resize((16, 16), Image.NEAREST)

    return Image.new("RGBA", (16, 16), (125, 125, 125, 255))


def resolve_source_ore_texture(element: str) -> Path | None:
    direct_candidate = EXTERNAL_BLOCK_TEXTURE_SOURCE / element / f"{element}_ore.png"
    if direct_candidate.exists():
        return direct_candidate
    return None


def resolve_ore_overlay(element: str) -> Path | None:
    overlay_candidates = (
        ITEM_TEXTURE_SOURCE / element / f"raw_{element}.png",
        ITEM_TEXTURE_TARGET / element / f"raw_{element}.png",
        BLOCK_TEXTURE_SOURCE / element / f"raw_{element}_block.png",
        BLOCK_TEXTURE_TARGET / element / f"raw_{element}_block.png",
    )
    for candidate in overlay_candidates:
        if candidate.exists():
            return candidate

    return None


def build_ore_textures(elements: list[str]) -> int:
    stone = load_stone_texture()
    count = 0

    for element in elements:
        source_ore_texture = resolve_source_ore_texture(element)
        target = BLOCK_TEXTURE_SOURCE / element / f"{element}_ore.png"
        target.parent.mkdir(parents=True, exist_ok=True)

        if source_ore_texture is not None:
            shutil.copy2(source_ore_texture, target)
            count += 1
            continue

        overlay_path = resolve_ore_overlay(element)
        if overlay_path is None:
            continue

        ore = stone.copy()
        overlay = Image.open(overlay_path).convert("RGBA").resize((16, 16), Image.NEAREST)
        alpha = overlay.getchannel("A").point(lambda value: 224 if value > 0 else 0)
        overlay.putalpha(alpha)
        ore.alpha_composite(overlay)
        ore.save(target)
        count += 1

    copy_tree(BLOCK_TEXTURE_SOURCE, BLOCK_TEXTURE_TARGET)
    return count


def load_zh_json() -> dict[str, str]:
    raw_map = json.loads(ZH_JSON_PATH.read_text(encoding="utf-8"))
    return {str(key): str(value) for key, value in raw_map.items()}


def json_key_for_lang_key(lang_key: str) -> str | None:
    if lang_key.startswith("itemGroup."):
        return lang_key

    if lang_key.startswith(f"item.{MOD_ID}.") and lang_key.endswith(".name"):
        return lang_key[:-5]

    if lang_key.startswith(f"tile.{MOD_ID}.") and lang_key.endswith(".name"):
        return "block." + lang_key[len("tile."):-5]

    return None


def normalized_translation(json_key: str | None, translated: str, zh_map: dict[str, str]) -> str:
    if json_key is None:
        return translated

    if ".crushed_raw_" in json_key:
        element = json_key.split(".")[2]
        raw_key = f"item.{MOD_ID}.{element}.raw_{element}"
        raw_value = zh_map.get(raw_key)
        if raw_value:
            translated = f"粉碎的{raw_value}"

    element = None
    parts = json_key.split(".")
    if len(parts) >= 3 and parts[0] in {"item", "block"}:
        element = parts[2]

    if element is not None:
        compatibility_name_value = compatibility_name(element)
        compatibility_char = compatibility_pua_char(element)
        if compatibility_name_value and compatibility_char:
            return translated.replace(compatibility_name_value, compatibility_char)

    return translated


def rebuild_zh_lang() -> tuple[int, int]:
    zh_map = load_zh_json()
    output_lines: list[str] = []
    matched = 0

    for raw_line in EN_LANG_PATH.read_text(encoding="utf-8").splitlines():
        if "=" not in raw_line:
            output_lines.append(raw_line)
            continue

        key, english_value = raw_line.split("=", 1)
        json_key = json_key_for_lang_key(key)
        translated = zh_map.get(json_key, english_value) if json_key is not None else english_value
        translated = normalized_translation(json_key, translated, zh_map)
        if json_key is not None and json_key in zh_map:
            matched += 1
        output_lines.append(f"{key}={translated}")

    ZH_LANG_PATH.write_text("\n".join(output_lines) + "\n", encoding="utf-8", newline="\n")
    return len(output_lines), matched


def main() -> None:
    elements = load_elements()
    ore_lang_lines = sync_en_lang_with_ores(elements)
    item_models = build_item_models()
    ore_textures = build_ore_textures(elements)
    font_glyphs = rebuild_superheavy_font_assets()
    zh_lines, matched = rebuild_zh_lang()
    print(f"ORE_LANG_LINES={ore_lang_lines}")
    print(f"ITEM_MODELS={item_models}")
    print(f"ORE_TEXTURES={ore_textures}")
    print(f"FONT_GLYPHS={font_glyphs}")
    print(f"ZH_LINES={zh_lines}")
    print(f"ZH_MATCHED={matched}")


if __name__ == "__main__":
    main()
