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
| 铁 Iron | 挖掘速度提升 | 每件 +挖掘效率；**穿戴全套 4 件获得「急迫」** |
| 下界合金 Netherite | 更硬、防火 | 每件 +2 护甲、+1 韧度；**免疫火焰伤害**，物品不会被烧毁 |
| 金 Gold | 猪灵中立、白天加成 | 使猪灵中立（**猪灵蛮兵不受影响**）；白天每件 +攻击/移速/护甲/攻速 |
| 钻石 Diamond | 更抗揍、耐穿 | 每件受到的伤害约 **-5%**；装备损耗有几率降低 |
| 紫水晶 Amethyst | 共振、爆发 | 攻击/受击时约 **5% 几率**大幅增伤（×5）或减伤，并发出共振音效 |
| 红石 Redstone | 移动加速 | 每件 +移动速度 |
| 铜 Copper | 免雷击、蓄电 | **免疫闪电伤害**；雷暴时导电弹射物会召雷/附加伤害 |
| 银 Silver | 夜晚加成、夜视 | 月光/夜晚每件 +攻击/移速/护甲/攻速；**头部装备获得夜视** |
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


