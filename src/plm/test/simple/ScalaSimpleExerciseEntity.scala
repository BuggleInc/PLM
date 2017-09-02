package plm.test.simple

import plm.test.simple.SimpleWorld; // Do not optimize this away, or it will be missing when running the test

class ScalaSimpleExerciseEntity extends plm.test.simple.SimpleExerciseEntity {
  /* BEGIN TEMPLATE */
  override def run() {
    /* BEGIN SOLUTION */
    world.asInstanceOf[SimpleWorld].setObjectif(true);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}