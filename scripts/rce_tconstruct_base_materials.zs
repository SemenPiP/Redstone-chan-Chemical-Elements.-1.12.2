#loader contenttweaker
#modloaded tconstruct

import mods.contenttweaker.tconstruct.MaterialBuilder;

val noTraits = [] as string[];

function addTraits(builder as MaterialBuilder, dependency as string, traits as string[]) {
    for traitName in traits {
        builder.addMaterialTrait(traitName, dependency);
    }
}

function finishRceMaterial(
    material as MaterialBuilder,
    localizedName as string,
    oreBase as string,
    color as int,
    headDurability as int,
    miningSpeed as float,
    attack as float,
    harvestLevel as int,
    handleModifier as float,
    handleDurability as int,
    extraDurability as int,
    headTraits as string[],
    handleTraits as string[],
    extraTraits as string[]
) {
    val ingot = oreDict.get("ingot" + oreBase);
    val nugget = oreDict.get("nugget" + oreBase);
    val block = oreDict.get("block" + oreBase);
    val plate = oreDict.get("plate" + oreBase);
    val rod = oreDict.get("rod" + oreBase);

    material.color = color;
    material.localizedName = localizedName;
    material.castable = true;
    material.craftable = true;
    material.representativeOre = ingot;

    material.addItem(ingot, 1, 144);
    material.addItem(nugget, 1, 16);
    material.addItem(block, 1, 1296);
    material.addItem(plate, 1, 144);
    material.addItem(rod, 1, 72);

    material.addHeadMaterialStats(headDurability, miningSpeed, attack, harvestLevel);
    material.addHandleMaterialStats(handleModifier, handleDurability);
    material.addExtraMaterialStats(extraDurability);

    addTraits(material, "head", headTraits);
    addTraits(material, "handle", handleTraits);
    addTraits(material, "extra", extraTraits);

    material.register();
}

val material_actinium = MaterialBuilder.create("rce_actinium");
material_actinium.liquid = <liquid:redstone_chemical_elements_actinium_fluid>;
finishRceMaterial(
    material_actinium,
    "锕",
    "Actinium",
    0xA0D157,
    565,
    5.5,
    5.0,
    4,
    0.93,
    80,
    98,
    ["rce_trait_001"],
    ["rce_trait_002"],
    noTraits
);

val material_aluminum = MaterialBuilder.create("rce_aluminum");
material_aluminum.liquid = <liquid:redstone_chemical_elements_aluminum_fluid>;
finishRceMaterial(
    material_aluminum,
    "铝",
    "Aluminum",
    0xD16C57,
    625,
    6.6,
    3.3,
    5,
    1.20,
    112,
    134,
    ["rce_trait_003"],
    ["rce_trait_004"],
    ["rce_trait_005"]
);

val material_americium = MaterialBuilder.create("rce_americium");
material_americium.liquid = <liquid:redstone_chemical_elements_americium_fluid>;
finishRceMaterial(
    material_americium,
    "镅",
    "Americium",
    0xD17E57,
    925,
    6.9,
    6.6,
    6,
    1.09,
    144,
    174,
    ["rce_trait_006"],
    ["rce_trait_007"],
    ["rce_trait_001"]
);

val material_antimony = MaterialBuilder.create("rce_antimony");
material_antimony.liquid = <liquid:redstone_chemical_elements_antimony_fluid>;
finishRceMaterial(
    material_antimony,
    "锑",
    "Antimony",
    0xD18E57,
    475,
    7.1,
    4.2,
    4,
    0.97,
    64,
    78,
    ["rce_trait_008"],
    ["rce_trait_009"],
    noTraits
);

val material_argon = MaterialBuilder.create("rce_argon");
material_argon.liquid = <liquid:redstone_chemical_elements_argon_fluid>;
finishRceMaterial(
    material_argon,
    "氩",
    "Argon",
    0xD157BA,
    390,
    6.1,
    3.9,
    3,
    0.93,
    52,
    64,
    ["rce_trait_010"],
    ["rce_trait_011"],
    noTraits
);

val material_arsenic = MaterialBuilder.create("rce_arsenic");
material_arsenic.liquid = <liquid:redstone_chemical_elements_arsenic_fluid>;
finishRceMaterial(
    material_arsenic,
    "砷",
    "Arsenic",
    0x57D15B,
    600,
    7.9,
    4.4,
    5,
    1.05,
    86,
    104,
    ["rce_trait_012"],
    ["rce_trait_013"],
    ["rce_trait_008"]
);

val material_astatine = MaterialBuilder.create("rce_astatine");
material_astatine.liquid = <liquid:redstone_chemical_elements_astatine_fluid>;
finishRceMaterial(
    material_astatine,
    "砹",
    "Astatine",
    0xD15763,
    605,
    6.3,
    6.5,
    5,
    1.01,
    100,
    122,
    ["rce_trait_014"],
    ["rce_trait_015"],
    ["rce_trait_016", "rce_trait_017"]
);

val material_barium = MaterialBuilder.create("rce_barium");
material_barium.liquid = <liquid:redstone_chemical_elements_barium_fluid>;
finishRceMaterial(
    material_barium,
    "钡",
    "Barium",
    0x57D1A2,
    380,
    6.3,
    3.1,
    3,
    1.00,
    56,
    68,
    ["rce_trait_005"],
    ["rce_trait_018"],
    noTraits
);

val material_berkelium = MaterialBuilder.create("rce_berkelium");
material_berkelium.liquid = <liquid:redstone_chemical_elements_berkelium_fluid>;
finishRceMaterial(
    material_berkelium,
    "锫",
    "Berkelium",
    0xD1C057,
    745,
    5.9,
    6.2,
    6,
    0.97,
    110,
    134,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006"]
);

val material_beryllium = MaterialBuilder.create("rce_beryllium");
material_beryllium.liquid = <liquid:redstone_chemical_elements_beryllium_fluid>;
finishRceMaterial(
    material_beryllium,
    "铍",
    "Beryllium",
    0xBCD157,
    635,
    7.6,
    3.5,
    5,
    1.23,
    112,
    134,
    ["rce_trait_003"],
    ["rce_trait_004"],
    ["rce_trait_005", "rce_trait_018"]
);

val material_bismuth = MaterialBuilder.create("rce_bismuth");
material_bismuth.liquid = <liquid:redstone_chemical_elements_bismuth_fluid>;
finishRceMaterial(
    material_bismuth,
    "铋",
    "Bismuth",
    0x57D1A8,
    460,
    6.0,
    4.0,
    4,
    0.91,
    64,
    78,
    ["rce_trait_019"],
    ["rce_trait_020"],
    noTraits
);

