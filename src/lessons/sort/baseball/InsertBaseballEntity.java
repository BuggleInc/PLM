package lessons.sort.baseball;

import lessons.sort.baseball.universe.BaseballEntity;

public class InsertBaseballEntity extends BaseballEntity {

  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    /* Bring the hole in 0,1 */
    if (_getHole() == 0) // It is already on base 0, but on another position
      _move(1);
    while (_getHole() > 1)
      _move(_getHole() - 1);

    for (int player = 2; player < getBasesAmount() * getPositionsAmount(); player++) {
      // out("Sort player "+player);

      // out("Compare "+(getHole()+1)+":"+getPlayerColor(getHole()+1)+" < "+(getHole()-1)+":"+getPlayerColor(getHole()-1));
      while (_getHole() > 0 && _getPlayerColor(_getHole() + 1) < _getPlayerColor(_getHole() - 1)) {
        int center = _getHole(); // ...2x1... with ascending positions from left to right
        _move(center + 1);       // ...21x...
        _move(center - 1);       // ...x12...
      }
      while (_getHole() != player)
        _move(_getHole() + 1);
    }
    assertSorted("insertion sort");
  }

  int _getPlayerColor(int pos) { return getPlayerColor(pos / getPositionsAmount(), pos % getPositionsAmount()); }
  void _move(int pos) { move(pos / getPositionsAmount(), pos % getPositionsAmount()); }
  int _getHole()
  {
    return getPositionsAmount() * getHoleBase() + getHolePosition();

    /* END SOLUTION */
  }

  /* END TEMPLATE */
}