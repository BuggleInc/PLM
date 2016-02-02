package plm.test.simple

class ScalaSimpleExerciseEntity extends plm.test.simple.SimpleExerciseEntity {
  /* BEGIN TEMPLATE */
  override def run() {
    /* BEGIN SOLUTION */
    world.asInstanceOf[plm.test.simple.SimpleWorld].setObjectif(true);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}