val material_bohrium = MaterialBuilder.create("rce_bohrium");
material_bohrium.liquid = <liquid:redstone_chemical_elements_bohrium_fluid>;
finishRceMaterial(
    material_bohrium,
    "bohrium",
    "Bohrium",
    0xD15798,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007", "rce_trait_001"]
);

val material_boron = MaterialBuilder.create("rce_boron");
material_boron.liquid = <liquid:redstone_chemical_elements_boron_fluid>;
finishRceMaterial(
    material_boron,
    "硼",
    "Boron",
    0xD15761,
    605,
    8.4,
    5.4,
    6,
    1.01,
    84,
    102,
    ["rce_trait_009"],
    ["rce_trait_012"],
    ["rce_trait_013"]
);

val material_bromine = MaterialBuilder.create("rce_bromine");
material_bromine.liquid = <liquid:redstone_chemical_elements_bromine_fluid>;
finishRceMaterial(
    material_bromine,
    "溴",
    "Bromine",
    0x576CD1,
    460,
    5.7,
    4.7,
    4,
    0.97,
    72,
    88,
    ["rce_trait_017"],
    ["rce_trait_014"],
    noTraits
);

val material_cadmium = MaterialBuilder.create("rce_cadmium");
material_cadmium.liquid = <liquid:redstone_chemical_elements_cadmium_fluid>;
finishRceMaterial(
    material_cadmium,
    "镉",
    "Cadmium",
    0x57ACD1,
    460,
    6.0,
    4.0,
    4,
    0.91,
    64,
    78,
    ["rce_trait_019"],
    ["rce_trait_020"],
    noTraits
);

val material_calcium = MaterialBuilder.create("rce_calcium");
material_calcium.liquid = <liquid:redstone_chemical_elements_calcium_fluid>;
finishRceMaterial(
    material_calcium,
    "钙",
    "Calcium",
    0xB8D157,
    530,
    6.6,
    3.3,
    4,
    1.12,
    90,
    108,
    ["rce_trait_005"],
    ["rce_trait_018"],
    ["rce_trait_003"]
);

val material_californium = MaterialBuilder.create("rce_californium");
material_californium.liquid = <liquid:redstone_chemical_elements_californium_fluid>;
finishRceMaterial(
    material_californium,
    "锎",
    "Californium",
    0x577CD1,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006", "rce_trait_007"]
);

val material_carbon = MaterialBuilder.create("rce_carbon");
material_carbon.liquid = <liquid:redstone_chemical_elements_carbon_fluid>;
finishRceMaterial(
    material_carbon,
    "碳",
    "Carbon",
    0x78D157,
    475,
    7.1,
    4.2,
    4,
    0.97,
    64,
    78,
    ["rce_trait_012"],
    ["rce_trait_013"],
    noTraits
);

val material_cerium = MaterialBuilder.create("rce_cerium");
material_cerium.liquid = <liquid:redstone_chemical_elements_cerium_fluid>;
finishRceMaterial(
    material_cerium,
    "铈",
    "Cerium",
    0xD19C57,
    540,
    7.4,
    3.6,
    5,
    1.05,
    80,
    96,
    ["rce_trait_021"],
    ["rce_trait_022"],
    ["rce_trait_023"]
);

val material_cesium = MaterialBuilder.create("rce_cesium");
material_cesium.liquid = <liquid:redstone_chemical_elements_cesium_fluid>;
finishRceMaterial(
    material_cesium,
    "铯",
    "Cesium",
    0x94D157,
    635,
    7.6,
    3.5,
    5,
    1.23,
    112,
    134,
    ["rce_trait_005"],
    ["rce_trait_018"],
    ["rce_trait_003", "rce_trait_004"]
);

val material_chlorine = MaterialBuilder.create("rce_chlorine");
material_chlorine.liquid = <liquid:redstone_chemical_elements_chlorine_fluid>;
finishRceMaterial(
    material_chlorine,
    "氯",
    "Chlorine",
    0xD157A6,
    460,
    5.7,
    4.7,
    4,
    0.97,
    72,
    88,
    ["rce_trait_016"],
    ["rce_trait_017"],
    noTraits
);

val material_chromium = MaterialBuilder.create("rce_chromium");
material_chromium.liquid = <liquid:redstone_chemical_elements_chromium_fluid>;
finishRceMaterial(
    material_chromium,
    "铬",
    "Chromium",
    0xB6D157,
    605,
    6.9,
    5.3,
    5,
    0.89,
    84,
    102,
    ["rce_trait_024"],
    ["rce_trait_019"],
    ["rce_trait_020"]
);

val material_cobalt = MaterialBuilder.create("rce_cobalt");
material_cobalt.liquid = <liquid:redstone_chemical_elements_cobalt_fluid>;
finishRceMaterial(
    material_cobalt,
    "钴",
    "Cobalt",
    0x578AD1,
    505,
    6.2,
    4.9,
    4,
    0.91,
    72,
    88,
    ["rce_trait_019"],
    ["rce_trait_020"],
    ["rce_trait_025"]
);

val material_copernicium = MaterialBuilder.create("rce_copernicium");
material_copernicium.liquid = <liquid:redstone_chemical_elements_copernicium_fluid>;
finishRceMaterial(
    material_copernicium,
    "鎶",
    "Copernicium",
    0xD157B8,
    745,
    6.5,
    5.4,
    6,
    1.05,
    114,
    138,
    ["rce_trait_007"],
    ["rce_trait_001"],
    noTraits
);

val material_copper = MaterialBuilder.create("rce_copper");
material_copper.liquid = <liquid:redstone_chemical_elements_copper_fluid>;
finishRceMaterial(
    material_copper,
    "铜",
    "Copper",
    0xC457D1,
    410,
    6.2,
    4.9,
    4,
    0.83,
    50,
    62,
    ["rce_trait_025"],
    ["rce_trait_024"],
    noTraits
);

val material_curium = MaterialBuilder.create("rce_curium");
material_curium.liquid = <liquid:redstone_chemical_elements_curium_fluid>;
finishRceMaterial(
    material_curium,
    "锔",
    "Curium",
    0xD1BE57,
    830,
    6.9,
    6.6,
    6,
    1.01,
    122,
    148,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007"]
);

val material_darmstadtium = MaterialBuilder.create("rce_darmstadtium");
material_darmstadtium.liquid = <liquid:redstone_chemical_elements_darmstadtium_fluid>;
finishRceMaterial(
    material_darmstadtium,
    "",
    "Darmstadtium",
    0x57C9D1,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_006"],
    ["rce_trait_007"],
    ["rce_trait_001", "rce_trait_002"]
);

