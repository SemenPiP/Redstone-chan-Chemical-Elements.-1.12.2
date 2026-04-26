# Redstone-chan Chemical Elements 1.12.2

This mod is a Minecraft 1.12.2 port of Hongshijiang's Chemical Elements, originally developed for newer Minecraft versions.

## Overview

- Adds 120 chemical elements and their related item forms.
- Keeps three main block forms for elements: storage blocks, raw blocks, and ores.
- Adds element fluids, gas cylinders, derived materials, and singularities for modpack authors.
- Provides basic recipes and registry entries, while leaving progression and acquisition routes open for custom modpack scripting.
- Includes optional Tinkers' Construct integration scripts under [`scripts/`](scripts) for registering element and derived materials as tool materials.

## Materials

See [Material Introduction](docs/materials.md) for a structured description of element materials, derived materials, fluids, singularities, naming rules, and modpack integration suggestions.

## Tinkers' Construct Integration

Built-in optional Tinkers' Construct integration is now included in the mod source.

- If `tconstruct` is installed, Redstone-chan Chemical Elements registers its Tinkers' Construct traits, base materials, derived materials, and material melting/casting data automatically.
- If `tconstruct` is not installed, the mod still loads normally.
- Legacy CraftTweaker / ContentTweaker scripts under [`scripts/`](scripts) are kept as source/reference data only. Do not load them together with the built-in integration in a runtime instance, or duplicate registration will occur.

## Notes

- The project is intended for modpack customization and does not force a fixed gameplay progression.
- Some superheavy element Chinese characters are not supported by the vanilla Minecraft font. This port includes additional glyphs to improve display compatibility.
- Local Gradle builds require a real Java 8 JDK. Set `JAVA8_HOME` or `JAVA_HOME_8` before running `gradlew build`.

---

# 红石酱的化学元素 1.12.2

本模组是高版本“红石酱的化学元素”的 Minecraft 1.12.2 移植版。

## 简介

- 添加 120 种化学元素及其相关材料形态。
- 元素方块保留三类：储存块、粗矿块、矿石。
- 添加元素流体、气瓶、衍生材料和奇点，方便整合包作者进行魔改。
- 项目只提供基础配方和注册内容，不强制限定自然获取方式或发展路线。
- 仓库额外提供 [`scripts/`](scripts) 下的匠魂联动脚本，用于把元素与衍生材料接入匠魂工具材料系统。

## 材料说明

完整材料文本介绍见：[docs/materials.md](docs/materials.md)。

## 匠魂联动

可选的 CraftTweaker / ContentTweaker 匠魂联动脚本位于 [`scripts/`](scripts)：

- `00_rce_tconstruct_traits.zs`：注册自定义特性。
- `rce_tconstruct_base_materials.zs`：注册 120 个元素材料。
- `rce_tconstruct_materials.zs`：注册 52 个衍生材料。

整合包运行时可将这些脚本复制到实例的 `scripts` 目录中使用。

## 说明

- 本模组主要面向整合包作者，用于 CraftTweaker、GroovyScript 或机器配方系统扩展。
- 原版 Minecraft 字体不支持部分超重元素中文字符，本移植版已加入补充字形以改善显示。
