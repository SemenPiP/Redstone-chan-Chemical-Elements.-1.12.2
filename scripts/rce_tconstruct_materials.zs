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

val material_advanced_alloy = MaterialBuilder.create("rce_advanced_alloy");
material_advanced_alloy.liquid = <liquid:redstone_chemical_elements_advanced_alloy_fluid>;
finishRceMaterial(
    material_advanced_alloy,
    "高级合金",
    "AdvancedAlloy",
    0x8C5D32,
    320,
    6.5,
    4.0,
    3,
    1.00,
    40,
    55,
    ["rce_trait_037"],
    ["rce_trait_038"],
    ["rce_trait_039"]
);

val material_arcane_machine_alloy = MaterialBuilder.create("rce_arcane_machine_alloy");
material_arcane_machine_alloy.liquid = <liquid:redstone_chemical_elements_arcane_machine_alloy_fluid>;
finishRceMaterial(
    material_arcane_machine_alloy,
    "奥术机械合金",
    "ArcaneMachineAlloy",
    0x7A59C7,
    780,
    8.5,
    6.4,
    4,
    1.10,
    80,
    90,
    ["rce_trait_040"],
    ["rce_trait_041"],
    ["rce_trait_042"]
);

val material_brass = MaterialBuilder.create("rce_brass");
material_brass.liquid = <liquid:redstone_chemical_elements_brass_fluid>;
finishRceMaterial(
    material_brass,
    "黄铜",
    "Brass",
    0xB38A2B,
    220,
    7.2,
    3.7,
    2,
    0.90,
    25,
    35,
    ["rce_trait_043"],
    ["rce_trait_044"],
    noTraits
);

val material_bronze = MaterialBuilder.create("rce_bronze");
material_bronze.liquid = <liquid:redstone_chemical_elements_bronze_fluid>;
finishRceMaterial(
    material_bronze,
    "青铜",
    "Bronze",
    0x8E695C,
    300,
    6.3,
    4.2,
    3,
    1.05,
    35,
    45,
    ["rce_trait_045"],
    ["rce_trait_046"],
    noTraits
);

val material_circuit_steel = MaterialBuilder.create("rce_circuit_steel");
material_circuit_steel.liquid = <liquid:redstone_chemical_elements_circuit_steel_fluid>;
finishRceMaterial(
    material_circuit_steel,
    "电路钢",
    "CircuitSteel",
    0x5E7083,
    760,
    8.0,
    6.0,
    4,
    1.15,
    90,
    100,
    ["rce_trait_047"],
    ["rce_trait_048"],
    ["rce_trait_049"]
);

val material_conductive_iron = MaterialBuilder.create("rce_conductive_iron");
material_conductive_iron.liquid = <liquid:redstone_chemical_elements_conductive_iron_fluid>;
finishRceMaterial(
    material_conductive_iron,
    "导电铁",
    "ConductiveIron",
    0x8C5C43,
    280,
    6.2,
    4.2,
    3,
    0.95,
    30,
    45,
    ["rce_trait_050"],
    ["rce_trait_051"],
    noTraits
);

val material_constantan = MaterialBuilder.create("rce_constantan");
material_constantan.liquid = <liquid:redstone_chemical_elements_constantan_fluid>;
finishRceMaterial(
    material_constantan,
    "康铜",
    "Constantan",
    0x8B7562,
    260,
    6.0,
    4.0,
    2,
    0.95,
    25,
    40,
    ["rce_trait_052"],
    ["rce_trait_053"],
    ["rce_trait_054"]
);

val material_construction_alloy = MaterialBuilder.create("rce_construction_alloy");
material_construction_alloy.liquid = <liquid:redstone_chemical_elements_construction_alloy_fluid>;
finishRceMaterial(
    material_construction_alloy,
    "构造合金",
    "ConstructionAlloy",
    0xB27A2C,
    300,
    6.4,
    4.0,
    3,
    1.00,
    35,
    50,
    ["rce_trait_055"],
    ["rce_trait_056"],
    ["rce_trait_057"]
);

