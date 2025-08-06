# TODO

Upcoming changes.

## Questcraft

- Start a CHANGELOG
- Start a README
- Implement a simple Mad Libs style quest that can be used as an example for teaching how to use the Crafting Table
- Start working on Super Genie Journey with Greyson

### App

- Implement JavaFXApplication
-- https://openjfx.io/openjfx-docs/#IDE-NetBeans
-- Protoype application controller seems to work well!  Note that maximizing the application window causes text to float.  Implement all of the abstract methods.
-- Research how to use the Deployment Toolkit library or if there's a better solution (ie, Gluon CloudLink or JPro)
- Application should be able to relaunch using a different application control while preserving the model and view data

### Quest

- Add list of learned spells to spell book
- Add ability for spell to fail because of insufficient MP
- For shorter scene images, expand the width past 400
- Add keyboard arrow key bindings to the navigation buttons
- Add yellow highlighting to new items on the Inventory view
- Add xp increases to aquiring items and other significant events
- When magic words are in use, the current text column calculation gets weird.  Mylee's parting words to Shmebulock in the introduction has exclamation points that overlap the Gold inventory item.
- Refactor the current Twin Quest book to use the cool new control features.  Shmebulock variables can be removed and <if condition="variable player=Shmebulock" SHMEBULOCK!> can be used instead.
- "Library" should instead be "Application" with a "Now Playing" section that shows the book's metadata over a transparent overlay over a full image.  Can add app-level options like changing the GUI and styling here.
- Add a return icon with tooltip to the upper-left hand corner of each non-quest view for returning to the Quest tab... "right arrow curving left" U+21A9 or "BACK arrow" U+1F519 with a floating overlay that can contain not just the return icon but the Spell Book spell field, etc.  Quest can return back to Application.  Application's Now Playing will include a Link to the Quest.
 - inventory-add in Quest needs to limit additions based on how many items are in a book
- setPlayerHP() in Quest needs to require a source of the damage so if the player is unalived, they can be told by who or whom
- Add gasp sound when player dies
- Implement Book-specific game over page via a new Book property "finalAct".  Also add a High Score handler page that shows how the player did in comparison to the other high scores.  And, a cool skull and crossbones picture would be nice.
- Fix loading a new book not closing the current book's open tabs
- Figure out how to support high score entry.
- Rename quest controls to be noun-action so they're easier to find
- Add category to the quest controls so they can be grouped
- Figure out how to document each quest control with example usage in such a way that the Crafting Table can dynamically display help text
- Convert quest files to standard XML format
- Fix spell-cast.wav... it has a 1/4 second delay
- Implement a collection for each inventory item.  (ie, a collection of keys, a collection of spells, etc.)
- Fix the performance issue with displaying magic (multi-shades of purple) text.  The random number generation is likely to blame.
- Implement the Crafting Table by modeling the book into a click-and-drag multi-paneled grid, with available objects to the left and the current objects on the right.  When an object is selected, it shows a properties window that matches the object's class.

## Twin Quest

### Introduction

- Mylee states that to continue up each level she demands payment
- Mylee explains about the elevator and shares that it's solar-powered

### Chapter 1 - A Dragon in the Kingdom

- Each scene's description should be in 3 sections-- short introduction to where the player is, the interesting detail the player can pick out in this scene, and next steps available to the player.
- Reimplement Wilderness 1
-- Gold Bunny enables DOWN THE RABBIT HOLE spell that Shmebulock can use to access the stairwell.  The stairwell has a 1/14 chance of encountering Big Chung.  Also, invincibility star is hidden (1/7 chance).
-- 1/14 chance there will be no wildlife.  That's when Big Chung is present in the stairwell.
-- Improve the images that are used.
-- Foreshadowing for Chapter 2: The grass is tall and could be hiding things.  The commander bunnies are evidence of that.
- Finish reimplementing Wilderness 2
-- Only Shmebulock with infinite MP recharging and a healing spell can capture 10 bunnies without dying.
-- Foreshadowing for Chapter 2: The wilderness has a large dirt path.  Perhaps someday it will be used?
- Reimplement Magic Ring of Toadstools
-- Implement DOWN THE RABBIT HOLE SPELL for accessing the stairwell
-- The open-door.wav sound is reduced after truncating it and it can't be heard over the other sounds
-- Maybe remove the sign and add an overlay and text label where the picture frame should have text
- Reimplement Gianni's Den
-- Foreshadowing for Chapter 2: The den has a sense of foritified security
- Reimplement Woods
-- Foreshadowing for Chapter 2: The trees are tall but the player believes they could throw rocks over them.
- Reimplement Mount Fluff
-- Add spanish guitar image
-- SHMEBULOCK play-through needs additional work...Shmebulock should only be able to talk if a certain variable is set
-- Foreshadowing for Chapter 2: The mountain climbs as high as the eye can see.  Who knows how high up it might go?  Imagine skiing down it!
- Reimplement Mystery Room
-- For Shmebulock, this is cosmic wonder #1.  He must collect all of them to imbue the new ring of taming with magic.
-- When aquiring Cosmic Terror, allude to it being a new short-range attack
-- Foreshadowing for Chapter 2: The room is perhaps some sort of cosmic center?
- Reimplement Castle
-- Foreshadowing for Chapter 2: The gold is scattered about the maze-like interior of the castle.
- Reimplement Dragon's Den
-- Shmebulock's Cosmic Terror should require 10MP but it currently doesn't
- Reimplement Mylee's Elevator
-- Night Owl doesn't die, he just flies away, defeated.
-- Ends with full screen mode of ominous music and seeing the outline of a giant black cat watching.  Mylee intervenes and talks to Chung.
-- As a reward for beating Night Owl, Mylee teaches the "warp to" spell for being able to warp to a chapter once the player learns the chapter's name
-- Mylee's payment is Gold