val material_dubnium = MaterialBuilder.create("rce_dubnium");
material_dubnium.liquid = <liquid:redstone_chemical_elements_dubnium_fluid>;
finishRceMaterial(
    material_dubnium,
    "",
    "Dubnium",
    0x57D19C,
    745,
    6.5,
    5.4,
    6,
    1.05,
    114,
    138,
    ["rce_trait_007"],
    ["rce_trait_001"],
    noTraits
);

val material_dysprosium = MaterialBuilder.create("rce_dysprosium");
material_dysprosium.liquid = <liquid:redstone_chemical_elements_dysprosium_fluid>;
finishRceMaterial(
    material_dysprosium,
    "镝",
    "Dysprosium",
    0xD1CB57,
    585,
    7.6,
    4.5,
    5,
    1.05,
    88,
    106,
    ["rce_trait_023"],
    ["rce_trait_026"],
    ["rce_trait_021"]
);

val material_einsteinium = MaterialBuilder.create("rce_einsteinium");
material_einsteinium.liquid = <liquid:redstone_chemical_elements_einsteinium_fluid>;
finishRceMaterial(
    material_einsteinium,
    "锿",
    "Einsteinium",
    0x88D157,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007", "rce_trait_001"]
);

val material_erbium = MaterialBuilder.create("rce_erbium");
material_erbium.liquid = <liquid:redstone_chemical_elements_erbium_fluid>;
finishRceMaterial(
    material_erbium,
    "铒",
    "Erbium",
    0xD19657,
    360,
    6.4,
    3.3,
    3,
    0.93,
    46,
    56,
    ["rce_trait_021"],
    ["rce_trait_022"],
    noTraits
);

val material_europium = MaterialBuilder.create("rce_europium");
material_europium.liquid = <liquid:redstone_chemical_elements_europium_fluid>;
finishRceMaterial(
    material_europium,
    "铕",
    "Europium",
    0xD157B6,
    555,
    6.8,
    4.3,
    5,
    1.05,
    88,
    106,
    ["rce_trait_022"],
    ["rce_trait_023"],
    ["rce_trait_026"]
);

val material_fermium = MaterialBuilder.create("rce_fermium");
material_fermium.liquid = <liquid:redstone_chemical_elements_fermium_fluid>;
finishRceMaterial(
    material_fermium,
    "镄",
    "Fermium",
    0xD18657,
    745,
    5.9,
    6.2,
    6,
    0.97,
    110,
    134,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006"]
);

val material_flerovium = MaterialBuilder.create("rce_flerovium");
material_flerovium.liquid = <liquid:redstone_chemical_elements_flerovium_fluid>;
finishRceMaterial(
    material_flerovium,
    "",
    "Flerovium",
    0xD19257,
    525,
    5.7,
    5.2,
    4,
    0.89,
    70,
    86,
    ["rce_trait_002"],
    ["rce_trait_006"],
    noTraits
);

val material_fluorine = MaterialBuilder.create("rce_fluorine");
material_fluorine.liquid = <liquid:redstone_chemical_elements_fluorine_fluid>;
finishRceMaterial(
    material_fluorine,
    "氟",
    "Fluorine",
    0xD1CF57,
    365,
    5.7,
    4.7,
    3,
    0.89,
    50,
    62,
    ["rce_trait_014"],
    ["rce_trait_015"],
    noTraits
);

val material_francium = MaterialBuilder.create("rce_francium");
material_francium.liquid = <liquid:redstone_chemical_elements_francium_fluid>;
finishRceMaterial(
    material_francium,
    "钫",
    "Francium",
    0xD1D157,
    870,
    6.7,
    6.5,
    6,
    1.05,
    132,
    160,
    ["rce_trait_007"],
    ["rce_trait_001"],
    ["rce_trait_002"]
);

val material_gadolinium = MaterialBuilder.create("rce_gadolinium");
material_gadolinium.liquid = <liquid:redstone_chemical_elements_gadolinium_fluid>;
finishRceMaterial(
    material_gadolinium,
    "钆",
    "Gadolinium",
    0x57D19C,
    640,
    7.9,
    4.6,
    6,
    1.09,
    100,
    120,
    ["rce_trait_023"],
    ["rce_trait_026"],
    ["rce_trait_021", "rce_trait_022"]
);

val material_gallium = MaterialBuilder.create("rce_gallium");
material_gallium.liquid = <liquid:redstone_chemical_elements_gallium_fluid>;
finishRceMaterial(
    material_gallium,
    "镓",
    "Gallium",
    0xD1BC57,
    465,
    7.1,
    4.9,
    4,
    0.93,
    60,
    74,
    ["rce_trait_027"],
    ["rce_trait_028"],
    noTraits
);

val material_germanium = MaterialBuilder.create("rce_germanium");
material_germanium.liquid = <liquid:redstone_chemical_elements_germanium_fluid>;
finishRceMaterial(
    material_germanium,
    "锗",
    "Germanium",
    0xD1C657,
    645,
    8.1,
    5.2,
    6,
    1.05,
    94,
    114,
    ["rce_trait_013"],
    ["rce_trait_008"],
    ["rce_trait_009"]
);

val material_gold = MaterialBuilder.create("rce_gold");
material_gold.liquid = <liquid:redstone_chemical_elements_gold_fluid>;
finishRceMaterial(
    material_gold,
    "金",
    "Gold",
    0x57D1CD,
    525,
    6.0,
    4.1,
    4,
    1.05,
    88,
    106,
    ["rce_trait_029"],
    ["rce_trait_030"],
    ["rce_trait_031", "rce_trait_032"]
);

val material_hafnium = MaterialBuilder.create("rce_hafnium");
material_hafnium.liquid = <liquid:redstone_chemical_elements_hafnium_fluid>;
finishRceMaterial(
    material_hafnium,
    "铪",
    "Hafnium",
    0x57D18A,
    380,
    6.3,
    3.1,
    3,
    1.00,
    56,
    68,
    ["rce_trait_018"],
    ["rce_trait_003"],
    noTraits
);

val material_hassium = MaterialBuilder.create("rce_hassium");
material_hassium.liquid = <liquid:redstone_chemical_elements_hassium_fluid>;
finishRceMaterial(
    material_hassium,
    "",
    "Hassium",
    0xD1BE57,
    830,
    6.9,
    6.6,
    6,
    1.01,
    122,
    148,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007"]
);

val material_helium = MaterialBuilder.create("rce_helium");
material_helium.liquid = <liquid:redstone_chemical_elements_helium_fluid>;
finishRceMaterial(
    material_helium,
    "氦",
    "Helium",
    0x57D163,
    590,
    6.2,
    4.8,
    5,
    1.12,
    106,
    128,
    ["rce_trait_033"],
    ["rce_trait_034"],
    ["rce_trait_010"]
);

