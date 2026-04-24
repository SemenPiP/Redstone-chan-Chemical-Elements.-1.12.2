# 材料介绍

本文档根据项目内的注册代码、语言文件和 `derived/manifest.json` 整理，用于给整合包作者快速了解本模组新增材料内容。

## 总览

- **化学元素材料**：覆盖 118 种元素，每种元素提供锭、粒、粉、板、杆、线、齿轮、粗矿、粉碎粗矿等物品。
- **元素方块**：每种元素保留储存块、粗矿块、矿石三类方块，使用偏原版风格的方块表现。
- **流体储存**：元素会按状态注册为气体、液体或熔融流体，并提供桶装流体；气体元素另有气瓶物品。
- **衍生材料**：额外提供 52 种合金、魔法金属与整合包常用材料，包含物品、方块、流体和奇点。
- **奇点材料**：金属元素与衍生材料均有对应奇点，适合在高阶机器、终局合成或自定义配方中使用。

## 材料分类

### 元素材料

元素材料是本模组的基础内容，面向整合包魔改使用。项目不强制提供自然获取链路，只提供基础注册、模型、语言与配方入口，方便作者自行接入矿物生成、机器处理、商店或任务线。

常见物品形态：

- **锭 / 粒**：适合参与压缩、分解、通用金属合成。
- **粉 / 粉碎粗矿**：适合接入粉碎机、洗矿、离心等处理流程。
- **板 / 杆 / 线 / 齿轮**：适合机器外壳、线圈、电路、传动件等配方。
- **粗矿 / 粗矿块**：适合 1.12.2 整合包模拟新版粗矿处理链。
- **矿石 / 储存块**：适合矿物生成、自定义维度和压缩存储。

### 流体材料

流体材料用于扩展冶炼、浇铸、冷却、化工和储罐系统。项目中气体元素表现为气态流体，溴与汞表现为常温液态流体，其余元素默认表现为熔融流体。

### 衍生材料

衍生材料来自 `derived/manifest.json`，主要用于补齐整合包常见材料体系。每种材料通常包含：粉、齿轮、锭、粒、板、杆、线、粉碎粗矿、粗矿、储存块、矿石、粗矿块、熔融流体与奇点。

