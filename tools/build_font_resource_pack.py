from __future__ import annotations

import json
import zipfile
from pathlib import Path


REPO_ROOT = Path(__file__).resolve().parent.parent
MINECRAFT_ASSETS = REPO_ROOT / "src" / "main" / "resources" / "assets" / "minecraft"
OUTPUT_DIR = REPO_ROOT / "build" / "resourcepacks"
OUTPUT_PATH = OUTPUT_DIR / "redstone_chemical_elements_superheavy_font_fix_1.12.2.zip"


def main() -> None:
    glyph_sizes = MINECRAFT_ASSETS / "font" / "glyph_sizes.bin"
    unicode_page = MINECRAFT_ASSETS / "textures" / "font" / "unicode_page_e1.png"

    if not glyph_sizes.exists() or not unicode_page.exists():
        raise FileNotFoundError("Missing generated font assets. Run tools/fix_assets.py first.")

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

    pack_meta = {
        "pack": {
            "pack_format": 3,
            "description": "Redstone Chemical Elements 1.12.2 superheavy font fix",
        }
    }

    with zipfile.ZipFile(OUTPUT_PATH, "w", compression=zipfile.ZIP_DEFLATED) as zf:
        zf.writestr("pack.mcmeta", json.dumps(pack_meta, ensure_ascii=False, indent=2) + "\n")
        zf.write(glyph_sizes, "assets/minecraft/font/glyph_sizes.bin")
        zf.write(unicode_page, "assets/minecraft/textures/font/unicode_page_e1.png")

    print(OUTPUT_PATH.resolve())


if __name__ == "__main__":
    main()