val material_holmium = MaterialBuilder.create("rce_holmium");
material_holmium.liquid = <liquid:redstone_chemical_elements_holmium_fluid>;
finishRceMaterial(
    material_holmium,
    "钬",
    "Holmium",
    0xD15798,
    455,
    6.4,
    3.3,
    4,
    1.01,
    68,
    82,
    ["rce_trait_022"],
    ["rce_trait_023"],
    noTraits
);

val material_hydrargyrum = MaterialBuilder.create("rce_hydrargyrum");
material_hydrargyrum.liquid = <liquid:redstone_chemical_elements_hydrargyrum_fluid>;
finishRceMaterial(
    material_hydrargyrum,
    "汞",
    "Hydrargyrum",
    0x578AD1,
    410,
    6.2,
    4.9,
    4,
    0.83,
    50,
    62,
    ["rce_trait_025"],
    ["rce_trait_024"],
    noTraits
);

val material_hydrogen = MaterialBuilder.create("rce_hydrogen");
material_hydrogen.liquid = <liquid:redstone_chemical_elements_hydrogen_fluid>;
finishRceMaterial(
    material_hydrogen,
    "氢",
    "Hydrogen",
    0x78D157,
    600,
    7.9,
    4.4,
    5,
    1.05,
    86,
    104,
    ["rce_trait_012"],
    ["rce_trait_013"],
    ["rce_trait_008"]
);

val material_indium = MaterialBuilder.create("rce_indium");
material_indium.liquid = <liquid:redstone_chemical_elements_indium_fluid>;
finishRceMaterial(
    material_indium,
    "铟",
    "Indium",
    0xBAD157,
    650,
    7.0,
    6.2,
    6,
    0.89,
    92,
    112,
    ["rce_trait_019"],
    ["rce_trait_020"],
    ["rce_trait_025", "rce_trait_024"]
);

val material_iodine = MaterialBuilder.create("rce_iodine");
material_iodine.liquid = <liquid:redstone_chemical_elements_iodine_fluid>;
finishRceMaterial(
    material_iodine,
    "碘",
    "Iodine",
    0xD19857,
    365,
    5.7,
    4.7,
    3,
    0.89,
    50,
    62,
    ["rce_trait_015"],
    ["rce_trait_016"],
    noTraits
);

val material_iridium = MaterialBuilder.create("rce_iridium");
material_iridium.liquid = <liquid:redstone_chemical_elements_iridium_fluid>;
finishRceMaterial(
    material_iridium,
    "铱",
    "Iridium",
    0x9657D1,
    480,
    5.8,
    3.2,
    4,
    1.05,
    80,
    96,
    ["rce_trait_030"],
    ["rce_trait_031"],
    ["rce_trait_032"]
);

val material_iron = MaterialBuilder.create("rce_iron");
material_iron.liquid = <liquid:redstone_chemical_elements_iron_fluid>;
finishRceMaterial(
    material_iron,
    "铁",
    "Iron",
    0x5776D1,
    650,
    7.0,
    6.2,
    6,
    0.89,
    92,
    112,
    ["rce_trait_024"],
    ["rce_trait_019"],
    ["rce_trait_020", "rce_trait_025"]
);

val material_krypton = MaterialBuilder.create("rce_krypton");
material_krypton.liquid = <liquid:redstone_chemical_elements_krypton_fluid>;
finishRceMaterial(
    material_krypton,
    "氪",
    "Krypton",
    0xD1CF57,
    545,
    6.0,
    3.9,
    4,
    1.12,
    98,
    118,
    ["rce_trait_033"],
    ["rce_trait_034"],
    noTraits
);

val material_lanthanum = MaterialBuilder.create("rce_lanthanum");
material_lanthanum.liquid = <liquid:redstone_chemical_elements_lanthanum_fluid>;
finishRceMaterial(
    material_lanthanum,
    "镧",
    "Lanthanum",
    0xD19057,
    475,
    7.1,
    4.2,
    4,
    0.97,
    64,
    78,
    ["rce_trait_035"],
    ["rce_trait_036"],
    ["rce_trait_027"]
);

val material_lawrencium = MaterialBuilder.create("rce_lawrencium");
material_lawrencium.liquid = <liquid:redstone_chemical_elements_lawrencium_fluid>;
finishRceMaterial(
    material_lawrencium,
    "铹",
    "Lawrencium",
    0xD19857,
    745,
    5.9,
    6.2,
    6,
    0.97,
    110,
    134,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006"]
);

val material_lead = MaterialBuilder.create("rce_lead");
material_lead.liquid = <liquid:redstone_chemical_elements_lead_fluid>;
finishRceMaterial(
    material_lead,
    "铅",
    "Lead",
    0xB2D157,
    480,
    6.1,
    5.1,
    4,
    0.81,
    62,
    76,
    ["rce_trait_024"],
    ["rce_trait_019"],
    noTraits
);

val material_lithium = MaterialBuilder.create("rce_lithium");
material_lithium.liquid = <liquid:redstone_chemical_elements_lithium_fluid>;
finishRceMaterial(
    material_lithium,
    "锂",
    "Lithium",
    0x57D16E,
    475,
    6.3,
    3.1,
    4,
    1.08,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    noTraits
);

val material_livermorium = MaterialBuilder.create("rce_livermorium");
material_livermorium.liquid = <liquid:redstone_chemical_elements_livermorium_fluid>;
finishRceMaterial(
    material_livermorium,
    "鉝",
    "Livermorium",
    0xD1B057,
    870,
    6.7,
    6.5,
    6,
    1.05,
    132,
    160,
    ["rce_trait_007"],
    ["rce_trait_001"],
    ["rce_trait_002"]
);

val material_longium = MaterialBuilder.create("rce_longium");
material_longium.liquid = <liquid:redstone_chemical_elements_longium_fluid>;
finishRceMaterial(
    material_longium,
    "鑨",
    "Longium",
    0x5780D1,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006", "rce_trait_007"]
);

val material_lutetium = MaterialBuilder.create("rce_lutetium");
material_lutetium.liquid = <liquid:redstone_chemical_elements_lutetium_fluid>;
finishRceMaterial(
    material_lutetium,
    "镥",
    "Lutetium",
    0x78D157,
    405,
    6.6,
    4.2,
    4,
    0.93,
    54,
    66,
    ["rce_trait_026"],
    ["rce_trait_021"],
    noTraits
);

val material_magnesium = MaterialBuilder.create("rce_magnesium");
material_magnesium.liquid = <liquid:redstone_chemical_elements_magnesium_fluid>;
finishRceMaterial(
    material_magnesium,
    "镁",
    "Magnesium",
    0xD1578E,
    485,
    7.3,
    3.3,
    4,
    1.11,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    ["rce_trait_018"]
);