| 材料 | 英文名 | 注册名 | 物品数 | 方块数 | 流体 | 用途定位 |
| --- | --- | --- | ---: | ---: | --- | --- |
| 青铜 | Bronze | `bronze` | 9 | 3 | `redstone_chemical_elements_bronze_fluid` | 科技/合金体系材料 |
| 康铜 | Constantan | `constantan` | 9 | 3 | `redstone_chemical_elements_constantan_fluid` | 科技/合金体系材料 |
| 钻石 | Diamond | `diamond` | 9 | 3 | `redstone_chemical_elements_diamond_fluid` | 原版资源扩展材料 |
| 琥珀金 | Electrum | `electrum` | 9 | 3 | `redstone_chemical_elements_electrum_fluid` | 科技/合金体系材料 |
| 绿宝石 | Emerald | `emerald` | 9 | 3 | `redstone_chemical_elements_emerald_fluid` | 原版资源扩展材料 |
| 末影 | Enderium | `enderium` | 9 | 3 | `redstone_chemical_elements_enderium_fluid` | 科技/合金体系材料 |
| 殷钢 | Invar | `invar` | 9 | 3 | `redstone_chemical_elements_invar_fluid` | 科技/合金体系材料 |
| 流明 | Lumium | `lumium` | 9 | 3 | `redstone_chemical_elements_lumium_fluid` | 科技/合金体系材料 |
| 红石 | Redstone | `redstone` | 9 | 3 | `redstone_chemical_elements_redstone_fluid` | 原版资源扩展材料 |
| 信素 | Signalum | `signalum` | 9 | 3 | `redstone_chemical_elements_signalum_fluid` | 科技/合金体系材料 |
| 钢 | Steel | `steel` | 9 | 3 | `redstone_chemical_elements_steel_fluid` | 科技/合金体系材料 |
| 神秘 | Thaumium | `thaumium` | 9 | 3 | `redstone_chemical_elements_thaumium_fluid` | 魔法/神秘体系材料 |
| 虚空 | Void | `void` | 9 | 3 | `redstone_chemical_elements_void_fluid` | 魔法/神秘体系材料 |
| 魔力钢 | Manasteel | `manasteel` | 9 | 3 | `redstone_chemical_elements_manasteel_fluid` | 魔法/神秘体系材料 |
| 源质钢 | Elementium | `elementium` | 9 | 3 | `redstone_chemical_elements_elementium_fluid` | 魔法/神秘体系材料 |
| 泰拉钢 | Terrasteel | `terrasteel` | 9 | 3 | `redstone_chemical_elements_terrasteel_fluid` | 魔法/神秘体系材料 |
| 精炼铁 | Refined Iron | `refined_iron` | 9 | 3 | `redstone_chemical_elements_refined_iron_fluid` | 科技/合金体系材料 |
| 高级合金 | Advanced Alloy | `advanced_alloy` | 9 | 3 | `redstone_chemical_elements_advanced_alloy_fluid` | 科技/合金体系材料 |
| 铱合金 | Iridium Alloy | `iridium_alloy` | 9 | 3 | `redstone_chemical_elements_iridium_alloy_fluid` | 科技/合金体系材料 |
| 黄铜 | Brass | `brass` | 9 | 3 | `redstone_chemical_elements_brass_fluid` | 科技/合金体系材料 |
| 红石合金 | Red Alloy | `red_alloy` | 9 | 3 | `redstone_chemical_elements_red_alloy_fluid` | 科技/合金体系材料 |
| 构造合金 | Construction Alloy | `construction_alloy` | 9 | 3 | `redstone_chemical_elements_construction_alloy_fluid` | 科技/合金体系材料 |
| 导电铁 | Conductive Iron | `conductive_iron` | 9 | 3 | `redstone_chemical_elements_conductive_iron_fluid` | 科技/合金体系材料 |
| 电气钢 | Electrical Steel | `electrical_steel` | 9 | 3 | `redstone_chemical_elements_electrical_steel_fluid` | 科技/合金体系材料 |
| 暗钢 | Dark Steel | `dark_steel` | 9 | 3 | `redstone_chemical_elements_dark_steel_fluid` | 科技/合金体系材料 |
| 脉冲铁 | Pulsating Iron | `pulsating_iron` | 9 | 3 | `redstone_chemical_elements_pulsating_iron_fluid` | 科技/合金体系材料 |
| 充能合金 | Energetic Alloy | `energetic_alloy` | 9 | 3 | `redstone_chemical_elements_energetic_alloy_fluid` | 科技/合金体系材料 |
| 活力合金 | Vibrant Alloy | `vibrant_alloy` | 9 | 3 | `redstone_chemical_elements_vibrant_alloy_fluid` | 科技/合金体系材料 |
| 魂金 | Soularium | `soularium` | 9 | 3 | `redstone_chemical_elements_soularium_fluid` | 通用整合包材料 |
| 末地钢 | End Steel | `end_steel` | 9 | 3 | `redstone_chemical_elements_end_steel_fluid` | 科技/合金体系材料 |
| 虚空黄铜 | Void Brass | `void_brass` | 9 | 3 | `redstone_chemical_elements_void_brass_fluid` | 魔法/神秘体系材料 |
| 虚空金属 | Void Metal | `void_metal` | 9 | 3 | `redstone_chemical_elements_void_metal_fluid` | 魔法/神秘体系材料 |
| 符文合金 | Runic Alloy | `runic_alloy` | 9 | 3 | `redstone_chemical_elements_runic_alloy_fluid` | 魔法/神秘体系材料 |
| 繁茂合金 | Verdant Alloy | `verdant_alloy` | 9 | 3 | `redstone_chemical_elements_verdant_alloy_fluid` | 科技/合金体系材料 |
| 生命钢 | Living Steel | `living_steel` | 9 | 3 | `redstone_chemical_elements_living_steel_fluid` | 科技/合金体系材料 |
| 盖亚合金 | Gaia Alloy | `gaia_alloy` | 9 | 3 | `redstone_chemical_elements_gaia_alloy_fluid` | 魔法/神秘体系材料 |
| 玫瑰金 | Rose Gold | `rose_gold` | 9 | 3 | `redstone_chemical_elements_rose_gold_fluid` | 通用整合包材料 |
| 反应堆合金 | Reactor Alloy | `reactor_alloy` | 9 | 3 | `redstone_chemical_elements_reactor_alloy_fluid` | 高阶能源/终局材料 |
| 机械钢 | Machine Steel | `machine_steel` | 9 | 3 | `redstone_chemical_elements_machine_steel_fluid` | 科技/合金体系材料 |
| 炽焰合金 | Pyrotheum Alloy | `pyrotheum_alloy` | 9 | 3 | `redstone_chemical_elements_pyrotheum_alloy_fluid` | 高阶能源/终局材料 |
| 寒霜合金 | Cryotheum Alloy | `cryotheum_alloy` | 9 | 3 | `redstone_chemical_elements_cryotheum_alloy_fluid` | 高阶能源/终局材料 |
| 星辉合金 | Stellar Alloy | `stellar_alloy` | 9 | 3 | `redstone_chemical_elements_stellar_alloy_fluid` | 高阶能源/终局材料 |
| 量子合金 | Quantum Alloy | `quantum_alloy` | 9 | 3 | `redstone_chemical_elements_quantum_alloy_fluid` | 高阶能源/终局材料 |
| 通量琥珀金 | Fluxed Electrum | `fluxed_electrum` | 9 | 3 | `redstone_chemical_elements_fluxed_electrum_fluid` | 科技/合金体系材料 |
| 共振末影 | Resonant Enderium | `resonant_enderium` | 9 | 3 | `redstone_chemical_elements_resonant_enderium_fluid` | 科技/合金体系材料 |
| 光子黄铜 | Photonic Brass | `photonic_brass` | 9 | 3 | `redstone_chemical_elements_photonic_brass_fluid` | 高阶能源/终局材料 |
| 电路钢 | Circuit Steel | `circuit_steel` | 9 | 3 | `redstone_chemical_elements_circuit_steel_fluid` | 科技/合金体系材料 |
| 中子合金 | Neutron Alloy | `neutron_alloy` | 9 | 3 | `redstone_chemical_elements_neutron_alloy_fluid` | 高阶能源/终局材料 |
| 等离子信素 | Plasma Signalum | `plasma_signalum` | 9 | 3 | `redstone_chemical_elements_plasma_signalum_fluid` | 高阶能源/终局材料 |
| 低温电路合金 | Cryo Circuit Alloy | `cryo_circuit_alloy` | 9 | 3 | `redstone_chemical_elements_cryo_circuit_alloy_fluid` | 高阶能源/终局材料 |
| 魔力线圈合金 | Mana Coil Alloy | `mana_coil_alloy` | 9 | 3 | `redstone_chemical_elements_mana_coil_alloy_fluid` | 魔法/神秘体系材料 |
| 奥术机械合金 | Arcane Machine Alloy | `arcane_machine_alloy` | 9 | 3 | `redstone_chemical_elements_arcane_machine_alloy_fluid` | 魔法/神秘体系材料 |