### Chapter 2 - A Darkness over the Land

- Implement Mylee's elevator
-- Mylee takes the player up to chapter 2 where she demands some chicken meat
-- When player steps out of the elevator and into a mysterious darkness, the elevator crashes
-- Mylee calls the player with an emergency phone booth and explains that she told Big Chung to go on a "Light Diet", and he took it too literally.  Given that the Elevator is solar-powered and Chung ate all of the light, an illumination spell is needed.  Draco can help.  (Phone call cuts out before she can say more.)
- 1) Implement Snakes in the Grass
-- 10 pink bunnies are needed to find and face the pythons.  (Game not playable unless Shmebulock.)
- 2) Implement Frogger
-- Nothing is needed to access game.  Game drops car.
- 3) Implement Racing Game
-- Needs both car and gas, game drops ski trip (snow sled).  Player saves some gas.
- 4) Implement Chicken Tanks
-- Needs military rank, drops laser cannon
- 5) Implement Gorrilas
-- Two games, first is a moving target for shooting monkeys in trees.  Each successful hit causes the monkey to drop a banana.  Second is the classic gorrilas game.  Each launch requires a banana to be used.
-- Needs bananas, drops military rank
- 6) Implement Ski Free
-- Needs snow sled
-- After beating ski free, player ignites the remaining gas and burns a bright light into Night Owl's Woods to prevent from being attacked.
-- Chicken invaders game ensues
- 7) Implement Asteroids
-- Needs laser cannon, drops ping pong
- 8) Pac-man
-- Needs key (the level is a castle), drops gas (farts)
- 9) Implement Pong/Breakout
-- Needs ping pong, drops key
-- Dragon appears at end of game with arcade style attacks (miniboss)
- Implement Chicken Invaders and final Night Owl boss battle
-- After Chicken Invaders, player must chase Night Owl throught the trees via platformer game
-- Night Owl drops an illumination spell which the player can use to undo dark mode and power Mylee's Elevator
-- Mylee's payment is Chicken dropped by the Chicken invaders game

### Chapter 3 - (Platformer)

- Scene Map is much larger with missing squares around a Castle where the twin (or twins) is held.  Magic is needed to get to the castle.  At the end you find out that the twin is in another castle.
- Dragon is used to fight the boss.

### Chapter 4 - (Raycaster)

-

### Chapter 5 - (Minecraft clone)

-

### Chapter 6 - (Mobile games)

- ie, The Haunting of Chung Castle with hidden item puzzles, bubble blaster

### Chapter 7 - (Chess)

- Chapter 7 is essentially a cat tree and the player must fight a boss to go up each level of the tree 
- Chapter 7 is every boss which the player must fight in sequence.  The final battle with King Chung is a chess game.  The player uses this to get close enough to Chung to try to use the Ring of Taming but it isn't strong enough.  Mylee steps in and helps the player before they are eaten by giving a time travel spell (the opposite of warp to), but the player can only cast it as one magical enough to make it work.  The player needs to become Shmebulock and go back in time.  The player needs to figure out how to enhance the Ring of Taming.
-- Mylee explains to the player that the elevator uses a static warp bubble to traverse different time periods, and that each scene is the same location, just a different time.  She calls out the commonalities across each to support this.  She can use the time machine to send shmebulock back in the hopes he can complete the Chungus Ring of Taming with his abilities.  She achieves this by using the dark mode spell of inversion on the ring of taming and by summoning shmebulock.

### Shmebulock Replay

- Reimplement Chapter 1
- Reimplement Chapter 2
- Reimplement Chapter 3
- Reimplement Chapter 4
- Reimplement Chapter 5
- Reimplement Chapter 6
- Reimplement Chapter 7
-- The Cosmic Wonders can be used like Mario stars to grant temporary invincibility.  This allows Shmebulock to enter certain areas that were previously off limits to complete the Chungus Ring of Taming.
-- This time the player surprises King Chung with a Chungus Ring of Taming that tames Chung but in his panic he begins eating space-time which forces him into his own time portal, setting the stage for a prequel game.
-- Chungus Ring of Taming can be the copyright emoji but once assembled becomes the ball of yarn emoji, with the comment that his only weakness is enjoying a good game.  Sound can be epic choir ah's.