val material_manganese = MaterialBuilder.create("rce_manganese");
material_manganese.liquid = <liquid:redstone_chemical_elements_manganese_fluid>;
finishRceMaterial(
    material_manganese,
    "锰",
    "Manganese",
    0xD157BE,
    650,
    7.0,
    6.2,
    6,
    0.89,
    92,
    112,
    ["rce_trait_020"],
    ["rce_trait_025"],
    ["rce_trait_024", "rce_trait_019"]
);

val material_meitnerium = MaterialBuilder.create("rce_meitnerium");
material_meitnerium.liquid = <liquid:redstone_chemical_elements_meitnerium_fluid>;
finishRceMaterial(
    material_meitnerium,
    "鿏",
    "Meitnerium",
    0xD157CF,
    565,
    5.5,
    5.0,
    4,
    0.93,
    80,
    98,
    ["rce_trait_001"],
    ["rce_trait_002"],
    noTraits
);

val material_mendelevium = MaterialBuilder.create("rce_mendelevium");
material_mendelevium.liquid = <liquid:redstone_chemical_elements_mendelevium_fluid>;
finishRceMaterial(
    material_mendelevium,
    "钔",
    "Mendelevium",
    0xD18E57,
    830,
    6.9,
    6.6,
    6,
    1.01,
    122,
    148,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007"]
);

val material_molybdenum = MaterialBuilder.create("rce_molybdenum");
material_molybdenum.liquid = <liquid:redstone_chemical_elements_molybdenum_fluid>;
finishRceMaterial(
    material_molybdenum,
    "钼",
    "Molybdenum",
    0x6157D1,
    505,
    6.2,
    4.9,
    4,
    0.91,
    72,
    88,
    ["rce_trait_019"],
    ["rce_trait_020"],
    ["rce_trait_025"]
);

val material_moscovium = MaterialBuilder.create("rce_moscovium");
material_moscovium.liquid = <liquid:redstone_chemical_elements_moscovium_fluid>;
finishRceMaterial(
    material_moscovium,
    "镆",
    "Moscovium",
    0x5778D1,
    745,
    6.5,
    5.4,
    6,
    1.05,
    114,
    138,
    ["rce_trait_007"],
    ["rce_trait_001"],
    noTraits
);

val material_mysterium = MaterialBuilder.create("rce_mysterium");
material_mysterium.liquid = <liquid:redstone_chemical_elements_mysterium_fluid>;
finishRceMaterial(
    material_mysterium,
    "镾",
    "Mysterium",
    0xC957D1,
    565,
    5.5,
    5.0,
    4,
    0.93,
    80,
    98,
    ["rce_trait_001"],
    ["rce_trait_002"],
    noTraits
);

val material_neodymium = MaterialBuilder.create("rce_neodymium");
material_neodymium.liquid = <liquid:redstone_chemical_elements_neodymium_fluid>;
finishRceMaterial(
    material_neodymium,
    "钕",
    "Neodymium",
    0x86D157,
    460,
    6.8,
    4.3,
    4,
    0.97,
    66,
    80,
    ["rce_trait_026"],
    ["rce_trait_021"],
    ["rce_trait_022"]
);

val material_neon = MaterialBuilder.create("rce_neon");
material_neon.liquid = <liquid:redstone_chemical_elements_neon_fluid>;
finishRceMaterial(
    material_neon,
    "氖",
    "Neon",
    0xD1B857,
    715,
    7.0,
    5.0,
    6,
    1.20,
    128,
    154,
    ["rce_trait_033"],
    ["rce_trait_034"],
    ["rce_trait_010", "rce_trait_011"]
);

val material_neptunium = MaterialBuilder.create("rce_neptunium");
material_neptunium.liquid = <liquid:redstone_chemical_elements_neptunium_fluid>;
finishRceMaterial(
    material_neptunium,
    "镎",
    "Neptunium",
    0x7657D1,
    745,
    6.5,
    5.4,
    6,
    1.05,
    114,
    138,
    ["rce_trait_007"],
    ["rce_trait_001"],
    noTraits
);

val material_nickel = MaterialBuilder.create("rce_nickel");
material_nickel.liquid = <liquid:redstone_chemical_elements_nickel_fluid>;
finishRceMaterial(
    material_nickel,
    "镍",
    "Nickel",
    0xD1B457,
    525,
    6.2,
    6.0,
    5,
    0.81,
    70,
    86,
    ["rce_trait_025"],
    ["rce_trait_024"],
    ["rce_trait_019"]
);

val material_nihonium = MaterialBuilder.create("rce_nihonium");
material_nihonium.liquid = <liquid:redstone_chemical_elements_nihonium_fluid>;
finishRceMaterial(
    material_nihonium,
    "鉨",
    "Nihonium",
    0x57D16A,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007", "rce_trait_001"]
);

val material_niobium = MaterialBuilder.create("rce_niobium");
material_niobium.liquid = <liquid:redstone_chemical_elements_niobium_fluid>;
finishRceMaterial(
    material_niobium,
    "铌",
    "Niobium",
    0x7AD157,
    570,
    7.1,
    4.2,
    5,
    1.05,
    86,
    104,
    ["rce_trait_028"],
    ["rce_trait_035"],
    noTraits
);

val material_nitrogen = MaterialBuilder.create("rce_nitrogen");
material_nitrogen.liquid = <liquid:redstone_chemical_elements_nitrogen_fluid>;
finishRceMaterial(
    material_nitrogen,
    "氮",
    "Nitrogen",
    0x57CFD1,
    560,
    8.2,
    4.5,
    5,
    1.01,
    76,
    92,
    ["rce_trait_008"],
    ["rce_trait_009"],
    ["rce_trait_012"]
);

val material_nobelium = MaterialBuilder.create("rce_nobelium");
material_nobelium.liquid = <liquid:redstone_chemical_elements_nobelium_fluid>;
finishRceMaterial(
    material_nobelium,
    "锘",
    "Nobelium",
    0xD16157,
    745,
    5.9,
    6.2,
    6,
    0.97,
    110,
    134,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006"]
);

val material_oganesson = MaterialBuilder.create("rce_oganesson");
material_oganesson.liquid = <liquid:redstone_chemical_elements_oganesson_fluid>;
finishRceMaterial(
    material_oganesson,
    "鿫",
    "Oganesson",
    0xBAD157,
    525,
    5.7,
    5.2,
    4,
    0.89,
    70,
    86,
    ["rce_trait_002"],
    ["rce_trait_006"],
    noTraits
);

