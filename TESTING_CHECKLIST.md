# Icy's Better Horses — 1.21.1 Manual Test Checklist

Use a fresh Creative-mode world first. Test on a server with the same Fabric
mods as the client before treating multiplayer checks as passed.

## 1. Before you start

- [ ] Confirm the Mods screen lists **Icy's Better Horses**, Fabric API,
  GeckoLib, and Modonomicon.
- [ ] Open **Options → Controls → Key Binds → Icy's Better Horses**. The
  default keys are below; change any that conflict with your own binds.

| Action | Default key | What to expect |
| --- | --- | --- |
| Horse Whistle / horse info | `P` | Calls owned horses; while riding, opens the horse-info screen. |
| Horse Command Wheel | `R` | Opens commands while looking at an owned horse within 12 blocks. |
| Manage Horses | `G` | Opens your owned-horse roster. |
| Gait / gear shift | `V` | Cycles the ridden horse's gait gear. |
| Rear | `H` | Makes the ridden horse rear when eligible. |
| Free look | `Left Ctrl` | Hold while riding to look independently of the horse. |
| Cart size | `Left Alt` | Switches an eligible equipped cart between sizes. |

## 2. Create a repeatable test setup

Run these in chat (`T`) with cheats enabled. The commands give you one horse,
all gear, and enough testing supplies.

```mcfunction
/summon icys-better-horses:icelandic_horse ~ ~ ~
/give @s icys-better-horses:upgraded_saddle 1
/give @s icys-better-horses:horse_hooves_gear 1
/give @s icys-better-horses:horse_medkit_gear 1
/give @s icys-better-horses:horse_stabilizer_gear 1
/give @s icys-better-horses:horse_cart_gear 1
/give @s minecraft:chest 1
/give @s minecraft:ender_chest 1
/give @s minecraft:golden_apple 4
/give @s minecraft:name_tag 1
```

- [ ] Tame the horse normally: hold an empty hand, right-click it to mount,
  and repeat after each buck until hearts appear.
- [ ] Give it a name with the Name Tag (rename it in an anvil first, then
  right-click the horse). Confirm the horse remains owned and receives the
  expected bond reward.
- [ ] Feed a Golden Apple by holding it and right-clicking the tamed horse.

## 3. Saddle and gear inventory

1. Hold the **Upgraded Saddle** and right-click the tamed horse to equip it.
   If the horse is mounted, dismount first with `Left Shift`.
2. **Sneak** (`Left Shift`) and right-click the horse to open its inventory.
3. Verify the upgraded saddle is in the normal saddle slot. Four additional
   one-item gear slots appear across the top of the horse inventory.
4. Put the following items in their matching slot:

| Gear slot | Accepted item | Test |
| --- | --- | --- |
| Chest | Chest or Ender Chest | A normal chest exposes horse storage; an Ender Chest exposes the player's ender inventory. |
| Hooves | Horse Hooves | Ride over snow/ice and take a controlled fall. |
| Medkit | Horse Medkit | Damage the horse below half health; it should consume the kit and heal/protect itself. |
| Stabilizer | Horse Stabilizer **or** Horse Cart | Test safe descent, or cart behavior, separately. |

- [ ] Confirm each slot rejects an unrelated item.
- [ ] Confirm closing and reopening the inventory preserves every fitted item.
- [ ] Remove each item once, then put it back, to confirm pickup and placement
  work without duplication or loss.

## 4. Equipment behavior

### Chest and Ender Chest

- [ ] Fit a normal Chest, place a few items in the added horse storage, close
  the inventory, reopen it, and verify the items remain.
- [ ] Replace it with an Ender Chest and confirm the displayed inventory is
  your own Ender Chest inventory.
- [ ] Remove the Ender Chest and confirm normal horse storage is no longer
  exposed.

### Hooves

- [ ] Fit Horse Hooves, mount, and ride over snow and ice.
- [ ] Ride off a 5–10 block test platform. The horse should take reduced fall
  damage compared with the same fall without hooves.