val material_cryo_circuit_alloy = MaterialBuilder.create("rce_cryo_circuit_alloy");
material_cryo_circuit_alloy.liquid = <liquid:redstone_chemical_elements_cryo_circuit_alloy_fluid>;
finishRceMaterial(
    material_cryo_circuit_alloy,
    "低温电路合金",
    "CryoCircuitAlloy",
    0x5BAFD8,
    840,
    8.8,
    6.6,
    5,
    1.15,
    95,
    105,
    ["rce_trait_058"],
    ["rce_trait_059"],
    ["rce_trait_060"]
);

val material_cryotheum_alloy = MaterialBuilder.create("rce_cryotheum_alloy");
material_cryotheum_alloy.liquid = <liquid:redstone_chemical_elements_cryotheum_alloy_fluid>;
finishRceMaterial(
    material_cryotheum_alloy,
    "寒霜合金",
    "CryotheumAlloy",
    0x5FB8DC,
    860,
    9.4,
    6.8,
    5,
    1.15,
    100,
    115,
    ["rce_trait_061"],
    ["rce_trait_062"],
    ["rce_trait_063"]
);

val material_dark_steel = MaterialBuilder.create("rce_dark_steel");
material_dark_steel.liquid = <liquid:redstone_chemical_elements_dark_steel_fluid>;
finishRceMaterial(
    material_dark_steel,
    "暗钢",
    "DarkSteel",
    0x3B4250,
    900,
    8.6,
    7.2,
    5,
    1.20,
    100,
    115,
    ["rce_trait_064"],
    ["rce_trait_065"],
    noTraits
);

val material_diamond = MaterialBuilder.create("rce_diamond");
material_diamond.liquid = <liquid:redstone_chemical_elements_diamond_fluid>;
finishRceMaterial(
    material_diamond,
    "钻石",
    "Diamond",
    0x449083,
    900,
    7.5,
    6.5,
    4,
    0.95,
    60,
    110,
    ["rce_trait_066"],
    ["rce_trait_067"],
    noTraits
);

val material_electrical_steel = MaterialBuilder.create("rce_electrical_steel");
material_electrical_steel.liquid = <liquid:redstone_chemical_elements_electrical_steel_fluid>;
finishRceMaterial(
    material_electrical_steel,
    "电气钢",
    "ElectricalSteel",
    0x6E7D8D,
    460,
    7.3,
    5.0,
    4,
    1.05,
    55,
    70,
    ["rce_trait_068"],
    ["rce_trait_069"],
    ["rce_trait_070"]
);

val material_electrum = MaterialBuilder.create("rce_electrum");
material_electrum.liquid = <liquid:redstone_chemical_elements_electrum_fluid>;
finishRceMaterial(
    material_electrum,
    "琥珀金",
    "Electrum",
    0xD2B46F,
    190,
    9.8,
    3.8,
    3,
    0.85,
    20,
    30,
    ["rce_trait_071"],
    ["rce_trait_072"],
    ["rce_trait_073"]
);

val material_elementium = MaterialBuilder.create("rce_elementium");
material_elementium.liquid = <liquid:redstone_chemical_elements_elementium_fluid>;
finishRceMaterial(
    material_elementium,
    "源质钢",
    "Elementium",
    0xA458A0,
    820,
    9.0,
    6.6,
    5,
    1.20,
    90,
    110,
    ["rce_trait_074"],
    ["rce_trait_075"],
    ["rce_trait_076"]
);

val material_emerald = MaterialBuilder.create("rce_emerald");
material_emerald.liquid = <liquid:redstone_chemical_elements_emerald_fluid>;
finishRceMaterial(
    material_emerald,
    "绿宝石",
    "Emerald",
    0x5E9B67,
    820,
    6.8,
    6.1,
    4,
    0.90,
    55,
    95,
    ["rce_trait_077"],
    ["rce_trait_078"],
    noTraits
);

val material_end_steel = MaterialBuilder.create("rce_end_steel");
material_end_steel.liquid = <liquid:redstone_chemical_elements_end_steel_fluid>;
finishRceMaterial(
    material_end_steel,
    "末地钢",
    "EndSteel",
    0x50697C,
    980,
    8.9,
    7.4,
    5,
    1.20,
    105,
    125,
    ["rce_trait_079"],
    ["rce_trait_080"],
    ["rce_trait_081"]
);