val material_osmium = MaterialBuilder.create("rce_osmium");
material_osmium.liquid = <liquid:redstone_chemical_elements_osmium_fluid>;
finishRceMaterial(
    material_osmium,
    "锇",
    "Osmium",
    0xD157A4,
    320,
    5.5,
    3.8,
    3,
    0.89,
    42,
    52,
    ["rce_trait_032"],
    ["rce_trait_029"],
    noTraits
);

val material_oxygen = MaterialBuilder.create("rce_oxygen");
material_oxygen.liquid = <liquid:redstone_chemical_elements_oxygen_fluid>;
finishRceMaterial(
    material_oxygen,
    "氧",
    "Oxygen",
    0xD1578A,
    560,
    8.2,
    4.5,
    5,
    1.01,
    76,
    92,
    ["rce_trait_008"],
    ["rce_trait_009"],
    ["rce_trait_012"]
);

val material_palladium = MaterialBuilder.create("rce_palladium");
material_palladium.liquid = <liquid:redstone_chemical_elements_palladium_fluid>;
finishRceMaterial(
    material_palladium,
    "钯",
    "Palladium",
    0x57B0D1,
    525,
    6.0,
    4.1,
    4,
    1.05,
    88,
    106,
    ["rce_trait_030"],
    ["rce_trait_031"],
    ["rce_trait_032", "rce_trait_029"]
);

val material_phosphorus = MaterialBuilder.create("rce_phosphorus");
material_phosphorus.liquid = <liquid:redstone_chemical_elements_phosphorus_fluid>;
finishRceMaterial(
    material_phosphorus,
    "磷",
    "Phosphorus",
    0x61D157,
    475,
    7.1,
    4.2,
    4,
    0.97,
    64,
    78,
    ["rce_trait_012"],
    ["rce_trait_013"],
    noTraits
);

val material_platinum = MaterialBuilder.create("rce_platinum");
material_platinum.liquid = <liquid:redstone_chemical_elements_platinum_fluid>;
finishRceMaterial(
    material_platinum,
    "铂",
    "Platinum",
    0x5770D1,
    470,
    5.8,
    3.9,
    4,
    1.01,
    76,
    92,
    ["rce_trait_032"],
    ["rce_trait_029"],
    ["rce_trait_030"]
);

val material_plutonium = MaterialBuilder.create("rce_plutonium");
material_plutonium.liquid = <liquid:redstone_chemical_elements_plutonium_fluid>;
finishRceMaterial(
    material_plutonium,
    "钚",
    "Plutonium",
    0xD157B8,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_007"],
    ["rce_trait_001"],
    ["rce_trait_002", "rce_trait_006"]
);

val material_polonium = MaterialBuilder.create("rce_polonium");
material_polonium.liquid = <liquid:redstone_chemical_elements_polonium_fluid>;
finishRceMaterial(
    material_polonium,
    "钋",
    "Polonium",
    0xD15794,
    565,
    5.5,
    5.0,
    4,
    0.93,
    80,
    98,
    ["rce_trait_001"],
    ["rce_trait_002"],
    noTraits
);

val material_potassium = MaterialBuilder.create("rce_potassium");
material_potassium.liquid = <liquid:redstone_chemical_elements_potassium_fluid>;
finishRceMaterial(
    material_potassium,
    "钾",
    "Potassium",
    0xD18457,
    625,
    6.6,
    3.3,
    5,
    1.20,
    112,
    134,
    ["rce_trait_003"],
    ["rce_trait_004"],
    ["rce_trait_005"]
);

val material_praseodymium = MaterialBuilder.create("rce_praseodymium");
material_praseodymium.liquid = <liquid:redstone_chemical_elements_praseodymium_fluid>;
finishRceMaterial(
    material_praseodymium,
    "镨",
    "Praseodymium",
    0x92D157,
    540,
    7.4,
    3.6,
    5,
    1.05,
    80,
    96,
    ["rce_trait_021"],
    ["rce_trait_022"],
    ["rce_trait_023"]
);

val material_promethium = MaterialBuilder.create("rce_promethium");
material_promethium.liquid = <liquid:redstone_chemical_elements_promethium_fluid>;
finishRceMaterial(
    material_promethium,
    "钷",
    "Promethium",
    0xD1575F,
    455,
    6.4,
    3.3,
    4,
    1.01,
    68,
    82,
    ["rce_trait_022"],
    ["rce_trait_023"],
    noTraits
);

val material_protactinium = MaterialBuilder.create("rce_protactinium");
material_protactinium.liquid = <liquid:redstone_chemical_elements_protactinium_fluid>;
finishRceMaterial(
    material_protactinium,
    "镤",
    "Protactinium",
    0x6E57D1,
    565,
    5.5,
    5.0,
    4,
    0.93,
    80,
    98,
    ["rce_trait_001"],
    ["rce_trait_002"],
    noTraits
);

val material_radium = MaterialBuilder.create("rce_radium");
material_radium.liquid = <liquid:redstone_chemical_elements_radium_fluid>;
finishRceMaterial(
    material_radium,
    "镭",
    "Radium",
    0xD1577A,
    830,
    6.9,
    6.6,
    6,
    1.01,
    122,
    148,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007"]
);

val material_radon = MaterialBuilder.create("rce_radon");
material_radon.liquid = <liquid:redstone_chemical_elements_radon_fluid>;
finishRceMaterial(
    material_radon,
    "氡",
    "Radon",
    0xD18657,
    715,
    7.0,
    5.0,
    6,
    1.20,
    128,
    154,
    ["rce_trait_033"],
    ["rce_trait_034"],
    ["rce_trait_010", "rce_trait_011"]
);

val material_rhenium = MaterialBuilder.create("rce_rhenium");
material_rhenium.liquid = <liquid:redstone_chemical_elements_rhenium_fluid>;
finishRceMaterial(
    material_rhenium,
    "铼",
    "Rhenium",
    0x90D157,
    390,
    6.1,
    3.9,
    3,
    0.93,
    52,
    64,
    ["rce_trait_020"],
    ["rce_trait_025"],
    noTraits
);

val material_rhodium = MaterialBuilder.create("rce_rhodium");
material_rhodium.liquid = <liquid:redstone_chemical_elements_rhodium_fluid>;
finishRceMaterial(
    material_rhodium,
    "铑",
    "Rhodium",
    0x8057D1,
    480,
    5.8,
    3.2,
    4,
    1.05,
    80,
    96,
    ["rce_trait_030"],
    ["rce_trait_031"],
    ["rce_trait_032"]
);