## 配方与魔改建议

- **低阶阶段**：使用铜、锡、铁、青铜、钢等材料作为基础机器和工具材料。
- **中阶阶段**：使用红石、信素、琥珀金、康铜、殷钢等材料连接能源、线圈、齿轮与机械外壳。
- **魔法阶段**：使用魔力钢、源质钢、泰拉钢、虚空金属、符文合金等材料对接魔法模组。
- **高阶阶段**：使用反应堆合金、量子合金、中子合金、恒星合金、等离子信素等材料作为终局配方门槛。
- **终局阶段**：使用奇点作为大量材料压缩后的高价值物品，可接入大型机械、多方块结构或最终合成。

## 资源命名约定

- 元素物品：`<element>/<element>_ingot`、`<element>/<element>_dust` 等。
- 衍生物品：`derived/<material>/<item_name>`。
- 衍生方块：通过变种方块分组注册，同时保留对应模型和语言键。
- 流体：`redstone_chemical_elements_<name>_fluid`。
- 奇点：`singularity/<name>_singularity`。

## 注意事项

- 本项目重点是材料注册与整合包兼容，不限定固定玩法路线。
- 建议整合包作者通过 CraftTweaker、GroovyScript 或机器模组配方系统补齐获取链。
- 中文显示依赖项目内补充字形，重元素名称在原版字体下可能存在兼容限制。
