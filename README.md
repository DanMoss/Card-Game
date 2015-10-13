# cardgame
## Welcome

## Aces to Kings
### How to Play
Aces to Kings is a variant of the popular card game Rummy. A standard deck of cards is used and the game progresses through 13 rounds. It is recommended to play with between 3 - 5 players, although up to 7 players (barely) can play in the same game.

###### Winning
Each player maintains a points total between rounds. Once all rounds have been played, the winner is the player with the lowest number of points.

###### Rounds
Each round starts with each player being dealt 7 cards from the deck. The top card of the deck is then flipped face-up and placed on what is known as the discard pile. The first player to play in each round cycles through the game with the rounds, and in the first round is typically decided randomly. A round ends when a player has no cards left remaining in their hand after ending their turn. For the rest of the players, they must add the value of cards in their hand to their points total. Card values are as follows:

Card | Value
-----|------
Round specific joker | 15 points
Ace | 1 point
Jack | 11 points
Queen | 12 points
King | 13 points
Two through Ten | 2 - 10 points, using the now clear relation between card and points

###### Turns
Each player starts their turn either by taking the face-up card from the top of the discard pile, or by drawing a card from the top of the deck. Afterwards, the player enters the main phase of their turn. If they wish to, they can now play any melds they have in their hand, and/or add cards to an already existing meld on the board. Once they have finished playing cards to the board, they must discard 1 card to the discard pile to end their turn. It is important to note that **a player must discard to end their turn**.

If a player draws the last card of the deck on their turn, then before their turn continues, the discard pile is shuffled and placed as the new deck. The card the player chooses to discard at the end of their turn will act as the start of the new discard pile.

###### Jokers
Each round, a specific card rank will serve as a joker, able to mimic properties of other cards in the deck. The specific rank scales through the game with the rounds, starting with aces and ending with kings.

###### Meld type 1: Runs of Cards of the Same Suit
This is one of the two kinds of melds that can be played in the game. Players require 3 cards to play this kind of meld from their hand. Aces can be low or high when being (added to/played as) a part of this kind of meld. In this kind of meld, a joker mimics both the suit of the cards in the meld, and a rank adjacent to the cards in the meld. Players must be specific about the suit and rank that the joker is mimicking. If the equivalent card that the joker is mimicking is played to this meld, then the player who played that card must pick the joker up. Jokers added to this kind of meld may not mimic a card already in the meld.

###### Meld type 2: Sets of Cards of the Same Rank
This is the other kind of meld that can be played in the game. Players require 3 cards to play this kind of meld from their hand. In this kind of meld, a joker mimics the rank of the cards in the meld. Players must be specific about the rank that the joker is mimicking. If another non-joker card is added to this meld, then the player who played that card must pick the joker up. Jokers may not be added to this kind of meld if it already contains 4 cards.

### Task List
Tasks are not necessarily listed any particular order. Entries are removed upon completion.

###### Documentation
- [X] Create a README.md.
- [ ] Fill out README.md.
- [ ] Review directory usage.
- [ ] Look into proper documentation standards.
- [ ] Apply proper documentation standards.

###### Code quality
- [ ] Review access modifier usage.
- [ ] Review complexity of methods surrounding playing cards.
- [ ] Review the implementation of event management.

###### Gameplay
- [ ] Support for multiple players.
- [ ] Implement a system to display the game state.
- [ ] Allow for cancelling player choices.
- [ ] Implement a turn timer.
- [ ] Implement Skype support.
- [ ] Support for playing aces as a high card in runs.
- [ ] Support for AI players. (Long term goal)