val material_roentgenium = MaterialBuilder.create("rce_roentgenium");
material_roentgenium.liquid = <liquid:redstone_chemical_elements_roentgenium_fluid>;
finishRceMaterial(
    material_roentgenium,
    "",
    "Roentgenium",
    0x57C4D1,
    1050,
    7.1,
    7.7,
    6,
    1.09,
    162,
    196,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007", "rce_trait_001"]
);

val material_rubidium = MaterialBuilder.create("rce_rubidium");
material_rubidium.liquid = <liquid:redstone_chemical_elements_rubidium_fluid>;
finishRceMaterial(
    material_rubidium,
    "铷",
    "Rubidium",
    0x7E57D1,
    475,
    6.3,
    3.1,
    4,
    1.08,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    noTraits
);

val material_ruthenium = MaterialBuilder.create("rce_ruthenium");
material_ruthenium.liquid = <liquid:redstone_chemical_elements_ruthenium_fluid>;
finishRceMaterial(
    material_ruthenium,
    "钌",
    "Ruthenium",
    0x72D157,
    470,
    5.8,
    3.9,
    4,
    1.01,
    76,
    92,
    ["rce_trait_029"],
    ["rce_trait_030"],
    ["rce_trait_031"]
);

val material_rutherfordium = MaterialBuilder.create("rce_rutherfordium");
material_rutherfordium.liquid = <liquid:redstone_chemical_elements_rutherfordium_fluid>;
finishRceMaterial(
    material_rutherfordium,
    "鑪",
    "Rutherfordium",
    0x9A57D1,
    745,
    5.9,
    6.2,
    6,
    0.97,
    110,
    134,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006"]
);

val material_samarium = MaterialBuilder.create("rce_samarium");
material_samarium.liquid = <liquid:redstone_chemical_elements_samarium_fluid>;
finishRceMaterial(
    material_samarium,
    "钐",
    "Samarium",
    0xCD57D1,
    405,
    6.6,
    4.2,
    4,
    0.93,
    54,
    66,
    ["rce_trait_026"],
    ["rce_trait_021"],
    noTraits
);

val material_scandium = MaterialBuilder.create("rce_scandium");
material_scandium.liquid = <liquid:redstone_chemical_elements_scandium_fluid>;
finishRceMaterial(
    material_scandium,
    "钪",
    "Scandium",
    0x90D157,
    475,
    6.3,
    3.1,
    4,
    1.08,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    noTraits
);

val material_seaborgium = MaterialBuilder.create("rce_seaborgium");
material_seaborgium.liquid = <liquid:redstone_chemical_elements_seaborgium_fluid>;
finishRceMaterial(
    material_seaborgium,
    "",
    "Seaborgium",
    0xD1B457,
    870,
    6.7,
    6.5,
    6,
    1.05,
    132,
    160,
    ["rce_trait_007"],
    ["rce_trait_001"],
    ["rce_trait_002"]
);

val material_selenium = MaterialBuilder.create("rce_selenium");
material_selenium.liquid = <liquid:redstone_chemical_elements_selenium_fluid>;
finishRceMaterial(
    material_selenium,
    "硒",
    "Selenium",
    0xA4D157,
    730,
    9.2,
    5.6,
    6,
    1.09,
    106,
    128,
    ["rce_trait_009"],
    ["rce_trait_012"],
    ["rce_trait_013", "rce_trait_008"]
);

val material_silicon = MaterialBuilder.create("rce_silicon");
material_silicon.liquid = <liquid:redstone_chemical_elements_silicon_fluid>;
finishRceMaterial(
    material_silicon,
    "硅",
    "Silicon",
    0x57A4D1,
    475,
    7.1,
    4.2,
    4,
    0.97,
    64,
    78,
    ["rce_trait_012"],
    ["rce_trait_013"],
    noTraits
);

val material_silver = MaterialBuilder.create("rce_silver");
material_silver.liquid = <liquid:redstone_chemical_elements_silver_fluid>;
finishRceMaterial(
    material_silver,
    "银",
    "Silver",
    0xD1CB57,
    470,
    5.8,
    3.9,
    4,
    1.01,
    76,
    92,
    ["rce_trait_032"],
    ["rce_trait_029"],
    ["rce_trait_030"]
);

val material_sodium = MaterialBuilder.create("rce_sodium");
material_sodium.liquid = <liquid:redstone_chemical_elements_sodium_fluid>;
finishRceMaterial(
    material_sodium,
    "钠",
    "Sodium",
    0x57D19A,
    635,
    7.6,
    3.5,
    5,
    1.23,
    112,
    134,
    ["rce_trait_005"],
    ["rce_trait_018"],
    ["rce_trait_003", "rce_trait_004"]
);

val material_strontium = MaterialBuilder.create("rce_strontium");
material_strontium.liquid = <liquid:redstone_chemical_elements_strontium_fluid>;
finishRceMaterial(
    material_strontium,
    "锶",
    "Strontium",
    0x8457D1,
    380,
    6.3,
    3.1,
    3,
    1.00,
    56,
    68,
    ["rce_trait_018"],
    ["rce_trait_003"],
    noTraits
);

val material_sulfur = MaterialBuilder.create("rce_sulfur");
material_sulfur.liquid = <liquid:redstone_chemical_elements_sulfur_fluid>;
finishRceMaterial(
    material_sulfur,
    "硫",
    "Sulfur",
    0xD15B57,
    600,
    7.9,
    4.4,
    5,
    1.05,
    86,
    104,
    ["rce_trait_012"],
    ["rce_trait_013"],
    ["rce_trait_008"]
);

val material_tantalum = MaterialBuilder.create("rce_tantalum");
material_tantalum.liquid = <liquid:redstone_chemical_elements_tantalum_fluid>;
finishRceMaterial(
    material_tantalum,
    "钽",
    "Tantalum",
    0x57D19C,
    485,
    7.3,
    3.3,
    4,
    1.11,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    ["rce_trait_018"]
);

val material_technetium = MaterialBuilder.create("rce_technetium");
material_technetium.liquid = <liquid:redstone_chemical_elements_technetium_fluid>;
finishRceMaterial(
    material_technetium,
    "锝",
    "Technetium",
    0x57D1AA,
    400,
    6.1,
    3.1,
    3,
    0.97,
    56,
    68,
    ["rce_trait_035"],
    ["rce_trait_036"],
    noTraits
);

val material_tellurium = MaterialBuilder.create("rce_tellurium");
material_tellurium.liquid = <liquid:redstone_chemical_elements_tellurium_fluid>;
finishRceMaterial(
    material_tellurium,
    "碲",
    "Tellurium",
    0x578ED1,
    435,
    7.4,
    4.3,
    4,
    0.93,
    54,
    66,
    ["rce_trait_009"],
    ["rce_trait_012"],
    noTraits
);

