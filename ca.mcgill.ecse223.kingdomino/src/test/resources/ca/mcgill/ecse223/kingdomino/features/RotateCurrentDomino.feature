Feature: Rotate Current Domino
  As a player, I wish to evaluate a provisional placement of my current domino in my kingdom by rotating it (clockwise or counter-clockwise) (F12)

  Background: 
    Given the game is initialized for rotate current domino

  Scenario Outline: Player rotates a tentatively placed domino
    Given it is "<player>"'s turn
    Given "<player>"'s kingdom has following dominoes:
      | id | dir   | posx | posy |
      |  1 | right |    1 |    0 |
      | 12 | left  |    1 |   -1 |
      | 46 | right |    1 |   -2 |
      | 28 | right |   -2 |   -1 |
      | 18 | up    |   -1 |    0 |
    Given "<player>" has selected domino <id>
    Given domino <id> is tentatively placed at position <posx>:<posy> with direction "<dir>"
    When "<player>" requests to rotate the domino with "<rotation>"
    Then the domino <id> is still tentatively placed at <posx>:<posy> but with new direction "<newDir>"
    Then the domino <id> should have status "<dstatus>"

    Examples: 
      | player | id | posx | posy | dir   | rotation         | newDir | dstatus              |
      | pink   | 48 |    1 |    1 | right | clockwise        | down   | erroneouslyPreplaced |
      | pink   | 48 |    1 |    1 | down  | clockwise        | left   | correctlyPreplaced   |
      | pink   | 48 |    1 |    1 | left  | clockwise        | up     | correctlyPreplaced   |
      | pink   | 48 |    1 |    1 | up    | clockwise        | right  | correctlyPreplaced   |
      | yellow | 22 |   -2 |   -1 | up    | counterclockwise | left   | erroneouslyPreplaced |
      | yellow | 22 |   -2 |   -1 | left  | counterclockwise | down   | erroneouslyPreplaced |
      | yellow | 22 |   -2 |   -1 | down  | counterclockwise | right  | erroneouslyPreplaced |
      | yellow | 22 |   -2 |   -1 | right | counterclockwise | up     | erroneouslyPreplaced |
      | blue   |  8 |    2 |   -1 | up    | counterclockwise | left   | erroneouslyPreplaced |
      | blue   |  8 |    2 |   -1 | up    | clockwise        | right  | erroneouslyPreplaced |
      | green  | 10 |    0 |    2 | left  | counterclockwise | down   | correctlyPreplaced   |
      | green  | 10 |    0 |    2 | down  | clockwise        | left   | correctlyPreplaced   |

  Scenario Outline: Player attempts to rotate the tentatively placed domino but fails due to board size restrictions
    Given it is "<player>"'s turn
    Given "<player>" has selected domino <id>
    Given "<player>"'s kingdom has following dominoes:
      | id | dir   | posx | posy |
      |  1 | right |    1 |    0 |
      | 13 | down  |    1 |   -1 |
      | 28 | up    |    1 |   -4 |
      |  5 | right |    2 |   -3 |
      |  7 | right |    2 |   -4 |
    Given domino <id> is tentatively placed at position <posx>:<posy> with direction "<dir>"
    Given domino <id> has status "<dstatus>"
    When "<player>" requests to rotate the domino with "<rotation>"
    Then domino <id> is tentatively placed at the same position <posx>:<posy> with the same direction "<dir>"
    Then domino <id> should still have status "<dstatus>"

    Examples: 
      | player | id | posx | posy | dir   | rotation         | dstatus              |
      | pink   | 48 |   -4 |    0 | down  | clockwise        | erroneouslyPreplaced |
      | pink   | 48 |    0 |   -4 | right | clockwise        | erroneouslyPreplaced |
      | yellow |  4 |    4 |   -4 | up    | clockwise        | correctlyPreplaced   |
      | pink   | 48 |    2 |    4 | left  | clockwise        | erroneouslyPreplaced |
      | yellow | 23 |   -4 |    0 | up    | counterclockwise | erroneouslyPreplaced |
      | yellow | 23 |    0 |   -4 | left  | counterclockwise | erroneouslyPreplaced |
      | blue   |  4 |    4 |   -3 | down  | counterclockwise | correctlyPreplaced   |
      | blue   | 23 |   -2 |    4 | right | counterclockwise | erroneouslyPreplaced |