val material_enderium = MaterialBuilder.create("rce_enderium");
material_enderium.liquid = <liquid:redstone_chemical_elements_enderium_fluid>;
finishRceMaterial(
    material_enderium,
    "末影",
    "Enderium",
    0x506470,
    980,
    9.0,
    7.0,
    5,
    1.15,
    110,
    125,
    ["rce_trait_082"],
    ["rce_trait_083"],
    noTraits
);

val material_energetic_alloy = MaterialBuilder.create("rce_energetic_alloy");
material_energetic_alloy.liquid = <liquid:redstone_chemical_elements_energetic_alloy_fluid>;
finishRceMaterial(
    material_energetic_alloy,
    "充能合金",
    "EnergeticAlloy",
    0xCC8A28,
    560,
    9.2,
    5.9,
    4,
    1.10,
    65,
    85,
    ["rce_trait_084"],
    ["rce_trait_085"],
    ["rce_trait_086"]
);

val material_fluxed_electrum = MaterialBuilder.create("rce_fluxed_electrum");
material_fluxed_electrum.liquid = <liquid:redstone_chemical_elements_fluxed_electrum_fluid>;
finishRceMaterial(
    material_fluxed_electrum,
    "通量琥珀金",
    "FluxedElectrum",
    0xE3B85B,
    620,
    10.4,
    6.1,
    5,
    1.15,
    75,
    95,
    ["rce_trait_087"],
    ["rce_trait_088"],
    ["rce_trait_089"]
);

val material_gaia_alloy = MaterialBuilder.create("rce_gaia_alloy");
material_gaia_alloy.liquid = <liquid:redstone_chemical_elements_gaia_alloy_fluid>;
finishRceMaterial(
    material_gaia_alloy,
    "盖亚合金",
    "GaiaAlloy",
    0xCE669C,
    1280,
    10.4,
    8.2,
    6,
    1.30,
    140,
    165,
    ["rce_trait_090"],
    ["rce_trait_091"],
    ["rce_trait_092", "rce_trait_093"]
);

val material_invar = MaterialBuilder.create("rce_invar");
material_invar.liquid = <liquid:redstone_chemical_elements_invar_fluid>;
finishRceMaterial(
    material_invar,
    "殷钢",
    "Invar",
    0x747E7D,
    650,
    7.0,
    5.5,
    4,
    1.10,
    70,
    85,
    ["rce_trait_094"],
    ["rce_trait_095"],
    noTraits
);

val material_iridium_alloy = MaterialBuilder.create("rce_iridium_alloy");
material_iridium_alloy.liquid = <liquid:redstone_chemical_elements_iridium_alloy_fluid>;
finishRceMaterial(
    material_iridium_alloy,
    "铱合金",
    "IridiumAlloy",
    0x5C8790,
    1250,
    9.8,
    8.0,
    6,
    1.30,
    135,
    155,
    ["rce_trait_096"],
    ["rce_trait_097"],
    ["rce_trait_098"]
);

val material_living_steel = MaterialBuilder.create("rce_living_steel");
material_living_steel.liquid = <liquid:redstone_chemical_elements_living_steel_fluid>;
finishRceMaterial(
    material_living_steel,
    "生命钢",
    "LivingSteel",
    0x6FBC8F,
    840,
    8.7,
    6.7,
    5,
    1.20,
    95,
    115,
    ["rce_trait_099"],
    ["rce_trait_100"],
    ["rce_trait_101"]
);

val material_lumium = MaterialBuilder.create("rce_lumium");
material_lumium.liquid = <liquid:redstone_chemical_elements_lumium_fluid>;
finishRceMaterial(
    material_lumium,
    "流明",
    "Lumium",
    0xD6C27A,
    420,
    9.5,
    5.0,
    4,
    1.00,
    45,
    70,
    ["rce_trait_102"],
    ["rce_trait_103"],
    ["rce_trait_104"]
);

val material_machine_steel = MaterialBuilder.create("rce_machine_steel");
material_machine_steel.liquid = <liquid:redstone_chemical_elements_machine_steel_fluid>;
finishRceMaterial(
    material_machine_steel,
    "机械钢",
    "MachineSteel",
    0x7D93A6,
    720,
    8.3,
    6.2,
    4,
    1.10,
    85,
    95,
    ["rce_trait_105"],
    ["rce_trait_106"],
    ["rce_trait_107"]
);