val material_tennessine = MaterialBuilder.create("rce_tennessine");
material_tennessine.liquid = <liquid:redstone_chemical_elements_tennessine_fluid>;
finishRceMaterial(
    material_tennessine,
    "鿬",
    "Tennessine",
    0xD15957,
    830,
    6.9,
    6.6,
    6,
    1.01,
    122,
    148,
    ["rce_trait_002"],
    ["rce_trait_006"],
    ["rce_trait_007"]
);

val material_terbium = MaterialBuilder.create("rce_terbium");
material_terbium.liquid = <liquid:redstone_chemical_elements_terbium_fluid>;
finishRceMaterial(
    material_terbium,
    "铽",
    "Terbium",
    0x8ED157,
    640,
    7.9,
    4.6,
    6,
    1.09,
    100,
    120,
    ["rce_trait_021"],
    ["rce_trait_022"],
    ["rce_trait_023", "rce_trait_026"]
);

val material_thallium = MaterialBuilder.create("rce_thallium");
material_thallium.liquid = <liquid:redstone_chemical_elements_thallium_fluid>;
finishRceMaterial(
    material_thallium,
    "铊",
    "Thallium",
    0xC4D157,
    400,
    6.1,
    3.1,
    3,
    0.97,
    56,
    68,
    ["rce_trait_035"],
    ["rce_trait_036"],
    noTraits
);

val material_thorium = MaterialBuilder.create("rce_thorium");
material_thorium.liquid = <liquid:redstone_chemical_elements_thorium_fluid>;
finishRceMaterial(
    material_thorium,
    "钍",
    "Thorium",
    0xD1B257,
    745,
    5.9,
    6.2,
    6,
    0.97,
    110,
    134,
    ["rce_trait_001"],
    ["rce_trait_002"],
    ["rce_trait_006"]
);

val material_thulium = MaterialBuilder.create("rce_thulium");
material_thulium.liquid = <liquid:redstone_chemical_elements_thulium_fluid>;
finishRceMaterial(
    material_thulium,
    "铥",
    "Thulium",
    0xD15761,
    640,
    7.9,
    4.6,
    6,
    1.09,
    100,
    120,
    ["rce_trait_026"],
    ["rce_trait_021"],
    ["rce_trait_022", "rce_trait_023"]
);

val material_tin = MaterialBuilder.create("rce_tin");
material_tin.liquid = <liquid:redstone_chemical_elements_tin_fluid>;
finishRceMaterial(
    material_tin,
    "锡",
    "Tin",
    0xD15767,
    475,
    6.3,
    3.1,
    4,
    1.08,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    noTraits
);

val material_titanium = MaterialBuilder.create("rce_titanium");
material_titanium.liquid = <liquid:redstone_chemical_elements_titanium_fluid>;
finishRceMaterial(
    material_titanium,
    "钛",
    "Titanium",
    0x57A4D1,
    530,
    6.6,
    3.3,
    4,
    1.12,
    90,
    108,
    ["rce_trait_005"],
    ["rce_trait_018"],
    ["rce_trait_003"]
);

val material_tungsten = MaterialBuilder.create("rce_tungsten");
material_tungsten.liquid = <liquid:redstone_chemical_elements_tungsten_fluid>;
finishRceMaterial(
    material_tungsten,
    "钨",
    "Tungsten",
    0xD1AC57,
    525,
    6.2,
    6.0,
    5,
    0.81,
    70,
    86,
    ["rce_trait_025"],
    ["rce_trait_024"],
    ["rce_trait_019"]
);

val material_uranium = MaterialBuilder.create("rce_uranium");
material_uranium.liquid = <liquid:redstone_chemical_elements_uranium_fluid>;
finishRceMaterial(
    material_uranium,
    "uranium",
    "Uranium",
    0xD1B857,
    870,
    6.7,
    6.5,
    6,
    1.05,
    132,
    160,
    ["rce_trait_007"],
    ["rce_trait_001"],
    ["rce_trait_002"]
);

val material_vanadium = MaterialBuilder.create("rce_vanadium");
material_vanadium.liquid = <liquid:redstone_chemical_elements_vanadium_fluid>;
finishRceMaterial(
    material_vanadium,
    "钒",
    "Vanadium",
    0xD15776,
    475,
    6.3,
    3.1,
    4,
    1.08,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    noTraits
);

val material_xenon = MaterialBuilder.create("rce_xenon");
material_xenon.liquid = <liquid:redstone_chemical_elements_xenon_fluid>;
finishRceMaterial(
    material_xenon,
    "氙",
    "Xenon",
    0x5FD157,
    565,
    6.7,
    4.9,
    5,
    1.08,
    94,
    114,
    ["rce_trait_034"],
    ["rce_trait_010"],
    ["rce_trait_011"]
);

val material_ytterbium = MaterialBuilder.create("rce_ytterbium");
material_ytterbium.liquid = <liquid:redstone_chemical_elements_ytterbium_fluid>;
finishRceMaterial(
    material_ytterbium,
    "镱",
    "Ytterbium",
    0x8257D1,
    640,
    7.9,
    4.6,
    6,
    1.09,
    100,
    120,
    ["rce_trait_023"],
    ["rce_trait_026"],
    ["rce_trait_021", "rce_trait_022"]
);

val material_yttrium = MaterialBuilder.create("rce_yttrium");
material_yttrium.liquid = <liquid:redstone_chemical_elements_yttrium_fluid>;
finishRceMaterial(
    material_yttrium,
    "钇",
    "Yttrium",
    0x76D157,
    475,
    6.3,
    3.1,
    4,
    1.08,
    78,
    94,
    ["rce_trait_003"],
    ["rce_trait_004"],
    noTraits
);

val material_zinc = MaterialBuilder.create("rce_zinc");
material_zinc.liquid = <liquid:redstone_chemical_elements_zinc_fluid>;
finishRceMaterial(
    material_zinc,
    "锌",
    "Zinc",
    0xD15788,
    485,
    7.3,
    3.3,
    4,
    1.11,
    78,
    94,
    ["rce_trait_004"],
    ["rce_trait_005"],
    ["rce_trait_018"]
);

val material_zirconium = MaterialBuilder.create("rce_zirconium");
material_zirconium.liquid = <liquid:redstone_chemical_elements_zirconium_fluid>;
finishRceMaterial(
    material_zirconium,
    "锆",
    "Zirconium",
    0x7E57D1,
    635,
    7.6,
    3.5,
    5,
    1.23,
    112,
    134,
    ["rce_trait_005"],
    ["rce_trait_018"],
    ["rce_trait_003", "rce_trait_004"]
);

