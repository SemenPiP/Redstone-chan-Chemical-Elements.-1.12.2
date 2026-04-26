#!/usr/bin/env python3
import json
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SCRIPTS_DIR = ROOT / "scripts"
OUTPUT_DIR = ROOT / "src" / "main" / "resources" / "assets" / "redstone_chemical_elements" / "tconstruct"

TRAITS_SOURCE = SCRIPTS_DIR / "00_rce_tconstruct_traits.zs"
BASE_MATERIALS_SOURCE = SCRIPTS_DIR / "rce_tconstruct_base_materials.zs"
DERIVED_MATERIALS_SOURCE = SCRIPTS_DIR / "rce_tconstruct_materials.zs"

TRAITS_OUTPUT = OUTPUT_DIR / "traits.json"
MATERIALS_OUTPUT = OUTPUT_DIR / "materials.json"


def split_args(raw: str):
    args = []
    current = []
    depth = 0
    in_string = False
    escape = False

    for char in raw:
        if in_string:
            current.append(char)
            if escape:
                escape = False
            elif char == "\\":
                escape = True
            elif char == '"':
                in_string = False
            continue

        if char == '"':
            in_string = True
            current.append(char)
            continue

        if char == "[":
            depth += 1
            current.append(char)
            continue

        if char == "]":
            depth -= 1
            current.append(char)
            continue

        if char == "," and depth == 0:
            args.append("".join(current).strip())
            current = []
            continue

        current.append(char)

    tail = "".join(current).strip()
    if tail:
        args.append(tail)
    return args


def parse_scalar(token: str):
    token = token.strip()
    if token == "noTraits":
        return []
    if token.startswith("[") and token.endswith("]"):
        inner = token[1:-1].strip()
        if not inner:
            return []
        return [parse_scalar(part) for part in split_args(inner)]
    if token.startswith('"') and token.endswith('"'):
        return token[1:-1].replace('\\"', '"').replace("\\\\", "\\")
    if token.startswith("0x") or token.startswith("0X"):
        return int(token, 16)
    if re.fullmatch(r"-?\d+", token):
        return int(token)
    if re.fullmatch(r"-?\d+\.\d+", token):
        return float(token)
    return token


def parse_traits():
    content = TRAITS_SOURCE.read_text(encoding="utf-8")
    pattern = re.compile(r'registerTraitBundle\((.+?)\);\s*$', re.MULTILINE)
    traits = []
    for match in pattern.finditer(content):
        args = [parse_scalar(part) for part in split_args(match.group(1))]
        if len(args) != 19:
            raise ValueError(f"Unexpected trait argument count: {len(args)} for {match.group(0)}")
        traits.append(
            {
                "identifier": args[0],
                "name": args[1],
                "description": args[2],
                "color": args[3],
                "miningMultiplier": args[4],
                "damageMultiplier": args[5],
                "flatDamage": args[6],
                "critChance": args[7],
                "critDamageBonus": args[8],
                "saveChance": args[9],
                "saveAmount": args[10],
                "penaltyChance": args[11],
                "penaltyAmount": args[12],
                "healOnHit": args[13],
                "healOnKill": args[14],
                "burnTime": args[15],
                "slowChance": args[16],
                "knockbackBonus": args[17],
                "repairBonus": args[18],
            }
        )
    return traits


def parse_materials_file(path: Path):
    lines = path.read_text(encoding="utf-8").splitlines()
    materials = []

    current_identifier = None
    current_fluid = None
    collecting = False
    buffer = []

    create_pattern = re.compile(r'val\s+\w+\s*=\s*MaterialBuilder\.create\("([^"]+)"\);')
    fluid_pattern = re.compile(r'\.\s*liquid\s*=\s*<liquid:([^>]+)>;')

    for line in lines:
        stripped = line.strip()
        create_match = create_pattern.match(stripped)
        if create_match:
            current_identifier = create_match.group(1)
            current_fluid = None
            continue

        fluid_match = fluid_pattern.search(stripped)
        if fluid_match:
            current_fluid = fluid_match.group(1)
            continue

        if stripped == "finishRceMaterial(":
            collecting = True
            buffer = []
            continue

        if collecting:
            if stripped == ");":
                args = [parse_scalar(part) for part in split_args(" ".join(buffer))]
                if len(args) != 14:
                    raise ValueError(f"Unexpected material argument count: {len(args)} in {path.name}: {args}")
                if current_identifier is None:
                    raise ValueError(f"Material block without identifier in {path.name}")
                materials.append(
                    {
                        "identifier": current_identifier,
                        "fluidName": current_fluid,
                        "localizedName": args[1],
                        "oreBase": args[2],
                        "color": args[3],
                        "headDurability": args[4],
                        "miningSpeed": args[5],
                        "attack": args[6],
                        "harvestLevel": args[7],
                        "handleModifier": args[8],
                        "handleDurability": args[9],
                        "extraDurability": args[10],
                        "headTraits": args[11],
                        "handleTraits": args[12],
                        "extraTraits": args[13],
                    }
                )
                collecting = False
                buffer = []
                continue

            buffer.append(stripped)

    return materials


def write_json(path: Path, payload):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def main():
    traits = parse_traits()
    materials = parse_materials_file(BASE_MATERIALS_SOURCE) + parse_materials_file(DERIVED_MATERIALS_SOURCE)

    write_json(TRAITS_OUTPUT, traits)
    write_json(MATERIALS_OUTPUT, materials)

    print(f"Generated {len(traits)} traits -> {TRAITS_OUTPUT}")
    print(f"Generated {len(materials)} materials -> {MATERIALS_OUTPUT}")


if __name__ == "__main__":
    main()
