# CustomAgriculture

一个支持 Minecraft 1.12.2 和 1.7.10 的 Spigot 插件。

## 功能

- 自定义种子生长时间：您可以通过配置文件设置每种植物的生长时间（以秒为单位）。
- 自定义随机种子：您可以通过配置文件设置带有特定描述的种子种出的植物类型，以及每种植物的生成几率。

## 安装

1. 将插件放入服务器的 `plugins` 文件夹。
2. 重启服务器，让插件自动生成配置文件。
3. 按照示例配置文件进行自定义设置。
4. 保存配置文件并重载插件或重启服务器。

## 命令

- `/getseed <seedName>`：获取某种随机种子，`a` 和 `b` 是示例配置中的种子类型。

## 权限

- `customagriculture.getseed`：允许使用 `/getseed` 命令。

## 示例配置

```yaml
ca:
  seed:
    a: # /getseed a name和lore不要和别的参数完全一致
      name: '&6随机种子1'
      lore:
        - '&a这个种子有魔法~'
        - '&b随机种出许多植物~'
      chance: #几率=设定数/总数
        crops: 1
        carrot: 1
        potato: 1
        beetroot: 1
        pumpkin: 1
        melon: 1
    b:
      name: '&6随机种子2'
      lore:
        - '&a这个种子有魔法~'
        - '&b随机种出许多植物~'
      chance: #删除的不参与随机
        crops: 1
        carrot: 1
        potato: 1
  time: #秒为单位, 一个阶段的生长时间
    crops: 5 #7个阶段
    carrot: 5 #7
    potato: 5 #7
    pumpkin: 5 #7
    melon: 5 #7
    beetroot: 5 #4
    warts: 5 #4
    cocoa: 5 #3
```

## 许可证

本项目使用 LGPL-3.0 许可证开源。

_亿年前写的定制插件，现在开源，烂码勿喷_