val material_mana_coil_alloy = MaterialBuilder.create("rce_mana_coil_alloy");
material_mana_coil_alloy.liquid = <liquid:redstone_chemical_elements_mana_coil_alloy_fluid>;
finishRceMaterial(
    material_mana_coil_alloy,
    "魔力线圈合金",
    "ManaCoilAlloy",
    0x3E6FAF,
    900,
    9.0,
    6.9,
    5,
    1.20,
    110,
    120,
    ["rce_trait_108"],
    ["rce_trait_109"],
    ["rce_trait_110"]
);

val material_manasteel = MaterialBuilder.create("rce_manasteel");
material_manasteel.liquid = <liquid:redstone_chemical_elements_manasteel_fluid>;
finishRceMaterial(
    material_manasteel,
    "魔力钢",
    "Manasteel",
    0x2F678D,
    760,
    8.4,
    6.2,
    4,
    1.15,
    85,
    100,
    ["rce_trait_111"],
    ["rce_trait_112"],
    noTraits
);

val material_neutron_alloy = MaterialBuilder.create("rce_neutron_alloy");
material_neutron_alloy.liquid = <liquid:redstone_chemical_elements_neutron_alloy_fluid>;
finishRceMaterial(
    material_neutron_alloy,
    "中子合金",
    "NeutronAlloy",
    0x8FAF87,
    1500,
    10.5,
    8.4,
    6,
    1.35,
    160,
    180,
    ["rce_trait_113"],
    ["rce_trait_114"],
    ["rce_trait_115"]
);

val material_photonic_brass = MaterialBuilder.create("rce_photonic_brass");
material_photonic_brass.liquid = <liquid:redstone_chemical_elements_photonic_brass_fluid>;
finishRceMaterial(
    material_photonic_brass,
    "光子黄铜",
    "PhotonicBrass",
    0xE0A452,
    650,
    10.0,
    6.5,
    5,
    1.25,
    60,
    80,
    ["rce_trait_116"],
    ["rce_trait_117"],
    ["rce_trait_118"]
);

val material_plasma_signalum = MaterialBuilder.create("rce_plasma_signalum");
material_plasma_signalum.liquid = <liquid:redstone_chemical_elements_plasma_signalum_fluid>;
finishRceMaterial(
    material_plasma_signalum,
    "等离子信素",
    "PlasmaSignalum",
    0xFF6B47,
    1200,
    11.5,
    8.0,
    6,
    1.25,
    140,
    150,
    ["rce_trait_119"],
    ["rce_trait_120"],
    ["rce_trait_121"]
);

val material_pulsating_iron = MaterialBuilder.create("rce_pulsating_iron");
material_pulsating_iron.liquid = <liquid:redstone_chemical_elements_pulsating_iron_fluid>;
finishRceMaterial(
    material_pulsating_iron,
    "脉冲铁",
    "PulsatingIron",
    0x4B9B82,
    640,
    8.0,
    6.0,
    4,
    1.15,
    75,
    90,
    ["rce_trait_122"],
    ["rce_trait_123"],
    noTraits
);

val material_pyrotheum_alloy = MaterialBuilder.create("rce_pyrotheum_alloy");
material_pyrotheum_alloy.liquid = <liquid:redstone_chemical_elements_pyrotheum_alloy_fluid>;
finishRceMaterial(
    material_pyrotheum_alloy,
    "炽焰合金",
    "PyrotheumAlloy",
    0xDB6A22,
    950,
    10.0,
    7.1,
    5,
    1.15,
    110,
    125,
    ["rce_trait_124"],
    ["rce_trait_125"],
    ["rce_trait_126"]
);

val material_quantum_alloy = MaterialBuilder.create("rce_quantum_alloy");
material_quantum_alloy.liquid = <liquid:redstone_chemical_elements_quantum_alloy_fluid>;
finishRceMaterial(
    material_quantum_alloy,
    "量子合金",
    "QuantumAlloy",
    0x7A95FF,
    1300,
    11.0,
    8.2,
    6,
    1.30,
    130,
    150,
    ["rce_trait_127"],
    ["rce_trait_128"],
    ["rce_trait_129", "rce_trait_130"]
);

