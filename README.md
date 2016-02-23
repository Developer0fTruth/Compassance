![](http://i67.tinypic.com/o7sxm8.png)

#####`Currently under development. Now in BETA stages.`

# Compassance
Compassance brings a unique gadget for players on servers. This project allows for a whole new level of customization and personalization of a player's experience on a Bukkit/Spigot server. The plugin allows for a Skyrim flat compass right on the action bar, turning around will move the compass respectively. Honestly you'll turn around just for the fun of it.

#### Theming Guide

More in-depth guide will come soon.

This will dictate what characters will go where, what sub patterns will go where, etc. It is highly recomended that you follow the 'default' template provided, so that data can be accurate and the plugin won't start throwing all kinds of errors.

**Direct** `<?>` are direct replacers. These keys will immediately point the generator towards the 'data/direct' section and look for the corresponding character to replace in the main map. The generator will process them first.

**Sub-Pattern** `<s/?>` are sub patterns. They function like the main pattern map, except you can put patterns inside them to make the main pattern map cleaner. These keys will point to the 'sub-pattern' section and replace them in the main pattern map. During generation of the compass string, the plugin will look for the corresponding character and process them after the direct replacers.

**Post Processing** `final` are the final replacers. They take place after all the processing is done, they do not care much for the generated string, which you can reference as `%str%`.

```yml
default:
  meta:
    name: "&lMidnight"
    desc: "&7The default theme.%nl%&7The moon fills you with determination."
  data:
    main-pattern-map: "<S>;<s/sep>;<W>;<s/sep>;<N>;<s/sep>;<E>;<s/sep>"
    function:
      cursor: "&f[;%str%;&f]"
      target: "&9\u2588"
    direct:
      <S>: "&f&l&n S "
      <W>: "&f&l&n W "
      <N>: "&f&l&n N "
      <E>: "&f&l&n E "
    sub-pattern:
      <s/sep>:
        pattern-map: "<1>;<2>;<3>;<3>;<4>;<4>;<4>;<4>;<4>;<4>;<3>;<3>;<2>;<1>"
        <1>: "&0\u2588"
        <2>: "&0\u2593"
        <3>: "&0\u2592"
        <4>: "&0\u2591"
  final:
    pattern-map: "<<>;%str%;<>>"
    <<>: "&f&l["
    <>>: "&f&l]"
```
        
#### Development Information
This project is developed in Java 7. No APIs to hook up because most of the code relates to the theming anyways.