- [ ] Remove Hooves and repeat the fall from a safe, measurable height.

### Medkit

- [ ] Fit Horse Medkit and reduce the horse below half health with controlled
  damage (for example, a low-power weapon while in Creative testing).
- [ ] Confirm the medkit slot empties and the horse gains its recovery effects.
- [ ] Fit a second kit and repeat once; do not test lethal damage until normal
  recovery has worked.

### Stabilizer

- [ ] Fit Horse Stabilizer, mount, then ride off a 30-block platform into a
  clear landing area.
- [ ] Confirm the stabilizer deploys visually/audio-wise and prevents the
  damaging landing.
- [ ] Remove it and repeat from a safe lower height to establish the contrast.

## 5. Cart

1. Empty the Stabilizer slot.
2. Hold the **Horse Cart** item and right-click the tamed horse; do **not**
   sneak. This is the quick-fit path.
3. Confirm the cart appears behind the horse.

- [ ] Mount the horse and ride it forward, backward, and through a turn.
- [ ] Ask a second player to mount a rear cart seat. Have them use `Left Shift`
  to dismount.
- [ ] With an eligible cart, press `Left Alt` while mounted to swap cart size;
  confirm the model/seat layout changes.
- [ ] Open the horse inventory and verify the cart occupies the Stabilizer
  gear slot.
- [ ] Test the cart's cargo/chest interaction if a chest is fitted, then remove
  the cart and verify normal Stabilizer-slot behavior returns.

## 6. Riding controls

- [ ] Mount the horse with right-click; dismount with `Left Shift`.
- [ ] Tap `V` several times while riding. Confirm gait changes are visible and
  the HUD/horse response changes. Press `W` or `S` to confirm ordinary
  forward/back input still takes priority.
- [ ] Press `H` while mounted and stationary/on safe ground; confirm a rear.
- [ ] Hold `Left Ctrl` while riding and move the mouse. Confirm the camera can
  look independently of the horse's heading.
- [ ] Have a second player right-click the mounted horse to test multi-riding;
  verify it respects the server's `multiriding` and ownership settings.

## 7. Commands, recall, and roster

1. Stand within 12 blocks of your owned horse and aim the crosshair at it.
2. Press `R`, then click a command wedge. Click outside the wheel to cancel.

- [ ] **Follow:** walk away; the horse follows.
- [ ] **Stay:** walk away; the horse remains where it was commanded.
- [ ] **Wander:** confirm it roams/grazes near the command location rather than
  following indefinitely.
- [ ] **Set Home:** stand at a recognizable location, use Set Home, and confirm
  the horse switches to Stay.
- [ ] **Return Home:** move the horse away, use Return Home, and confirm it
  walks toward home and eventually returns/teleports if necessary.
- [ ] While unmounted, press `P`; nearby owned mounts should follow, and distant
  ones should be recalled closer.
- [ ] While mounted, press `P`; the horse-info screen should open instead of a
  recall.
- [ ] Press `G`; confirm the roster lists the horse, its current state, and
  its correct dimension/position.

## 8. Breed, persistence, and multiplayer smoke tests

- [ ] Repeat the basic saddle, inventory, mount, and command-wheel tests with
  at least three breed entities (for example `friesian_horse`,
  `arabian_horse`, and `shire_horse`).
- [ ] Leave the world, return, and confirm the horse's owner, name, bond,
  saddle, fitted gear, storage, command, and saved home persist.
- [ ] On a dedicated server, reconnect with a second client. Confirm both
  clients see the same horse model, saddle/gear state, cart, and command
  changes.
- [ ] With the actual Terralith/BYG pack installed, visit several configured
  biomes and confirm natural breed spawns occur. The standalone development
  client cannot validate these optional-biome spawn rules by itself.

## 9. If something fails

- [ ] Record the exact action, key pressed, item equipped, breed, and whether
  the horse was owned/tamed.
- [ ] Save `logs/latest.log` and any new file in `crash-reports/`.
- [ ] For a server issue, also save the server console/log and the mod list from
  both client and server.