val material_reactor_alloy = MaterialBuilder.create("rce_reactor_alloy");
material_reactor_alloy.liquid = <liquid:redstone_chemical_elements_reactor_alloy_fluid>;
finishRceMaterial(
    material_reactor_alloy,
    "反应堆合金",
    "ReactorAlloy",
    0x8BB068,
    1100,
    9.8,
    7.6,
    5,
    1.25,
    125,
    135,
    ["rce_trait_131"],
    ["rce_trait_132"],
    ["rce_trait_133"]
);

val material_red_alloy = MaterialBuilder.create("rce_red_alloy");
material_red_alloy.liquid = <liquid:redstone_chemical_elements_red_alloy_fluid>;
finishRceMaterial(
    material_red_alloy,
    "红石合金",
    "RedAlloy",
    0x9D4331,
    260,
    8.8,
    4.5,
    3,
    0.95,
    30,
    40,
    ["rce_trait_134"],
    ["rce_trait_135"],
    ["rce_trait_136"]
);

val material_redstone = MaterialBuilder.create("rce_redstone");
material_redstone.liquid = <liquid:redstone_chemical_elements_redstone_fluid>;
finishRceMaterial(
    material_redstone,
    "红石",
    "Redstone",
    0x9F2320,
    150,
    11.0,
    3.0,
    2,
    0.75,
    15,
    20,
    ["rce_trait_137"],
    ["rce_trait_138"],
    noTraits
);

val material_refined_iron = MaterialBuilder.create("rce_refined_iron");
material_refined_iron.liquid = <liquid:redstone_chemical_elements_refined_iron_fluid>;
finishRceMaterial(
    material_refined_iron,
    "精炼铁",
    "RefinedIron",
    0x7D838A,
    480,
    6.9,
    4.8,
    3,
    1.00,
    50,
    65,
    ["rce_trait_139"],
    ["rce_trait_140"],
    noTraits
);

val material_resonant_enderium = MaterialBuilder.create("rce_resonant_enderium");
material_resonant_enderium.liquid = <liquid:redstone_chemical_elements_resonant_enderium_fluid>;
finishRceMaterial(
    material_resonant_enderium,
    "共振末影",
    "ResonantEnderium",
    0x3D8F8A,
    1150,
    9.5,
    7.4,
    5,
    1.20,
    120,
    140,
    ["rce_trait_141"],
    ["rce_trait_142"],
    ["rce_trait_143"]
);

val material_rose_gold = MaterialBuilder.create("rce_rose_gold");
material_rose_gold.liquid = <liquid:redstone_chemical_elements_rose_gold_fluid>;
finishRceMaterial(
    material_rose_gold,
    "玫瑰金",
    "RoseGold",
    0xD69584,
    300,
    8.5,
    4.8,
    3,
    1.05,
    35,
    55,
    ["rce_trait_144"],
    ["rce_trait_145"],
    ["rce_trait_146"]
);

val material_runic_alloy = MaterialBuilder.create("rce_runic_alloy");
material_runic_alloy.liquid = <liquid:redstone_chemical_elements_runic_alloy_fluid>;
finishRceMaterial(
    material_runic_alloy,
    "符文合金",
    "RunicAlloy",
    0x6372CA,
    980,
    9.2,
    7.0,
    5,
    1.20,
    100,
    120,
    ["rce_trait_147"],
    ["rce_trait_148"],
    ["rce_trait_149"]
);

val material_signalum = MaterialBuilder.create("rce_signalum");
material_signalum.liquid = <liquid:redstone_chemical_elements_signalum_fluid>;
finishRceMaterial(
    material_signalum,
    "信素",
    "Signalum",
    0xC3634A,
    540,
    8.2,
    5.8,
    4,
    1.10,
    60,
    75,
    ["rce_trait_150"],
    ["rce_trait_151"],
    noTraits
);

val material_soularium = MaterialBuilder.create("rce_soularium");
material_soularium.liquid = <liquid:redstone_chemical_elements_soularium_fluid>;
finishRceMaterial(
    material_soularium,
    "魂金",
    "Soularium",
    0xA08561,
    520,
    7.4,
    5.3,
    4,
    1.00,
    60,
    80,
    ["rce_trait_152"],
    ["rce_trait_153"],
    ["rce_trait_154"]
);

