# Compassance
Compassance brings a unique gadget for players on servers. This project allows for a whole new level of customization and personalization of a player's experience on a Bukkit/Spigot server.

#### Theming Guide

    default:
      meta:
        name: "&lMidnight"
        desc: "&7The default theme.%nl%&7The moon fills you with determination."
      data:
        main-pattern-map: "<S>;<s/sep>;<W>;<s/sep>;<N>;<s/sep>;<E>;<s/sep>"
        direct:
          <d/S>: "&f&l&n S "
          <d/W>: "&f&l&n W "
          <d/N>: "&f&l&n N "
          <d/E>: "&f&l&n E "
        sub-pattern:
          <s/sep>:
            pattern-map: "<1>;<2>;<3>;<3>;<4>;<4>;<4>;<4>;<4>;<4>;<3>;<3>;<2>;<1>"
            <1>: "&8\u2588"
            <2>: "&8\u2593"
            <3>: "&8\u2592"
            <4>: "&8\u2591"
      final:
        pattern-map: "<(>[str]<)>"
        <(>: "&f["
        <)>: "&f]"