![](http://i67.tinypic.com/o7sxm8.png)

#####`Currently under development. Still in Alpha stages.`

# Compassance
Compassance brings a unique gadget for players on servers. This project allows for a whole new level of customization and personalization of a player's experience on a Bukkit/Spigot server. The plugin allows for a Skyrim flat compass right on the action bar, turning around will move the compass respectively. Honestly you'll turn around just for the fun of it.

#### Theming Guide

This will dictate what characters will go where, what sub patterns will go where, etc. It is highly recomended that you follow the 'default' template provided, so that data can be accurate and the plugin won't start throwing all kinds of errors.

**Direct** `<?>` are direct replacers. These keys will immediately point the generator towards the 'data/direct' section and look for the corresponding character to replace in the main map. The generator will process them first.

**Sub-Pattern** `<s/?>` are sub patterns. They function like the main pattern map, except you can put patterns inside them to make the main pattern map cleaner. These keys will point to the 'sub-pattern' section and replace them in the main pattern map. During generation of the compass string, the plugin will look for the corresponding character and process them after the direct replacers.

**Post Processing** `final` are not implemented yet.

    default:            # This is the ID of the theme.
      meta:             # General info.
        name: "&lMidnight"
        desc: "&7The default theme.%nl%&7The moon fills you with determination."
      data:
        main-pattern-map: "<S>;<s/sep>;<W>;<s/sep>;<N>;<s/sep>;<E>;<s/sep>"
        direct:         # Replace any keys shown below right after pattern is generated.
          <d/S>: "&f&l&n S "
          <d/W>: "&f&l&n W "
          <d/N>: "&f&l&n N "
          <d/E>: "&f&l&n E "
        sub-pattern:
          <s/sep>:      # Replaces any <s/sep> with the pattern map shown below.
            pattern-map: "<1>;<2>;<3>;<3>;<4>;<4>;<4>;<4>;<4>;<4>;<3>;<3>;<2>;<1>"
            <1>: "&8\u2588"
            <2>: "&8\u2593"
            <3>: "&8\u2592"
            <4>: "&8\u2591"
      final:            # Not implemented yet.
        pattern-map: "<(>[str]<)>"
        <(>: "&f["
        <)>: "&f]"
        
#### Development Information
This project is developed in Java 7. No APIs to hook up because most of the code relates to the theming anyways.
