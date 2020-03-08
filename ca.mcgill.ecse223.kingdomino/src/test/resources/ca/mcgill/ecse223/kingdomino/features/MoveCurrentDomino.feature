Feature: Move current domino
  
    As a player, I wish to evaluate a provisional placement of my current domino 
    by moving the domino around into my kingdom (up, down, left, right) (F11)

  Background: 
    Given the game is initialized for move current domino

  Scenario Outline: Initial tentative place of the domino
    Given it is "<player>"'s turn
    Given "<player>" has selected domino <id>
    When "<player>" removes his king from the domino <id>
    Then domino <id> should be tentative placed at position 0:0 of "<player>"'s kingdom with ErroneouslyPreplaced status

    Examples: 
      | player | id |
      | pink   |  6 |
      | blue   | 23 |
      | green  | 48 |
      | yellow |  1 |

  Scenario Outline: Player moves tentatively placed domino to a new neighboring tile successfully
    Given it is "<player>"'s turn
    Given "<player>"'s kingdom has following dominoes:
      | id | dir   | posx | posy |
      |  2 | right |    1 |    0 |
      | 12 | left  |    1 |   -1 |
      | 38 | left  |    1 |   -2 |
      | 28 | right |   -2 |   -1 |
      | 18 | up    |   -1 |    0 |
    Given "<player>" has selected domino <id>
    Given domino <id> is tentatively placed at position <posx>:<posy> with direction "<dir>"
    When "<player>" requests to move the domino "<movement>"
    Then the domino <id> should be tentatively placed at position <nposx>:<nposy> with direction "<dir>"
    Then the new status of the domino is "<dstatus>"

    Examples: 
      | player | id | dir   | posx | posy | movement | nposx | nposy | dstatus              |
      | pink   |  6 | right |    1 |    1 | left     |     0 |     1 | correctlyPreplaced   |
      | pink   | 48 | down  |   -1 |    1 | left     |    -2 |     1 | erroneouslyPreplaced |
      | green  | 48 | left  |    2 |    1 | down     |     2 |     0 | erroneouslyPreplaced |
      | pink   | 17 | left  |    0 |   -1 | up       |     0 |     0 | erroneouslyPreplaced |
      | blue   | 23 | down  |   -2 |    0 | right    |    -1 |     0 | erroneouslyPreplaced |
      | yellow |  1 | right |   -2 |   -2 | up       |    -2 |    -1 | erroneouslyPreplaced |
      | blue   | 21 | left  |    0 |   -1 | down     |     0 |    -2 | erroneouslyPreplaced |
      | blue   |  1 | up    |    0 |    1 | right    |     1 |     1 | correctlyPreplaced   |
      | green  | 14 | down  |   -1 |    1 | left     |    -2 |     1 | correctlyPreplaced   |
      | green  |  3 | right |   -1 |   -2 | left     |    -2 |    -2 | correctlyPreplaced   |
      | green  |  3 | up    |    1 |    1 | left     |     0 |     1 | correctlyPreplaced   |
      | green  |  3 | right |    0 |    2 | down     |     0 |     1 | correctlyPreplaced   |
      | yellow | 48 | left  |    2 |   -1 | right    |     3 |    -1 | erroneouslyPreplaced |
      | yellow | 48 | up    |    1 |    1 | up       |     1 |     2 | erroneouslyPreplaced |
      | yellow | 48 | down  |    0 |   -2 | down     |     0 |    -3 | erroneouslyPreplaced |
      | yellow | 48 | right |   -2 |   -2 | left     |    -3 |    -2 | erroneouslyPreplaced |

  Scenario Outline: Player attempts to move the tentatively placed domino but fails due to board size restrictions
    Given it is "<player>"'s turn
    Given "<player>" has selected domino <id>
    Given "<player>"'s kingdom has following dominoes:
      | id | dir   | posx | posy  |
      |  2 | right |   1  |   0   |
      | 13 | down  |   1  |  -1   |
      | 28 | up    |   1  |  -4   |
      |  5 | right |   2  |  -3   |
      |  7 | right |   2  |  -4   |
    Given domino <id> is tentatively placed at position <posx>:<posy> with direction "<dir>"
    Given domino <id> has status "<dstatus>"
    When "<player>" requests to move the domino "<movement>"
    Then the domino <id> is still tentatively placed at position <posx>:<posy>
    Then the domino should still have status "<dstatus>"

    Examples: 
      | player | id | dir   | posx | posy | movement | dstatus              |
      | pink   |  6 | right |    3 |    1 | right    | erroneouslyPreplaced |
      | pink   |  4 | down  |    4 |   -3 | right    | correctlyPreplaced   |
      | blue   |  4 | down  |    4 |   -3 | down     | correctlyPreplaced   |
      | blue   | 23 | left  |   -1 |   -4 | down     | erroneouslyPreplaced |
      | green  | 48 | left  |   -3 |    0 | left     | erroneouslyPreplaced |
      | green  | 48 | up    |   -4 |   -2 | left     | erroneouslyPreplaced |
      | yellow |  1 | up    |    0 |    3 | up       | erroneouslyPreplaced |
      | yellow |  1 | right |    0 |    4 | up       | erroneouslyPreplaced |
