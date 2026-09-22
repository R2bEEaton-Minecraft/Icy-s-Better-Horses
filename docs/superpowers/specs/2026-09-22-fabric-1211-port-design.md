# Fabric 1.21.1 2.0 Port Design

## Goal

Ship Icy's Better Horses 2.0 for Fabric 1.21.1 with parity with the
`origin/1.21.1-neo` 2.0 branch. It must run with the supplied private pack
and preserve usable horse data from the supplied 1.1.6 Fabric build.

## Source of truth

The current Fabric 26.2 branch remains the loader, networking, registry, and
Fabric entrypoint reference. The NeoForge 1.21.1 branch is the source of truth
for every 1.21.1 vanilla API shape, GeckoLib 4 integration, persistent NBT
format, and client rendering implementation.

## Compatibility contract

Existing vanilla horses retain all recognised `BH_*` data: ownership, bond,
command, home/wander locations, gender, breed and mixed-breed state, gear,
chest contents, upgraded saddles, and cart-related data. The port accepts both
the legacy numeric `BH_Breed` and string `BH_BreedId` forms. Removed hitchposts
are not retained; their data is harmlessly ignored.

The released jar declares Java 21, Fabric Loader 0.17+, Minecraft 1.21.1,
Fabric API 0.116.17, GeckoLib 4.9.3, and the pack's Modonomicon/Cloth/Mod Menu
versions. It must not bundle any dependency.

## Architecture

Port in layers. First establish a real Fabric 1.21.1 build. Then translate
common gameplay classes and mixins against the NeoForge branch while preserving
Fabric registration/event wiring. Port persistence before client code so saved
world compatibility is testable independently. Finally replace 26.2 render
state/extraction code with the 1.21.1 renderer model and GeckoLib 4 APIs.

Biome additions stay in the mod's existing spawn-tag datapack mechanism.
Terralith and BYG IDs are included by breed affinity. Tectonic and Ecologics
in the supplied pack register no additional biomes, so they require no IDs.

## Verification

Each layer compiles before the next begins. Automated tests cover legacy NBT
key migration and biome-tag validity. Final verification runs unit tests,
builds the distributable jar, checks mixin application at development launch,
and launches against the supplied server/client mod lists after replacing the
old 1.1.6 jar.