val material_steel = MaterialBuilder.create("rce_steel");
material_steel.liquid = <liquid:redstone_chemical_elements_steel_fluid>;
finishRceMaterial(
    material_steel,
    "钢",
    "Steel",
    0x6D6B73,
    720,
    7.8,
    6.0,
    4,
    1.10,
    80,
    95,
    ["rce_trait_155"],
    ["rce_trait_156"],
    noTraits
);

val material_stellar_alloy = MaterialBuilder.create("rce_stellar_alloy");
material_stellar_alloy.liquid = <liquid:redstone_chemical_elements_stellar_alloy_fluid>;
finishRceMaterial(
    material_stellar_alloy,
    "星辉合金",
    "StellarAlloy",
    0x6D83DE,
    1400,
    11.2,
    8.1,
    6,
    1.30,
    145,
    170,
    ["rce_trait_157"],
    ["rce_trait_158"],
    ["rce_trait_159"]
);

val material_terrasteel = MaterialBuilder.create("rce_terrasteel");
material_terrasteel.liquid = <liquid:redstone_chemical_elements_terrasteel_fluid>;
finishRceMaterial(
    material_terrasteel,
    "泰拉钢",
    "Terrasteel",
    0x2F7B4D,
    1100,
    10.2,
    7.6,
    6,
    1.25,
    120,
    140,
    ["rce_trait_160"],
    ["rce_trait_161"],
    ["rce_trait_162"]
);

val material_thaumium = MaterialBuilder.create("rce_thaumium");
material_thaumium.liquid = <liquid:redstone_chemical_elements_thaumium_fluid>;
finishRceMaterial(
    material_thaumium,
    "神秘",
    "Thaumium",
    0x5A428C,
    700,
    7.0,
    5.4,
    4,
    1.05,
    70,
    90,
    ["rce_trait_163"],
    ["rce_trait_164"],
    ["rce_trait_165"]
);

val material_verdant_alloy = MaterialBuilder.create("rce_verdant_alloy");
material_verdant_alloy.liquid = <liquid:redstone_chemical_elements_verdant_alloy_fluid>;
finishRceMaterial(
    material_verdant_alloy,
    "繁茂合金",
    "VerdantAlloy",
    0x59A354,
    880,
    9.1,
    6.8,
    4,
    1.15,
    90,
    110,
    ["rce_trait_166"],
    ["rce_trait_167"],
    ["rce_trait_168"]
);

val material_vibrant_alloy = MaterialBuilder.create("rce_vibrant_alloy");
material_vibrant_alloy.liquid = <liquid:redstone_chemical_elements_vibrant_alloy_fluid>;
finishRceMaterial(
    material_vibrant_alloy,
    "活力合金",
    "VibrantAlloy",
    0x85A63C,
    760,
    9.4,
    6.9,
    5,
    1.20,
    80,
    100,
    ["rce_trait_169"],
    ["rce_trait_170"],
    ["rce_trait_171"]
);

val material_void = MaterialBuilder.create("rce_void");
material_void.liquid = <liquid:redstone_chemical_elements_void_fluid>;
finishRceMaterial(
    material_void,
    "虚空",
    "Void",
    0x32293D,
    1050,
    8.8,
    7.2,
    5,
    1.20,
    115,
    130,
    ["rce_trait_172"],
    ["rce_trait_173"],
    ["rce_trait_174"]
);

val material_void_brass = MaterialBuilder.create("rce_void_brass");
material_void_brass.liquid = <liquid:redstone_chemical_elements_void_brass_fluid>;
finishRceMaterial(
    material_void_brass,
    "虚空黄铜",
    "VoidBrass",
    0x856646,
    760,
    9.3,
    6.5,
    5,
    1.20,
    85,
    105,
    ["rce_trait_175"],
    ["rce_trait_176"],
    ["rce_trait_177"]
);

val material_void_metal = MaterialBuilder.create("rce_void_metal");
material_void_metal.liquid = <liquid:redstone_chemical_elements_void_metal_fluid>;
finishRceMaterial(
    material_void_metal,
    "虚空金属",
    "VoidMetal",
    0x66587C,
    1450,
    10.2,
    8.6,
    6,
    1.30,
    150,
    185,
    ["rce_trait_178"],
    ["rce_trait_179"],
    ["rce_trait_180", "rce_trait_181"]
);

