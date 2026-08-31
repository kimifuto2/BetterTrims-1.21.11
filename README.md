BetterTrims 4.0
================
## Gives each armour trim material a unique effect when worn.

[![Modrinth](https://img.shields.io/modrinth/dt/bettertrims?color=00AF5C&label=downloads&logo=modrinth)](https://modrinth.com/mod/bettertrims)
[![CurseForge](https://cf.way2muchnoise.eu/full_821752_downloads.svg)](https://curseforge.com/minecraft/mc-mods/better-trims)

#### See the [Wiki](https://moddedmc.wiki/en/project/bettertrims/latest/docs) for Developer Documentaion (adding your own effects) and User Information about the included Datapacks.

### Features
#### Bespoke Data-Driven Armour Trim Property System

<p float="left">
    <img src="docs/.assets/bettertrims/example_data_file.png" alt="data_file" height="300"/>
    <img src="docs/.assets/bettertrims/redstone.gif" alt="in_game" height="300"/>
</p>

  - Includes Worn Properties, In-World Properties, Set Bonuses and More
  - Almost Infinite Extensbility and Customisation
  - Condition System Based on the [Predicate Data API](https://minecraft.wiki/w/Predicate)
  - Tooltip Inferred From Properties (Mostly translated — there's like 10,000 possible combinations and I am a solo developer)

#### Smart Tooltips
Only Shows Relevant Information Based on Context:

<img src="docs/.assets/bettertrims/gold.gif" alt="tooltip" height="350"/>

  - Only Visible When Holding Alt/Option 
  - Nested / Complex Conditions Only Expand When Holding Shift
  - Elements Colour Coded Based on Type

#### 2 Datapacks Included
  - **Default**: Similar effects to 2.0 (The more popular version of this mod)
  - **Trim Effects**: Re-implementation and balancing of the [TrimsEffects](https://modrinth.com/mod/trimseffects) mod as a datapack

---

## 效果一览 / Effects List (1.21.11)

### 材质效果（穿对应「纹饰材质」的装备，按件叠加）
材质效果来自内置数据包 **「Better Trims 基础包」（默认启用）**。

| 纹饰材质 | 效果 | 加成方式 |
|---|---|---|
| 下界石英 Nether Quartz | 获取经验提升 | 每件 **+25% 经验** |
| 铁 Iron | 挖掘速度提升 | 每件 +挖掘效率；**穿戴全套 4 件获得「急迫」**（8 秒，每秒刷新，脱下后消失） |
| 下界合金 Netherite | 斩首/火免/甲韧 | 每件 +2 护甲、+1 韧度；穿戴时获得**火焰抗性**（8 秒，每秒刷新，脱下后消失）；**免疫火焰伤害**，物品不会被烧毁；攻击有概率**追加穿透伤害**并**掉落生物头颅**（凋零骷髅头/骷髅头/僵尸头/苦力怕头/猪灵头） |
| 金 Gold | 猪灵中立、白天加成 | 使猪灵中立（**猪灵蛮兵不受影响**）；白天每件 +攻击/移速/护甲/攻速 |
| 钻石 Diamond | 更抗揍、耐穿 | 每件受到的伤害约 **-5%**；装备损耗有几率降低 |
| 紫水晶 Amethyst | 共振、爆发 | 攻击/受击时约 **5% 几率**大幅增伤（×5）或减伤，并发出共振音效 |
| 红石 Redstone | 移动加速 | 每件 +移动速度 |
| 铜 Copper | 免雷击、蓄电 | **免疫闪电伤害**；雷暴时导电弹射物会召雷/附加伤害 |
| 银 Silver | 夜晚加成、夜视 | 月光/夜晚每件 +攻击/移速/护甲/攻速；**头部装备获得夜视**（8 秒，每秒刷新，脱下后消失） |
| 绿宝石 Emerald | 交易折扣 | 每件 **-10%** 村民交易价格 |
| 青金石 Lapis | 附魔折扣 | 每件 **-10%** 附魔台经验消耗 |
| 树脂 Resin | 影遁 | 静止不动时隐遁（隐身）并获得伤害加成；移动时生成粒子 |

### 纹饰图案效果（穿戴 2 件相同图案触发）
图案效果来自内置数据包 **「纹饰效果」（需在游戏内手动启用）**。穿戴 **2 件及以上**同一图案 → 获得对应状态效果：

| 纹饰图案 | 获得效果 | | 纹饰图案 | 获得效果 |
|---|---|---|---|---|
| 闪电 Bolt | 海豚的恩惠 | | 塑形 Shaper | 幸运 |
| 海岸 Coast | 水下呼吸 | | 寂静 Silence | 生命提升 |
| 沙丘 Dune | 速度 | | 猪鼻 Snout | 抗火 |
| 遗迹之眼 Eye | 生命恢复 | | 尖顶 Spire | 力量 |
| 涡流 Flow | 跳跃提升 | | 潮涌 Tide | 潮涌能量 |
| 宿主 Host | 发光 | | 恼鬼 Vex | 隐身 |
| 崛起 Raiser | 饱和 | | 守卫 Ward | 伤害吸收 |
| 肋骨 Rib | 急迫 | | 向导 Wayfinder | 缓降 |
| 哨卫 Sentry | 抗性提升 | | 荒野 Wild | 村庄英雄 |

### 说明
- 纹饰 = **材质 + 图案**，两者独立生效（例如「铜材质 + 潮涌图案」= 免疫闪电 + 潮涌能量）。
- 纯原版可用的材质：石英、铁、金、铜、钻石、下界合金、红石、绿宝石、紫水晶、树脂（青金石原版也有）。**银**需第三方「银」模组。
- **青金石附魔折扣**为本 1.21.11 移植版新增；其余为 BetterTrims 自带能力。

### All The Trims 自定义材质（1.21.11 移植版）
本移植版额外注册了 **11 个可当作纹饰材质的物品**（`allthetrims:*`），并实现对应效果。物品的**原有用途不受影响**（煤炭仍可烧炼、紫颂果仍可传送等）。

| 材质物品 | 效果 | 状态 |
|---|---|---|
| 荧石粉 | 发光（8 秒，每秒刷新）；**下界中**每件 +攻击/攻速/移速/减伤 | ✅ 已实现 |
| 下界砖 | 防火（8 秒，每秒刷新）；每件 **-5% 火焰伤害** | ✅ 已实现 |
| 海晶碎片 | 水下呼吸（8 秒，每秒刷新）；对**水生生物**每件 +2 伤害 | ✅ 已实现 |
| 附魔金苹果 | +6/12/16/20 生命上限；每件 **-5% 伤害**（4 件 -20%）；**生命恢复**（8 秒，每秒刷新；1-3 件 I 级，4 件 II 级） | ✅ 已实现 |
| 火焰弹 | 攻击点燃敌人（4 秒/件） | ✅ 已实现 |
| 粘液球 | +击退抗性（每件+1）；攻击使目标**迟缓 2 秒/件** | ✅ 已实现 |
| 紫颂果 | 每件 **+7% 闪避**（概率免伤） | ✅ 已实现 |
| 煤炭 | **自动熔炼背包矿物**（每件 2 个，4 件 8 个） | ✅ 已实现 |
| 龙息 | 将**正面状态效果共享**给附近友军（每件 +1 格） | ✅ 已实现 |
| 回响碎片 | 死亡**逆转**：恢复满生命与饱食 + 图腾粒子音效；冷却 10/5/3/1 分钟按件数；喝牛奶可重置冷却 | ✅ 已实现 |
| 末影珍珠 | 每件 **+25% 弹射物闪避**；双击 X 键**瞬步**传送到准星位置（10/20/35/50 格按件数，超范围或贴底面提示无法传送）；冷却 2/1.5/1/0.5 分钟；喝牛奶可重置冷却 | ✅ 已实现 |

> ✅ 这 11 个材质**均已注册为纹饰材质**（`minecraft:trim_material/*` + `#minecraft:trim_materials`），并注入 `provides_trim_material`，可直接在锻造台为护甲打上纹饰。

---

## 配置（Config）

可通过 [ModMenu](https://modrinth.com/mod/modmenu) 在游戏内调整（默认写回 `config/better-trims.json5`）：

- **禁止复制盔甲纹饰模板**（默认关闭）：开启后锻造台无法复制盔甲纹饰锻造模板，适合想增加难度的玩家。默认允许复制（原版行为）。
- **开启纹饰图案效果**：启用/禁用 `bettertrims:trim_effects` 数据包（纹饰图案穿戴 2 件以上触发状态效果）。
- **调试模式**：BetterTrims 调试日志。



